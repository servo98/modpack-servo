package com.servo.core.gacha;

import com.servo.core.ServoCore;
import net.minecraft.world.entity.player.Player;

/**
 * Public static API for the gacha pity system.
 *
 * <p>Use this helper from gacha machine logic or KubeJS integration to:
 * <ul>
 *     <li>Query effective rates adjusted for pity</li>
 *     <li>Record pull results (increments counter or resets on Epic+)</li>
 *     <li>Check hard pity status</li>
 * </ul>
 *
 * <p>Example usage from a gacha machine:
 * <pre>{@code
 *     float epicRate = GachaPityHelper.getEffectiveEpicRate(player, GachaMachineType.GREEN);
 *     float legendaryRate = GachaPityHelper.getEffectiveLegendaryRate(player, GachaMachineType.GREEN);
 *
 *     // ... roll the gacha ...
 *
 *     boolean gotEpicOrHigher = (result == EPIC || result == LEGENDARY);
 *     GachaPityHelper.recordPullResult(player, GachaMachineType.GREEN, gotEpicOrHigher);
 * }</pre>
 */
public final class GachaPityHelper {

    private GachaPityHelper() {} // Utility class

    /**
     * Gets the pity data attached to a player. Creates default data if not present.
     */
    public static GachaPityData getPityData(Player player) {
        return player.getData(GachaPityAttachment.GACHA_PITY.get());
    }

    /**
     * Records the result of a gacha pull. If the player got Epic or higher,
     * the pity counter resets. Otherwise, the counter increments.
     *
     * @param player           the player who pulled
     * @param machineType      which machine was used
     * @param gotEpicOrHigher  true if the result was Epic or Legendary rarity
     */
    public static void recordPullResult(Player player, GachaMachineType machineType, boolean gotEpicOrHigher) {
        GachaPityData data = getPityData(player);
        if (gotEpicOrHigher) {
            data.resetPity(machineType);
            ServoCore.LOGGER.debug("Gacha pity reset for {} on {} machine",
                    player.getName().getString(), machineType.getId());
        } else {
            data.recordPull(machineType);
            ServoCore.LOGGER.debug("Gacha pity incremented to {} for {} on {} machine",
                    data.getPullCount(machineType), player.getName().getString(), machineType.getId());
        }
        // Mark the attachment as dirty so it gets saved
        player.setData(GachaPityAttachment.GACHA_PITY.get(), data);
    }

    /**
     * Returns the effective Epic rate for a player on a machine type,
     * including pity bonus. At hard pity, returns 0 (since it's guaranteed Legendary).
     */
    public static float getEffectiveEpicRate(Player player, GachaMachineType machineType) {
        return getPityData(player).getEffectiveRates(machineType).epicRate();
    }

    /**
     * Returns the effective Legendary rate for a player on a machine type.
     * Normally this equals the base rate. At hard pity (50 pulls), returns 1.0 (100%).
     */
    public static float getEffectiveLegendaryRate(Player player, GachaMachineType machineType) {
        return getPityData(player).getEffectiveRates(machineType).legendaryRate();
    }

    /**
     * Returns true if the player has hit hard pity (50+ pulls without Epic+)
     * on the given machine type.
     */
    public static boolean isHardPity(Player player, GachaMachineType machineType) {
        return getPityData(player).isHardPity(machineType);
    }

    /**
     * Returns the number of pulls since the last Epic+ result for a player
     * on the given machine type.
     */
    public static int getPullsSinceLastEpic(Player player, GachaMachineType machineType) {
        return getPityData(player).getPullCount(machineType);
    }
}
