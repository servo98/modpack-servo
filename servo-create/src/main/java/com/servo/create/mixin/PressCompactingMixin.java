package com.servo.create.mixin;

import com.servo.create.ServoCreate;
import com.servo.create.recipe.BoxCompactingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mixin on BasinOperatingBlockEntity to add box compacting recipe support.
 * When the Mechanical Press searches for basin recipes, this mixin checks
 * if the basin contains an Open Box + packable items and injects the
 * dynamic BoxCompactingRecipe.
 */
@Mixin(value = BasinOperatingBlockEntity.class, remap = false)
public abstract class PressCompactingMixin {

    @Shadow
    protected abstract Optional<BasinBlockEntity> getBasin();

    /**
     * Inject after getMatchingRecipes() returns to add our custom recipe.
     * Only applies when the machine is a MechanicalPress.
     */
    @Inject(method = "getMatchingRecipes", at = @At("RETURN"), cancellable = true)
    private void servoCreate$addBoxCompacting(CallbackInfoReturnable<List<RecipeHolder<?>>> cir) {
        // Only apply to Mechanical Press, not Mixer or other basin operators
        if (!((Object) this instanceof MechanicalPressBlockEntity)) return;

        Optional<BasinBlockEntity> optBasin = getBasin();
        if (optBasin.isEmpty()) return;

        try {
            RecipeHolder<BoxCompactingRecipe> recipe =
                    BoxCompactingRecipe.tryCreateForBasin(optBasin.get());
            if (recipe == null) return;

            // Add our recipe to the results
            List<RecipeHolder<?>> original = cir.getReturnValue();
            List<RecipeHolder<?>> modified = new ArrayList<>(original);
            modified.add(recipe);
            cir.setReturnValue(modified);
        } catch (Exception e) {
            ServoCreate.LOGGER.error("Error checking basin for box compacting", e);
        }
    }
}
