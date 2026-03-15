package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonTier;
import net.minecraft.core.BlockPos;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Data class representing an active dungeon instance.
 * Tracks the dungeon tier, participating players, and positions.
 */
public class DungeonInstance {

    private final UUID id;
    private final DungeonTier tier;
    private final UUID leaderId;
    private final Set<UUID> playerIds;
    private final BlockPos altarPos;
    private final BlockPos entrancePos;
    private boolean active;

    public DungeonInstance(UUID id, DungeonTier tier, UUID leaderId, BlockPos altarPos, BlockPos entrancePos) {
        this.id = id;
        this.tier = tier;
        this.leaderId = leaderId;
        this.playerIds = new HashSet<>();
        this.playerIds.add(leaderId);
        this.altarPos = altarPos;
        this.entrancePos = entrancePos;
        this.active = true;
    }

    public UUID getId() {
        return id;
    }

    public DungeonTier getTier() {
        return tier;
    }

    public UUID getLeaderId() {
        return leaderId;
    }

    public Set<UUID> getPlayerIds() {
        return playerIds;
    }

    public BlockPos getAltarPos() {
        return altarPos;
    }

    public BlockPos getEntrancePos() {
        return entrancePos;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addPlayer(UUID playerId) {
        playerIds.add(playerId);
    }

    public boolean hasPlayer(UUID playerId) {
        return playerIds.contains(playerId);
    }
}
