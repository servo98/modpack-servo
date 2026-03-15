package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonTier;
import com.servo.dungeons.ServoDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

/**
 * NeoForge SavedData that persists dungeon state to the world save.
 * Stores active dungeon instances and used offsets so they survive server restarts.
 */
public class DungeonSavedData extends SavedData {

    private static final String DATA_NAME = "servo_dungeons";

    private final Map<UUID, DungeonInstanceData> instances = new HashMap<>();
    private final Set<Integer> usedOffsets = new HashSet<>();

    /**
     * Serializable snapshot of a DungeonInstance's persistent state.
     * Layout and room tracker are intentionally omitted — the physical blocks
     * remain in the dimension, but the in-memory layout is lost on restart.
     */
    public static class DungeonInstanceData {
        public UUID id;
        public String tierName;
        public UUID leaderId;
        public BlockPos altarPos;
        public BlockPos entrancePos;
        public BlockPos center;
        public Set<UUID> playerIds;
        public long createdTime;

        public DungeonInstanceData() {
            this.playerIds = new HashSet<>();
        }
    }

    // ==================== Save ====================

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag instanceList = new ListTag();
        for (DungeonInstanceData data : instances.values()) {
            CompoundTag inst = new CompoundTag();
            inst.putUUID("Id", data.id);
            inst.putString("Tier", data.tierName);
            inst.putUUID("Leader", data.leaderId);
            inst.putInt("AltarX", data.altarPos.getX());
            inst.putInt("AltarY", data.altarPos.getY());
            inst.putInt("AltarZ", data.altarPos.getZ());
            inst.putInt("EntranceX", data.entrancePos.getX());
            inst.putInt("EntranceY", data.entrancePos.getY());
            inst.putInt("EntranceZ", data.entrancePos.getZ());
            inst.putInt("CenterX", data.center.getX());
            inst.putInt("CenterY", data.center.getY());
            inst.putInt("CenterZ", data.center.getZ());
            inst.putLong("CreatedTime", data.createdTime);

            // Save player IDs
            ListTag playerList = new ListTag();
            for (UUID playerId : data.playerIds) {
                CompoundTag playerTag = new CompoundTag();
                playerTag.putUUID("PlayerId", playerId);
                playerList.add(playerTag);
            }
            inst.put("Players", playerList);

            instanceList.add(inst);
        }
        tag.put("Instances", instanceList);

        // Save used offsets
        int[] offsets = usedOffsets.stream().mapToInt(Integer::intValue).toArray();
        tag.putIntArray("UsedOffsets", offsets);

        return tag;
    }

    // ==================== Load ====================

    public static DungeonSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        DungeonSavedData data = new DungeonSavedData();

        // Load instances
        ListTag instanceList = tag.getList("Instances", Tag.TAG_COMPOUND);
        for (int i = 0; i < instanceList.size(); i++) {
            CompoundTag inst = instanceList.getCompound(i);
            DungeonInstanceData instanceData = new DungeonInstanceData();
            instanceData.id = inst.getUUID("Id");
            instanceData.tierName = inst.getString("Tier");
            instanceData.leaderId = inst.getUUID("Leader");
            instanceData.altarPos = new BlockPos(
                    inst.getInt("AltarX"),
                    inst.getInt("AltarY"),
                    inst.getInt("AltarZ")
            );
            instanceData.entrancePos = new BlockPos(
                    inst.getInt("EntranceX"),
                    inst.getInt("EntranceY"),
                    inst.getInt("EntranceZ")
            );
            instanceData.center = new BlockPos(
                    inst.getInt("CenterX"),
                    inst.getInt("CenterY"),
                    inst.getInt("CenterZ")
            );
            instanceData.createdTime = inst.getLong("CreatedTime");

            // Load player IDs
            ListTag playerList = inst.getList("Players", Tag.TAG_COMPOUND);
            for (int j = 0; j < playerList.size(); j++) {
                CompoundTag playerTag = playerList.getCompound(j);
                instanceData.playerIds.add(playerTag.getUUID("PlayerId"));
            }

            data.instances.put(instanceData.id, instanceData);
        }

        // Load used offsets
        int[] offsets = tag.getIntArray("UsedOffsets");
        for (int offset : offsets) {
            data.usedOffsets.add(offset);
        }

        ServoDungeons.LOGGER.info("Loaded {} dungeon instances and {} used offsets from saved data",
                data.instances.size(), data.usedOffsets.size());

        return data;
    }

    // ==================== Factory ====================

    /**
     * Get or create the dungeon saved data from the overworld's data storage.
     */
    public static DungeonSavedData getOrCreate(ServerLevel overworld) {
        return overworld.getDataStorage().computeIfAbsent(
                new Factory<>(DungeonSavedData::new, DungeonSavedData::load),
                DATA_NAME
        );
    }

    // ==================== Sync Methods ====================

    /**
     * Save a DungeonInstance's current state to persistent storage.
     */
    public void saveInstance(DungeonInstance instance) {
        DungeonInstanceData data = new DungeonInstanceData();
        data.id = instance.getId();
        data.tierName = instance.getTier().name;
        data.leaderId = instance.getLeaderId();
        data.altarPos = instance.getAltarPos();
        data.entrancePos = instance.getEntrancePos();
        data.center = instance.getCenter();
        data.playerIds = new HashSet<>(instance.getPlayerIds());
        data.createdTime = instance.getCreatedTime();

        instances.put(data.id, data);

        // Also track the offset
        usedOffsets.add(instance.getCenter().getX());

        setDirty();
    }

    /**
     * Remove a dungeon instance from persistent storage.
     */
    public void removeInstance(UUID id) {
        DungeonInstanceData removed = instances.remove(id);
        if (removed != null) {
            usedOffsets.remove(removed.center.getX());
        }
        setDirty();
    }

    public Map<UUID, DungeonInstanceData> getInstances() {
        return instances;
    }

    public Set<Integer> getUsedOffsets() {
        return usedOffsets;
    }
}
