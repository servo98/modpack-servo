package com.servo.cooking.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.servo.cooking.CookingRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Prep Station recipe — shapeless cold assembly.
 * 1-4 ingredients -> 1 output. Instant (no timer).
 * Order does not matter (shapeless matching).
 */
public class PrepStationRecipe implements Recipe<PrepStationRecipeInput> {

    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;

    public PrepStationRecipe(NonNullList<Ingredient> ingredients, ItemStack result) {
        this.ingredients = ingredients;
        this.result = result;
    }

    @Override
    public boolean matches(PrepStationRecipeInput input, Level level) {
        if (level.isClientSide()) return false;

        // Count non-empty input items
        List<ItemStack> inputItems = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                inputItems.add(stack);
            }
        }

        // Must have same number of ingredients as recipe requires
        if (inputItems.size() != ingredients.size()) return false;

        // Shapeless matching: each ingredient must match exactly one input item
        boolean[] used = new boolean[inputItems.size()];
        for (Ingredient ingredient : ingredients) {
            boolean found = false;
            for (int i = 0; i < inputItems.size(); i++) {
                if (!used[i] && ingredient.test(inputItems.get(i))) {
                    used[i] = true;
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(PrepStationRecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= ingredients.size();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CookingRegistry.PREP_STATION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CookingRegistry.PREP_STATION_RECIPE_TYPE.get();
    }

    public ItemStack getResult() {
        return result;
    }

    // === Serializer ===

    public static class Serializer implements RecipeSerializer<PrepStationRecipe> {

        private static final MapCodec<PrepStationRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients")
                        .forGetter(r -> r.ingredients.stream().toList()),
                ItemStack.STRICT_CODEC.fieldOf("result")
                        .forGetter(r -> r.result)
        ).apply(inst, (ingredients, result) -> {
            NonNullList<Ingredient> list = NonNullList.create();
            list.addAll(ingredients);
            return new PrepStationRecipe(list, result);
        }));

        private static final StreamCodec<RegistryFriendlyByteBuf, PrepStationRecipe> STREAM_CODEC =
                new StreamCodec<>() {
                    @Override
                    public PrepStationRecipe decode(RegistryFriendlyByteBuf buf) {
                        int size = buf.readVarInt();
                        NonNullList<Ingredient> ingredients = NonNullList.create();
                        for (int i = 0; i < size; i++) {
                            ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                        }
                        ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                        return new PrepStationRecipe(ingredients, result);
                    }

                    @Override
                    public void encode(RegistryFriendlyByteBuf buf, PrepStationRecipe recipe) {
                        buf.writeVarInt(recipe.ingredients.size());
                        for (Ingredient ingredient : recipe.ingredients) {
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
                        }
                        ItemStack.STREAM_CODEC.encode(buf, recipe.result);
                    }
                };

        @Override
        public MapCodec<PrepStationRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PrepStationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
