package com.servo.dungeons.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A stone brick block that looks cracked and can only be broken by {@link com.servo.dungeons.item.DungeonBombItem}.
 * Unbreakable by hand or tools — the bomb is the only way to destroy it.
 */
public class CrackedWallBlock extends Block {

    public static final MapCodec<CrackedWallBlock> CODEC = simpleCodec(CrackedWallBlock::new);

    @Override
    protected MapCodec<? extends Block> codec() { return CODEC; }

    public CrackedWallBlock(Properties properties) {
        super(properties);
    }

    // Make it unbreakable by normal means (only DungeonBombItem can break it)
    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return 0.0f;
    }
}
