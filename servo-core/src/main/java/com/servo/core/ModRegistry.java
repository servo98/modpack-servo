package com.servo.core;

import com.servo.core.item.DungeonKeyItem;
import com.servo.core.item.TooltipItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRegistry {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, ServoCore.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, ServoCore.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ServoCore.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ServoCore.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ServoCore.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ServoCore.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ServoCore.MOD_ID);

    // === ITEMS: Currency ===
    public static final DeferredHolder<Item, TooltipItem> PEPE_COIN =
            ITEMS.register("pepe_coin", () -> new TooltipItem(
                    new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON),
                    "item.servo_core.pepe_coin.tooltip"
            ));

    // === ITEMS: Dungeon Materials ===
    public static final DeferredHolder<Item, TooltipItem> DUNGEON_ESSENCE =
            ITEMS.register("dungeon_essence", () -> new TooltipItem(
                    new Item.Properties().stacksTo(64).rarity(Rarity.RARE),
                    "item.servo_core.dungeon_essence.tooltip"
            ));

    public static final DeferredHolder<Item, TooltipItem> CORE_CRYSTAL_FRAGMENT =
            ITEMS.register("core_crystal_fragment", () -> new TooltipItem(
                    new Item.Properties().stacksTo(64).rarity(Rarity.EPIC),
                    "item.servo_core.core_crystal_fragment.tooltip"
            ));

    // === ITEMS: Dungeon Keys (4 tiers) ===
    public static final DeferredHolder<Item, DungeonKeyItem> BASIC_DUNGEON_KEY =
            ITEMS.register("basic_dungeon_key", () -> new DungeonKeyItem(1, Rarity.COMMON));

    public static final DeferredHolder<Item, DungeonKeyItem> ADVANCED_DUNGEON_KEY =
            ITEMS.register("advanced_dungeon_key", () -> new DungeonKeyItem(2, Rarity.UNCOMMON));

    public static final DeferredHolder<Item, DungeonKeyItem> MASTER_DUNGEON_KEY =
            ITEMS.register("master_dungeon_key", () -> new DungeonKeyItem(3, Rarity.RARE));

    public static final DeferredHolder<Item, DungeonKeyItem> CORE_DUNGEON_KEY =
            ITEMS.register("core_dungeon_key", () -> new DungeonKeyItem(4, Rarity.EPIC));

    // === CREATIVE TAB ===
    public static final Supplier<CreativeModeTab> CORE_TAB =
            CREATIVE_TABS.register("core", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.servo_core"))
                    .icon(() -> PEPE_COIN.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(PEPE_COIN.get());
                        output.accept(DUNGEON_ESSENCE.get());
                        output.accept(CORE_CRYSTAL_FRAGMENT.get());
                        output.accept(BASIC_DUNGEON_KEY.get());
                        output.accept(ADVANCED_DUNGEON_KEY.get());
                        output.accept(MASTER_DUNGEON_KEY.get());
                        output.accept(CORE_DUNGEON_KEY.get());
                    })
                    .build()
            );

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
