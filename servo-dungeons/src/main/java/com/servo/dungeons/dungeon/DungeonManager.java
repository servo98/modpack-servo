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

import java.util.Set;
import java.util.UUID;

/**
 * Server-side singleton managing the active dungeon instance.
 * Only one dungeon can be active at a time (per design doc).
 *
 * Initialized on {@code ServerStartedEvent}, cleared on {@code ServerStoppedEvent}.
 */
public class DungeonManager {

    private static DungeonManager instance;

    private final MinecraftServer server;
    @Nullable
    private DungeonInstance activeInstance;

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
     * Start a new dungeon. Creates a platform in the dungeon dimension and teleports the leader.
     *
     * @return the dungeon instance UUID, or null if creation failed
     */
    @Nullable
    public UUID startDungeon(ServerLevel overworld, BlockPos altarPos, ServerPlayer leader, DungeonTier tier) {
        if (activeInstance != null) {
            ServoDungeons.LOGGER.warn("Cannot start dungeon: one is already active");
            return null;
        }

        // Get or create the dungeon dimension
        ServerLevel dungeonLevel = server.getLevel(DungeonRegistry.DUNGEON_LEVEL_KEY);
        if (dungeonLevel == null) {
            ServoDungeons.LOGGER.error("Dungeon dimension not found! Make sure the dimension JSON is properly configured.");
            return null;
        }

        // Create spawn platform at Y=64
        BlockPos entrancePos = new BlockPos(0, 65, 0);
        createSpawnPlatform(dungeonLevel);

        // Create instance
        UUID id = UUID.randomUUID();
        activeInstance = new DungeonInstance(id, tier, leader.getUUID(), altarPos, entrancePos);

        // Teleport leader to dungeon
        teleportToDungeon(leader, entrancePos, dungeonLevel);

        ServoDungeons.LOGGER.info("Dungeon started: tier={}, leader={}, id={}",
                tier.name, leader.getName().getString(), id);

        return id;
    }

    /**
     * End the active dungeon. Teleports all remaining players out and cleans up.
     */
    public void endDungeon() {
        if (activeInstance == null) return;

        BlockPos altarPos = activeInstance.getAltarPos();
        Set<UUID> playerIds = activeInstance.getPlayerIds();

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

        activeInstance.setActive(false);
        activeInstance = null;

        ServoDungeons.LOGGER.info("Dungeon ended, players teleported back to overworld");
    }

    /**
     * Re-enter the dungeon (e.g., after death or disconnect).
     */
    public void reenterDungeon(ServerPlayer player) {
        if (activeInstance == null) return;

        ServerLevel dungeonLevel = server.getLevel(DungeonRegistry.DUNGEON_LEVEL_KEY);
        if (dungeonLevel == null) return;

        activeInstance.addPlayer(player.getUUID());
        teleportToDungeon(player, activeInstance.getEntrancePos(), dungeonLevel);

        ServoDungeons.LOGGER.info("Player {} re-entered dungeon", player.getName().getString());
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
     */
    public void exitDungeon(ServerPlayer player) {
        if (activeInstance == null) return;

        BlockPos altarPos = activeInstance.getAltarPos();
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld != null) {
            player.teleportTo(overworld,
                    altarPos.getX() + 0.5, altarPos.getY() + 1.0, altarPos.getZ() + 0.5,
                    player.getYRot(), player.getXRot());
        }
    }

    // ==================== State Queries ====================

    public boolean isActive() {
        return activeInstance != null && activeInstance.isActive();
    }

    @Nullable
    public DungeonInstance getActiveInstance() {
        return activeInstance;
    }

    // ==================== Internal Helpers ====================

    /**
     * Create a simple stone brick platform at Y=64 as placeholder spawn room.
     * Places an exit portal at the entrance.
     */
    private void createSpawnPlatform(ServerLevel dungeonLevel) {
        int platformSize = 5;
        BlockPos center = new BlockPos(0, 64, 0);

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
