package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonTier;
import com.servo.dungeons.ServoDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Places loot chests in generated dungeon rooms using vanilla chest blocks
 * with loot tables assigned per dungeon tier.
 *
 * <p>Chest placement rules by room type:
 * <ul>
 *   <li>ENTRANCE: 0 chests</li>
 *   <li>DEAD_END: 1-2 chests (guaranteed)</li>
 *   <li>HALLWAY/CORNER: 0-1 chest (30% chance)</li>
 *   <li>T_JUNCTION/CROSS: 1 chest (50% chance)</li>
 *   <li>BOSS: 1 chest (guaranteed, final reward)</li>
 * </ul>
 */
public class DungeonLootPlacer {

    private DungeonLootPlacer() {
        // Utility class
    }

    /**
     * Place loot chests in all rooms of a dungeon layout.
     *
     * @param layout        the generated dungeon layout
     * @param tier          the dungeon tier (determines loot table)
     * @param dungeonLevel  the dungeon dimension's ServerLevel
     * @param dungeonCenter the dungeon instance center
     * @param random        random source for chest count and positioning
     */
    public static void placeChests(DungeonLayout layout, DungeonTier tier,
                                    ServerLevel dungeonLevel, BlockPos dungeonCenter,
                                    RandomSource random) {
        int totalChests = 0;

        for (RoomData room : layout.getRooms()) {
            int placed = placeChestsInRoom(room, tier, dungeonLevel, dungeonCenter, random);
            totalChests += placed;
        }

        ServoDungeons.LOGGER.debug("Placed {} loot chests across {} rooms",
                totalChests, layout.getRoomCount());
    }

    /**
     * Place chests in a single room based on its type and tier.
     *
     * @return the number of chests placed
     */
    private static int placeChestsInRoom(RoomData room, DungeonTier tier,
                                          ServerLevel level, BlockPos dungeonCenter,
                                          RandomSource random) {
        int chestCount = getChestCount(room.getType(), random);
        if (chestCount <= 0) return 0;

        BlockPos origin = room.getWorldPos(dungeonCenter);
        ResourceKey<LootTable> lootTableKey = getLootTableKey(tier, room.getType());
        int placed = 0;

        for (int i = 0; i < chestCount; i++) {
            BlockPos chestPos = findChestPosition(room, origin, i, random);
            if (chestPos != null) {
                placeChest(level, chestPos, lootTableKey, random);
                placed++;
            }
        }

        return placed;
    }

    /**
     * Determine how many chests to place in a room based on its type.
     */
    private static int getChestCount(RoomType type, RandomSource random) {
        return switch (type) {
            case ENTRANCE -> 0;
            case DEAD_END -> 1 + (random.nextFloat() < 0.5f ? 1 : 0); // 1-2
            case HALLWAY, CORNER -> random.nextFloat() < 0.30f ? 1 : 0; // 30% chance
            case T_JUNCTION, CROSS -> random.nextFloat() < 0.50f ? 1 : 0; // 50% chance
            case BOSS -> 1; // guaranteed
        };
    }

    /**
     * Get the loot table resource key for the given tier.
     * Boss rooms always use the tier's matching loot table.
     */
    private static ResourceKey<LootTable> getLootTableKey(DungeonTier tier, RoomType roomType) {
        String tableName = switch (tier) {
            case BASIC -> "chests/dungeon_chest_basic";
            case ADVANCED -> "chests/dungeon_chest_advanced";
            case MASTER -> "chests/dungeon_chest_master";
            case CORE -> "chests/dungeon_chest_core";
        };

        return ResourceKey.create(
                net.minecraft.core.registries.Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(ServoDungeons.MOD_ID, tableName)
        );
    }

