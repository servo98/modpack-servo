package com.servo.delivery;

import com.servo.delivery.block.DeliveryTerminalBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * IItemHandler for delivery ports. Passthrough: accepts items and delegates
 * to the master terminal's delivery logic. No buffer — items are consumed
 * immediately (1 at a time) or rejected (returned to the inserter).
 *
 * <p>Supports hopper, Create funnel/belt, RS exporter, and any IItemHandler-aware automation.
 *
 * <h3>IItemHandler contract:</h3>
 * <ul>
 *   <li>{@code insertItem} must NOT mutate the input stack.</li>
 *   <li>Return {@link ItemStack#EMPTY} if everything was consumed.</li>
 *   <li>Return the remainder (a new stack) if partially or fully rejected.</li>
 * </ul>
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

    /**
     * Inserts one item into the delivery terminal (if it matches a current requirement).
     * This handler consumes at most 1 item per call, matching the delivery-per-item logic.
     *
     * @param slot     ignored (single virtual slot)
     * @param stack    the stack being offered — must NOT be mutated
     * @param simulate if true, only check whether insertion would succeed
     * @return the remainder after insertion (count-1 on success, original stack on rejection)
     */
    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        if (!master.canAcceptItem(stack)) {
            return stack; // rejected — nothing consumed
        }

        if (simulate) {
            return remainder(stack);
        }

        // Real insertion: work on a single-item copy so tryInsertItem's shrink(1)
        // does NOT mutate the caller's stack (IItemHandler contract).
        ItemStack singleCopy = stack.copyWithCount(1);
        if (master.tryInsertItem(singleCopy, null)) {
            return remainder(stack);
        }

        return stack; // rejected by tryInsertItem (race / state change)
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

    /**
     * Returns a new stack with count-1 (the remainder after consuming one item).
     * Never mutates the input.
     */
    private static ItemStack remainder(ItemStack stack) {
        if (stack.getCount() <= 1) return ItemStack.EMPTY;
        return stack.copyWithCount(stack.getCount() - 1);
    }
}
