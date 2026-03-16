package com.servo.dungeons.client;

import com.servo.dungeons.DungeonRegistry;
import com.servo.dungeons.ServoDungeons;
import com.servo.dungeons.client.renderer.DungeonBossRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Client-side event handler for registering entity renderers.
 * Automatically subscribed to the mod event bus on the client distribution.
 */
@EventBusSubscriber(modid = ServoDungeons.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DungeonClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Register the shared boss renderer for all 8 boss entity types
        event.registerEntityRenderer(DungeonRegistry.GUARDIAN_DEL_BOSQUE.get(), DungeonBossRenderer::new);
        event.registerEntityRenderer(DungeonRegistry.BESTIA_GLOTONA.get(), DungeonBossRenderer::new);
        event.registerEntityRenderer(DungeonRegistry.COLOSO_MECANICO.get(), DungeonBossRenderer::new);
        event.registerEntityRenderer(DungeonRegistry.LOCOMOTORA_FANTASMA.get(), DungeonBossRenderer::new);
        event.registerEntityRenderer(DungeonRegistry.EL_ARQUITECTO.get(), DungeonBossRenderer::new);
        event.registerEntityRenderer(DungeonRegistry.SENOR_COSECHAS.get(), DungeonBossRenderer::new);
        event.registerEntityRenderer(DungeonRegistry.NUCLEO_DEL_DUNGEON.get(), DungeonBossRenderer::new);
        event.registerEntityRenderer(DungeonRegistry.DEVORADOR_DES_MUNDOS.get(), DungeonBossRenderer::new);
    }
}
