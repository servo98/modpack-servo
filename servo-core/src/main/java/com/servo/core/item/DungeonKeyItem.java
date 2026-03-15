package com.servo.core.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Dungeon key consumed on dungeon portal use.
 * 4 tiers: basic (common), advanced (uncommon), master (rare), core (epic).
 */
public class DungeonKeyItem extends Item {

    private final int tier;

    public DungeonKeyItem(int tier, Rarity rarity) {
        super(new Item.Properties().stacksTo(16).rarity(rarity));
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return tier >= 4;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        String key = "item.servo_core.dungeon_key.tier_" + tier;
        tooltip.add(Component.translatable(key).withStyle(ChatFormatting.GRAY));
    }
}
