package com.servo.core.stage;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

/**
 * Curios-specific code for scanning equipped items.
 * This class is ONLY loaded when Curios mod is confirmed present (lazy class loading).
 * Do NOT reference this class from any path that could execute without Curios.
 */
public class CuriosSlotScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger("ServoCore/CuriosScanner");

    /**
     * Scan all Curios slots for items that the player's stage doesn't allow.
     * Restricted items are removed from the slot and dropped on the ground.
     */
    public static void scanAndEnforce(ServerPlayer player) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            handler.getCurios().forEach((slotId, stacksHandler) -> {
                checkSlotHandler(player, slotId, stacksHandler);
            });
        });
    }

    private static void checkSlotHandler(ServerPlayer player, String slotId, ICurioStacksHandler stacksHandler) {
        IDynamicStackHandler stacks = stacksHandler.getStacks();
        int slots = stacks.getSlots();

        for (int i = 0; i < slots; i++) {
            ItemStack equipped = stacks.getStackInSlot(i);
            if (equipped.isEmpty()) continue;

            String requiredStage = CuriosStageEnforcer.getRequiredStage(equipped);
            if (requiredStage == null) continue;

            if (!CuriosStageEnforcer.playerHasStage(player, requiredStage)) {
                // Remove from Curios slot
                stacks.setStackInSlot(i, ItemStack.EMPTY);

                // Drop the item at the player's feet
                ItemEntity drop = new ItemEntity(
                        player.level(),
                        player.getX(), player.getY(), player.getZ(),
                        equipped
                );
                drop.setNoPickUpDelay(); // Player can pick it up, but ProgressiveStages will block it
                player.level().addFreshEntity(drop);

                // Notify player
                String itemName = equipped.getHoverName().getString();
                player.sendSystemMessage(Component.translatable(
                        "servo_core.curios_enforcer.removed",
                        itemName, requiredStage
                ).withStyle(style -> style.withColor(0xFF5555)));

                LOGGER.debug("Removed {} from Curios slot {}[{}] of player {} (requires {})",
                        equipped, slotId, i, player.getName().getString(), requiredStage);
            }
        }
    }
}
