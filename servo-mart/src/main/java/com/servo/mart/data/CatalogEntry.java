package com.servo.mart.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A single entry in the PepeMart catalog.
 * Loaded from JSON datapacks at data/servo_mart/catalog/*.json
 */
public record CatalogEntry(
        ResourceLocation item,
        String category,
        int tier,
        Map<ResourceLocation, Integer> price,
        String requiredStage
) {

    public static CatalogEntry fromJson(JsonObject json) {
        ResourceLocation item = ResourceLocation.parse(json.get("item").getAsString());
        String category = json.has("category") ? json.get("category").getAsString() : "misc";
        int tier = json.has("tier") ? json.get("tier").getAsInt() : 1;
        String stage = json.has("requires_stage") ? json.get("requires_stage").getAsString() : "";

        Map<ResourceLocation, Integer> price = new LinkedHashMap<>();
        if (json.has("price")) {
            JsonObject priceObj = json.getAsJsonObject("price");
            for (Map.Entry<String, JsonElement> entry : priceObj.entrySet()) {
                price.put(ResourceLocation.parse(entry.getKey()), entry.getValue().getAsInt());
            }
        }

        return new CatalogEntry(item, category, tier, price, stage);
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(item);
        buf.writeUtf(category);
        buf.writeVarInt(tier);
        buf.writeUtf(requiredStage);
        buf.writeVarInt(price.size());
        for (Map.Entry<ResourceLocation, Integer> entry : price.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            buf.writeVarInt(entry.getValue());
        }
    }

    public static CatalogEntry fromNetwork(FriendlyByteBuf buf) {
        ResourceLocation item = buf.readResourceLocation();
        String category = buf.readUtf();
        int tier = buf.readVarInt();
        String stage = buf.readUtf();
        int priceCount = buf.readVarInt();
        Map<ResourceLocation, Integer> price = new LinkedHashMap<>();
        for (int i = 0; i < priceCount; i++) {
            price.put(buf.readResourceLocation(), buf.readVarInt());
        }
        return new CatalogEntry(item, category, tier, price, stage);
    }
}
