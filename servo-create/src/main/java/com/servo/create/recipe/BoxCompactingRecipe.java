package com.servo.create.recipe;

import com.mojang.serialization.MapCodec;
import com.servo.create.CreateRegistry;
import com.servo.create.ServoCreate;
import com.servo.packaging.PackagingRegistry;
import com.servo.packaging.item.ShippingBoxItem;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

/**
 * Custom basin recipe for packing items via Mechanical Press + Basin.
 * Basin receives: N items (same type, tagged packable) + 1 Open Box
 * Press compacts -> produces 1 Shipping Box with correct BoxContents.
 *
 * This is a DYNAMIC recipe: one recipe type handles all packable items.
 * Instances are created programmatically by the Mixin when basin contents match.
 */
public class BoxCompactingRecipe extends BasinRecipe {

    private static final ResourceLocation RECIPE_ID = ServoCreate.modLoc("box_compacting");

    public BoxCompactingRecipe(Params params, ItemStack dynamicOutput) {
        super(params);
        // Force the output so BasinRecipe.apply() uses our dynamic ShippingBox
        if (!dynamicOutput.isEmpty()) {
            enforceNextResult(() -> dynamicOutput.copy());
        }
    }

    public BoxCompactingRecipe(Params params) {
        super(params);
    }

    @Override
    public RecipeType<?> getType() {
        return CreateRegistry.BOX_COMPACTING_TYPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CreateRegistry.BOX_COMPACTING_SERIALIZER.get();
    }

    /**
     * Examine basin contents and create a matching recipe if the pattern is valid.
     * Pattern: 1 open_box + N packable items (all same type, count >= pack_size)
     *
     * @return RecipeHolder with the dynamic recipe, or null if no match
     */
    @Nullable
    public static RecipeHolder<BoxCompactingRecipe> tryCreateForBasin(BasinBlockEntity basin) {
        if (basin.getLevel() == null) return null;

        IItemHandler handler = basin.getLevel().getCapability(
                Capabilities.ItemHandler.BLOCK, basin.getBlockPos(), null);
        if (handler == null) return null;

        // Scan basin contents
        boolean hasOpenBox = false;
        ResourceLocation packableItemId = null;
        int packableCount = 0;
        String category = "general";
        int packSize = 16; // default
        ItemStack samplePackable = ItemStack.EMPTY;

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (stack.is(PackagingRegistry.OPEN_BOX_ITEM.get())) {
                hasOpenBox = true;
            } else if (isPackable(stack)) {
                ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
                if (packableItemId == null) {
                    packableItemId = id;
                    category = detectCategory(stack);
                    packSize = detectPackSize(stack);
                    samplePackable = stack;
                } else if (!id.equals(packableItemId)) {
                    return null; // Mixed item types - not our recipe
                }
                packableCount += stack.getCount();
            }
        }

        // Need open box + enough packable items
        if (!hasOpenBox || packableItemId == null || packableCount < packSize) {
            return null;
        }

        // Build ingredients matching basin contents
        Params params = new Params();

        // Add packable ingredients (one per item needed)
        Ingredient packableIngredient = Ingredient.of(samplePackable.getItem());
        for (int i = 0; i < packSize; i++) {
            params.addIngredient(packableIngredient);
        }
        // Add open box ingredient
        params.addIngredient(Ingredient.of(PackagingRegistry.OPEN_BOX_ITEM.get()));

        // Create the output shipping box with correct BoxContents
        ItemStack output = ShippingBoxItem.createBox(packableItemId, packSize, category);
        params.addResult(new ProcessingOutput(output, 1.0f));
        params.setDuration(100); // 5 seconds

        BoxCompactingRecipe recipe = new BoxCompactingRecipe(params, output);
        return new RecipeHolder<>(RECIPE_ID, recipe);
    }

    private static boolean isPackable(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(PackagingRegistry.OPEN_BOX_ITEM.get())) return false;
        if (stack.is(PackagingRegistry.FLAT_CARDBOARD_ITEM.get())) return false;
        if (stack.is(PackagingRegistry.SHIPPING_BOX_ITEM.get())) return false;
        return stack.is(PackagingRegistry.PACKABLE_TAG);
    }

    private static int detectPackSize(ItemStack stack) {
        if (stack.is(PackagingRegistry.PACK_SIZE_1_TAG)) return 1;
        if (stack.is(PackagingRegistry.PACK_SIZE_8_TAG)) return 8;
        return 16;
    }

    private static String detectCategory(ItemStack stack) {
        if (stack.is(PackagingRegistry.CATEGORY_FOOD_TAG)) return "food";
        if (stack.is(PackagingRegistry.CATEGORY_CROPS_TAG)) return "crops";
        if (stack.is(PackagingRegistry.CATEGORY_PROCESSED_TAG)) return "processed";
        if (stack.is(PackagingRegistry.CATEGORY_MAGIC_TAG)) return "magic";
        if (stack.is(PackagingRegistry.CATEGORY_SPECIAL_TAG)) return "special";
        return "general";
    }

    /**
     * Subclass to expose protected ProcessingRecipeParams constructor and fields.
     * Java protected access only works from within the subclass itself,
     * so we provide public delegate methods.
     */
    public static class Params extends ProcessingRecipeParams {
        public Params() {
            super();
        }

        public void addIngredient(Ingredient ingredient) {
            this.ingredients.add(ingredient);
        }

        public void addResult(ProcessingOutput output) {
            this.results.add(output);
        }

        public void setDuration(int ticks) {
            this.processingDuration = ticks;
        }
    }

    /**
     * Minimal serializer - recipes are created dynamically, not from JSON.
     * This serializer exists only to satisfy NeoForge's recipe type registration.
     */
    public static class Serializer implements RecipeSerializer<BoxCompactingRecipe> {
        private static final BoxCompactingRecipe EMPTY = new BoxCompactingRecipe(new Params());

        @Override
        public MapCodec<BoxCompactingRecipe> codec() {
            return MapCodec.unit(EMPTY);
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BoxCompactingRecipe> streamCodec() {
            return StreamCodec.unit(EMPTY);
        }
    }
}
