package com.servo.delivery.block;

import com.mojang.serialization.MapCodec;
import com.servo.delivery.DeliveryRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Master block of the Delivery Terminal multiblock.
 * Renders the full GeckoLib animated model when FORMED=true.
 * Holds all delivery progress state.
 *
 * Multiblock layout (looking at front, FACING = south):
 *
 *   [Antenna ]          <- pos.above()
 *   [Port][THIS][Port]  <- this row (ports at left/right relative to facing)
 *   [Base][Base][Base]  <- pos.below() and neighbors
 */
public class DeliveryTerminalBlock extends BaseEntityBlock {

    public static final MapCodec<DeliveryTerminalBlock> CODEC = simpleCodec(DeliveryTerminalBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    public DeliveryTerminalBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FORMED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FORMED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        // GeckoLib handles rendering when formed; use model when not formed
        return state.getValue(FORMED) ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DeliveryTerminalBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, DeliveryRegistry.TERMINAL_BE.get(), DeliveryTerminalBlockEntity::serverTick);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                              BlockPos pos, Player player, InteractionHand hand,
                                              BlockHitResult hit) {
        if (!state.getValue(FORMED)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (stack.isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof DeliveryTerminalBlockEntity terminal) {
            terminal.tryInsertItem(stack, player); // consumes item or shows "not needed"
            return ItemInteractionResult.CONSUME;  // always consume — don't open GUI
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        // When not formed, let players place blocks on the terminal faces
        if (!state.getValue(FORMED)) return InteractionResult.PASS;

        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof DeliveryTerminalBlockEntity terminal) {
            terminal.openScreen(player);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos,
                                   Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof DeliveryTerminalBlockEntity terminal) {
            terminal.tryFormMultiblock();
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock()) && state.getValue(FORMED)) {
            // Terminal is being destroyed while formed — reset all slaves to visible
            if (level.getBlockEntity(pos) instanceof DeliveryTerminalBlockEntity terminal) {
                terminal.unformSlaves();
            }
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof DeliveryTerminalBlockEntity terminal) {
            if (placer instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                terminal.setOwnerFromPlayer(serverPlayer);
            }
            terminal.tryFormMultiblock();
        }
    }

    /**
     * Checks if the multiblock structure is valid around the given terminal position.
     * Returns true if all 8 surrounding blocks are correct.
     */
    public static boolean validateStructure(Level level, BlockPos terminalPos, Direction facing) {
        Direction left = facing.getClockWise();
        Direction right = facing.getCounterClockWise();

        // Row below: 3 bases
        BlockPos baseCenter = terminalPos.below();
        if (!isBlock(level, baseCenter, DeliveryRegistry.BASE_BLOCK.get())) return false;
        if (!isBlock(level, baseCenter.relative(left), DeliveryRegistry.BASE_BLOCK.get())) return false;
        if (!isBlock(level, baseCenter.relative(right), DeliveryRegistry.BASE_BLOCK.get())) return false;

        // Same row: 2 ports
        if (!isBlock(level, terminalPos.relative(left), DeliveryRegistry.PORT_BLOCK.get())) return false;
        if (!isBlock(level, terminalPos.relative(right), DeliveryRegistry.PORT_BLOCK.get())) return false;

        // Row above: antenna
        if (!isBlock(level, terminalPos.above(), DeliveryRegistry.ANTENNA_BLOCK.get())) return false;

        return true;
    }

    private static boolean isBlock(Level level, BlockPos pos, Block expected) {
        return level.getBlockState(pos).is(expected);
    }
}
