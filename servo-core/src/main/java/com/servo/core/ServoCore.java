package com.servo.core;

import net.neoforged.bus.api.IEventBus;
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
        LOGGER.info("Servo Core initialized!");
    }
}
