package com.servo.dungeons;

import com.servo.dungeons.block.DungeonBeamBlock;
import com.servo.dungeons.block.DungeonBeamBlockEntity;
import com.servo.dungeons.block.DungeonPedestalBlock;
import com.servo.dungeons.block.DungeonPedestalBlockEntity;
import com.servo.dungeons.block.DungeonRuneBlock;
import com.servo.dungeons.block.ExitPortalBlock;
import com.servo.dungeons.item.DungeonKeyItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DungeonRegistry {

    // === Deferred Registers ===
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, ServoDungeons.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, ServoDungeons.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ServoDungeons.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ServoDungeons.MOD_ID);

    // === Dimension Keys ===
    public static final ResourceKey<Level> DUNGEON_LEVEL_KEY =
            ResourceKey.create(Registries.DIMENSION,
                    ResourceLocation.fromNamespaceAndPath(ServoDungeons.MOD_ID, "dungeon"));
    public static final ResourceKey<DimensionType> DUNGEON_DIMENSION_TYPE_KEY =
            ResourceKey.create(Registries.DIMENSION_TYPE,
                    ResourceLocation.fromNamespaceAndPath(ServoDungeons.MOD_ID, "dungeon_void"));

    // === Blocks ===
    public static final DeferredHolder<Block, DungeonPedestalBlock> PEDESTAL_BLOCK =
            BLOCKS.register("dungeon_pedestal", () -> new DungeonPedestalBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(5.0f, 1200.0f)
                            .sound(SoundType.STONE)
                            .noOcclusion()
                            .lightLevel(state -> switch (state.getValue(DungeonPedestalBlock.STATE)) {
                                case INACTIVE -> 0;
                                case CHARGING -> 7;
                                case ACTIVE -> 4;
                            })
            ));

    public static final DeferredHolder<Block, DungeonRuneBlock> RUNE_BLOCK =
            BLOCKS.register("dungeon_rune", () -> new DungeonRuneBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.LAPIS)
                            .strength(5.0f, 1200.0f)
                            .sound(SoundType.STONE)
                            .lightLevel(state -> state.getValue(DungeonRuneBlock.ACTIVE) ? 12 : 0)
            ));

    public static final DeferredHolder<Block, ExitPortalBlock> EXIT_PORTAL_BLOCK =
            BLOCKS.register("exit_portal", () -> new ExitPortalBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_CYAN)
                            .strength(-1.0f, 3600000.0f)
                            .sound(SoundType.GLASS)
                            .noOcclusion()
                            .noCollission()
                            .lightLevel(state -> 15)
                            .pushReaction(PushReaction.BLOCK)
            ));

    public static final DeferredHolder<Block, DungeonBeamBlock> BEAM_BLOCK =
            BLOCKS.register("dungeon_beam", () -> new DungeonBeamBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NONE)
                            .strength(-1.0f, 3600000.0f)
                            .sound(SoundType.EMPTY)
                            .noOcclusion()
                            .noCollission()
                            .lightLevel(state -> 10)
                            .pushReaction(PushReaction.BLOCK)
                            .noLootTable()
            ));

    // === Block Items ===
    public static final DeferredHolder<Item, BlockItem> PEDESTAL_ITEM =
            ITEMS.register("dungeon_pedestal", () -> new BlockItem(
                    PEDESTAL_BLOCK.get(), new Item.Properties()
            ));

    public static final DeferredHolder<Item, BlockItem> RUNE_ITEM =
            ITEMS.register("dungeon_rune", () -> new BlockItem(
                    RUNE_BLOCK.get(), new Item.Properties()
            ));

    // === Key Items ===
    public static final DeferredHolder<Item, DungeonKeyItem> KEY_BASIC =
            ITEMS.register("dungeon_key_basic", () -> new DungeonKeyItem(DungeonTier.BASIC));

    public static final DeferredHolder<Item, DungeonKeyItem> KEY_ADVANCED =
            ITEMS.register("dungeon_key_advanced", () -> new DungeonKeyItem(DungeonTier.ADVANCED));

    public static final DeferredHolder<Item, DungeonKeyItem> KEY_MASTER =
            ITEMS.register("dungeon_key_master", () -> new DungeonKeyItem(DungeonTier.MASTER));

    public static final DeferredHolder<Item, DungeonKeyItem> KEY_CORE =
            ITEMS.register("dungeon_key_core", () -> new DungeonKeyItem(DungeonTier.CORE));

    // === Block Entities ===
    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DungeonPedestalBlockEntity>> PEDESTAL_BE =
            BLOCK_ENTITIES.register("dungeon_pedestal", () ->
                    BlockEntityType.Builder.of(DungeonPedestalBlockEntity::new, PEDESTAL_BLOCK.get())
                            .build(null)
            );

    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DungeonBeamBlockEntity>> BEAM_BE =
            BLOCK_ENTITIES.register("dungeon_beam", () ->
                    BlockEntityType.Builder.of(DungeonBeamBlockEntity::new, BEAM_BLOCK.get())
                            .build(null)
            );

    // === Creative Tab ===
    public static final Supplier<CreativeModeTab> DUNGEONS_TAB =
            CREATIVE_TABS.register("dungeons", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.servo_dungeons"))
                    .icon(() -> KEY_BASIC.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(PEDESTAL_ITEM.get());
                        output.accept(RUNE_ITEM.get());
                        output.accept(KEY_BASIC.get());
                        output.accept(KEY_ADVANCED.get());
                        output.accept(KEY_MASTER.get());
                        output.accept(KEY_CORE.get());
                    })
                    .build()
            );

    // === Registration ===
    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }
}
