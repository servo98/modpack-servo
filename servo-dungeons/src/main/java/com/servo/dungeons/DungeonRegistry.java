package com.servo.dungeons;

import com.servo.dungeons.block.CrackedWallBlock;
import com.servo.dungeons.block.DungeonBarrierBlock;
import com.servo.dungeons.block.DungeonBeamBlock;
import com.servo.dungeons.block.DungeonBeamBlockEntity;
import com.servo.dungeons.block.DungeonPedestalBlock;
import com.servo.dungeons.block.DungeonPedestalBlockEntity;
import com.servo.dungeons.block.DungeonRopeBlock;
import com.servo.dungeons.block.DungeonRuneBlock;
import com.servo.dungeons.block.DungeonSpikesBlock;
import com.servo.dungeons.block.ExitPortalBlock;
import com.servo.dungeons.entity.boss.*;
import com.servo.dungeons.item.BossKeyItem;
import com.servo.dungeons.item.DungeonBombItem;
import com.servo.dungeons.item.DungeonKeyItem;
import com.servo.dungeons.item.DungeonRopeItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
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
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ServoDungeons.MOD_ID);
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

    public static final DeferredHolder<Block, CrackedWallBlock> CRACKED_WALL_BLOCK =
            BLOCKS.register("cracked_wall", () -> new CrackedWallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(-1.0f, 3600000.0f)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()
            ));

    public static final DeferredHolder<Block, DungeonRopeBlock> ROPE_BLOCK =
            BLOCKS.register("dungeon_rope", () -> new DungeonRopeBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WOOD)
                            .strength(0.5f)
                            .sound(SoundType.CHAIN)
                            .noOcclusion()
                            .pushReaction(PushReaction.DESTROY)
            ));

    public static final DeferredHolder<Block, DungeonSpikesBlock> SPIKES_BLOCK =
            BLOCKS.register("dungeon_spikes", () -> new DungeonSpikesBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(2.0f)
                            .sound(SoundType.METAL)
                            .noOcclusion()
            ));

    public static final DeferredHolder<Block, DungeonBarrierBlock> BARRIER_BLOCK =
            BLOCKS.register("dungeon_barrier", () -> new DungeonBarrierBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_RED)
                            .strength(-1.0f, 3600000.0f)
                            .sound(SoundType.GLASS)
                            .noOcclusion()
                            .lightLevel(state -> 3)
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

    public static final DeferredHolder<Item, BlockItem> CRACKED_WALL_ITEM =
            ITEMS.register("cracked_wall", () -> new BlockItem(
                    CRACKED_WALL_BLOCK.get(), new Item.Properties()
            ));

    public static final DeferredHolder<Item, BlockItem> ROPE_BLOCK_ITEM =
            ITEMS.register("dungeon_rope", () -> new BlockItem(
                    ROPE_BLOCK.get(), new Item.Properties()
            ));

    public static final DeferredHolder<Item, BlockItem> SPIKES_BLOCK_ITEM =
            ITEMS.register("dungeon_spikes", () -> new BlockItem(
                    SPIKES_BLOCK.get(), new Item.Properties()
            ));

    public static final DeferredHolder<Item, BlockItem> BARRIER_ITEM =
            ITEMS.register("dungeon_barrier", () -> new BlockItem(
                    BARRIER_BLOCK.get(), new Item.Properties()
            ));

    // === Dungeon Items ===
    public static final DeferredHolder<Item, DungeonBombItem> DUNGEON_BOMB =
            ITEMS.register("dungeon_bomb", DungeonBombItem::new);

    public static final DeferredHolder<Item, DungeonRopeItem> DUNGEON_ROPE =
            ITEMS.register("dungeon_rope_item", DungeonRopeItem::new);

    // === Drop Items ===
    public static final DeferredHolder<Item, Item> DUNGEON_ESSENCE =
            ITEMS.register("dungeon_essence", () -> new Item(
                    new Item.Properties().rarity(Rarity.RARE).stacksTo(64)
            ));

    // === Boss Drop Items (for key crafting chain) ===
    public static final DeferredHolder<Item, Item> BOSS_HEART =
            ITEMS.register("boss_heart", () -> new Item(
                    new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(16)
            ));

    public static final DeferredHolder<Item, Item> BOSS_CRYSTAL =
            ITEMS.register("boss_crystal", () -> new Item(
                    new Item.Properties().rarity(Rarity.EPIC).stacksTo(16)
            ));

    public static final DeferredHolder<Item, Item> NETHER_STAR_FRAGMENT =
            ITEMS.register("nether_star_fragment", () -> new Item(
                    new Item.Properties().rarity(Rarity.RARE).stacksTo(64)
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

    // === Boss Key Items ===
    public static final DeferredHolder<Item, BossKeyItem> BOSS_KEY_1 =
            ITEMS.register("boss_key_ch1", () -> new BossKeyItem(1));
    public static final DeferredHolder<Item, BossKeyItem> BOSS_KEY_2 =
            ITEMS.register("boss_key_ch2", () -> new BossKeyItem(2));
    public static final DeferredHolder<Item, BossKeyItem> BOSS_KEY_3 =
            ITEMS.register("boss_key_ch3", () -> new BossKeyItem(3));
    public static final DeferredHolder<Item, BossKeyItem> BOSS_KEY_4 =
            ITEMS.register("boss_key_ch4", () -> new BossKeyItem(4));
    public static final DeferredHolder<Item, BossKeyItem> BOSS_KEY_5 =
            ITEMS.register("boss_key_ch5", () -> new BossKeyItem(5));
    public static final DeferredHolder<Item, BossKeyItem> BOSS_KEY_6 =
            ITEMS.register("boss_key_ch6", () -> new BossKeyItem(6));
    public static final DeferredHolder<Item, BossKeyItem> BOSS_KEY_7 =
            ITEMS.register("boss_key_ch7", () -> new BossKeyItem(7));
    public static final DeferredHolder<Item, BossKeyItem> BOSS_KEY_8 =
            ITEMS.register("boss_key_ch8", () -> new BossKeyItem(8));

    // === Boss Entity Types ===
    public static final DeferredHolder<EntityType<?>, EntityType<GuardianDelBosqueBoss>> GUARDIAN_DEL_BOSQUE =
            ENTITY_TYPES.register("guardian_del_bosque", () ->
                    EntityType.Builder.of(GuardianDelBosqueBoss::new, MobCategory.MONSTER)
                            .sized(1.4F, 3.0F)
                            .clientTrackingRange(64)
                            .build(ServoDungeons.MOD_ID + ":guardian_del_bosque"));

    public static final DeferredHolder<EntityType<?>, EntityType<BestiaGlotonaBoss>> BESTIA_GLOTONA =
            ENTITY_TYPES.register("bestia_glotona", () ->
                    EntityType.Builder.of(BestiaGlotonaBoss::new, MobCategory.MONSTER)
                            .sized(3.0F, 2.5F)
                            .clientTrackingRange(64)
                            .build(ServoDungeons.MOD_ID + ":bestia_glotona"));

    public static final DeferredHolder<EntityType<?>, EntityType<ColosoMecanicoBoss>> COLOSO_MECANICO =
            ENTITY_TYPES.register("coloso_mecanico", () ->
                    EntityType.Builder.of(ColosoMecanicoBoss::new, MobCategory.MONSTER)
                            .sized(2.5F, 5.0F)
                            .clientTrackingRange(64)
                            .build(ServoDungeons.MOD_ID + ":coloso_mecanico"));

    public static final DeferredHolder<EntityType<?>, EntityType<LocomotoraFantasmaBoss>> LOCOMOTORA_FANTASMA =
            ENTITY_TYPES.register("locomotora_fantasma", () ->
                    EntityType.Builder.of(LocomotoraFantasmaBoss::new, MobCategory.MONSTER)
                            .sized(2.0F, 3.0F)
                            .clientTrackingRange(80)
                            .build(ServoDungeons.MOD_ID + ":locomotora_fantasma"));

    public static final DeferredHolder<EntityType<?>, EntityType<ElArquitectoBoss>> EL_ARQUITECTO =
            ENTITY_TYPES.register("el_arquitecto", () ->
                    EntityType.Builder.of(ElArquitectoBoss::new, MobCategory.MONSTER)
                            .sized(1.2F, 4.0F)
                            .clientTrackingRange(64)
                            .build(ServoDungeons.MOD_ID + ":el_arquitecto"));

    public static final DeferredHolder<EntityType<?>, EntityType<SenorCosechasBoss>> SENOR_COSECHAS =
            ENTITY_TYPES.register("senor_cosechas", () ->
                    EntityType.Builder.of(SenorCosechasBoss::new, MobCategory.MONSTER)
                            .sized(2.5F, 6.0F)
                            .clientTrackingRange(64)
                            .build(ServoDungeons.MOD_ID + ":senor_cosechas"));

    public static final DeferredHolder<EntityType<?>, EntityType<NucleoDelDungeonBoss>> NUCLEO_DEL_DUNGEON =
            ENTITY_TYPES.register("nucleo_del_dungeon", () ->
                    EntityType.Builder.of(NucleoDelDungeonBoss::new, MobCategory.MONSTER)
                            .sized(4.0F, 4.0F)
                            .clientTrackingRange(80)
                            .build(ServoDungeons.MOD_ID + ":nucleo_del_dungeon"));

    public static final DeferredHolder<EntityType<?>, EntityType<DevoradorDesMundosBoss>> DEVORADOR_DES_MUNDOS =
            ENTITY_TYPES.register("devorador_des_mundos", () ->
                    EntityType.Builder.of(DevoradorDesMundosBoss::new, MobCategory.MONSTER)
                            .sized(3.0F, 3.0F)
                            .clientTrackingRange(96)
                            .build(ServoDungeons.MOD_ID + ":devorador_des_mundos"));

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
                        output.accept(CRACKED_WALL_ITEM.get());
                        output.accept(ROPE_BLOCK_ITEM.get());
                        output.accept(SPIKES_BLOCK_ITEM.get());
                        output.accept(BARRIER_ITEM.get());
                        output.accept(KEY_BASIC.get());
                        output.accept(KEY_ADVANCED.get());
                        output.accept(KEY_MASTER.get());
                        output.accept(KEY_CORE.get());
                        output.accept(DUNGEON_ESSENCE.get());
                        output.accept(BOSS_HEART.get());
                        output.accept(BOSS_CRYSTAL.get());
                        output.accept(NETHER_STAR_FRAGMENT.get());
                        output.accept(DUNGEON_BOMB.get());
                        output.accept(DUNGEON_ROPE.get());
                        output.accept(BOSS_KEY_1.get());
                        output.accept(BOSS_KEY_2.get());
                        output.accept(BOSS_KEY_3.get());
                        output.accept(BOSS_KEY_4.get());
                        output.accept(BOSS_KEY_5.get());
                        output.accept(BOSS_KEY_6.get());
                        output.accept(BOSS_KEY_7.get());
                        output.accept(BOSS_KEY_8.get());
                    })
                    .build()
            );

    // === Registration ===
    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);

        // Register entity attributes
        modEventBus.addListener(DungeonRegistry::registerEntityAttributes);
    }

    /**
     * Register attributes for all boss entity types.
     * Called automatically via the mod event bus.
     */
    private static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(GUARDIAN_DEL_BOSQUE.get(), GuardianDelBosqueBoss.createAttributes().build());
        event.put(BESTIA_GLOTONA.get(), BestiaGlotonaBoss.createAttributes().build());
        event.put(COLOSO_MECANICO.get(), ColosoMecanicoBoss.createAttributes().build());
        event.put(LOCOMOTORA_FANTASMA.get(), LocomotoraFantasmaBoss.createAttributes().build());
        event.put(EL_ARQUITECTO.get(), ElArquitectoBoss.createAttributes().build());
        event.put(SENOR_COSECHAS.get(), SenorCosechasBoss.createAttributes().build());
        event.put(NUCLEO_DEL_DUNGEON.get(), NucleoDelDungeonBoss.createAttributes().build());
        event.put(DEVORADOR_DES_MUNDOS.get(), DevoradorDesMundosBoss.createAttributes().build());
    }

    /**
     * Get the boss entity type for a given chapter number (1-8).
     *
     * @param chapter the chapter number
     * @return the EntityType for that chapter's boss, or null if invalid
     */
    @org.jetbrains.annotations.Nullable
    public static EntityType<? extends AbstractDungeonBoss> getBossTypeForChapter(int chapter) {
        return switch (chapter) {
            case 1 -> GUARDIAN_DEL_BOSQUE.get();
            case 2 -> BESTIA_GLOTONA.get();
            case 3 -> COLOSO_MECANICO.get();
            case 4 -> LOCOMOTORA_FANTASMA.get();
            case 5 -> EL_ARQUITECTO.get();
            case 6 -> SENOR_COSECHAS.get();
            case 7 -> NUCLEO_DEL_DUNGEON.get();
            case 8 -> DEVORADOR_DES_MUNDOS.get();
            default -> null;
        };
    }
}
