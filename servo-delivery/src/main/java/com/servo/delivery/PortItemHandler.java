package com.servo.delivery;

import com.servo.delivery.block.DeliveryTerminalBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * IItemHandler for delivery ports. Passthrough: accepts items and delegates
 * to the master terminal's tryInsertItem. No buffer — items are consumed immediately
 * or rejected (returned to the inserter).
 *
 * Supports hopper, Create funnel/belt, RS exporter, and any IItemHandler-aware automation.
 */
public class PortItemHandler implements IItemHandler {

    private final DeliveryTerminalBlockEntity master;

    public PortItemHandler(DeliveryTerminalBlockEntity master) {
        this.master = master;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return ItemStack.EMPTY; // passthrough, never holds items
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        if (simulate) {
            return master.canAcceptItem(stack) ? shrinkCopy(stack) : stack;
        }

        if (master.tryInsertItem(stack, null)) {
            // tryInsertItem already called stack.shrink(1)
            return stack; // returns the shrunk stack (or empty if was 1)
        }

        return stack; // rejected — return unchanged
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY; // cannot extract from delivery terminal
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return master.canAcceptItem(stack);
    }

    private static ItemStack shrinkCopy(ItemStack stack) {
        if (stack.getCount() <= 1) return ItemStack.EMPTY;
        ItemStack copy = stack.copy();
        copy.shrink(1);
        return copy;
    }
}
