package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonTier;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Data class representing an active dungeon instance.
 * Tracks the dungeon tier, participating players, positions, and offset center.
 */
public class DungeonInstance {

    /** Half-width of the area assigned to each dungeon instance along X/Z. */
    private static final int AREA_HALF_SIZE = 5000;

    private final UUID id;
    private final DungeonTier tier;
    private final UUID leaderId;
    private final Set<UUID> playerIds;
    private final BlockPos altarPos;
    private final BlockPos entrancePos;
    private final BlockPos center;
    @Nullable
    private DungeonLayout layout;
    @Nullable
    private RoomTracker roomTracker;
    private boolean active;
    private boolean bossFight;
    private int bossChapter = -1;
    private final long createdTime;

    public DungeonInstance(UUID id, DungeonTier tier, UUID leaderId, BlockPos altarPos, BlockPos entrancePos, BlockPos center) {
        this(id, tier, leaderId, altarPos, entrancePos, center, -1);
    }

    public DungeonInstance(UUID id, DungeonTier tier, UUID leaderId, BlockPos altarPos, BlockPos entrancePos, BlockPos center, long createdTime) {
        this.id = id;
        this.tier = tier;
        this.leaderId = leaderId;
        this.playerIds = new HashSet<>();
        this.playerIds.add(leaderId);
        this.altarPos = altarPos;
        this.entrancePos = entrancePos;
        this.center = center;
        this.active = true;
        this.createdTime = createdTime;
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

    public BlockPos getCenter() {
        return center;
    }

    @Nullable
    public DungeonLayout getLayout() {
        return layout;
    }

    public void setLayout(DungeonLayout layout) {
        this.layout = layout;
    }

    @Nullable
    public RoomTracker getRoomTracker() {
        return roomTracker;
    }

    public void setRoomTracker(RoomTracker roomTracker) {
        this.roomTracker = roomTracker;
    }

    public boolean isBossFight() {
        return bossFight;
    }

    public void setBossFight(boolean bossFight) {
        this.bossFight = bossFight;
    }

    public int getBossChapter() {
        return bossChapter;
    }

    public void setBossChapter(int bossChapter) {
        this.bossChapter = bossChapter;
    }

    /**
     * Check if a position is within this instance's area.
     * Uses a half-size of {@value AREA_HALF_SIZE} blocks along X and Z from the center.
     */
    public boolean isPlayerInArea(BlockPos pos) {
        return Math.abs(pos.getX() - center.getX()) <= AREA_HALF_SIZE
                && Math.abs(pos.getZ() - center.getZ()) <= AREA_HALF_SIZE;
    }

    public long getCreatedTime() {
        return createdTime;
    }
}
