package com.servo.core.progression;

import com.servo.core.ServoCore;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.fml.ModList;

import java.util.List;
import java.util.UUID;

/**
 * Listens to DeliveryCompleteEvent (from servo_delivery) and manages chapter progression.
 *
 * <p>Flow:
 * <ol>
 *     <li>DeliveryCompleteEvent fires -> mark delivery_ch[N] = true</li>
 *     <li>Check if boss_ch[N] is also true</li>
 *     <li>If both -> grant servo_ch[N+1] to all team members via ProgressiveStages</li>
 *     <li>If only delivery -> log and message: "Entrega completa. Derrota al boss para avanzar."</li>
 * </ol>
 *
 * <p>Boss kills are handled separately (future: BossKillEvent from servo_dungeons).
 * The conditions can be completed in any order.
 */
public final class ChapterProgressionHandler {

    private ChapterProgressionHandler() {}

    /** Chapter names for display messages (Spanish). */
    private static final String[] CHAPTER_NAMES = {
            "Primeras Raices",     // Ch1
            "La Mesa Servida",     // Ch2
            "Engranajes y Magia",  // Ch3
            "Horizontes",          // Ch4
            "La Red",              // Ch5
            "Maestria",            // Ch6
            "Profundidades",       // Ch7
            "El Legado"            // Ch8
    };

    /**
     * Registers the event listener for DeliveryCompleteEvent.
     * Only call this if servo_delivery is loaded.
     */
    public static void init() {
        if (!ModList.get().isLoaded("servo_delivery")) {
            ServoCore.LOGGER.info("servo_delivery not loaded, chapter progression handler disabled");
            return;
        }

        // Use a separate class to avoid loading DeliveryCompleteEvent when servo_delivery is absent
        DeliveryEventBridge.register();
        ServoCore.LOGGER.info("Chapter progression handler registered (listening for DeliveryCompleteEvent)");
    }

    /**
     * Called when a chapter delivery is completed.
     * Package-private: invoked by {@link DeliveryEventBridge}.
     */
    static void onDeliveryComplete(MinecraftServer server, UUID teamId, int chapter) {
        ChapterProgressionData data = ChapterProgressionData.get(server);

        // Mark delivery as complete
        boolean isNew = data.markDeliveryComplete(teamId, chapter);
        if (!isNew) {
            ServoCore.LOGGER.debug("Team {} delivery for chapter {} was already marked complete", teamId, chapter);
            return;
        }

        ServoCore.LOGGER.info("Team {} completed delivery for chapter {}", teamId, chapter);

        // Check if boss is also done
        if (data.isBossKilled(teamId, chapter)) {
            // Both conditions met! Grant next stage.
            tryGrantNextStage(server, data, teamId, chapter);
        } else {
            // Only delivery done — notify team
            notifyTeamDeliveryOnly(server, teamId, chapter);
        }
    }

    /**
     * Called when a chapter boss is killed.
     * Public API for servo_dungeons (or any future boss kill event handler).
     */
    public static void onBossKilled(MinecraftServer server, UUID teamId, int chapter) {
        ChapterProgressionData data = ChapterProgressionData.get(server);

        boolean isNew = data.markBossKilled(teamId, chapter);
        if (!isNew) {
            ServoCore.LOGGER.debug("Team {} boss kill for chapter {} was already marked", teamId, chapter);
            return;
        }

        ServoCore.LOGGER.info("Team {} defeated boss for chapter {}", teamId, chapter);

        // Check if delivery is also done
        if (data.isDeliveryComplete(teamId, chapter)) {
            tryGrantNextStage(server, data, teamId, chapter);
        } else {
            notifyTeamBossOnly(server, teamId, chapter);
        }
    }

    /**
     * Both conditions met — grant servo_ch[N+1] to the whole team.
     */
    private static void tryGrantNextStage(MinecraftServer server, ChapterProgressionData data,
                                           UUID teamId, int completedChapter) {
        // Prevent double-granting
        if (data.isStageGranted(teamId, completedChapter)) {
            ServoCore.LOGGER.debug("Stage for chapter {} already granted to team {}", completedChapter, teamId);
            return;
        }

        int nextChapter = completedChapter + 1;
        if (nextChapter > ChapterProgressionData.MAX_CHAPTER) {
            // Chapter 8 complete — final chapter! No next stage to grant.
            ServoCore.LOGGER.info("Team {} completed the FINAL chapter (Ch{})!", teamId, completedChapter);
            data.markStageGranted(teamId, completedChapter);
            broadcastFinalChapterComplete(server, teamId);
            return;
        }

        String nextStage = "servo_ch" + nextChapter;

        // Grant stage to all team members
        List<String> grantedTo = TeamStageGranter.grantStageToTeam(server, teamId, nextStage);

        data.markStageGranted(teamId, completedChapter);

        ServoCore.LOGGER.info("Team {} advanced to chapter {}! Stage '{}' granted to: {}",
                teamId, nextChapter, nextStage, grantedTo);

        // Broadcast to all online players
        broadcastChapterAdvance(server, teamId, completedChapter, nextChapter, grantedTo);
    }

    /**
     * Sends chat + title messages to the team when delivery is complete but boss is pending.
     */
    private static void notifyTeamDeliveryOnly(MinecraftServer server, UUID teamId, int chapter) {
        List<ServerPlayer> players = getOnlineTeamMembers(server, teamId);
        for (ServerPlayer player : players) {
            player.sendSystemMessage(Component.translatable(
                    "servo_core.progression.delivery_complete", chapter
            ).withStyle(ChatFormatting.GREEN));
            player.sendSystemMessage(Component.translatable(
                    "servo_core.progression.boss_pending", chapter
            ).withStyle(ChatFormatting.YELLOW));
        }
    }

