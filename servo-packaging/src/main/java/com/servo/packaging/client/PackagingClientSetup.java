package com.servo.packaging.client;

import com.servo.packaging.PackagingRegistry;
import com.servo.packaging.ServoPackaging;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = ServoPackaging.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PackagingClientSetup {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(PackagingRegistry.OPEN_BOX_BE.get(),
                OpenBoxBER::new);
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(PackagingRegistry.PACKING_STATION_MENU.get(), PackingStationScreen::new);
    }

    @SubscribeEvent
    public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
        event.register(PackagingRegistry.SHIPPING_BOX_ITEM.get(), new ShippingBoxDecorator());
    }
}
