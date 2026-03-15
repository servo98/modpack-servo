package com.servo.create;

import com.servo.create.compat.DeployerFoldingHandler;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ServoCreate.MOD_ID)
public class ServoCreate {
    public static final String MOD_ID = "servo_create";
    public static final Logger LOGGER = LoggerFactory.getLogger("ServoCreate");

    public ServoCreate(IEventBus modEventBus) {
        LOGGER.info("Servo Create initializing...");
        CreateRegistry.register(modEventBus);

        // Register deployer folding handler on NeoForge event bus
        NeoForge.EVENT_BUS.register(DeployerFoldingHandler.class);

        LOGGER.info("Servo Create initialized!");
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
