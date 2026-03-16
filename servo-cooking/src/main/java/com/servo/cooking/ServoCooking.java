package com.servo.cooking;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ServoCooking.MOD_ID)
public class ServoCooking {
    public static final String MOD_ID = "servo_cooking";
    public static final Logger LOGGER = LoggerFactory.getLogger("ServoCooking");

    public ServoCooking(IEventBus modEventBus) {
        LOGGER.info("Servo Cooking initializing...");
        CookingRegistry.register(modEventBus);
        LOGGER.info("Servo Cooking initialized!");
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
