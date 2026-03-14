package com.servo.packaging;

import com.servo.packaging.block.OpenBoxBlock;
import com.servo.packaging.block.OpenBoxBlockEntity;
import com.servo.packaging.block.PackingStationBlock;
import com.servo.packaging.block.PackingStationBlockEntity;
import com.servo.packaging.block.PackingStationMenu;
import com.servo.packaging.component.BoxContents;
import com.servo.packaging.item.FlatCardboardItem;
import com.servo.packaging.item.ShippingBoxItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PackagingRegistry {

    // === Deferred Registers ===
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, ServoPackaging.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, ServoPackaging.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ServoPackaging.MOD_ID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ServoPackaging.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ServoPackaging.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ServoPackaging.MOD_ID);

    // === Data Components ===
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BoxContents>> BOX_CONTENTS =
            DATA_COMPONENTS.register("box_contents", () ->
                    DataComponentType.<BoxContents>builder()
                            .persistent(BoxContents.CODEC)
                            .networkSynchronized(BoxContents.STREAM_CODEC)
                            .build()
            );

    // === Blocks ===
    public static final DeferredHolder<Block, PackingStationBlock> PACKING_STATION_BLOCK =
            BLOCKS.register("packing_station", () -> new PackingStationBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WOOD)
                            .strength(2.0f)
                            .sound(SoundType.WOOD)
                            .noOcclusion()
            ));

    public static final DeferredHolder<Block, OpenBoxBlock> OPEN_BOX_BLOCK =
            BLOCKS.register("open_box", () -> new OpenBoxBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WOOD)
                            .strength(0.5f)
                            .sound(SoundType.WOOL)
                            .noOcclusion()
            ));

    // === Items ===
    public static final DeferredHolder<Item, FlatCardboardItem> FLAT_CARDBOARD_ITEM =
            ITEMS.register("flat_cardboard", () -> new FlatCardboardItem(
                    new Item.Properties().stacksTo(64)
            ));

    // Open Box is now a BlockItem that places the OpenBoxBlock
    public static final DeferredHolder<Item, BlockItem> OPEN_BOX_ITEM =
            ITEMS.register("open_box", () -> new BlockItem(
                    OPEN_BOX_BLOCK.get(),
                    new Item.Properties().stacksTo(16)
            ));

    public static final DeferredHolder<Item, ShippingBoxItem> SHIPPING_BOX_ITEM =
            ITEMS.register("shipping_box", () -> new ShippingBoxItem(
                    new Item.Properties().stacksTo(16)
            ));

    public static final DeferredHolder<Item, BlockItem> PACKING_STATION_ITEM =
            ITEMS.register("packing_station", () -> new BlockItem(
                    PACKING_STATION_BLOCK.get(),
                    new Item.Properties()
            ));

    // === Block Entities ===
    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PackingStationBlockEntity>> PACKING_STATION_BE =
            BLOCK_ENTITIES.register("packing_station", () ->
                    BlockEntityType.Builder.of(PackingStationBlockEntity::new, PACKING_STATION_BLOCK.get())
                            .build(null)
            );

    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OpenBoxBlockEntity>> OPEN_BOX_BE =
            BLOCK_ENTITIES.register("open_box", () ->
                    BlockEntityType.Builder.of(OpenBoxBlockEntity::new, OPEN_BOX_BLOCK.get())
                            .build(null)
            );

    // === Menus ===
    public static final DeferredHolder<MenuType<?>, MenuType<PackingStationMenu>> PACKING_STATION_MENU =
            MENUS.register("packing_station", () ->
                    new MenuType<>(PackingStationMenu::clientMenu, FeatureFlags.DEFAULT_FLAGS)
            );

    // === Tags (items) ===
    public static final TagKey<Item> PACKABLE_TAG =
            ItemTags.create(ServoPackaging.modLoc("packable"));
    public static final TagKey<Item> PACK_SIZE_1_TAG =
            ItemTags.create(ServoPackaging.modLoc("pack_size_1"));
    public static final TagKey<Item> PACK_SIZE_8_TAG =
            ItemTags.create(ServoPackaging.modLoc("pack_size_8"));
    public static final TagKey<Item> PACK_SIZE_16_TAG =
            ItemTags.create(ServoPackaging.modLoc("pack_size_16"));
    public static final TagKey<Item> CATEGORY_FOOD_TAG =
            ItemTags.create(ServoPackaging.modLoc("category/food"));
    public static final TagKey<Item> CATEGORY_CROPS_TAG =
            ItemTags.create(ServoPackaging.modLoc("category/crops"));
    public static final TagKey<Item> CATEGORY_PROCESSED_TAG =
            ItemTags.create(ServoPackaging.modLoc("category/processed"));
    public static final TagKey<Item> CATEGORY_MAGIC_TAG =
            ItemTags.create(ServoPackaging.modLoc("category/magic"));
    public static final TagKey<Item> CATEGORY_SPECIAL_TAG =
            ItemTags.create(ServoPackaging.modLoc("category/special"));

    // === Creative Tab ===
    public static final Supplier<CreativeModeTab> PACKAGING_TAB =
            CREATIVE_TABS.register("packaging", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.servo_packaging"))
                    .icon(() -> SHIPPING_BOX_ITEM.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(FLAT_CARDBOARD_ITEM.get());
                        output.accept(OPEN_BOX_ITEM.get());
                        output.accept(SHIPPING_BOX_ITEM.get());
                        output.accept(PACKING_STATION_ITEM.get());
                    })
                    .build()
            );

    // === Registration ===
    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENUS.register(modEventBus);
        DATA_COMPONENTS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }
}
