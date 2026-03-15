package com.servo.delivery.data;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

import java.util.UUID;

/**
 * Resolves the team UUID for a player.
 * Uses FTB Teams if available, otherwise falls back to the player's own UUID.
 */
public final class TeamHelper {

    private TeamHelper() {}

    public static UUID resolveTeamId(ServerPlayer player) {
        if (ModList.get().isLoaded("ftbteams")) {
            return FTBTeamsCompat.resolveTeam(player);
        }
        return player.getUUID();
    }

    /**
     * Isolated in a separate class so FTB Teams classes are never loaded
     * unless the mod is actually present (class loading safety).
     */
    private static final class FTBTeamsCompat {
        static UUID resolveTeam(ServerPlayer player) {
            return dev.ftb.mods.ftbteams.api.FTBTeamsAPI.api()
                    .getManager()
                    .getTeamForPlayer(player)
                    .map(team -> team.getId())
                    .orElse(player.getUUID());
        }
    }
}
