package com.servo.core.progression;

import com.servo.delivery.block.DeliveryCompleteEvent;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Bridge class that imports DeliveryCompleteEvent from servo_delivery.
 * Isolated in its own class so it is ONLY loaded when servo_delivery is confirmed present.
 * This prevents ClassNotFoundException when servo_delivery is absent.
 *
 * <p>Do NOT reference this class from any code path that can execute without servo_delivery.
 */
final class DeliveryEventBridge {

    private DeliveryEventBridge() {}

    /**
     * Registers the DeliveryCompleteEvent listener on the NeoForge event bus.
     */
    static void register() {
        NeoForge.EVENT_BUS.addListener(DeliveryEventBridge::onDeliveryComplete);
    }

    private static void onDeliveryComplete(DeliveryCompleteEvent event) {
        ChapterProgressionHandler.onDeliveryComplete(
                event.getLevel().getServer(),
                event.getTeamId(),
                event.getChapter()
        );
    }
}
