package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonRegistry;
import com.servo.dungeons.DungeonTier;
import com.servo.dungeons.ServoDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Server-side singleton managing multiple concurrent dungeon instances.
 * Each instance is placed at a different X offset in the dungeon dimension.
 *
 * Initialized on {@code ServerStartedEvent}, cleared on {@code ServerStoppedEvent}.
 */
public class DungeonManager {

    private static DungeonManager instance;

    private final MinecraftServer server;
    private final Map<UUID, DungeonInstance> activeInstances = new HashMap<>();
    private final OffsetAllocator offsetAllocator = new OffsetAllocator();

    private DungeonManager(MinecraftServer server) {
        this.server = server;
    }

    // ==================== Lifecycle ====================

    public static void init(MinecraftServer server) {
        instance = new DungeonManager(server);
        ServoDungeons.LOGGER.info("DungeonManager initialized");
    }

    public static void clear() {
        instance = null;
        ServoDungeons.LOGGER.info("DungeonManager cleared");
    }

    @Nullable
    public static DungeonManager getInstance() {
        return instance;
    }

    // ==================== Dungeon Management ====================

    /**
     * Start a new dungeon. Allocates an offset, creates a platform in the dungeon dimension,
     * and teleports the leader.
     *
     * @return the dungeon instance UUID, or null if creation failed
     */
    @Nullable
    public UUID startDungeon(ServerLevel overworld, BlockPos altarPos, ServerPlayer leader, DungeonTier tier) {
        // Get or create the dungeon dimension
        ServerLevel dungeonLevel = server.getLevel(DungeonRegistry.DUNGEON_LEVEL_KEY);
        if (dungeonLevel == null) {
            ServoDungeons.LOGGER.error("Dungeon dimension not found! Make sure the dimension JSON is properly configured.");
            return null;
        }

        // Allocate an offset for this instance
        BlockPos center = offsetAllocator.allocate();

        // Create spawn platform at the allocated offset
        BlockPos entrancePos = center.above(1); // Y=65 (center is Y=64)
        createSpawnPlatform(dungeonLevel, center);

        // Create instance
        UUID id = UUID.randomUUID();
        DungeonInstance dungeonInstance = new DungeonInstance(id, tier, leader.getUUID(), altarPos, entrancePos, center);
        activeInstances.put(id, dungeonInstance);

        // Teleport leader to dungeon
        teleportToDungeon(leader, entrancePos, dungeonLevel);

        ServoDungeons.LOGGER.info("Dungeon started: tier={}, leader={}, id={}, center={}",
                tier.name, leader.getName().getString(), id, center);

        return id;
    }

    /**
     * End a specific dungeon instance. Teleports all remaining players out and cleans up.
     *
     * @param dungeonId the UUID of the dungeon instance to end
     */
    public void endDungeon(UUID dungeonId) {
        DungeonInstance dungeonInstance = activeInstances.get(dungeonId);
        if (dungeonInstance == null) return;

        BlockPos altarPos = dungeonInstance.getAltarPos();
        Set<UUID> playerIds = dungeonInstance.getPlayerIds();

        // Teleport all players back to overworld
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld != null) {
            for (UUID playerId : playerIds) {
                ServerPlayer player = server.getPlayerList().getPlayer(playerId);
                if (player != null) {
                    player.teleportTo(overworld,
                            altarPos.getX() + 0.5, altarPos.getY() + 1.0, altarPos.getZ() + 0.5,
                            player.getYRot(), player.getXRot());
                }
            }
        }

        dungeonInstance.setActive(false);

        // Clean up the instance's area in the dungeon dimension
        ServerLevel dungeonLevel = server.getLevel(DungeonRegistry.DUNGEON_LEVEL_KEY);
        if (dungeonLevel != null) {
            cleanupInstance(dungeonId, dungeonLevel);
        } else {
            // Still remove from map and release offset even if dimension is unavailable
            activeInstances.remove(dungeonId);
            offsetAllocator.release(dungeonInstance.getCenter());
        }

