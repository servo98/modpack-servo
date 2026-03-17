package com.servo.mart.client;

import com.servo.mart.block.PepeMartMenu;
import com.servo.mart.data.CatalogEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;
import java.util.stream.Collectors;

/**
 * PepeMart catalog screen. Tabbed categories, scrollable item list, buy button.
 */
public class PepeMartScreen extends AbstractContainerScreen<PepeMartMenu> {

    private static final int BG_COLOR = 0xFF1a1a2e;
    private static final int PANEL_COLOR = 0xFF16213e;
    private static final int HEADER_COLOR = 0xFF0f3460;
    private static final int ACCENT_COLOR = 0xFF53a653;
    private static final int LOCKED_COLOR = 0xFF8b0000;
    private static final int TEXT_COLOR = 0xFFe0e0e0;
    private static final int TEXT_DIM = 0xFF888888;

    private static final int ROWS_VISIBLE = 5;
    private static final int ROW_HEIGHT = 22;

    private List<String> categories;
    private String selectedCategory;
    private List<CatalogEntry> currentEntries;
    private int scrollOffset = 0;
    private int selectedIndex = -1;

    /** Y offset where the player inventory slots begin (must match PepeMartMenu) */
    private static final int INV_Y = 186;

    public PepeMartScreen(PepeMartMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 256;
        this.imageHeight = 270;
    }

    @Override
    protected void init() {
        super.init();

        // Hide default inventory labels — we draw our own
        this.inventoryLabelY = INV_Y - 11;
        this.inventoryLabelX = (this.imageWidth - 162) / 2;

        // Build category list
        categories = menu.getCatalog().stream()
                .map(CatalogEntry::category)
                .distinct()
                .collect(Collectors.toList());

        if (!categories.isEmpty()) {
            selectedCategory = categories.get(0);
            refreshEntries();
        }

        // Category tab buttons
        int tabX = leftPos + 4;
        int tabY = topPos + 18;
        for (int i = 0; i < categories.size(); i++) {
            String cat = categories.get(i);
            String label = cat.substring(0, 1).toUpperCase() + cat.substring(1);
            int idx = i;
            addRenderableWidget(Button.builder(Component.literal(label), btn -> {
                selectedCategory = categories.get(idx);
                scrollOffset = 0;
                selectedIndex = -1;
                refreshEntries();
            }).bounds(tabX, tabY + i * 22, 50, 20).build());
        }

        // Buy button — sits between the detail panel and the player inventory
        addRenderableWidget(Button.builder(Component.translatable("servo_mart.buy"), btn -> {
            if (selectedIndex >= 0 && selectedIndex < currentEntries.size()) {
                int globalIndex = menu.getCatalog().indexOf(currentEntries.get(selectedIndex));
                if (globalIndex >= 0) {
                    minecraft.gameMode.handleInventoryButtonClick(menu.containerId, globalIndex);
                }
            }
        }).bounds(leftPos + imageWidth - 60, topPos + 160, 52, 20).build());
    }

    private void refreshEntries() {
        if (selectedCategory == null) {
            currentEntries = Collections.emptyList();
        } else {
            currentEntries = menu.getCatalog().stream()
                    .filter(e -> e.category().equals(selectedCategory))
                    .collect(Collectors.toList());
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        // Full background
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, BG_COLOR);

        // Header
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + 16, HEADER_COLOR);
        graphics.drawCenteredString(font, "PepeMart", leftPos + imageWidth / 2, topPos + 4, 0xFFffd700);

        // Catalog panel
        int panelX = leftPos + 58;
        int panelY = topPos + 18;
        int panelW = imageWidth - 62;
        int panelH = ROWS_VISIBLE * ROW_HEIGHT + 4;
        graphics.fill(panelX, panelY, panelX + panelW, panelY + panelH, PANEL_COLOR);

