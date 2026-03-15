package com.servo.dungeons;

import net.minecraft.world.item.Rarity;

public enum DungeonTier {
    BASIC(1, 5, 7, "basic", 0xFFFFFF, Rarity.COMMON),
    ADVANCED(2, 10, 14, "advanced", 0x5555FF, Rarity.UNCOMMON),
    MASTER(3, 15, 20, "master", 0xAA00AA, Rarity.RARE),
    CORE(4, 20, 25, "core", 0xFFAA00, Rarity.EPIC);

    public final int tier;
    public final int minRooms;
    public final int maxRooms;
    public final String name;
    public final int color;
    public final Rarity rarity;

    DungeonTier(int tier, int minRooms, int maxRooms, String name, int color, Rarity rarity) {
        this.tier = tier;
        this.minRooms = minRooms;
        this.maxRooms = maxRooms;
        this.name = name;
        this.color = color;
        this.rarity = rarity;
    }

    /**
     * Get the translation key for this tier's display name.
     */
    public String getTranslationKey() {
        return "tooltip.servo_dungeons.tier." + name;
    }
}
