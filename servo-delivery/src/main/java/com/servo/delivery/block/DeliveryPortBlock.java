package com.servo.delivery.block;

import com.mojang.serialization.MapCodec;
import com.servo.delivery.DeliveryRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Delivery Port — accepts items via automation (hopper, Create funnel/belt, RS exporter).
 * When formed, rendering is handled by the master terminal's GeckoLib model.
 * Clicking delegates to the terminal's GUI.
 */
public class DeliveryPortBlock extends BaseEntityBlock {

    public static final MapCodec<DeliveryPortBlock> CODEC = simpleCodec(DeliveryPortBlock::new);
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    public DeliveryPortBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FORMED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(FORMED) ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SlaveBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        return delegateToMaster(level, pos, player, hit);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            notifyMasterBroken(level, pos);
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    static InteractionResult delegateToMaster(Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (level.getBlockEntity(pos) instanceof SlaveBlockEntity slave) {
            DeliveryTerminalBlockEntity master = slave.getMaster();
            if (master != null) {
                master.openScreen(player);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    static void notifyMasterBroken(Level level, BlockPos pos) {
        if (level.isClientSide()) return;
        if (level.getBlockEntity(pos) instanceof SlaveBlockEntity slave) {
            DeliveryTerminalBlockEntity master = slave.getMaster();
            if (master != null) {
                master.tryFormMultiblock();
            }
        }
    }
}
