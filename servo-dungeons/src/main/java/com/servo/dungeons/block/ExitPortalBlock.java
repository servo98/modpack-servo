package com.servo.dungeons.block;

import com.mojang.serialization.MapCodec;
import com.servo.dungeons.dungeon.DungeonManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Exit portal block placed in the dungeon dimension.
 * When a player walks through it, they are teleported back to the altar in the overworld.
 * No collision, full light emission, semi-transparent appearance.
 */
public class ExitPortalBlock extends Block {

    public static final MapCodec<ExitPortalBlock> CODEC = simpleCodec(ExitPortalBlock::new);

    @Override
    protected MapCodec<? extends Block> codec() { return CODEC; }

    public ExitPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide()) return;

        if (entity instanceof ServerPlayer serverPlayer) {
            DungeonManager manager = DungeonManager.getInstance();
            if (manager != null && manager.isActive()) {
                serverPlayer.sendSystemMessage(
                        Component.translatable("message.servo_dungeons.dungeon_exit"));
                manager.exitDungeon(serverPlayer);
            }
        }
    }
}
