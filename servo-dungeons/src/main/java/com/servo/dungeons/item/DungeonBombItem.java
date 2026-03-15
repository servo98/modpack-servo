package com.servo.dungeons.item;

import com.servo.dungeons.block.CrackedWallBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Throwable item that breaks {@link CrackedWallBlock} on impact.
 * Use on a cracked wall within reach range to destroy it with an explosion sound.
 */
public class DungeonBombItem extends Item {

    public DungeonBombItem() {
        super(new Item.Properties().stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            // Simple "use" that checks for cracked wall in front of player
            // Breaks any CrackedWallBlock the player is looking at
            BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockPos targetPos = hit.getBlockPos();
                if (level.getBlockState(targetPos).getBlock() instanceof CrackedWallBlock) {
                    level.destroyBlock(targetPos, false);
                    // Play explosion sound
                    level.playSound(null, targetPos, SoundEvents.GENERIC_EXPLODE.value(),
                            SoundSource.BLOCKS, 0.5f, 1.0f);
                    // Consume item
                    stack.shrink(1);
                    return InteractionResultHolder.success(stack);
                }
            }
        }
        return InteractionResultHolder.pass(stack);
    }
}
