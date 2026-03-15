package com.servo.dungeons.dungeon;

/**
 * Type of room in a procedurally generated dungeon layout.
 * Determined by the number and direction of connections to neighboring rooms.
 */
public enum RoomType {
    /** Starting room at grid (0,0). Contains the Exit Portal. */
    ENTRANCE,
    /** 2 connections in a straight line (N-S or E-W). */
    HALLWAY,
    /** 2 connections at a right angle. */
    CORNER,
    /** 3 connections. */
    T_JUNCTION,
    /** 4 connections. */
    CROSS,
    /** 1 connection, end of a path. */
    DEAD_END,
    /** 1 connection, farthest room from entrance. Only in CORE tier dungeons. */
    BOSS
}
