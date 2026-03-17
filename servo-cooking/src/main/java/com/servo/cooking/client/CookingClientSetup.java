package com.servo.cooking.client;

import com.servo.cooking.CookingRegistry;
import com.servo.cooking.ServoCooking;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = ServoCooking.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CookingClientSetup {

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(CookingRegistry.PREP_STATION_MENU.get(), PrepStationScreen::new);
    }
}
