package com.servo.dungeons.block;

import com.mojang.serialization.MapCodec;
import com.servo.dungeons.DungeonRegistry;
import com.servo.dungeons.DungeonTier;
import com.servo.dungeons.dungeon.DungeonManager;
import com.servo.dungeons.item.DungeonKeyItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Central altar block for dungeon rituals.
 * Right-click with a DungeonKeyItem to start the ritual.
 * Requires 4 DungeonRuneBlocks in a cross pattern (N/S/E/W).
 */
public class DungeonPedestalBlock extends BaseEntityBlock {

    public static final MapCodec<DungeonPedestalBlock> CODEC = simpleCodec(DungeonPedestalBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<PedestalState> STATE = EnumProperty.create("state", PedestalState.class);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    public DungeonPedestalBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(STATE, PedestalState.INACTIVE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STATE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DungeonPedestalBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, DungeonRegistry.PEDESTAL_BE.get(), DungeonPedestalBlockEntity::serverTick);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                               Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return ItemInteractionResult.SUCCESS;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof DungeonPedestalBlockEntity pedestal)) {
            return ItemInteractionResult.FAIL;
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;

        // If THIS pedestal's dungeon is already active — allow re-entry
        if (pedestal.isActive()) {
            UUID dungeonId = pedestal.getDungeonId();
            DungeonManager manager = DungeonManager.getInstance();
            if (manager != null && dungeonId != null && manager.isActive(dungeonId)) {
                manager.reenterDungeon(serverPlayer, dungeonId);
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.FAIL;
        }

        // If player holds a dungeon key and no ritual in progress on THIS pedestal
        if (stack.getItem() instanceof DungeonKeyItem keyItem && !pedestal.isRitualInProgress()) {
            // Validate multiblock
            if (!pedestal.validateMultiblock()) {
                serverPlayer.sendSystemMessage(
                        Component.translatable("message.servo_dungeons.invalid_altar"));
                return ItemInteractionResult.FAIL;
            }

            // Start ritual — multiple simultaneous dungeons are allowed
            DungeonTier tier = keyItem.getTier();
            pedestal.startRitual(tier, serverPlayer);
            stack.shrink(1);
            serverPlayer.sendSystemMessage(
                    Component.translatable("message.servo_dungeons.ritual_started"));
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /**
     * Visual state of the pedestal block.
     */
    public enum PedestalState implements StringRepresentable {
        INACTIVE("inactive"),
        CHARGING("charging"),
        ACTIVE("active");

        private final String name;

        PedestalState(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
