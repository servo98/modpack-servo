package com.servo.delivery;

import com.servo.delivery.block.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
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
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DeliveryRegistry {

    // === Deferred Registers ===
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, ServoDelivery.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, ServoDelivery.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ServoDelivery.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ServoDelivery.MOD_ID);

    // === Blocks ===

    // Master block — renders full GeckoLib model, holds delivery state
    public static final DeferredHolder<Block, DeliveryTerminalBlock> TERMINAL_BLOCK =
            BLOCKS.register("delivery_terminal", () -> new DeliveryTerminalBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(-1.0f, 3600000.0f) // indestructible like bedrock
                            .sound(SoundType.METAL)
                            .noOcclusion()
                            .lightLevel(state -> state.getValue(DeliveryTerminalBlock.FORMED) ? 7 : 0)
            ));

    // Slave blocks — invisible when multiblock formed, delegate to master
    public static final DeferredHolder<Block, DeliveryPortBlock> PORT_BLOCK =
            BLOCKS.register("delivery_port", () -> new DeliveryPortBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(-1.0f, 3600000.0f)
                            .sound(SoundType.METAL)
                            .noOcclusion()
            ));

    public static final DeferredHolder<Block, ElevatorBaseBlock> BASE_BLOCK =
            BLOCKS.register("elevator_base", () -> new ElevatorBaseBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(-1.0f, 3600000.0f)
                            .sound(SoundType.METAL)
            ));

    public static final DeferredHolder<Block, ElevatorAntennaBlock> ANTENNA_BLOCK =
            BLOCKS.register("elevator_antenna", () -> new ElevatorAntennaBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(-1.0f, 3600000.0f)
                            .sound(SoundType.METAL)
                            .noOcclusion()
                            .lightLevel(state -> 4)
            ));

    // === Items ===
    public static final DeferredHolder<Item, BlockItem> TERMINAL_ITEM =
            ITEMS.register("delivery_terminal", () -> new BlockItem(
                    TERMINAL_BLOCK.get(), new Item.Properties()
            ));

    public static final DeferredHolder<Item, BlockItem> PORT_ITEM =
            ITEMS.register("delivery_port", () -> new BlockItem(
                    PORT_BLOCK.get(), new Item.Properties()
            ));

    public static final DeferredHolder<Item, BlockItem> BASE_ITEM =
            ITEMS.register("elevator_base", () -> new BlockItem(
                    BASE_BLOCK.get(), new Item.Properties()
            ));

    public static final DeferredHolder<Item, BlockItem> ANTENNA_ITEM =
            ITEMS.register("elevator_antenna", () -> new BlockItem(
                    ANTENNA_BLOCK.get(), new Item.Properties()
            ));

    // === Block Entities ===
    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DeliveryTerminalBlockEntity>> TERMINAL_BE =
            BLOCK_ENTITIES.register("delivery_terminal", () ->
                    BlockEntityType.Builder.of(DeliveryTerminalBlockEntity::new, TERMINAL_BLOCK.get())
                            .build(null)
            );

    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SlaveBlockEntity>> SLAVE_BE =
            BLOCK_ENTITIES.register("delivery_slave", () ->
                    BlockEntityType.Builder.of(SlaveBlockEntity::new,
                                    PORT_BLOCK.get(), BASE_BLOCK.get(), ANTENNA_BLOCK.get())
                            .build(null)
            );

    // === Menus ===
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ServoDelivery.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<DeliveryTerminalMenu>> TERMINAL_MENU =
            MENUS.register("delivery_terminal", () ->
                    IMenuTypeExtension.create(DeliveryTerminalMenu::new));

    // === Creative Tab ===
    public static final Supplier<CreativeModeTab> DELIVERY_TAB =
            CREATIVE_TABS.register("delivery", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.servo_delivery"))
                    .icon(() -> TERMINAL_ITEM.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(TERMINAL_ITEM.get());
                        output.accept(PORT_ITEM.get());
                        output.accept(BASE_ITEM.get());
                        output.accept(ANTENNA_ITEM.get());
                    })
                    .build()
            );

    // === Registration ===
    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENUS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }
}
