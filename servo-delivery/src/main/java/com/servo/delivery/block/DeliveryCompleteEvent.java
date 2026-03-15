package com.servo.delivery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

import java.util.UUID;

/**
 * Fired on the NeoForge event bus when a chapter delivery is completed.
 * Other mods (servo_core) can listen to this to grant stages, trigger quests, etc.
 *
 * Usage in servo_core:
 * <pre>
 * NeoForge.EVENT_BUS.addListener((DeliveryCompleteEvent e) -> {
 *     // Grant ProgressiveStages stage to all team members
 *     grantStage("servo_delivery_ch" + e.getChapter(), e.getLevel(), e.getTeamId());
 * });
 * </pre>
 */
public class DeliveryCompleteEvent extends Event {

    private final ServerLevel level;
    private final BlockPos terminalPos;
    private final int chapter;
    private final UUID teamId;

    public DeliveryCompleteEvent(ServerLevel level, BlockPos terminalPos, int chapter, UUID teamId) {
        this.level = level;
        this.terminalPos = terminalPos;
        this.chapter = chapter;
        this.teamId = teamId;
    }

    public ServerLevel getLevel() { return level; }
    public BlockPos getTerminalPos() { return terminalPos; }
    public int getChapter() { return chapter; }
    public UUID getTeamId() { return teamId; }

    /**
     * Fires the event on the NeoForge bus.
     */
    public static void fire(ServerLevel level, BlockPos pos, int chapter, UUID teamId) {
        NeoForge.EVENT_BUS.post(new DeliveryCompleteEvent(level, pos, chapter, teamId));
    }
}
