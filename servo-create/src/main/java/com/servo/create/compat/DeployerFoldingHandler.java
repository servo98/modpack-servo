package com.servo.create.compat;

import com.servo.create.ServoCreate;
import com.servo.create.recipe.BoxCompactingRecipe;
import com.servo.packaging.PackagingRegistry;
import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Handles the Deployer folding recipe: Flat Cardboard on belt -> Open Box.
 * Uses Create's DeployerRecipeSearchEvent to inject the recipe dynamically.
 * The deployer in "Use" mode folds the cardboard without needing a held item.
 */
public class DeployerFoldingHandler {

    private static RecipeHolder<?> cachedRecipe;

    @SubscribeEvent
    public static void onDeployerRecipeSearch(DeployerRecipeSearchEvent event) {
        var deployer = event.getBlockEntity();
        if (deployer == null) return;

        try {
            var inventory = event.getInventory();
            boolean hasFlatCardboard = false;

            // RecipeWrapper wraps an IItemHandler - iterate via IItemHandler methods
            if (inventory instanceof IItemHandler handler) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (!stack.isEmpty() && stack.is(PackagingRegistry.FLAT_CARDBOARD_ITEM.get())) {
                        hasFlatCardboard = true;
                        break;
                    }
                }
            }

            if (hasFlatCardboard) {
                @SuppressWarnings({"unchecked", "rawtypes"})
                Supplier supplier = () -> Optional.of(getFoldingRecipe());
                event.addRecipe(supplier, 10);
            }
        } catch (Exception e) {
            ServoCreate.LOGGER.debug("Error in deployer recipe search", e);
        }
    }

    private static RecipeHolder<?> getFoldingRecipe() {
        if (cachedRecipe == null) {
            cachedRecipe = createFoldingRecipe();
        }
        return cachedRecipe;
    }

    /**
     * Create a deployer recipe: flat_cardboard -> open_box.
     */
    private static RecipeHolder<?> createFoldingRecipe() {
        BoxCompactingRecipe.Params params = new BoxCompactingRecipe.Params();
        params.addIngredient(Ingredient.of(PackagingRegistry.FLAT_CARDBOARD_ITEM.get()));
        params.addResult(new ProcessingOutput(
                new ItemStack(PackagingRegistry.OPEN_BOX_ITEM.get()), 1.0f));
        params.setDuration(20); // 1 second

        BoxCompactingRecipe recipe = new BoxCompactingRecipe(params,
                new ItemStack(PackagingRegistry.OPEN_BOX_ITEM.get()));
        return new RecipeHolder<>(ServoCreate.modLoc("fold_cardboard"), recipe);
    }
}
