package com.servo.delivery.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.servo.packaging.PackagingRegistry;
import com.servo.packaging.component.BoxContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents the delivery requirements for one chapter.
 * Loaded from data packs: data/servo_delivery/delivery/chapter_1.json etc.
 *
 * Example JSON:
 * <pre>
 * {
 *   "chapter": 1,
 *   "name": "Primeras Raices",
 *   "requirements": [
 *     { "id": "vegetable_soup_box", "item": "servo_packaging:shipping_box", "nbt_match": "vegetable_soup", "count": 4, "description": "Caja de Vegetable Soup" },
 *     { "id": "guardian_root", "item": "servo_dungeons:guardian_root", "count": 1, "direct": true, "description": "Raiz del Guardian" }
 *   ],
 *   "stage_granted": "servo_delivery_ch1"
 * }
 * </pre>
 */
public class ChapterDelivery {

    private final int chapter;
    private final String name;
    private final List<Requirement> requirements;
    private final String stageGranted;

    public ChapterDelivery(int chapter, String name, List<Requirement> requirements, String stageGranted) {
        this.chapter = chapter;
        this.name = name;
        this.requirements = requirements;
        this.stageGranted = stageGranted;
    }

    public int getChapter() { return chapter; }
    public String getName() { return name; }
    public List<Requirement> getRequirements() { return requirements; }
    public String getStageGranted() { return stageGranted; }

    /**
     * Finds a requirement that matches the given item and hasn't been fully delivered yet.
     *
     * @return the requirement ID if matched, null if not needed
     */
    public String findMatchingRequirement(ItemStack stack, Map<String, Integer> delivered) {
        for (Requirement req : requirements) {
            int current = delivered.getOrDefault(req.id(), 0);
            if (current >= req.count()) continue; // already fulfilled

            if (req.matches(stack)) {
                return req.id();
            }
        }
        return null;
    }

    /**
     * Checks if all requirements have been met.
     */
    public boolean isComplete(Map<String, Integer> delivered) {
        for (Requirement req : requirements) {
            int current = delivered.getOrDefault(req.id(), 0);
            if (current < req.count()) return false;
        }
        return true;
    }

    /**
     * Returns the overall progress percentage (0-100).
     */
    public int getProgressPercent(Map<String, Integer> delivered) {
        int totalRequired = 0;
        int totalDelivered = 0;
        for (Requirement req : requirements) {
            totalRequired += req.count();
            totalDelivered += Math.min(delivered.getOrDefault(req.id(), 0), req.count());
        }
        if (totalRequired == 0) return 100;
        return (totalDelivered * 100) / totalRequired;
    }

    /**
     * Returns the total number of items required.
     */
    public int getTotalRequired() {
        return requirements.stream().mapToInt(Requirement::count).sum();
    }

    // === Parsing from JSON ===

    public static ChapterDelivery fromJson(JsonObject json) {
        int chapter = json.get("chapter").getAsInt();
        String name = json.get("name").getAsString();
        String stage = json.has("stage_granted") ? json.get("stage_granted").getAsString()
                : "servo_delivery_ch" + chapter;

        List<Requirement> reqs = new ArrayList<>();
        JsonArray reqsArray = json.getAsJsonArray("requirements");
        for (JsonElement element : reqsArray) {
            reqs.add(Requirement.fromJson(element.getAsJsonObject()));
        }

        return new ChapterDelivery(chapter, name, reqs, stage);
    }

    // === Requirement ===

    /**
     * A single delivery requirement.
     *
     * @param id          unique ID within the chapter (e.g., "vegetable_soup_box")
     * @param item        the item ResourceLocation to match (e.g., "servo_packaging:shipping_box")
     * @param contentTag  if set, matches the box content type (for shipping boxes)
     * @param count       how many are needed
     * @param direct      if true, item goes in directly (not in a box) — for boss drops
     * @param description human-readable description for the GUI
     */
    public record Requirement(
            String id,
            ResourceLocation item,
            String contentTag,
            int count,
            boolean direct,
            String description
    ) {
        public boolean matches(ItemStack stack) {
            // Check if the item type matches
            ResourceLocation stackId = stack.getItemHolder().unwrapKey()
                    .map(key -> key.location())
                    .orElse(null);
            if (stackId == null || !stackId.equals(item)) return false;

            // If this is a direct drop (boss item), no further checks needed
            if (direct) return true;

            // For shipping boxes, check content tag matches against BoxContents
            if (contentTag != null && !contentTag.isEmpty()) {
                BoxContents contents = stack.get(PackagingRegistry.BOX_CONTENTS.get());
                if (contents == null) return false;

                if (contentTag.startsWith("category/")) {
                    // Category match: "category/food" matches any box with category "food"
                    String requiredCategory = contentTag.substring("category/".length());
                    return requiredCategory.equals(contents.category());
                } else {
                    // Exact item match: "farmersdelight:vegetable_soup" matches that specific item
                    ResourceLocation requiredItem = ResourceLocation.parse(contentTag);
                    return requiredItem.equals(contents.itemId());
                }
            }

            return true;
        }

        public static Requirement fromJson(JsonObject json) {
            return new Requirement(
                    json.get("id").getAsString(),
                    ResourceLocation.parse(json.get("item").getAsString()),
                    json.has("content_tag") ? json.get("content_tag").getAsString() : null,
                    json.get("count").getAsInt(),
                    json.has("direct") && json.get("direct").getAsBoolean(),
                    json.has("description") ? json.get("description").getAsString() : ""
            );
        }
    }
}
