package com.servo.core;

import com.servo.core.gacha.GachaPityAttachment;
import com.servo.core.progression.ChapterProgressionHandler;
import com.servo.core.stage.CuriosStageEnforcer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ServoCore.MOD_ID)
public class ServoCore {
    public static final String MOD_ID = "servo_core";
    public static final Logger LOGGER = LoggerFactory.getLogger("ServoCore");

    public ServoCore(IEventBus modEventBus) {
        LOGGER.info("Servo Core initializing...");
        ModRegistry.register(modEventBus);
        GachaPityAttachment.register(modEventBus);

        // Initialize Curios stage enforcer (supplements ProgressiveStages for Curios slots)
        if (ModList.get().isLoaded("curios") && ModList.get().isLoaded("progressivestages")) {
            CuriosStageEnforcer.init();
        }

        // Initialize chapter progression handler (listens to DeliveryCompleteEvent from servo_delivery)
        ChapterProgressionHandler.init();

        LOGGER.info("Servo Core initialized!");
    }
}
