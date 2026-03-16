package com.servo.core.progression;

import com.servo.core.ServoCore;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tracks per-team progression flags for boss kills and delivery completions.
 * servo_core uses this to determine when both conditions are met for a chapter,
 * then grants the next stage to all team members.
 *
 * <p>The two conditions can be completed in any order:
 * <ul>
 *     <li>{@code delivery_ch[N]} = true when DeliveryCompleteEvent fires for chapter N</li>
 *     <li>{@code boss_ch[N]} = true when the chapter N boss is killed (future: BossKillEvent)</li>
 * </ul>
 * When both are true for chapter N, servo_core grants {@code servo_ch[N+1]} to the team.
 *
 * <p>Stored in: data/servo_core_progression.dat (overworld)
 */
public class ChapterProgressionData extends SavedData {

    private static final String DATA_NAME = "servo_core_progression";
    public static final int MAX_CHAPTER = 8;

    /** Per-team progression state. */
    private final Map<UUID, TeamChapterState> teamStates = new HashMap<>();

    public ChapterProgressionData() {}

    public static ChapterProgressionData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                new Factory<>(ChapterProgressionData::new, ChapterProgressionData::load),
                DATA_NAME
        );
    }

    public static ChapterProgressionData load(CompoundTag tag, HolderLookup.Provider registries) {
        ChapterProgressionData data = new ChapterProgressionData();
        ListTag teams = tag.getList("Teams", Tag.TAG_COMPOUND);
        for (int i = 0; i < teams.size(); i++) {
            CompoundTag teamTag = teams.getCompound(i);
            UUID teamId = teamTag.getUUID("TeamId");
            TeamChapterState state = TeamChapterState.load(teamTag);
            data.teamStates.put(teamId, state);
        }
        ServoCore.LOGGER.info("Loaded chapter progression for {} teams", data.teamStates.size());
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag teams = new ListTag();
        for (var entry : teamStates.entrySet()) {
            CompoundTag teamTag = entry.getValue().save();
            teamTag.putUUID("TeamId", entry.getKey());
            teams.add(teamTag);
        }
        tag.put("Teams", teams);
        return tag;
    }

    // === Public API ===

    private TeamChapterState getOrCreate(UUID teamId) {
        return teamStates.computeIfAbsent(teamId, id -> new TeamChapterState());
    }

    /**
     * Marks delivery as complete for a chapter. Returns true if this is a NEW completion
     * (was not already marked).
     */
    public boolean markDeliveryComplete(UUID teamId, int chapter) {
        if (chapter < 1 || chapter > MAX_CHAPTER) return false;
        TeamChapterState state = getOrCreate(teamId);
        if (state.isDeliveryComplete(chapter)) return false;
        state.setDeliveryComplete(chapter, true);
        setDirty();
        return true;
    }

    /**
     * Marks boss as killed for a chapter. Returns true if this is a NEW completion.
     */
    public boolean markBossKilled(UUID teamId, int chapter) {
        if (chapter < 1 || chapter > MAX_CHAPTER) return false;
        TeamChapterState state = getOrCreate(teamId);
        if (state.isBossKilled(chapter)) return false;
        state.setBossKilled(chapter, true);
        setDirty();
        return true;
    }

    public boolean isDeliveryComplete(UUID teamId, int chapter) {
        return getOrCreate(teamId).isDeliveryComplete(chapter);
    }

    public boolean isBossKilled(UUID teamId, int chapter) {
        return getOrCreate(teamId).isBossKilled(chapter);
    }

    /**
     * Returns true if both delivery AND boss are complete for the given chapter.
     */
    public boolean isChapterFullyComplete(UUID teamId, int chapter) {
        TeamChapterState state = getOrCreate(teamId);
        return state.isDeliveryComplete(chapter) && state.isBossKilled(chapter);
    }

    /**
     * Returns true if the next stage has already been granted for this chapter.
     */
    public boolean isStageGranted(UUID teamId, int chapter) {
        return getOrCreate(teamId).isStageGranted(chapter);
    }

    /**
     * Marks the stage as granted for this chapter (prevents double-granting).
     */
    public void markStageGranted(UUID teamId, int chapter) {
        getOrCreate(teamId).setStageGranted(chapter, true);
        setDirty();
    }

    // === Inner state class ===

    private static class TeamChapterState {
        // Arrays indexed 0-7 for chapters 1-8
        private final boolean[] deliveryComplete = new boolean[MAX_CHAPTER];
        private final boolean[] bossKilled = new boolean[MAX_CHAPTER];
        private final boolean[] stageGranted = new boolean[MAX_CHAPTER];

        boolean isDeliveryComplete(int chapter) { return deliveryComplete[chapter - 1]; }
        void setDeliveryComplete(int chapter, boolean val) { deliveryComplete[chapter - 1] = val; }

        boolean isBossKilled(int chapter) { return bossKilled[chapter - 1]; }
        void setBossKilled(int chapter, boolean val) { bossKilled[chapter - 1] = val; }

        boolean isStageGranted(int chapter) { return stageGranted[chapter - 1]; }
        void setStageGranted(int chapter, boolean val) { stageGranted[chapter - 1] = val; }

        CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putByteArray("DeliveryComplete", toByteArray(deliveryComplete));
            tag.putByteArray("BossKilled", toByteArray(bossKilled));
            tag.putByteArray("StageGranted", toByteArray(stageGranted));
            return tag;
        }

        static TeamChapterState load(CompoundTag tag) {
            TeamChapterState state = new TeamChapterState();
            fromByteArray(tag.getByteArray("DeliveryComplete"), state.deliveryComplete);
            fromByteArray(tag.getByteArray("BossKilled"), state.bossKilled);
            fromByteArray(tag.getByteArray("StageGranted"), state.stageGranted);
            return state;
        }

        private static byte[] toByteArray(boolean[] booleans) {
            byte[] bytes = new byte[booleans.length];
            for (int i = 0; i < booleans.length; i++) {
                bytes[i] = (byte) (booleans[i] ? 1 : 0);
            }
            return bytes;
        }

        private static void fromByteArray(byte[] bytes, boolean[] target) {
            int len = Math.min(bytes.length, target.length);
            for (int i = 0; i < len; i++) {
                target[i] = bytes[i] != 0;
            }
        }
    }
}
