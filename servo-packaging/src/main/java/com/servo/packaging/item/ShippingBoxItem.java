package com.servo.packaging.item;

import com.servo.packaging.PackagingRegistry;
import com.servo.packaging.ServoPackaging;
import com.servo.packaging.block.OpenBoxBlock;
import com.servo.packaging.block.OpenBoxBlockEntity;
import com.servo.packaging.component.BoxContents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Shipping Box — a sealed box containing packed items.
 * Stores contents via the BoxContents DataComponent.
 * The player sees a tooltip with what's inside.
 */
public class ShippingBoxItem extends Item {

    public ShippingBoxItem(Properties properties) {
        super(properties.stacksTo(16));
    }

    /**
     * Right-click on a surface: place the sealed box as a block.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        ItemStack stack = context.getItemInHand();
        BoxContents contents = stack.get(PackagingRegistry.BOX_CONTENTS.get());
        if (contents == null) return InteractionResult.PASS;

        // Calculate placement position
        BlockPos placePos = context.getClickedPos().relative(context.getClickedFace());
        if (!level.getBlockState(placePos).isAir()) {
            return InteractionResult.PASS;
        }

        // Place sealed open box block facing the player
        Direction facing = player.getDirection().getOpposite();
        BlockState sealedState = PackagingRegistry.OPEN_BOX_BLOCK.get().defaultBlockState()
                .setValue(OpenBoxBlock.SEALED, true)
                .setValue(OpenBoxBlock.ITEMS_STORED, 4)
                .setValue(OpenBoxBlock.FACING, facing);
        level.setBlock(placePos, sealedState, 3);

        // Populate the block entity with contents
        BlockEntity be = level.getBlockEntity(placePos);
        if (be instanceof OpenBoxBlockEntity box) {
            box.loadFromBoxContents(contents);
        }

        stack.shrink(1);
        level.playSound(null, placePos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
        return InteractionResult.SUCCESS;
    }

    /**
     * Right-click in air: unbox contents.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BoxContents contents = stack.get(PackagingRegistry.BOX_CONTENTS.get());
        if (contents == null) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide()) {
            Item item = BuiltInRegistries.ITEM.get(contents.itemId());
            ServoPackaging.LOGGER.info("Unboxing: itemId={}, resolved={}, count={}, category={}",
                    contents.itemId(), BuiltInRegistries.ITEM.getKey(item), contents.count(), contents.category());

            if (item == Items.AIR) {
                ServoPackaging.LOGGER.warn("Unbox failed: item '{}' resolved to AIR!", contents.itemId());
                player.displayClientMessage(
                        Component.literal("Error: unknown item " + contents.itemId()).withStyle(ChatFormatting.RED), false);
                return InteractionResultHolder.fail(stack);
            }

            int remaining = contents.count();
            while (remaining > 0) {
                int dropCount = Math.min(remaining, item.getDefaultMaxStackSize());
                ItemStack dropStack = new ItemStack(item, dropCount);
                if (!player.getInventory().add(dropStack)) {
                    ItemEntity entity = player.drop(dropStack, false);
                    if (entity != null) {
                        entity.setNoPickUpDelay();
                    }
                }
                remaining -= dropCount;
            }

            stack.shrink(1);
            // Give back an open box
            ItemStack openBox = new ItemStack(PackagingRegistry.OPEN_BOX_ITEM.get());
            if (!player.getInventory().add(openBox)) {
                player.drop(openBox, false);
            }

            // Feedback
            String itemName = item.getDescription().getString();
            player.displayClientMessage(
                    Component.translatable("item.servo_packaging.shipping_box.unboxed", contents.count(), itemName)
                            .withStyle(ChatFormatting.GREEN), true);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        BoxContents contents = stack.get(PackagingRegistry.BOX_CONTENTS.get());
        if (contents != null) {
            var item = BuiltInRegistries.ITEM.get(contents.itemId());
            String itemName = item.getDescription().getString();

            ChatFormatting color = getCategoryColor(contents.category());
            tooltip.add(Component.translatable("item.servo_packaging.shipping_box.contains",
                            contents.count(), itemName)
                    .withStyle(color));

            tooltip.add(Component.translatable("item.servo_packaging.shipping_box.hint")
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    /**
     * Create a shipping box with the given contents.
     */
    public static ItemStack createBox(ResourceLocation itemId, int count, String category) {
        ItemStack box = new ItemStack(PackagingRegistry.SHIPPING_BOX_ITEM.get());
        box.set(PackagingRegistry.BOX_CONTENTS.get(), new BoxContents(itemId, count, category));
        return box;
    }

    /**
     * Get the label color for a category.
     */
    public static ChatFormatting getCategoryColor(String category) {
        return switch (category) {
            case "food" -> ChatFormatting.GREEN;
            case "crops" -> ChatFormatting.YELLOW;
            case "processed" -> ChatFormatting.BLUE;
            case "magic" -> ChatFormatting.DARK_PURPLE;
            case "special" -> ChatFormatting.GOLD;
            default -> ChatFormatting.GRAY;
        };
    }
}
