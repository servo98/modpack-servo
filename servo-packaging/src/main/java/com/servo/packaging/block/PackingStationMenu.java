package com.servo.packaging.block;

import com.servo.packaging.PackagingRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Menu (Container) for the Packing Station.
 * <p>
 * Slot layout:
 * - Slot 0: Input (flat_cardboard only)
 * - Slot 1: Output (open_box, extract only)
 * <p>
 * ContainerData indices:
 * - 0: foldTicks (current progress)
 * - 1: foldDuration (total ticks for one fold)
 */
public class PackingStationMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData data;

    // Slot indices in the menu
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int INV_SLOT_START = 2;
    private static final int INV_SLOT_END = 29;   // 2 + 27
    private static final int HOTBAR_SLOT_START = 29;
    private static final int HOTBAR_SLOT_END = 38; // 29 + 9

    /** Server-side constructor. */
    public PackingStationMenu(int containerId, Inventory playerInv, Container container, ContainerData data) {
        super(PackagingRegistry.PACKING_STATION_MENU.get(), containerId);
        checkContainerSize(container, 2);
        checkContainerDataCount(data, 2);
        this.container = container;
        this.data = data;

        // Input slot — only accepts flat_cardboard
        this.addSlot(new Slot(container, 0, 56, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(PackagingRegistry.FLAT_CARDBOARD_ITEM.get());
            }
        });

        // Output slot — extract only
        this.addSlot(new Slot(container, 1, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // Player inventory (3 rows of 9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }

        addDataSlots(data);
    }

    /** Client-side factory (called by MenuType registration). */
    public static PackingStationMenu clientMenu(int containerId, Inventory playerInv) {
        return new PackingStationMenu(containerId, playerInv, new SimpleContainer(2), new SimpleContainerData(2));
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    /** Shift-click support. */
    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack slotStack = slot.getItem();
        ItemStack original = slotStack.copy();

        if (slotIndex == OUTPUT_SLOT) {
            // Output slot -> player inventory
            if (!this.moveItemStackTo(slotStack, INV_SLOT_START, HOTBAR_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(slotStack, original);
        } else if (slotIndex == INPUT_SLOT) {
            // Input slot -> player inventory
            if (!this.moveItemStackTo(slotStack, INV_SLOT_START, HOTBAR_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // Player inventory -> try input slot
            if (slotStack.is(PackagingRegistry.FLAT_CARDBOARD_ITEM.get())) {
                if (!this.moveItemStackTo(slotStack, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= INV_SLOT_START && slotIndex < INV_SLOT_END) {
                // Main inventory -> hotbar
                if (!this.moveItemStackTo(slotStack, HOTBAR_SLOT_START, HOTBAR_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= HOTBAR_SLOT_START && slotIndex < HOTBAR_SLOT_END) {
                // Hotbar -> main inventory
                if (!this.moveItemStackTo(slotStack, INV_SLOT_START, INV_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (slotStack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (slotStack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, slotStack);
        return original;
    }

    /** Progress fraction for the GUI arrow (0.0 to 1.0). */
    public float getFoldProgress() {
        int ticks = data.get(0);
        int duration = data.get(1);
        return duration > 0 ? (float) ticks / (float) duration : 0.0f;
    }

    public boolean isFolding() {
        return data.get(0) > 0;
    }
}
