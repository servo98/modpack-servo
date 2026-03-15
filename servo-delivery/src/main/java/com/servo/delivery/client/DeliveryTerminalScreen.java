package com.servo.delivery.client;

import com.servo.delivery.block.DeliveryTerminalMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * GUI Screen for the Delivery Terminal.
 * Shows chapter requirements with item icons, progress bars, and a LAUNCH lever.
 */
public class DeliveryTerminalScreen extends AbstractContainerScreen<DeliveryTerminalMenu> {

    private static final int BG_COLOR = 0xCC1a1a2e;       // dark blue-gray, semi-transparent
    private static final int PANEL_COLOR = 0xFF16213e;     // slightly lighter panel
    private static final int BAR_BG = 0xFF2c2c3a;         // progress bar background
    private static final int BAR_FILL = 0xFF00d4aa;       // teal progress fill
    private static final int BAR_COMPLETE = 0xFF00ff88;   // green when complete
    private static final int TITLE_COLOR = 0xFF00d4aa;    // teal title
    private static final int TEXT_COLOR = 0xFFe0e0e0;     // light gray text
    private static final int DIM_COLOR = 0xFF808080;      // dimmed text
    private static final int CHECK_COLOR = 0xFF00ff88;    // green checkmark
    private static final int LAUNCH_READY = 0xFF00ff88;
    private static final int LAUNCH_NOT_READY = 0xFF555555;

    private Button launchButton;
    private List<Component> hoveredTooltip;
    private int tooltipX;
    private int tooltipY;

    public DeliveryTerminalScreen(DeliveryTerminalMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 220;
        this.imageHeight = 40 + menu.getRequirements().size() * 28 + 50; // dynamic height
        if (this.imageHeight > 240) this.imageHeight = 240;
    }

    @Override
    protected void init() {
        super.init();
        // Hide inventory label
        this.inventoryLabelY = -999;
        this.titleLabelY = -999;

        // Launch button
        int buttonW = 120;
        int buttonH = 20;
        int buttonX = this.leftPos + (this.imageWidth - buttonW) / 2;
        int buttonY = this.topPos + this.imageHeight - 32;

        launchButton = Button.builder(Component.literal("LAUNCH"), btn -> {
            if (menu.isReady() && minecraft != null && minecraft.gameMode != null) {
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
                onClose();
            }
        }).bounds(buttonX, buttonY, buttonW, buttonH).build();
        launchButton.active = false; // starts disabled until server confirms ready
        addRenderableWidget(launchButton);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        launchButton.active = menu.isReady();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Dark overlay
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        // Clear tooltip from previous frame
        hoveredTooltip = null;

        int x = this.leftPos;
        int y = this.topPos;
        int w = this.imageWidth;
        int h = this.imageHeight;

        // Main panel background
        graphics.fill(x, y, x + w, y + h, BG_COLOR);
        graphics.fill(x + 2, y + 2, x + w - 2, y + h - 2, PANEL_COLOR);

        // Title bar
        String title = "Chapter " + menu.getChapter() + ": " + menu.getChapterName();
        graphics.drawCenteredString(this.font, title, x + w / 2, y + 8, TITLE_COLOR);

        // Progress percentage
        String progress = menu.getProgress() + "% Complete";
        graphics.drawCenteredString(this.font, progress, x + w / 2, y + 20, TEXT_COLOR);

        // Requirements list
        int reqY = y + 36;
        var reqs = menu.getRequirements();
        for (int i = 0; i < reqs.size(); i++) {
            renderRequirement(graphics, x + 8, reqY, w - 16, reqs.get(i), i, mouseX, mouseY);
            reqY += 28;
        }

        // Launch button styling
        int btnY = y + h - 32;
        if (menu.isReady()) {
            // Glow effect around button when ready
            graphics.fill(x + (w - 124) / 2, btnY - 2, x + (w + 124) / 2, btnY + 22, 0x4400ff88);
        }

        // Render tooltip last so it draws on top of everything
        if (hoveredTooltip != null) {
            graphics.renderTooltip(this.font, hoveredTooltip, Optional.empty(), tooltipX, tooltipY);
        }
    }

    private void renderRequirement(GuiGraphics graphics, int x, int y, int width,
                                   DeliveryTerminalMenu.RequirementInfo req, int index,
                                   int mouseX, int mouseY) {
        int delivered = menu.getDelivered(index);
        boolean complete = delivered >= req.count();

        // Item icon
        ItemStack icon = resolveItem(req.itemId());
        graphics.renderItem(icon, x, y + 2);

        // Description
        String desc = req.description();
        if (desc.length() > 18) desc = desc.substring(0, 16) + "..";
        graphics.drawString(this.font, desc, x + 20, y + 2, complete ? CHECK_COLOR : TEXT_COLOR, false);

        // Count text (with checkmark when complete)
        String countStr = complete ? "\u2714 " + delivered + "/" + req.count() : delivered + "/" + req.count();
        int countX = x + width - this.font.width(countStr) - 2;
        graphics.drawString(this.font, countStr, countX, y + 2, complete ? CHECK_COLOR : DIM_COLOR, false);

        // Progress bar
        int barX = x + 20;
        int barY = y + 14;
        int barW = width - 22;
        int barH = 6;

        graphics.fill(barX, barY, barX + barW, barY + barH, BAR_BG);

        float pct = Math.min(1.0f, (float) delivered / req.count());
        int fillW = (int) (barW * pct);
        if (fillW > 0) {
            graphics.fill(barX, barY, barX + fillW, barY + barH, complete ? BAR_COMPLETE : BAR_FILL);
        }

        // Tooltip on hover
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 24) {
            List<Component> lines = new ArrayList<>();
            // Full item name (not truncated)
            lines.add(Component.literal(req.description()).withStyle(ChatFormatting.WHITE));
            // Delivered count
            lines.add(Component.literal("Delivered: " + delivered + " / " + req.count())
                    .withStyle(ChatFormatting.GRAY));
            // Category match info
            String tag = req.contentTag();
            if (tag != null && !tag.isEmpty() && tag.startsWith("category/")) {
                String category = tag.substring("category/".length());
                lines.add(Component.literal("Category: " + category)
                        .withStyle(ChatFormatting.AQUA));
            }
            // Complete indicator
            if (complete) {
                lines.add(Component.literal("Complete!").withStyle(ChatFormatting.GREEN));
            }
            hoveredTooltip = lines;
            tooltipX = mouseX;
            tooltipY = mouseY;
        }
    }

    private ItemStack resolveItem(String itemId) {
        try {
            ResourceLocation rl = ResourceLocation.parse(itemId);
            return new ItemStack(BuiltInRegistries.ITEM.get(rl));
        } catch (Exception e) {
            return new ItemStack(Items.BARRIER);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Don't render default labels
    }
}
