package com.servo.cooking.menu;

import com.servo.cooking.CookingRegistry;
import com.servo.cooking.block.entity.PrepStationBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Menu (Container) for the Prep Station.
 * <p>
 * Slot layout:
 * - Slots 0-3: Input ingredients (2x2 grid)
 * - Slot 4: Output (extract only — inputs consumed via removeItem in BlockEntity)
 * <p>
 * Player inventory: slots 5-31 (main), 32-40 (hotbar)
 */
public class PrepStationMenu extends AbstractContainerMenu {

    private final Container container;

    // Menu slot indices
    private static final int INPUT_SLOT_START = 0;
    private static final int INPUT_SLOT_END = 4;   // exclusive
    private static final int OUTPUT_SLOT = 4;
    private static final int INV_SLOT_START = 5;
    private static final int INV_SLOT_END = 32;     // 5 + 27
    private static final int HOTBAR_SLOT_START = 32;
    private static final int HOTBAR_SLOT_END = 41;  // 32 + 9

    /** Server-side constructor. */
    public PrepStationMenu(int containerId, Inventory playerInv, Container container) {
        super(CookingRegistry.PREP_STATION_MENU.get(), containerId);
        checkContainerSize(container, PrepStationBlockEntity.SLOT_COUNT);
        this.container = container;

        // 4 input slots in a 2x2 grid on the left
        // Row 1: slots 0, 1
        this.addSlot(new Slot(container, 0, 38, 26));
        this.addSlot(new Slot(container, 1, 56, 26));
        // Row 2: slots 2, 3
        this.addSlot(new Slot(container, 2, 38, 44));
        this.addSlot(new Slot(container, 3, 56, 44));

        // Output slot — extract only
        // Input consumption happens in BlockEntity.removeItem(SLOT_OUTPUT, ...)
        this.addSlot(new Slot(container, 4, 116, 35) {
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
    }

    /** Client-side factory (called by MenuType registration). */
    public static PrepStationMenu clientMenu(int containerId, Inventory playerInv) {
        return new PrepStationMenu(containerId, playerInv, new SimpleContainer(PrepStationBlockEntity.SLOT_COUNT));
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
        } else if (slotIndex >= INPUT_SLOT_START && slotIndex < INPUT_SLOT_END) {
            // Input slot -> player inventory
            if (!this.moveItemStackTo(slotStack, INV_SLOT_START, HOTBAR_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // Player inventory -> try input slots
            if (!this.moveItemStackTo(slotStack, INPUT_SLOT_START, INPUT_SLOT_END, false)) {
                // If can't go to inputs, try hotbar<->inventory swap
                if (slotIndex >= INV_SLOT_START && slotIndex < INV_SLOT_END) {
                    if (!this.moveItemStackTo(slotStack, HOTBAR_SLOT_START, HOTBAR_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= HOTBAR_SLOT_START && slotIndex < HOTBAR_SLOT_END) {
                    if (!this.moveItemStackTo(slotStack, INV_SLOT_START, INV_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
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
}
