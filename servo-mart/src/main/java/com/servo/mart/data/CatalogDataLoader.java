package com.servo.mart.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.servo.mart.ServoMart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.*;

/**
 * Loads PepeMart catalog entries from datapacks.
 * Reads from: data/servo_mart/catalog/*.json
 *
 * Each JSON file contains: { "entries": [ { CatalogEntry... }, ... ] }
 * Reloads on /reload.
 */
public class CatalogDataLoader extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final List<CatalogEntry> ALL_ENTRIES = new ArrayList<>();
    private static final Map<String, List<CatalogEntry>> BY_CATEGORY = new LinkedHashMap<>();

    public CatalogDataLoader() {
        super(GSON, "catalog");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> entries,
                         ResourceManager manager, ProfilerFiller profiler) {
        ALL_ENTRIES.clear();
        BY_CATEGORY.clear();

        for (var entry : entries.entrySet()) {
            try {
                JsonArray arr = entry.getValue().getAsJsonObject().getAsJsonArray("entries");
                if (arr == null) continue;

                for (JsonElement elem : arr) {
                    CatalogEntry catalogEntry = CatalogEntry.fromJson(elem.getAsJsonObject());
                    ALL_ENTRIES.add(catalogEntry);
                    BY_CATEGORY.computeIfAbsent(catalogEntry.category(), k -> new ArrayList<>())
                            .add(catalogEntry);
                }
            } catch (Exception e) {
                ServoMart.LOGGER.error("Failed to load catalog file: {}", entry.getKey(), e);
            }
        }

        ServoMart.LOGGER.info("Loaded {} PepeMart catalog entries in {} categories",
                ALL_ENTRIES.size(), BY_CATEGORY.size());
    }

    public static List<CatalogEntry> getAllEntries() {
        return Collections.unmodifiableList(ALL_ENTRIES);
    }

    public static List<CatalogEntry> getEntriesByCategory(String category) {
        return BY_CATEGORY.getOrDefault(category, Collections.emptyList());
    }

    public static Set<String> getCategories() {
        return BY_CATEGORY.keySet();
    }
}
