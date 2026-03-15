package com.servo.dungeons.item;

import com.servo.dungeons.DungeonRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * Item that places rope blocks downward when used on a block.
 * Places up to 10 {@link com.servo.dungeons.block.DungeonRopeBlock} blocks below the clicked position,
 * stopping at non-air blocks.
 */
public class DungeonRopeItem extends Item {

    public DungeonRopeItem() {
        super(new Item.Properties().stacksTo(16));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        if (!level.isClientSide()) {
            // Place rope blocks downward from the clicked position
            // Max 10 blocks down, stops at non-air
            BlockPos below = clickedPos.below();
            int placed = 0;
            while (placed < 10 && level.getBlockState(below).isAir()) {
                level.setBlock(below, DungeonRegistry.ROPE_BLOCK.get().defaultBlockState(), 3);
                below = below.below();
                placed++;
            }

            if (placed > 0) {
                context.getItemInHand().shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