        ServoDungeons.LOGGER.info("Dungeon {} ended, players teleported back to overworld", dungeonId);
    }

    /**
     * Re-enter a specific dungeon by ID (used by beam blocks).
     */
    public void reenterDungeon(ServerPlayer player, UUID dungeonId) {
        if (activeInstance == null || !activeInstance.getId().equals(dungeonId)) return;

        ServerLevel dungeonLevel = server.getLevel(DungeonRegistry.DUNGEON_LEVEL_KEY);
        if (dungeonLevel == null) return;

        activeInstance.addPlayer(player.getUUID());
        teleportToDungeon(player, activeInstance.getEntrancePos(), dungeonLevel);

        ServoDungeons.LOGGER.info("Player {} entered dungeon via beam", player.getName().getString());
    }

    /**
     * Exit a single player from the dungeon back to the altar.
     * Finds which dungeon the player is in based on their position.
     */
    public void exitDungeon(ServerPlayer player) {
        DungeonInstance dungeonInstance = getDungeonForPlayer(player.getUUID());
        if (dungeonInstance == null) return;

        BlockPos altarPos = dungeonInstance.getAltarPos();
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld != null) {
            player.teleportTo(overworld,
                    altarPos.getX() + 0.5, altarPos.getY() + 1.0, altarPos.getZ() + 0.5,
                    player.getYRot(), player.getXRot());
        }
    }

    /**
     * Re-enter a specific dungeon (e.g., after death or disconnect).
     *
     * @param player the player to teleport
     * @param dungeonId the UUID of the dungeon to re-enter
     */
    public void reenterDungeon(ServerPlayer player, UUID dungeonId) {
        DungeonInstance dungeonInstance = activeInstances.get(dungeonId);
        if (dungeonInstance == null) return;

        ServerLevel dungeonLevel = server.getLevel(DungeonRegistry.DUNGEON_LEVEL_KEY);
        if (dungeonLevel == null) return;

        dungeonInstance.addPlayer(player.getUUID());
        teleportToDungeon(player, dungeonInstance.getEntrancePos(), dungeonLevel);

        ServoDungeons.LOGGER.info("Player {} re-entered dungeon {}", player.getName().getString(), dungeonId);
    }

    // ==================== State Queries ====================

    /**
     * Check if a specific dungeon instance is active.
     */
    public boolean isActive(UUID dungeonId) {
        DungeonInstance dungeonInstance = activeInstances.get(dungeonId);
        return dungeonInstance != null && dungeonInstance.isActive();
    }

    /**
     * Check if there are any active dungeon instances.
     */
    public boolean hasActiveInstances() {
        return !activeInstances.isEmpty();
    }

    /**
     * Get a specific dungeon instance by its UUID.
     */
    @Nullable
    public DungeonInstance getInstance(UUID dungeonId) {
        return activeInstances.get(dungeonId);
    }

    /**
     * Find which dungeon instance a player is in, based on their player UUID.
     * Checks if the player is tracked in any active instance.
     *
     * @param playerId the player's UUID
     * @return the DungeonInstance the player is in, or null if not found
     */
    @Nullable
    public DungeonInstance getDungeonForPlayer(UUID playerId) {
        for (DungeonInstance dungeonInstance : activeInstances.values()) {
            if (dungeonInstance.hasPlayer(playerId)) {
                return dungeonInstance;
            }
        }
        return null;
    }

    /**
     * Find which dungeon instance contains a given position in the dungeon dimension.
     *
     * @param pos the position to check
     * @return the DungeonInstance whose area contains the position, or null
     */
    @Nullable
    public DungeonInstance getDungeonAtPosition(BlockPos pos) {
        for (DungeonInstance dungeonInstance : activeInstances.values()) {
            if (dungeonInstance.isPlayerInArea(pos)) {
                return dungeonInstance;
            }
        }
        return null;
    }

    // ==================== Cleanup ====================

    /**
     * Clean up a dungeon instance: clear blocks in the area, remove from map, release offset.
     *
     * @param dungeonId the dungeon instance UUID
     * @param dungeonLevel the dungeon dimension ServerLevel
     */
    private void cleanupInstance(UUID dungeonId, ServerLevel dungeonLevel) {
        DungeonInstance dungeonInstance = activeInstances.get(dungeonId);
        if (dungeonInstance == null) return;

        BlockPos center = dungeonInstance.getCenter();

        // Clear the spawn platform area (platform size 5 + margin of 1 = 6)
        int clearSize = 6;
        for (int x = -clearSize; x <= clearSize; x++) {
            for (int z = -clearSize; z <= clearSize; z++) {
                for (int y = 0; y <= 5; y++) {
                    dungeonLevel.setBlock(center.offset(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }

        // Remove from active instances and release offset
        activeInstances.remove(dungeonId);
        offsetAllocator.release(center);

        ServoDungeons.LOGGER.debug("Cleaned up dungeon instance {} at center {}", dungeonId, center);
    }

    // ==================== Internal Helpers ====================

    /**
     * Create a simple stone brick platform at the given center as placeholder spawn room.
     * Places an exit portal at the entrance.
     */
    private void createSpawnPlatform(ServerLevel dungeonLevel, BlockPos center) {
        int platformSize = 5;

        // Create floor
        for (int x = -platformSize; x <= platformSize; x++) {
            for (int z = -platformSize; z <= platformSize; z++) {
                dungeonLevel.setBlock(center.offset(x, 0, z), Blocks.STONE_BRICKS.defaultBlockState(), 3);
            }
        }

        // Clear air above
        for (int x = -platformSize; x <= platformSize; x++) {
            for (int z = -platformSize; z <= platformSize; z++) {
                for (int y = 1; y <= 4; y++) {
                    dungeonLevel.setBlock(center.offset(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }

        // Place exit portal at the edge of the platform
        BlockPos portalPos = center.offset(0, 1, -platformSize);
        dungeonLevel.setBlock(portalPos, DungeonRegistry.EXIT_PORTAL_BLOCK.get().defaultBlockState(), 3);
    }

    /**
     * Teleport a player to the dungeon dimension at the given position.
     */
    private void teleportToDungeon(ServerPlayer player, BlockPos pos, ServerLevel dungeonLevel) {
        player.teleportTo(dungeonLevel,
                pos.getX() + 0.5, (double) pos.getY(), pos.getZ() + 0.5,
                player.getYRot(), player.getXRot());
    }
}
