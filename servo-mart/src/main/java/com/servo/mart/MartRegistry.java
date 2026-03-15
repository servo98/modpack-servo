package com.servo.mart;

import com.servo.mart.block.PepeMartBlock;
import com.servo.mart.block.PepeMartBlockEntity;
import com.servo.mart.block.PepeMartMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
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
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MartRegistry {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, ServoMart.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, ServoMart.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ServoMart.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ServoMart.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ServoMart.MOD_ID);

    // === Block ===
    public static final DeferredHolder<Block, PepeMartBlock> PEPE_MART_BLOCK =
            BLOCKS.register("pepe_mart", () -> new PepeMartBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(3.0f)
                            .sound(SoundType.METAL)
                            .noOcclusion()
            ));

    // === Item ===
    public static final DeferredHolder<Item, BlockItem> PEPE_MART_ITEM =
            ITEMS.register("pepe_mart", () -> new BlockItem(
                    PEPE_MART_BLOCK.get(),
                    new Item.Properties()
            ));

    // === Block Entity ===
    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PepeMartBlockEntity>> PEPE_MART_BE =
            BLOCK_ENTITIES.register("pepe_mart", () ->
                    BlockEntityType.Builder.of(PepeMartBlockEntity::new, PEPE_MART_BLOCK.get())
                            .build(null)
            );

    // === Menu (IMenuTypeExtension for extra data) ===
    public static final DeferredHolder<MenuType<?>, MenuType<PepeMartMenu>> PEPE_MART_MENU =
            MENUS.register("pepe_mart", () ->
                    IMenuTypeExtension.create(PepeMartMenu::clientMenu)
            );

    // === Creative Tab ===
    public static final Supplier<CreativeModeTab> MART_TAB =
            CREATIVE_TABS.register("mart", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.servo_mart"))
                    .icon(() -> PEPE_MART_ITEM.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(PEPE_MART_ITEM.get());
                    })
                    .build()
            );

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENUS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }
}
