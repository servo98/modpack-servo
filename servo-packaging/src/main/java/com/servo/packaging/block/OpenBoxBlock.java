package com.servo.packaging.block;

import com.mojang.serialization.MapCodec;
import com.servo.packaging.PackagingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Open Box — a placeable box that items can be inserted into interactively.
 * No GUI. Right-click with items to fill, empty hand to remove, shift+empty to pick up.
 * Auto-seals when full. Sealed box can be picked up as a ShippingBox item.
 */
public class OpenBoxBlock extends BaseEntityBlock {

    public static final IntegerProperty ITEMS_STORED = IntegerProperty.create("items", 0, 4);
    public static final BooleanProperty SEALED = BooleanProperty.create("sealed");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final MapCodec<OpenBoxBlock> CODEC = simpleCodec(OpenBoxBlock::new);

    // Low box shape: 12x8x12 centered
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 8, 14);

    public OpenBoxBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ITEMS_STORED, 0)
                .setValue(SEALED, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ITEMS_STORED, SEALED, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // Right-click WITH item in hand
    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level,
                                              BlockPos pos, Player player, InteractionHand hand,
                                              BlockHitResult hitResult) {
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof OpenBoxBlockEntity box)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (state.getValue(SEALED)) {
            // Pick up sealed box
            ItemStack sealedBox = box.pickUpSealed();
            if (!sealedBox.isEmpty()) {
                if (!player.getInventory().add(sealedBox)) {
                    player.drop(sealedBox, false);
                }
                level.removeBlock(pos, false);
                player.displayClientMessage(
                        Component.translatable("servo_packaging.box.sealed"), true);
            }
            return ItemInteractionResult.SUCCESS;
        }

        // Try adding held item
        if (box.tryAddItem(player, heldItem)) {
            if (box.isSealed()) {
                player.displayClientMessage(
                        Component.translatable("servo_packaging.box.sealed"), true);
            } else {
                player.displayClientMessage(
                        Component.translatable("servo_packaging.box.item_added",
                                heldItem.getHoverName()), true);
            }
            return ItemInteractionResult.SUCCESS;
        } else {
            // Wrong type
            player.displayClientMessage(
                    Component.translatable("servo_packaging.box.wrong_type"), true);
            return ItemInteractionResult.FAIL;
        }
    }

    // Right-click with EMPTY hand
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof OpenBoxBlockEntity box)) return InteractionResult.PASS;

        if (state.getValue(SEALED)) {
            // Pick up sealed box
            ItemStack sealedBox = box.pickUpSealed();
            if (!sealedBox.isEmpty()) {
                if (!player.getInventory().add(sealedBox)) {
                    player.drop(sealedBox, false);
                }
                level.removeBlock(pos, false);
                player.displayClientMessage(
                        Component.translatable("servo_packaging.box.sealed"), true);
            }
            return InteractionResult.SUCCESS;
        }

        if (player.isShiftKeyDown()) {
            // Shift + empty hand: pick up the open box (drop contents first)
            box.dropAllContents();
            ItemStack openBoxItem = new ItemStack(PackagingRegistry.OPEN_BOX_ITEM.get());
            if (!player.getInventory().add(openBoxItem)) {
                player.drop(openBoxItem, false);
            }
            level.removeBlock(pos, false);
            return InteractionResult.SUCCESS;
        }

        // Empty hand: remove last item
        ItemStack removed = box.tryRemoveItem();
        if (!removed.isEmpty()) {
            if (!player.getInventory().add(removed)) {
                player.drop(removed, false);
            }
            player.displayClientMessage(
                    Component.translatable("servo_packaging.box.item_removed",
                            removed.getHoverName()), true);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OpenBoxBlockEntity(pos, state);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof OpenBoxBlockEntity box) {
                if (state.getValue(SEALED)) {
                    // Sealed box broken by mining — drop as ShippingBox if contents remain
                    if (!box.isEmpty()) {
                        ItemStack sealedBox = box.pickUpSealed();
                        if (!sealedBox.isEmpty()) {
                            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), sealedBox);
                        }
                    }
                    // If empty, pickup code already gave the ShippingBox — drop nothing
                } else {
                    // Open box broken — drop contents + 1 open_box item
                    box.dropAllContents();
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                            new ItemStack(PackagingRegistry.OPEN_BOX_ITEM.get()));
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
