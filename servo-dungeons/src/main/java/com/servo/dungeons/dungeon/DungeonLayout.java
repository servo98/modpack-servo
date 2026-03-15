package com.servo.dungeons.dungeon;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds the complete generated layout of a dungeon: all rooms and their connections.
 * Rooms are stored by their packed grid coordinates for fast lookup.
 */
public class DungeonLayout {

    private final Map<Long, RoomData> rooms;
    private final RoomData entrance;
    @Nullable
    private final RoomData bossRoom;

    public DungeonLayout(Map<Long, RoomData> rooms, RoomData entrance, @Nullable RoomData bossRoom) {
        this.rooms = Collections.unmodifiableMap(new HashMap<>(rooms));
        this.entrance = entrance;
        this.bossRoom = bossRoom;
    }

    /**
     * Pack two grid coordinates into a single long key for map storage.
     */
    public static long packGridPos(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    /**
     * Unpack the X coordinate from a packed grid key.
     */
    public static int unpackX(long key) {
        return (int) (key >> 32);
    }

    /**
     * Unpack the Z coordinate from a packed grid key.
     */
    public static int unpackZ(long key) {
        return (int) key;
    }

    /**
     * Get a room at the given grid coordinates.
     */
    @Nullable
    public RoomData getRoom(int gridX, int gridZ) {
        return rooms.get(packGridPos(gridX, gridZ));
    }

    /**
     * @return all rooms in this layout
     */
    public Collection<RoomData> getRooms() {
        return rooms.values();
    }

    /**
     * @return number of rooms in this layout
     */
    public int getRoomCount() {
        return rooms.size();
    }

    public RoomData getEntrance() {
        return entrance;
    }

    @Nullable
    public RoomData getBossRoom() {
        return bossRoom;
    }
}
