package com.servo.dungeons.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Invisible barrier block placed in doorways when a room enters COMBAT (ACTIVE) state.
 * Prevents players from fleeing combat. Removed when the room is CLEARED.
 *
 * <p>Properties:
 * <ul>
 *   <li>Indestructible by players (hardness -1)</li>
 *   <li>Full collision (blocks movement)</li>
 *   <li>Semi-transparent rendering so players can see through</li>
 *   <li>Light level 3 so it's visible in dark dungeons</li>
 * </ul>
 */
public class DungeonBarrierBlock extends Block {

    public static final MapCodec<DungeonBarrierBlock> CODEC = simpleCodec(DungeonBarrierBlock::new);

    @Override
    protected MapCodec<? extends Block> codec() { return CODEC; }

    public DungeonBarrierBlock(Properties properties) {
        super(properties);
    }

    /**
     * Make it unbreakable by players — only removed programmatically when room is cleared.
     */
    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return 0.0f;
    }
}
