package com.servo.cooking.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * Recipe input wrapper for the Prep Station's 4 ingredient slots.
 */
public record PrepStationRecipeInput(ItemStack slot0, ItemStack slot1, ItemStack slot2, ItemStack slot3) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> slot0;
            case 1 -> slot1;
            case 2 -> slot2;
            case 3 -> slot3;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 4;
    }
}
