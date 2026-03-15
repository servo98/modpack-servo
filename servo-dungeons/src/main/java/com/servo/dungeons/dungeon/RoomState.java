package com.servo.dungeons.dungeon;

/**
 * Tracks the progression state of a single room within a dungeon instance.
 */
public enum RoomState {
    /** Player hasn't entered yet, door to next room closed. */
    LOCKED,
    /** Player entered, mobs spawned, fight in progress. */
    ACTIVE,
    /** All mobs dead and all spawns exhausted, doors open. */
    CLEARED
}
