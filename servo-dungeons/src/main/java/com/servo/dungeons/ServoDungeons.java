package com.servo.dungeons;

import com.servo.dungeons.dungeon.DungeonManager;
import com.servo.dungeons.dungeon.MobDeathListener;
import com.servo.dungeons.dungeon.RoomActivationListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ServoDungeons.MOD_ID)
public class ServoDungeons {
    public static final String MOD_ID = "servo_dungeons";
    public static final Logger LOGGER = LoggerFactory.getLogger("ServoDungeons");

    public ServoDungeons(IEventBus modEventBus) {
        LOGGER.info("Servo Dungeons initializing...");
        DungeonRegistry.register(modEventBus);

        // Register DungeonManager lifecycle on the NeoForge event bus
        NeoForge.EVENT_BUS.addListener((ServerStartedEvent event) -> {
            DungeonManager.init(event.getServer());
        });
        NeoForge.EVENT_BUS.addListener((ServerStoppedEvent event) -> {
            DungeonManager.clear();
        });

        // Room activation: detect players entering LOCKED rooms, spawn mobs
        NeoForge.EVENT_BUS.addListener((LevelTickEvent.Post event) -> {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                RoomActivationListener.onServerTick(serverLevel);
            }
        });

        // Mob death: track kills and mark rooms as CLEARED
        NeoForge.EVENT_BUS.addListener(MobDeathListener::onMobDeath);

        LOGGER.info("Servo Dungeons initialized!");
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
