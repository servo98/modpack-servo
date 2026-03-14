package com.servo.delivery;

import com.servo.delivery.data.DeliveryDataLoader;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ServoDelivery.MOD_ID)
public class ServoDelivery {
    public static final String MOD_ID = "servo_delivery";
    public static final Logger LOGGER = LoggerFactory.getLogger("ServoDelivery");

    public ServoDelivery(IEventBus modEventBus) {
        LOGGER.info("Servo Delivery initializing...");
        DeliveryRegistry.register(modEventBus);

        // Register data pack loader for delivery definitions
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener(new DeliveryDataLoader());
        });

        LOGGER.info("Servo Delivery initialized!");
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
