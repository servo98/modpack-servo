package com.servo.delivery.client;

import com.servo.delivery.DeliveryRegistry;
import com.servo.delivery.ServoDelivery;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = ServoDelivery.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DeliveryClientSetup {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(DeliveryRegistry.TERMINAL_BE.get(),
                DeliveryTerminalRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(DeliveryRegistry.TERMINAL_MENU.get(), DeliveryTerminalScreen::new);
    }
}
