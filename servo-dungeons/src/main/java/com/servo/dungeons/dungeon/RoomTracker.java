package com.servo.dungeons.dungeon;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks the state of each room in a dungeon instance.
 * Uses packed grid positions as keys (see {@link DungeonLayout#packGridPos}).
 */
public class RoomTracker {

    private final Map<Long, RoomState> roomStates;
    private final Map<Long, Integer> livingMobs;

    /**
     * Initialize room tracking for the given layout.
     * All rooms start as {@link RoomState#LOCKED} except the entrance, which starts as
     * {@link RoomState#CLEARED} (no mobs spawn in the entrance room).
     *
     * @param layout the dungeon layout to track
     */
    public RoomTracker(DungeonLayout layout) {
        this.roomStates = new HashMap<>();
        this.livingMobs = new HashMap<>();

        for (RoomData room : layout.getRooms()) {
            long key = DungeonLayout.packGridPos(room.getGridX(), room.getGridZ());
            if (room.getType() == RoomType.ENTRANCE) {
                roomStates.put(key, RoomState.CLEARED);
            } else {
                roomStates.put(key, RoomState.LOCKED);
            }
            livingMobs.put(key, 0);
        }
    }

    /**
     * Get the current state of the room at the given grid position.
     *
     * @return the room state, or null if no room exists at that position
     */
    public RoomState getState(int gridX, int gridZ) {
        return roomStates.get(DungeonLayout.packGridPos(gridX, gridZ));
    }

    /**
     * Transition a room from LOCKED to ACTIVE.
     * Called when a player first enters a locked room.
     */
    public void activateRoom(int gridX, int gridZ) {
        long key = DungeonLayout.packGridPos(gridX, gridZ);
        RoomState current = roomStates.get(key);
        if (current == RoomState.LOCKED) {
            roomStates.put(key, RoomState.ACTIVE);
        }
    }

    /**
     * Set the number of living mobs in a room.
     * Called after spawning mobs in an activated room.
     */
    public void setLivingMobs(int gridX, int gridZ, int count) {
        long key = DungeonLayout.packGridPos(gridX, gridZ);
        livingMobs.put(key, count);
    }

    /**
     * Decrement the living mob count for a room by one.
     * Called when a mob dies inside a dungeon room.
     */
    public void onMobKilled(int gridX, int gridZ) {
        long key = DungeonLayout.packGridPos(gridX, gridZ);
        int current = livingMobs.getOrDefault(key, 0);
        if (current > 0) {
            livingMobs.put(key, current - 1);
        }
    }

    /**
     * Check if a room is ready to be cleared (all mobs dead and room is active).
     *
     * @return true if the room is ACTIVE and has 0 living mobs
     */
    public boolean isCleared(int gridX, int gridZ) {
        long key = DungeonLayout.packGridPos(gridX, gridZ);
        RoomState state = roomStates.get(key);
        int mobs = livingMobs.getOrDefault(key, 0);
        return state == RoomState.ACTIVE && mobs <= 0;
    }

    /**
     * Transition a room from ACTIVE to CLEARED.
     * Called when all mobs in the room have been killed.
     */
    public void markCleared(int gridX, int gridZ) {
        long key = DungeonLayout.packGridPos(gridX, gridZ);
        RoomState current = roomStates.get(key);
        if (current == RoomState.ACTIVE) {
            roomStates.put(key, RoomState.CLEARED);
        }
    }

    /**
     * Get the number of living mobs in a room.
     */
    public int getLivingMobs(int gridX, int gridZ) {
        long key = DungeonLayout.packGridPos(gridX, gridZ);
        return livingMobs.getOrDefault(key, 0);
    }

    /**
     * Check if ALL rooms in the dungeon are CLEARED.
     */
    public boolean isAllCleared() {
        for (RoomState state : roomStates.values()) {
            if (state != RoomState.CLEARED) return false;
        }
        return true;
    }
}
