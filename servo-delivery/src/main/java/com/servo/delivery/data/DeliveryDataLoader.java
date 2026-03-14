package com.servo.delivery.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.servo.delivery.ServoDelivery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads chapter delivery definitions from data packs.
 * Reads from: data/servo_delivery/delivery/*.json
 *
 * This is a server-side reload listener. Definitions reload on /reload.
 * Modpackers can override or add chapters via datapacks.
 */
public class DeliveryDataLoader extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // chapter number -> definition
    private static final Map<Integer, ChapterDelivery> CHAPTERS = new HashMap<>();

    public DeliveryDataLoader() {
        super(GSON, "delivery");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> entries,
                         ResourceManager manager, ProfilerFiller profiler) {
        CHAPTERS.clear();

        for (var entry : entries.entrySet()) {
            try {
                ChapterDelivery chapter = ChapterDelivery.fromJson(entry.getValue().getAsJsonObject());
                CHAPTERS.put(chapter.getChapter(), chapter);
                ServoDelivery.LOGGER.info("Loaded delivery definition: chapter {} ({}) - {} requirements",
                        chapter.getChapter(), chapter.getName(), chapter.getRequirements().size());
            } catch (Exception e) {
                ServoDelivery.LOGGER.error("Failed to load delivery definition: {}", entry.getKey(), e);
            }
        }

        ServoDelivery.LOGGER.info("Loaded {} chapter delivery definitions", CHAPTERS.size());
    }

    public static @Nullable ChapterDelivery getChapter(int chapter) {
        return CHAPTERS.get(chapter);
    }

    public static int getMaxChapter() {
        return CHAPTERS.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
    }
}
