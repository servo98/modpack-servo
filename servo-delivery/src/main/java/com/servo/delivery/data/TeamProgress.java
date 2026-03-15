package com.servo.delivery.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * Mutable POJO holding delivery progress for a single team.
 * Serialized as part of {@link DeliverySavedData}.
 */
public class TeamProgress {

    private int currentChapter;
    private final Map<String, Integer> deliveredItems;

    public TeamProgress() {
        this(1, new HashMap<>());
    }

    public TeamProgress(int currentChapter, Map<String, Integer> deliveredItems) {
        this.currentChapter = currentChapter;
        this.deliveredItems = deliveredItems;
    }

    // === Accessors ===

    public int getCurrentChapter() { return currentChapter; }
    public Map<String, Integer> getDeliveredItems() { return deliveredItems; }

    public void deliverItem(String requirementId) {
        int current = deliveredItems.getOrDefault(requirementId, 0);
        deliveredItems.put(requirementId, current + 1);
    }

    public void advanceChapter() {
        currentChapter++;
        deliveredItems.clear();
    }

    // === Serialization ===

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
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

    public static TeamProgress load(CompoundTag tag) {
        int chapter = tag.getInt("Chapter");
        if (chapter < 1) chapter = 1;

        Map<String, Integer> items = new HashMap<>();
        ListTag deliveries = tag.getList("Deliveries", Tag.TAG_COMPOUND);
        for (int i = 0; i < deliveries.size(); i++) {
            CompoundTag itemTag = deliveries.getCompound(i);
            items.put(itemTag.getString("Id"), itemTag.getInt("Count"));
        }

        return new TeamProgress(chapter, items);
    }
}
