package com.servo.packaging.client;

import com.servo.packaging.PackagingRegistry;
import com.servo.packaging.component.BoxContents;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.IItemDecorator;

/**
 * Renders the contained item's icon on top of the ShippingBox item sprite in inventory.
 * The base shipping_box texture is a brown box; this overlay adds the content icon centered.
 */
public class ShippingBoxDecorator implements IItemDecorator {

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        BoxContents contents = stack.get(PackagingRegistry.BOX_CONTENTS.get());
        if (contents == null) return false;

        var item = BuiltInRegistries.ITEM.get(contents.itemId());
        if (item == Items.AIR) return false;

        ItemStack displayStack = new ItemStack(item);

        var pose = guiGraphics.pose();
        pose.pushPose();
        // Move to center of the 16x16 item area, above base item Z
        pose.translate(xOffset + 8.0f, yOffset + 8.0f, 100.0f);
        // Scale to 50% (renders 8x8 pixel icon)
        pose.scale(0.5f, 0.5f, 0.5f);
        // renderItem internally translates by (x+8, y+8), so pass (-8,-8) to center at origin
        guiGraphics.renderItem(displayStack, -8, -8);
        pose.popPose();

        return false; // don't suppress default decorations (stack count text)
    }
}
