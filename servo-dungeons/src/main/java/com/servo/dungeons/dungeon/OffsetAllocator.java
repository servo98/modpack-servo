package com.servo.dungeons.dungeon;

import com.servo.dungeons.ServoDungeons;
import net.minecraft.core.BlockPos;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple allocator that hands out X-coordinate offsets for dungeon instances,
 * separated by 10,000 blocks in the same void dimension.
 * <p>
 * Each allocation returns a BlockPos center at (X, 64, 0) where X is a multiple of 10,000.
 * Thread-safety is not needed since the server is single-threaded.
 */
public class OffsetAllocator {

    /** Distance between dungeon instance centers along the X axis. */
    private static final int OFFSET_SPACING = 10_000;

    /** Y coordinate for all dungeon centers. */
    private static final int CENTER_Y = 64;

    /** Z coordinate for all dungeon centers. */
    private static final int CENTER_Z = 0;

    /** Set of currently used X offsets. */
    private final Set<Integer> usedOffsets = new HashSet<>();

    /** Next candidate offset to try when all lower ones are taken. */
    private int nextCandidate = 0;

    /**
     * Allocate the next available center position for a dungeon instance.
     *
     * @return a BlockPos at (X, 64, 0) where X is a multiple of {@value OFFSET_SPACING}
     */
    public BlockPos allocate() {
        // Try to find the lowest available offset
        int offset = 0;
        while (usedOffsets.contains(offset)) {
            offset += OFFSET_SPACING;
        }

        usedOffsets.add(offset);

        // Update nextCandidate if needed
        if (offset >= nextCandidate) {
            nextCandidate = offset + OFFSET_SPACING;
        }

        BlockPos center = new BlockPos(offset, CENTER_Y, CENTER_Z);
        ServoDungeons.LOGGER.debug("OffsetAllocator: allocated center at {}", center);
        return center;
    }

    /**
     * Release a previously allocated center, making that offset available for reuse.
     *
     * @param center the center BlockPos that was returned by {@link #allocate()}
     */
    public void release(BlockPos center) {
        int offset = center.getX();
        if (usedOffsets.remove(offset)) {
            ServoDungeons.LOGGER.debug("OffsetAllocator: released offset at X={}", offset);
        } else {
            ServoDungeons.LOGGER.warn("OffsetAllocator: tried to release unknown offset at X={}", offset);
        }
    }

    /**
     * Restore the allocator state from a set of previously used offsets.
     * Used when loading from SavedData after a server restart.
     *
     * @param offsets the set of X offsets that are currently in use
     */
    public void restore(Set<Integer> offsets) {
        usedOffsets.clear();
        usedOffsets.addAll(offsets);

        // Recalculate nextCandidate to be past the highest used offset
        nextCandidate = 0;
        for (int offset : usedOffsets) {
            if (offset >= nextCandidate) {
                nextCandidate = offset + OFFSET_SPACING;
            }
        }

        ServoDungeons.LOGGER.debug("OffsetAllocator: restored {} used offsets, nextCandidate={}",
                usedOffsets.size(), nextCandidate);
    }

    /**
     * @return the number of currently allocated offsets
     */
    public int getActiveCount() {
        return usedOffsets.size();
    }
}
