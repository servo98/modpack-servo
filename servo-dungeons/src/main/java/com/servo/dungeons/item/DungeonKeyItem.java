package com.servo.dungeons.item;

import com.servo.dungeons.DungeonTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Consumable key item used to start a dungeon ritual at the Dungeon Pedestal.
 * Each key corresponds to a {@link DungeonTier} which determines dungeon difficulty and room count.
 */
public class DungeonKeyItem extends Item {

    private final DungeonTier tier;

    public DungeonKeyItem(DungeonTier tier) {
        super(new Item.Properties()
                .stacksTo(16)
                .rarity(tier.rarity));
        this.tier = tier;
    }

    public DungeonTier getTier() {
        return tier;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        // Tier name with colored text
        Component tierName = Component.translatable(tier.getTranslationKey())
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(tier.color)));
        tooltipComponents.add(Component.translatable("tooltip.servo_dungeons.key_tier", tierName)
                .withStyle(ChatFormatting.GRAY));

        // Room count range
        tooltipComponents.add(Component.translatable("tooltip.servo_dungeons.key_rooms", tier.minRooms, tier.maxRooms)
                .withStyle(ChatFormatting.DARK_GRAY));
    }
}
