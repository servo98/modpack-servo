package com.servo.delivery;

import com.servo.delivery.block.DeliveryPortBlock;
import com.servo.delivery.block.DeliveryTerminalBlockEntity;
import com.servo.delivery.block.SlaveBlockEntity;
import com.servo.delivery.data.DeliveryDataLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
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

        // Register IItemHandler capability on delivery ports for hopper/funnel compat
        modEventBus.addListener((RegisterCapabilitiesEvent event) -> {
            event.registerBlockEntity(
                    Capabilities.ItemHandler.BLOCK,
                    DeliveryRegistry.SLAVE_BE.get(),
                    (slave, direction) -> {
                        if (slave.getBlockState().getBlock() instanceof DeliveryPortBlock) {
                            DeliveryTerminalBlockEntity master = slave.getMaster();
                            if (master != null) {
                                return new PortItemHandler(master);
                            }
                        }
                        return null;
                    }
            );
        });

        LOGGER.info("Servo Delivery initialized!");
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
