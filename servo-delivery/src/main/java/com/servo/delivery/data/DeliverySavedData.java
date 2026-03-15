package com.servo.delivery.data;

import com.servo.delivery.ServoDelivery;
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
 * Per-team delivery progress stored via SavedData.
 * Each FTB Team (or solo player if FTB Teams is absent) has its own
 * chapter + delivered items. Progress survives terminal destruction.
 *
 * Stored in: data/servo_delivery_progress.dat (overworld)
 */
public class DeliverySavedData extends SavedData {

    private static final String DATA_NAME = "servo_delivery_progress";

    /** UUID used when migrating legacy (pre-team) data. */
    static final UUID LEGACY_TEAM_UUID = new UUID(0L, 0L);

    private final Map<UUID, TeamProgress> teamData = new HashMap<>();

    public DeliverySavedData() {}

    public static DeliverySavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                new Factory<>(DeliverySavedData::new, DeliverySavedData::load),
                DATA_NAME
        );
    }

    public static DeliverySavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        DeliverySavedData data = new DeliverySavedData();

        // Legacy format: root has "Chapter" + "Deliveries" directly
        if (tag.contains("Chapter", Tag.TAG_INT) && !tag.contains("Teams", Tag.TAG_LIST)) {
            ServoDelivery.LOGGER.info("Migrating legacy delivery progress to per-team format");
            TeamProgress legacy = new TeamProgress();
            int chapter = tag.getInt("Chapter");
            if (chapter < 1) chapter = 1;

            Map<String, Integer> items = new HashMap<>();
            ListTag deliveries = tag.getList("Deliveries", Tag.TAG_COMPOUND);
            for (int i = 0; i < deliveries.size(); i++) {
                CompoundTag itemTag = deliveries.getCompound(i);
                items.put(itemTag.getString("Id"), itemTag.getInt("Count"));
            }

            legacy = new TeamProgress(chapter, items);
            data.teamData.put(LEGACY_TEAM_UUID, legacy);
            ServoDelivery.LOGGER.info("Legacy migration complete: chapter {}, {} items → team {}",
                    chapter, items.size(), LEGACY_TEAM_UUID);
        } else {
            // New format: Teams list
            ListTag teams = tag.getList("Teams", Tag.TAG_COMPOUND);
            for (int i = 0; i < teams.size(); i++) {
                CompoundTag teamTag = teams.getCompound(i);
                UUID teamId = teamTag.getUUID("TeamId");
                TeamProgress progress = TeamProgress.load(teamTag);
                data.teamData.put(teamId, progress);
            }
        }

        ServoDelivery.LOGGER.info("Loaded delivery progress for {} teams", data.teamData.size());
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag teams = new ListTag();
        for (var entry : teamData.entrySet()) {
            CompoundTag teamTag = entry.getValue().save();
            teamTag.putUUID("TeamId", entry.getKey());
            teams.add(teamTag);
        }
        tag.put("Teams", teams);
        return tag;
    }

    // === Per-team accessors ===

    public TeamProgress getOrCreate(UUID teamId) {
        return teamData.computeIfAbsent(teamId, id -> new TeamProgress());
    }

    public int getCurrentChapter(UUID teamId) {
        return getOrCreate(teamId).getCurrentChapter();
    }

    public Map<String, Integer> getDeliveredItems(UUID teamId) {
        return getOrCreate(teamId).getDeliveredItems();
    }

    public void deliverItem(UUID teamId, String requirementId) {
        getOrCreate(teamId).deliverItem(requirementId);
        setDirty();
    }

    public void advanceChapter(UUID teamId) {
        TeamProgress progress = getOrCreate(teamId);
        ServoDelivery.LOGGER.info("Team {} chapter {} delivery complete! Advancing to {}",
                teamId, progress.getCurrentChapter(), progress.getCurrentChapter() + 1);
        progress.advanceChapter();
        setDirty();
    }
}
