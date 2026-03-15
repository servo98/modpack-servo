package com.servo.mart.compat;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/**
 * Soft-dependency helper for ProgressiveStages.
 * Returns true (unlocked) if the mod is not present.
 */
public class StageHelper {

    private static final boolean STAGES_LOADED = ModList.get().isLoaded("progressivestages");

    public static boolean hasStage(ServerPlayer player, String stage) {
        if (stage == null || stage.isEmpty()) return true;
        if (!STAGES_LOADED) return true;

        try {
            return StageChecker.check(player, stage);
        } catch (Exception e) {
            return true; // fail open
        }
    }

    /**
     * Isolated inner class — only loaded if ProgressiveStages is present.
     * Prevents ClassNotFoundException when the mod is absent.
     */
    private static class StageChecker {
        static boolean check(ServerPlayer player, String stage) {
            // ProgressiveStages uses the GameStages API under the hood
            // The API class is: net.darkhax.gamestages.GameStageHelper
            try {
                var clazz = Class.forName("net.darkhax.gamestages.GameStageHelper");
                var method = clazz.getMethod("hasStage", net.minecraft.world.entity.player.Player.class, String.class);
                return (boolean) method.invoke(null, player, stage);
            } catch (Exception e) {
                return true;
            }
        }
    }
}
