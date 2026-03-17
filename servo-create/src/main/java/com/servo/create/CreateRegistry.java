package com.servo.create;

import com.servo.create.recipe.BoxCompactingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreateRegistry {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ServoCreate.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ServoCreate.MOD_ID);

    // Custom recipe type for box compacting via Press+Basin
    public static final DeferredHolder<RecipeType<?>, RecipeType<BoxCompactingRecipe>> BOX_COMPACTING_TYPE =
            RECIPE_TYPES.register("box_compacting", () -> RecipeType.simple(ServoCreate.modLoc("box_compacting")));

    public static final DeferredHolder<RecipeSerializer<?>, BoxCompactingRecipe.Serializer> BOX_COMPACTING_SERIALIZER =
            RECIPE_SERIALIZERS.register("box_compacting", BoxCompactingRecipe.Serializer::new);

    public static void register(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
