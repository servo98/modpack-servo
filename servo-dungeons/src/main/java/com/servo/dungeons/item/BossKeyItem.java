package com.servo.dungeons.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Consumable key item used to start a boss fight ritual at the Dungeon Pedestal.
 * Each key corresponds to a chapter (1-8) and summons the chapter's boss in a dedicated arena.
 */
public class BossKeyItem extends Item {

    private final int chapter;

    public BossKeyItem(int chapter) {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(chapter <= 4 ? Rarity.UNCOMMON : Rarity.RARE));
        this.chapter = chapter;
    }

    public int getChapter() {
        return chapter;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(Component.translatable("tooltip.servo_dungeons.boss_key_chapter", chapter)
                .withStyle(ChatFormatting.GOLD));
    }
}