        // Item rows
        if (currentEntries != null) {
            for (int i = 0; i < ROWS_VISIBLE && i + scrollOffset < currentEntries.size(); i++) {
                int entryIdx = i + scrollOffset;
                CatalogEntry entry = currentEntries.get(entryIdx);
                int rowY = panelY + 2 + i * ROW_HEIGHT;

                // Selection highlight
                if (entryIdx == selectedIndex) {
                    graphics.fill(panelX + 1, rowY, panelX + panelW - 1, rowY + ROW_HEIGHT - 2, 0xFF2a4a7f);
                }

                // Item icon
                Item item = BuiltInRegistries.ITEM.get(entry.item());
                if (item != Items.AIR) {
                    graphics.renderItem(new ItemStack(item), panelX + 4, rowY + 2);
                }

                // Item name
                String name = item != Items.AIR ? item.getDescription().getString() : entry.item().toString();
                if (name.length() > 20) name = name.substring(0, 18) + "..";
                graphics.drawString(font, name, panelX + 24, rowY + 3, TEXT_COLOR, false);

                // Tier indicator
                int tierColor = switch (entry.tier()) {
                    case 1 -> 0xFF888888; // gray
                    case 2 -> 0xFF55ff55; // green
                    case 3 -> 0xFF5555ff; // blue
                    case 4 -> 0xFFaa00aa; // purple
                    case 5 -> 0xFFffaa00; // gold
                    default -> TEXT_DIM;
                };
                graphics.drawString(font, "T" + entry.tier(), panelX + panelW - 22, rowY + 3, tierColor, false);

                // Lock/stage indicator
                if (!entry.requiredStage().isEmpty()) {
                    graphics.drawString(font, "\u26BF", panelX + panelW - 36, rowY + 3, TEXT_DIM, false);
                }
            }
        }

        // Detail panel (below catalog)
        int detailY = panelY + panelH + 2;
        graphics.fill(panelX, detailY, panelX + panelW, detailY + 24, PANEL_COLOR);

        if (selectedIndex >= 0 && selectedIndex < currentEntries.size()) {
            CatalogEntry selected = currentEntries.get(selectedIndex);
            // Show price
            int priceX = panelX + 4;
            graphics.drawString(font, Component.translatable("servo_mart.price"), priceX, detailY + 3, TEXT_DIM, false);
            priceX += 30;
            for (Map.Entry<ResourceLocation, Integer> pe : selected.price().entrySet()) {
                Item priceItem = BuiltInRegistries.ITEM.get(pe.getKey());
                if (priceItem != Items.AIR) {
                    graphics.renderItem(new ItemStack(priceItem), priceX, detailY + 1);
                    graphics.drawString(font, "x" + pe.getValue(), priceX + 17, detailY + 6, TEXT_COLOR, false);
                    priceX += 40;
                }
            }
        }

        // Separator line between catalog area and player inventory
        int sepY = topPos + INV_Y - 14;
        graphics.fill(leftPos + 4, sepY, leftPos + imageWidth - 4, sepY + 1, 0xFF3a3a5e);

        // Player inventory background panel
        int invSlotX = leftPos + (imageWidth - 162) / 2 - 4;
        int invSlotY = topPos + INV_Y - 4;
        graphics.fill(invSlotX, invSlotY, invSlotX + 170, invSlotY + 80, PANEL_COLOR);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check if click is in the catalog panel
        int panelX = leftPos + 58;
        int panelY = topPos + 18;
        int panelW = imageWidth - 62;

        int panelH = ROWS_VISIBLE * ROW_HEIGHT + 4;
        if (mouseX >= panelX && mouseX < panelX + panelW
                && mouseY >= panelY && mouseY < panelY + panelH) {
            int row = (int) ((mouseY - panelY - 2) / ROW_HEIGHT);
            if (row >= 0 && row < ROWS_VISIBLE) {
                int idx = row + scrollOffset;
                if (currentEntries != null && idx < currentEntries.size()) {
                    selectedIndex = idx;
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (currentEntries != null) {
            int maxScroll = Math.max(0, currentEntries.size() - ROWS_VISIBLE);
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int) scrollY));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Don't render default labels (we draw our own in renderBg)
    }
}
