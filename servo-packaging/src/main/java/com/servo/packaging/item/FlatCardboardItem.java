package com.servo.packaging.item;

import com.servo.packaging.PackagingRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Flat Cardboard — right-click to fold into an Open Box.
 * Crafted from 4 Paper + 1 String = 4 Flat Cardboard.
 */
public class FlatCardboardItem extends Item {

    public FlatCardboardItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            // Consume 1 flat cardboard
            stack.shrink(1);

            // Give 1 open box
            ItemStack openBox = new ItemStack(PackagingRegistry.OPEN_BOX_ITEM.get());
            if (!player.getInventory().add(openBox)) {
                player.drop(openBox, false);
            }

            level.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN,
                    SoundSource.PLAYERS, 0.8f, 1.2f);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
