package com.servo.dungeons.client.renderer;

import com.servo.dungeons.client.model.DungeonBossModel;
import com.servo.dungeons.entity.boss.AbstractDungeonBoss;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * Generic GeckoLib entity renderer for all dungeon bosses.
 * Uses {@link DungeonBossModel} to resolve per-boss model/texture/animation files.
 */
public class DungeonBossRenderer extends GeoEntityRenderer<AbstractDungeonBoss> {

    public DungeonBossRenderer(EntityRendererProvider.Context context) {
        super(context, new DungeonBossModel());
    }
}
