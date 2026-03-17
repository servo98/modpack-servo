package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonRegistry;
import com.servo.dungeons.ServoDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Manages placement and removal of barrier blocks in room doorways.
 * Barriers are placed when a room transitions to ACTIVE (combat) and
 * removed when the room transitions to CLEARED.
 *
 * <p>Door geometry matches {@link com.servo.dungeons.dungeon.DungeonGenerator#carveDoor}:
 * 4-wide (positions 6,7,8,9), 4-tall (Y 1-4), on the wall edge.
 */
public class DungeonBarrierManager {

    private DungeonBarrierManager() {
        // Utility class
    }

    /**
     * Place barrier blocks in all doorways of a room.
     * Called when a room transitions from LOCKED to ACTIVE.
     * Skips barriers for connections to the ENTRANCE room (entrance never locks).
     *
     * @param room          the room that just activated
     * @param layout        the dungeon layout (to check neighbor types)
     * @param dungeonLevel  the dungeon dimension
     * @param dungeonCenter the dungeon instance center
     */
    public static void placeBarriers(RoomData room, DungeonLayout layout,
                                      ServerLevel dungeonLevel, BlockPos dungeonCenter) {
        BlockState barrierState = DungeonRegistry.BARRIER_BLOCK.get().defaultBlockState();

        for (Direction dir : room.getConnections()) {
            // Check if the neighbor room is ENTRANCE — if so, skip this barrier
            int neighborGX = room.getGridX() + dir.getStepX();
            int neighborGZ = room.getGridZ() + dir.getStepZ();
            RoomData neighbor = layout.getRoom(neighborGX, neighborGZ);
            if (neighbor != null && neighbor.getType() == RoomType.ENTRANCE) {
                continue;
            }

            // Place barriers in this doorway
            BlockPos origin = room.getWorldPos(dungeonCenter);
            placeDoorBarrier(origin, dir, dungeonLevel, barrierState);
        }

        ServoDungeons.LOGGER.debug("Placed barriers in room ({},{})",
                room.getGridX(), room.getGridZ());
    }

    /**
     * Remove barrier blocks from all doorways of a room.
     * Called when a room transitions from ACTIVE to CLEARED.
     *
     * @param room          the room that was just cleared
     * @param dungeonLevel  the dungeon dimension
     * @param dungeonCenter the dungeon instance center
     */
    public static void removeBarriers(RoomData room, ServerLevel dungeonLevel, BlockPos dungeonCenter) {
        BlockState air = Blocks.AIR.defaultBlockState();

        for (Direction dir : room.getConnections()) {
            BlockPos origin = room.getWorldPos(dungeonCenter);
            removeDoorBarrier(origin, dir, dungeonLevel, air);
        }

        ServoDungeons.LOGGER.debug("Removed barriers from room ({},{})",
                room.getGridX(), room.getGridZ());
    }

    /**
     * Place barrier blocks in a single doorway. Uses the same positions as
     * {@link DungeonGenerator}'s carveDoor method: positions 6-9, Y 1-4.
     */
    private static void placeDoorBarrier(BlockPos origin, Direction dir,
                                          ServerLevel level, BlockState barrierState) {
        int size = RoomData.ROOM_SIZE;
        int doorStart = 6;
        int doorEnd = 9;
        int doorMinY = 1;
        int doorMaxY = 4;

        for (int i = doorStart; i <= doorEnd; i++) {
            for (int y = doorMinY; y <= doorMaxY; y++) {
                BlockPos pos = getDoorBlockPos(origin, dir, i, y, size);
                level.setBlock(pos, barrierState, 3);
            }
        }
    }

    /**
     * Remove barrier blocks from a single doorway, restoring air.
     * Only removes blocks that are actually barrier blocks (to avoid
     * destroying other blocks if room geometry changed).
     */
    private static void removeDoorBarrier(BlockPos origin, Direction dir,
                                           ServerLevel level, BlockState air) {
        int size = RoomData.ROOM_SIZE;
        int doorStart = 6;
        int doorEnd = 9;
        int doorMinY = 1;
        int doorMaxY = 4;

        for (int i = doorStart; i <= doorEnd; i++) {
            for (int y = doorMinY; y <= doorMaxY; y++) {
                BlockPos pos = getDoorBlockPos(origin, dir, i, y, size);
                if (level.getBlockState(pos).getBlock() instanceof
                        com.servo.dungeons.block.DungeonBarrierBlock) {
                    level.setBlock(pos, air, 3);
                }
            }
        }
    }

    /**
     * Calculate the world position of a door block, matching DungeonGenerator.carveDoor's logic.
     */
    private static BlockPos getDoorBlockPos(BlockPos origin, Direction dir, int i, int y, int size) {
        return switch (dir) {
            case NORTH -> origin.offset(i, y, 0);
            case SOUTH -> origin.offset(i, y, size - 1);
            case WEST -> origin.offset(0, y, i);
            case EAST -> origin.offset(size - 1, y, i);
            default -> origin;
        };
    }
}