    /**
     * Find a valid position for a chest in the room.
     * Chests are placed against walls, not blocking doorways.
     *
     * @param room   the room data
     * @param origin the room's world origin
     * @param index  which chest this is (0, 1, etc.) — used to pick different wall positions
     * @param random random source
     * @return a valid chest position, or null if none found
     */
    private static BlockPos findChestPosition(RoomData room, BlockPos origin,
                                               int index, RandomSource random) {
        // Wall positions: 1 block from wall, Y=1 (floor level)
        // Avoid door positions (6-9 on each wall) and corners
        int size = RoomData.ROOM_SIZE;

        // Pre-defined positions near walls, avoiding door openings
        // Door is at positions 6-9, so safe positions are 2-4 and 11-13
        int[][] wallPositions = {
                // Against north wall (z=1), various x
                {3, 1, 1}, {4, 1, 1}, {11, 1, 1}, {12, 1, 1},
                // Against south wall (z=14)
                {3, 1, 14}, {4, 1, 14}, {11, 1, 14}, {12, 1, 14},
                // Against west wall (x=1)
                {1, 1, 3}, {1, 1, 4}, {1, 1, 11}, {1, 1, 12},
                // Against east wall (x=14)
                {14, 1, 3}, {14, 1, 4}, {14, 1, 11}, {14, 1, 12},
        };

        // Filter positions that are not adjacent to a doorway
        // Shuffle by starting at a random offset
        int startIdx = (index * 5 + random.nextInt(wallPositions.length)) % wallPositions.length;

        for (int attempt = 0; attempt < wallPositions.length; attempt++) {
            int posIdx = (startIdx + attempt) % wallPositions.length;
            int[] pos = wallPositions[posIdx];
            BlockPos candidate = origin.offset(pos[0], pos[1], pos[2]);

            // Check that the position is not in a doorway
            if (!isInDoorway(room, pos[0], pos[2])) {
                return candidate;
            }
        }

        // Fallback: center of room
        return origin.offset(size / 2 - 1, 1, size / 2 - 1);
    }

    /**
     * Check if a position (relative to room origin) is inside a doorway area.
     * Doorways span positions 5-10 on the wall edge (positions 6-9 for the door
     * itself, plus 1 block buffer on each side).
     */
    private static boolean isInDoorway(RoomData room, int relX, int relZ) {
        int size = RoomData.ROOM_SIZE;

        for (Direction dir : room.getConnections()) {
            switch (dir) {
                case NORTH:
                    if (relZ <= 1 && relX >= 5 && relX <= 10) return true;
                    break;
                case SOUTH:
                    if (relZ >= size - 2 && relX >= 5 && relX <= 10) return true;
                    break;
                case WEST:
                    if (relX <= 1 && relZ >= 5 && relZ <= 10) return true;
                    break;
                case EAST:
                    if (relX >= size - 2 && relZ >= 5 && relZ <= 10) return true;
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    /**
     * Place a vanilla chest block with a loot table at the given position.
     */
    private static void placeChest(ServerLevel level, BlockPos pos,
                                    ResourceKey<LootTable> lootTableKey, RandomSource random) {
        // Determine chest facing: face away from the nearest wall
        Direction facing = Direction.NORTH;
        int rx = pos.getX() % RoomData.ROOM_SIZE;
        int rz = pos.getZ() % RoomData.ROOM_SIZE;

        // Simple heuristic: face toward room center
        if (rz <= 2) facing = Direction.SOUTH;       // near north wall, face south
        else if (rz >= 13) facing = Direction.NORTH;  // near south wall, face north
        else if (rx <= 2) facing = Direction.EAST;     // near west wall, face east
        else if (rx >= 13) facing = Direction.WEST;    // near east wall, face west

        BlockState chestState = Blocks.CHEST.defaultBlockState()
                .setValue(ChestBlock.FACING, facing);

        level.setBlock(pos, chestState, 3);

        // Set the loot table on the chest's block entity
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ChestBlockEntity chestBE) {
            chestBE.setLootTable(lootTableKey, random.nextLong());
        }
    }
}
