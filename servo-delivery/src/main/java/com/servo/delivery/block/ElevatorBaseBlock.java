package com.servo.delivery.block;

import com.mojang.serialization.MapCodec;
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
 * Structural base block for the Delivery Terminal multiblock.
 * 3 of these form the bottom row.
 */
public class ElevatorBaseBlock extends BaseEntityBlock {

    public static final MapCodec<ElevatorBaseBlock> CODEC = simpleCodec(ElevatorBaseBlock::new);
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    public ElevatorBaseBlock(Properties properties) {
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
        return DeliveryPortBlock.delegateToMaster(level, pos, player, hit);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            DeliveryPortBlock.notifyMasterBroken(level, pos);
        }
        super.onRemove(state, level, pos, newState, moved);
    }
}
