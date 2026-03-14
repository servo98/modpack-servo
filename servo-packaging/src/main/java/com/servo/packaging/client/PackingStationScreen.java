package com.servo.packaging.client;

import com.servo.packaging.block.PackingStationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the Packing Station.
 * Simple layout: input slot (flat cardboard) on the left, progress arrow, output slot (open box) on the right.
 */
public class PackingStationScreen extends AbstractContainerScreen<PackingStationMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("servo_packaging", "textures/gui/packing_station.png");

    // Arrow position in the GUI (where the empty arrow background is drawn)
    private static final int ARROW_X = 79;
    private static final int ARROW_Y = 35;
    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 16;

    // Arrow source in the texture (the filled/progress arrow sprite)
    private static final int ARROW_TEX_X = 176;
    private static final int ARROW_TEX_Y = 14;

    public PackingStationScreen(PackingStationMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        // Draw the background texture
        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // Draw the progress arrow
        float progress = this.menu.getFoldProgress();
        if (progress > 0) {
            int progressPixels = (int) (progress * ARROW_WIDTH);
            graphics.blit(TEXTURE, x + ARROW_X, y + ARROW_Y,
                    ARROW_TEX_X, ARROW_TEX_Y,
                    progressPixels, ARROW_HEIGHT);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
