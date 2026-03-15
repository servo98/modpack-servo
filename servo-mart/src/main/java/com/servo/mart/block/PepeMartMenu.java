package com.servo.mart.block;

import com.servo.mart.MartRegistry;
import com.servo.mart.ServoMart;
import com.servo.mart.compat.StageHelper;
import com.servo.mart.data.CatalogDataLoader;
import com.servo.mart.data.CatalogEntry;
import com.servo.packaging.PackagingRegistry;
import com.servo.packaging.component.BoxContents;
import com.servo.packaging.item.ShippingBoxItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Menu for PepeMart. Syncs the catalog from server to client.
 * Purchase actions are handled via clickMenuButton.
 */
public class PepeMartMenu extends AbstractContainerMenu {

    private final PepeMartBlockEntity mart;
    private final List<CatalogEntry> catalog;

    /** Server constructor */
    public PepeMartMenu(int containerId, Inventory playerInv, PepeMartBlockEntity mart) {
        super(MartRegistry.PEPE_MART_MENU.get(), containerId);
        this.mart = mart;
        this.catalog = CatalogDataLoader.getAllEntries();

        addPlayerInventory(playerInv);
    }

    /** Client constructor — reads catalog from network buffer */
    public static PepeMartMenu clientMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
        return new PepeMartMenu(containerId, playerInv, buf);
    }

    private PepeMartMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
        super(MartRegistry.PEPE_MART_MENU.get(), containerId);
        this.mart = null;

        int count = buf.readVarInt();
        this.catalog = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            catalog.add(CatalogEntry.fromNetwork(buf));
        }

        addPlayerInventory(playerInv);
    }

    private void addPlayerInventory(Inventory inv) {
        // Player inventory (3 rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
            }
        }
        // Hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inv, col, 8 + col * 18, 198));
        }
    }

    /** Write catalog data to network buffer (called on server) */
    public static void writeCatalogData(FriendlyByteBuf buf, List<CatalogEntry> entries) {
        buf.writeVarInt(entries.size());
        for (CatalogEntry entry : entries) {
            entry.toNetwork(buf);
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id < 0 || id >= catalog.size()) return false;
        if (!(player instanceof ServerPlayer serverPlayer)) return false;

        CatalogEntry entry = catalog.get(id);

        // Check stage
        if (!StageHelper.hasStage(serverPlayer, entry.requiredStage())) {
            serverPlayer.displayClientMessage(
                    Component.translatable("servo_mart.locked", entry.requiredStage())
                            .withStyle(ChatFormatting.RED), true);
            return false;
        }

        // Check materials
        for (Map.Entry<ResourceLocation, Integer> priceEntry : entry.price().entrySet()) {
            Item priceItem = BuiltInRegistries.ITEM.get(priceEntry.getKey());
            if (priceItem == Items.AIR) {
                ServoMart.LOGGER.warn("Unknown price item: {}", priceEntry.getKey());
                return false;
            }
            int needed = priceEntry.getValue();
            int found = countItem(serverPlayer, priceItem);
            if (found < needed) {
                serverPlayer.displayClientMessage(
                        Component.translatable("servo_mart.insufficient")
                                .withStyle(ChatFormatting.RED), true);
                return false;
            }
        }

        // Consume materials
        for (Map.Entry<ResourceLocation, Integer> priceEntry : entry.price().entrySet()) {
            Item priceItem = BuiltInRegistries.ITEM.get(priceEntry.getKey());
            consumeItem(serverPlayer, priceItem, priceEntry.getValue());
        }

        // Create shipping box with purchased item
        ItemStack box = ShippingBoxItem.createBox(entry.item(), 1, "shop");

        // Give to player
        if (!serverPlayer.getInventory().add(box)) {
            serverPlayer.drop(box, false);
        }

        // Feedback
        Item purchasedItem = BuiltInRegistries.ITEM.get(entry.item());
        String itemName = purchasedItem.getDescription().getString();
        serverPlayer.displayClientMessage(
                Component.translatable("servo_mart.purchased", itemName)
                        .withStyle(ChatFormatting.GREEN), true);

        return true;
    }

    private int countItem(Player player, Item item) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    private void consumeItem(Player player, Item item, int amount) {
        int remaining = amount;
        for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                int take = Math.min(remaining, stack.getCount());
                stack.shrink(take);
                remaining -= take;
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        if (mart == null) return true;
        return player.distanceToSqr(mart.getBlockPos().getX() + 0.5,
                mart.getBlockPos().getY() + 0.5,
                mart.getBlockPos().getZ() + 0.5) <= 64.0;
    }

    // === Getters for Screen ===
    public List<CatalogEntry> getCatalog() { return catalog; }
}
