package com.servo.dungeons.dungeon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Data class for a single room in the dungeon grid.
 * Each room occupies a 16x16x10 block volume in the world.
 */
public class RoomData {

    /** Room width/depth in blocks. */
    public static final int ROOM_SIZE = 16;
    /** Room height in blocks (floor to ceiling inclusive). */
    public static final int ROOM_HEIGHT = 10;

    private final int gridX;
    private final int gridZ;
    private final RoomType type;
    private final Set<Direction> connections;

    public RoomData(int gridX, int gridZ, RoomType type, Set<Direction> connections) {
        this.gridX = gridX;
        this.gridZ = gridZ;
        this.type = type;
        this.connections = Collections.unmodifiableSet(EnumSet.copyOf(connections));
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridZ() {
        return gridZ;
    }

    public RoomType getType() {
        return type;
    }

    /**
     * @return unmodifiable set of horizontal directions where this room connects to a neighbor
     */
    public Set<Direction> getConnections() {
        return connections;
    }

    /**
     * Calculate the world position of this room's origin (bottom-south-west corner)
     * based on the dungeon center offset.
     *
     * @param dungeonCenter the BlockPos center allocated by OffsetAllocator
     * @return the world position of this room's origin corner
     */
    public BlockPos getWorldPos(BlockPos dungeonCenter) {
        return new BlockPos(
                dungeonCenter.getX() + gridX * ROOM_SIZE,
                dungeonCenter.getY(),
                dungeonCenter.getZ() + gridZ * ROOM_SIZE
        );
    }
}
