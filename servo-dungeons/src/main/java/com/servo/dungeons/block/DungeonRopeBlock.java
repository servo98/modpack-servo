package com.servo.dungeons.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Climbable rope block that hangs from a surface. Works like a ladder but doesn't need a wall behind it.
 * Placed by {@link com.servo.dungeons.item.DungeonRopeItem} which creates a chain of rope blocks downward.
 */
public class DungeonRopeBlock extends Block {

    public static final MapCodec<DungeonRopeBlock> CODEC = simpleCodec(DungeonRopeBlock::new);

    // Thin shape (like a chain)
    private static final VoxelShape SHAPE = Block.box(6, 0, 6, 10, 16, 10);

    @Override
    protected MapCodec<? extends Block> codec() { return CODEC; }

    public DungeonRopeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    // Make it climbable like a ladder
    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, net.minecraft.world.entity.LivingEntity entity) {
        return true;
    }
}
