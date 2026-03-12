package com.servo.packaging;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ServoPackaging.MOD_ID)
public class ServoPackaging {
    public static final String MOD_ID = "servo_packaging";
    public static final Logger LOGGER = LoggerFactory.getLogger("ServoPackaging");

    public ServoPackaging(IEventBus modEventBus) {
        LOGGER.info("Servo Packaging initializing...");
        PackagingRegistry.register(modEventBus);
        LOGGER.info("Servo Packaging initialized!");
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
