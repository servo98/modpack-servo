package com.servo.mart;

import com.servo.mart.data.CatalogDataLoader;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ServoMart.MOD_ID)
public class ServoMart {
    public static final String MOD_ID = "servo_mart";
    public static final Logger LOGGER = LoggerFactory.getLogger("ServoMart");

    public ServoMart(IEventBus modEventBus) {
        LOGGER.info("PepeMart initializing...");
        MartRegistry.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::onAddReloadListeners);
        LOGGER.info("PepeMart initialized!");
    }

    private void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new CatalogDataLoader());
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
