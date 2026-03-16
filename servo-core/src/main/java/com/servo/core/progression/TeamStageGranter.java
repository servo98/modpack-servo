package com.servo.core.progression;

import com.servo.core.ServoCore;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Grants ProgressiveStages stages to all members of an FTB Team.
 * Uses reflection for both FTB Teams and GameStages APIs to maintain soft-dependency safety.
 *
 * <p>If FTB Teams is not loaded, the stage is only granted to the triggering player.
 * If ProgressiveStages/GameStages is not loaded, stage granting is a no-op (logged).
 */
public final class TeamStageGranter {

    private TeamStageGranter() {}

    private static final boolean FTB_TEAMS_LOADED = ModList.get().isLoaded("ftbteams");
    private static final boolean STAGES_LOADED = ModList.get().isLoaded("progressivestages");

    /**
     * Grants the specified stage to all online members of the team.
     * Returns the list of player names who received the stage.
     */
    public static List<String> grantStageToTeam(MinecraftServer server, UUID teamId, String stage) {
        List<String> grantedTo = new ArrayList<>();

        if (!STAGES_LOADED) {
            ServoCore.LOGGER.warn("ProgressiveStages not loaded, cannot grant stage '{}'", stage);
            return grantedTo;
        }

        List<ServerPlayer> members = resolveTeamMembers(server, teamId);

        for (ServerPlayer player : members) {
            if (grantStageToPlayer(player, stage)) {
                grantedTo.add(player.getName().getString());
            }
        }

        return grantedTo;
    }

    /**
     * Resolves all online players in the given team.
     * Falls back to finding any player with that UUID if FTB Teams is not present.
     */
    private static List<ServerPlayer> resolveTeamMembers(MinecraftServer server, UUID teamId) {
        if (FTB_TEAMS_LOADED) {
            try {
                return FTBTeamsCompat.getOnlineMembers(server, teamId);
            } catch (Exception e) {
                ServoCore.LOGGER.error("Failed to resolve FTB Teams members for team {}", teamId, e);
            }
        }

        // Fallback: team ID might be a player UUID (solo player without FTB Teams)
        List<ServerPlayer> result = new ArrayList<>();
        ServerPlayer player = server.getPlayerList().getPlayer(teamId);
        if (player != null) {
            result.add(player);
        }
        return result;
    }

    /**
     * Grants a stage to a single player via GameStages API (reflection).
     * Returns true if the stage was newly granted (player didn't already have it).
     */
    private static boolean grantStageToPlayer(ServerPlayer player, String stage) {
        try {
            return StageReflectionHelper.addStage(player, stage);
        } catch (Exception e) {
            ServoCore.LOGGER.error("Failed to grant stage '{}' to player {}",
                    stage, player.getName().getString(), e);
            return false;
        }
    }

    /**
     * Checks if a player already has the given stage.
     */
    public static boolean playerHasStage(ServerPlayer player, String stage) {
        if (!STAGES_LOADED) return false;
        try {
            return StageReflectionHelper.hasStage(player, stage);
        } catch (Exception e) {
            ServoCore.LOGGER.debug("Failed to check stage '{}' for player {}", stage, player.getName().getString());
            return false;
        }
    }

    /**
     * Isolated class for FTB Teams API calls. Only loaded when ftbteams is present.
     */
    private static class FTBTeamsCompat {
        static List<ServerPlayer> getOnlineMembers(MinecraftServer server, UUID teamId) {
            List<ServerPlayer> online = new ArrayList<>();
            var api = dev.ftb.mods.ftbteams.api.FTBTeamsAPI.api().getManager();
            var teamOpt = api.getTeamByID(teamId);
            if (teamOpt.isEmpty()) {
                // teamId might be a player UUID (party of 1); try to find their team
                ServerPlayer player = server.getPlayerList().getPlayer(teamId);
                if (player != null) {
                    teamOpt = api.getTeamForPlayer(player);
                }
            }
            if (teamOpt.isPresent()) {
                var team = teamOpt.get();
                for (UUID memberId : team.getMembers()) {
                    ServerPlayer member = server.getPlayerList().getPlayer(memberId);
                    if (member != null) {
                        online.add(member);
                    }
                }
            } else {
                // Last resort: if teamId is a player UUID, just use that player
                ServerPlayer player = server.getPlayerList().getPlayer(teamId);
                if (player != null) {
                    online.add(player);
                }
            }
            return online;
        }
    }

    /**
     * Isolated class for GameStages reflection calls. Avoids direct class references
     * so the code compiles even without GameStages on the classpath.
     */
    private static class StageReflectionHelper {
        @SuppressWarnings("unchecked")
        static boolean addStage(ServerPlayer player, String stage) throws Exception {
            // GameStageHelper.addStage(player, stageName) — grants + syncs
            Class<?> helperClass = Class.forName("net.darkhax.gamestages.GameStageHelper");
            // First check if player already has the stage
            var hasMethod = helperClass.getMethod("hasStage",
                    net.minecraft.world.entity.player.Player.class, String.class);
            boolean alreadyHas = (boolean) hasMethod.invoke(null, player, stage);
            if (alreadyHas) return false;

            // Grant the stage
            var getPlayerData = helperClass.getMethod("getPlayerData", net.minecraft.world.entity.player.Player.class);
            Object stageData = getPlayerData.invoke(null, player);

            // IStageData.addStage(String)
            var addMethod = stageData.getClass().getMethod("addStage", String.class);
            addMethod.invoke(stageData, stage);

            // Sync to client — GameStageHelper.syncPlayer(ServerPlayer)
            try {
                var syncMethod = helperClass.getMethod("syncPlayer", ServerPlayer.class);
                syncMethod.invoke(null, player);
            } catch (NoSuchMethodException e) {
                // Some versions don't have syncPlayer; the stage is still granted server-side
                ServoCore.LOGGER.debug("GameStageHelper.syncPlayer not found, stage granted server-side only");
            }

            return true;
        }

        static boolean hasStage(ServerPlayer player, String stage) throws Exception {
            Class<?> helperClass = Class.forName("net.darkhax.gamestages.GameStageHelper");
            var method = helperClass.getMethod("hasStage",
                    net.minecraft.world.entity.player.Player.class, String.class);
            return (boolean) method.invoke(null, player, stage);
        }
    }
}