    /**
     * Sends chat messages to the team when boss is killed but delivery is pending.
     */
    private static void notifyTeamBossOnly(MinecraftServer server, UUID teamId, int chapter) {
        List<ServerPlayer> players = getOnlineTeamMembers(server, teamId);
        for (ServerPlayer player : players) {
            player.sendSystemMessage(Component.translatable(
                    "servo_core.progression.boss_killed", chapter
            ).withStyle(ChatFormatting.GREEN));
            player.sendSystemMessage(Component.translatable(
                    "servo_core.progression.delivery_pending", chapter
            ).withStyle(ChatFormatting.YELLOW));
        }
    }

    /**
     * Broadcasts a chapter advancement to the entire server.
     */
    private static void broadcastChapterAdvance(MinecraftServer server, UUID teamId,
                                                 int completedChapter, int nextChapter,
                                                 List<String> playerNames) {
        String chapterName = getChapterName(nextChapter);
        Component broadcastMsg = Component.translatable(
                "servo_core.progression.chapter_advance",
                String.join(", ", playerNames),
                completedChapter,
                nextChapter,
                chapterName
        ).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD);

        // Send to all online players (server-wide announcement)
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(broadcastMsg);

            // Title screen for team members
            if (isTeamMember(server, teamId, player)) {
                sendChapterTitle(player, nextChapter, chapterName);
                // Play advancement sound
                player.playNotifySound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
                        SoundSource.MASTER, 1.0f, 1.0f);
            }
        }
    }

    /**
     * Broadcasts final chapter completion.
     */
    private static void broadcastFinalChapterComplete(MinecraftServer server, UUID teamId) {
        Component broadcastMsg = Component.translatable(
                "servo_core.progression.final_complete"
        ).withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(broadcastMsg);

            if (isTeamMember(server, teamId, player)) {
                sendFinalTitle(player);
                player.playNotifySound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
                        SoundSource.MASTER, 1.5f, 0.8f);
            }
        }
    }

    private static void sendChapterTitle(ServerPlayer player, int chapter, String chapterName) {
        player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 60, 20));
        player.connection.send(new ClientboundSetTitleTextPacket(
                Component.translatable("servo_core.progression.title", chapter)
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
        ));
        player.connection.send(new ClientboundSetSubtitleTextPacket(
                Component.literal(chapterName)
                        .withStyle(ChatFormatting.YELLOW)
        ));
    }

    private static void sendFinalTitle(ServerPlayer player) {
        player.connection.send(new ClientboundSetTitlesAnimationPacket(20, 100, 30));
        player.connection.send(new ClientboundSetTitleTextPacket(
                Component.translatable("servo_core.progression.final_title")
                        .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD)
        ));
        player.connection.send(new ClientboundSetSubtitleTextPacket(
                Component.translatable("servo_core.progression.final_subtitle")
                        .withStyle(ChatFormatting.WHITE)
        ));
    }

    private static String getChapterName(int chapter) {
        if (chapter < 1 || chapter > CHAPTER_NAMES.length) return "???";
        return CHAPTER_NAMES[chapter - 1];
    }

    /**
     * Gets all online members of a team. Delegates to FTB Teams if available.
     */
    private static List<ServerPlayer> getOnlineTeamMembers(MinecraftServer server, UUID teamId) {
        return resolveOnlineMembers(server, teamId);
    }

    private static List<ServerPlayer> resolveOnlineMembers(MinecraftServer server, UUID teamId) {
        if (ModList.get().isLoaded("ftbteams")) {
            try {
                return FTBTeamsMemberResolver.resolve(server, teamId);
            } catch (Exception e) {
                ServoCore.LOGGER.debug("Failed to resolve FTB Teams members", e);
            }
        }
        // Fallback: teamId is a player UUID
        java.util.List<ServerPlayer> result = new java.util.ArrayList<>();
        ServerPlayer player = server.getPlayerList().getPlayer(teamId);
        if (player != null) result.add(player);
        return result;
    }

    private static boolean isTeamMember(MinecraftServer server, UUID teamId, ServerPlayer player) {
        List<ServerPlayer> members = resolveOnlineMembers(server, teamId);
        return members.stream().anyMatch(m -> m.getUUID().equals(player.getUUID()));
    }

    /**
     * Isolated FTB Teams class — only loaded when ftbteams is present.
     */
    private static class FTBTeamsMemberResolver {
        static List<ServerPlayer> resolve(MinecraftServer server, UUID teamId) {
            List<ServerPlayer> online = new java.util.ArrayList<>();
            var api = dev.ftb.mods.ftbteams.api.FTBTeamsAPI.api().getManager();
            var teamOpt = api.getTeamByID(teamId);
            if (teamOpt.isEmpty()) {
                ServerPlayer player = server.getPlayerList().getPlayer(teamId);
                if (player != null) {
                    teamOpt = api.getTeamForPlayer(player);
                }
            }
            if (teamOpt.isPresent()) {
                for (UUID memberId : teamOpt.get().getMembers()) {
                    ServerPlayer member = server.getPlayerList().getPlayer(memberId);
                    if (member != null) online.add(member);
                }
            } else {
                ServerPlayer player = server.getPlayerList().getPlayer(teamId);
                if (player != null) online.add(player);
            }
            return online;
        }
    }
}
