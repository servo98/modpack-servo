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

/**
 * World-global delivery progress stored via SavedData.
 * All terminals read/write from this single instance, so progress
 * is shared across all terminals and survives terminal destruction.
 *
 * Stored in: data/servo_delivery_progress.dat (overworld)
 */
public class DeliverySavedData extends SavedData {

    private static final String DATA_NAME = "servo_delivery_progress";

    private int currentChapter = 1;
    private final Map<String, Integer> deliveredItems = new HashMap<>();

    public DeliverySavedData() {}

    public static DeliverySavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                new Factory<>(DeliverySavedData::new, DeliverySavedData::load),
                DATA_NAME
        );
    }

    public static DeliverySavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        DeliverySavedData data = new DeliverySavedData();
        data.currentChapter = tag.getInt("Chapter");
        if (data.currentChapter < 1) data.currentChapter = 1;

        ListTag deliveries = tag.getList("Deliveries", Tag.TAG_COMPOUND);
        for (int i = 0; i < deliveries.size(); i++) {
            CompoundTag itemTag = deliveries.getCompound(i);
            data.deliveredItems.put(itemTag.getString("Id"), itemTag.getInt("Count"));
        }

        ServoDelivery.LOGGER.info("Loaded delivery progress: chapter {}, {} items delivered",
                data.currentChapter, data.deliveredItems.size());
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("Chapter", currentChapter);

        ListTag deliveries = new ListTag();
        for (var entry : deliveredItems.entrySet()) {
            CompoundTag itemTag = new CompoundTag();
            itemTag.putString("Id", entry.getKey());
            itemTag.putInt("Count", entry.getValue());
            deliveries.add(itemTag);
        }
        tag.put("Deliveries", deliveries);
        return tag;
    }

    // === Accessors ===

    public int getCurrentChapter() { return currentChapter; }
    public Map<String, Integer> getDeliveredItems() { return deliveredItems; }

    public void deliverItem(String requirementId) {
        int current = deliveredItems.getOrDefault(requirementId, 0);
        deliveredItems.put(requirementId, current + 1);
        setDirty();
    }

    public void advanceChapter() {
        ServoDelivery.LOGGER.info("Chapter {} delivery complete! Advancing to {}",
                currentChapter, currentChapter + 1);
        currentChapter++;
        deliveredItems.clear();
        setDirty();
    }
}
