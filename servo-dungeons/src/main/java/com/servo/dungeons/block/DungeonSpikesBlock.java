package com.servo.dungeons.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Floor trap that damages entities walking on it.
 * Deals 2 damage (1 heart) and applies Slowness II for 2 seconds.
 */
public class DungeonSpikesBlock extends Block {

    public static final MapCodec<DungeonSpikesBlock> CODEC = simpleCodec(DungeonSpikesBlock::new);

    // Flat shape (like a carpet with spikes)
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);

    @Override
    protected MapCodec<? extends Block> codec() { return CODEC; }

    public DungeonSpikesBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity living) {
            // Deal 2 damage (1 heart) and slow
            living.hurt(level.damageSources().generic(), 2.0f);
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1)); // Slowness II for 2 sec
        }
    }
}
