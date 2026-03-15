package com.servo.dungeons.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * Rune block placed around the Dungeon Pedestal in a cross pattern.
 * Glows when ACTIVE=true (controlled by the pedestal during rituals).
 */
public class DungeonRuneBlock extends Block {

    public static final MapCodec<DungeonRuneBlock> CODEC = simpleCodec(DungeonRuneBlock::new);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    @Override
    protected MapCodec<? extends Block> codec() { return CODEC; }

    public DungeonRuneBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }
}
