package com.servo.cooking;

import com.servo.cooking.block.PrepStationBlock;
import com.servo.cooking.block.entity.PrepStationBlockEntity;
import com.servo.cooking.menu.PrepStationMenu;
import com.servo.cooking.recipe.PrepStationRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CookingRegistry {

    // === Deferred Registers ===
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, ServoCooking.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, ServoCooking.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ServoCooking.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ServoCooking.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ServoCooking.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ServoCooking.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ServoCooking.MOD_ID);

    // === Blocks ===

    public static final DeferredHolder<Block, PrepStationBlock> PREP_STATION_BLOCK =
            BLOCKS.register("prep_station", () -> new PrepStationBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WOOD)
                            .strength(2.5f)
                            .sound(SoundType.WOOD)
                            .noOcclusion()
            ));

    // Future workstations — placeholder blocks for now
    public static final DeferredHolder<Block, Block> BLENDER_BLOCK =
            BLOCKS.register("blender", () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(3.0f)
                            .sound(SoundType.METAL)
                            .noOcclusion()
            ));

    public static final DeferredHolder<Block, Block> WOK_BLOCK =
            BLOCKS.register("wok", () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(3.0f)
                            .sound(SoundType.METAL)
                            .noOcclusion()
            ));

    public static final DeferredHolder<Block, Block> BAKERS_OVEN_BLOCK =
            BLOCKS.register("bakers_oven", () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(3.5f)
                            .sound(SoundType.STONE)
                            .noOcclusion()
            ));

    // === Block Items ===
    public static final DeferredHolder<Item, BlockItem> PREP_STATION_ITEM =
            ITEMS.register("prep_station", () -> new BlockItem(
                    PREP_STATION_BLOCK.get(), new Item.Properties()
            ));

    public static final DeferredHolder<Item, BlockItem> BLENDER_ITEM =
            ITEMS.register("blender", () -> new BlockItem(
                    BLENDER_BLOCK.get(), new Item.Properties()
            ));

    public static final DeferredHolder<Item, BlockItem> WOK_ITEM =
            ITEMS.register("wok", () -> new BlockItem(
                    WOK_BLOCK.get(), new Item.Properties()
            ));

    public static final DeferredHolder<Item, BlockItem> BAKERS_OVEN_ITEM =
            ITEMS.register("bakers_oven", () -> new BlockItem(
                    BAKERS_OVEN_BLOCK.get(), new Item.Properties()
            ));

    // === Block Entities ===

    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PrepStationBlockEntity>> PREP_STATION_BE =
            BLOCK_ENTITIES.register("prep_station", () ->
                    BlockEntityType.Builder.of(PrepStationBlockEntity::new, PREP_STATION_BLOCK.get())
                            .build(null)
            );

    // === Menus ===

    public static final DeferredHolder<MenuType<?>, MenuType<PrepStationMenu>> PREP_STATION_MENU =
            MENUS.register("prep_station", () ->
                    new MenuType<>(PrepStationMenu::clientMenu, FeatureFlags.DEFAULT_FLAGS)
            );

    // === Recipe Types ===

    public static final DeferredHolder<RecipeType<?>, RecipeType<PrepStationRecipe>> PREP_STATION_RECIPE_TYPE =
            RECIPE_TYPES.register("prep_station", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "servo_cooking:prep_station";
                }
            });

    // === Recipe Serializers ===

    public static final DeferredHolder<RecipeSerializer<?>, PrepStationRecipe.Serializer> PREP_STATION_SERIALIZER =
            RECIPE_SERIALIZERS.register("prep_station", PrepStationRecipe.Serializer::new);

    // === Creative Tab ===
    public static final Supplier<CreativeModeTab> COOKING_TAB =
            CREATIVE_TABS.register("cooking", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.servo_cooking"))
                    .icon(() -> PREP_STATION_ITEM.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(PREP_STATION_ITEM.get());
                        output.accept(BLENDER_ITEM.get());
                        output.accept(WOK_ITEM.get());
                        output.accept(BAKERS_OVEN_ITEM.get());
                    })
                    .build()
            );

    // === Registration ===
    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENUS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }
}
