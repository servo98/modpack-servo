package com.servo.packaging.item;

import com.servo.packaging.PackagingRegistry;
import com.servo.packaging.component.BoxContents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Shipping Box — a sealed box containing packed items.
 * Stores contents via the BoxContents DataComponent.
 * The player sees a tooltip with what's inside.
 */
public class ShippingBoxItem extends Item {

    public ShippingBoxItem(Properties properties) {
        super(properties.stacksTo(16));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        BoxContents contents = stack.get(PackagingRegistry.BOX_CONTENTS.get());
        if (contents != null) {
            var item = BuiltInRegistries.ITEM.get(contents.itemId());
            String itemName = item.getDescription().getString();

            ChatFormatting color = getCategoryColor(contents.category());
            tooltip.add(Component.translatable("item.servo_packaging.shipping_box.contains",
                            contents.count(), itemName)
                    .withStyle(color));

            tooltip.add(Component.translatable("item.servo_packaging.shipping_box.hint")
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    /**
     * Create a shipping box with the given contents.
     */
    public static ItemStack createBox(ResourceLocation itemId, int count, String category) {
        ItemStack box = new ItemStack(PackagingRegistry.SHIPPING_BOX_ITEM.get());
        box.set(PackagingRegistry.BOX_CONTENTS.get(), new BoxContents(itemId, count, category));
        return box;
    }

    /**
     * Get the label color for a category.
     */
    public static ChatFormatting getCategoryColor(String category) {
        return switch (category) {
            case "food" -> ChatFormatting.GREEN;
            case "crops" -> ChatFormatting.YELLOW;
            case "processed" -> ChatFormatting.BLUE;
            case "magic" -> ChatFormatting.DARK_PURPLE;
            case "special" -> ChatFormatting.GOLD;
            default -> ChatFormatting.GRAY;
        };
    }
}
