package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonRegistry;
import com.servo.dungeons.DungeonTier;
import com.servo.dungeons.ServoDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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

        // Generate procedural dungeon layout
        RandomSource random = dungeonLevel.getRandom();
        DungeonLayout layout = DungeonGenerator.generate(tier, random);
        DungeonGenerator.placeInWorld(layout, dungeonLevel, center);

        // Entrance position: on top of the entrance room's floor (Y+1)
        BlockPos entranceWorldPos = layout.getEntrance().getWorldPos(center);
        // Place player in the center of the entrance room
        BlockPos entrancePos = entranceWorldPos.offset(RoomData.ROOM_SIZE / 2, 1, RoomData.ROOM_SIZE / 2);

        // Create instance
        UUID id = UUID.randomUUID();
        DungeonInstance dungeonInstance = new DungeonInstance(id, tier, leader.getUUID(), altarPos, entrancePos, center);
        dungeonInstance.setLayout(layout);
        activeInstances.put(id, dungeonInstance);

        // Teleport leader to dungeon
        teleportToDungeon(leader, entrancePos, dungeonLevel);

        ServoDungeons.LOGGER.info("Dungeon started: tier={}, leader={}, id={}, center={}, rooms={}",
                tier.name, leader.getName().getString(), id, center, layout.getRoomCount());

        return id;
    }

    /**
     * Start a boss fight. Allocates an offset, creates a boss arena in the dungeon dimension,
     * and teleports the leader.
     *
     * @param chapter the boss chapter (1-8)
     * @return the dungeon instance UUID, or null if creation failed
     */
    @Nullable
    public UUID startBossFight(ServerLevel overworld, BlockPos altarPos, ServerPlayer leader, int chapter) {
        ServerLevel dungeonLevel = server.getLevel(DungeonRegistry.DUNGEON_LEVEL_KEY);
        if (dungeonLevel == null) {
            ServoDungeons.LOGGER.error("Dungeon dimension not found! Cannot create boss arena.");
            return null;
        }

        BlockPos center = offsetAllocator.allocate();

        // Create a simple boss arena (single large room)
        createBossArena(dungeonLevel, center, chapter);

        // Entrance: center of the arena floor
        BlockPos entrancePos = center.offset(16, 1, 2);

        UUID id = UUID.randomUUID();
        DungeonInstance instance = new DungeonInstance(id, DungeonTier.CORE, leader.getUUID(), altarPos, entrancePos, center);
        instance.setBossFight(true);
        instance.setBossChapter(chapter);
        activeInstances.put(id, instance);

        teleportToDungeon(leader, entrancePos, dungeonLevel);

        ServoDungeons.LOGGER.info("Boss fight started: chapter={}, leader={}, id={}, center={}",
                chapter, leader.getName().getString(), id, center);

        return id;
    }

    /**
     * Create a boss arena: a single large room (32x32x16) made of deepslate bricks.
     */
    private void createBossArena(ServerLevel level, BlockPos center, int chapter) {
        int size = 32;
        int height = 16;
        BlockState wall = Blocks.DEEPSLATE_BRICKS.defaultBlockState();
        BlockState floor = Blocks.DEEPSLATE_TILES.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState light = Blocks.SHROOMLIGHT.defaultBlockState();

        // Build shell: floor, walls, ceiling
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                level.setBlock(center.offset(x, 0, z), floor, 3);
                level.setBlock(center.offset(x, height - 1, z), wall, 3);
                for (int y = 1; y < height - 1; y++) {
                    boolean isWall = (x == 0 || x == size - 1 || z == 0 || z == size - 1);
                    level.setBlock(center.offset(x, y, z), isWall ? wall : air, 3);
                }
            }
        }

        // Lighting: shroomlight in ceiling every 6 blocks
        for (int x = 3; x < size; x += 6) {
            for (int z = 3; z < size; z += 6) {
                level.setBlock(center.offset(x, height - 2, z), light, 3);
            }
        }

        // Exit portal near entrance
        level.setBlock(center.offset(size / 2, 1, 1),
                DungeonRegistry.EXIT_PORTAL_BLOCK.get().defaultBlockState(), 3);
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
     * Uses the stored DungeonLayout to clear only the exact rooms that were placed.
     *
     * @param dungeonId the dungeon instance UUID
     * @param dungeonLevel the dungeon dimension ServerLevel
     */
    private void cleanupInstance(UUID dungeonId, ServerLevel dungeonLevel) {
        DungeonInstance dungeonInstance = activeInstances.get(dungeonId);
        if (dungeonInstance == null) return;

        BlockPos center = dungeonInstance.getCenter();
        DungeonLayout layout = dungeonInstance.getLayout();

        if (layout != null) {
            // Clear exact room positions from the layout
            DungeonGenerator.clearFromWorld(layout, dungeonLevel, center);
        } else {
            // Fallback: clear a generous fixed area if layout is somehow missing
            ServoDungeons.LOGGER.warn("No layout stored for dungeon {}, using fallback cleanup", dungeonId);
            int clearRadius = 400;
            for (int x = -clearRadius; x <= clearRadius; x += RoomData.ROOM_SIZE) {
                for (int z = -clearRadius; z <= clearRadius; z += RoomData.ROOM_SIZE) {
                    for (int bx = 0; bx < RoomData.ROOM_SIZE; bx++) {
                        for (int bz = 0; bz < RoomData.ROOM_SIZE; bz++) {
                            for (int y = 0; y < RoomData.ROOM_HEIGHT; y++) {
                                dungeonLevel.setBlock(
                                        center.offset(x + bx, y, z + bz),
                                        Blocks.AIR.defaultBlockState(), 3);
                            }
                        }
                    }
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
     * Teleport a player to the dungeon dimension at the given position.
     */
    private void teleportToDungeon(ServerPlayer player, BlockPos pos, ServerLevel dungeonLevel) {
        player.teleportTo(dungeonLevel,
                pos.getX() + 0.5, (double) pos.getY(), pos.getZ() + 0.5,
                player.getYRot(), player.getXRot());
    }
}
