package com.servo.dungeons.client.model;

import com.servo.dungeons.ServoDungeons;
import com.servo.dungeons.entity.boss.AbstractDungeonBoss;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/**
 * Generic GeckoLib model that loads per-boss geo/texture/animation files.
 * Each boss can provide its own model file, or falls back to the shared placeholder.
 *
 * <p>File resolution pattern:</p>
 * <ul>
 *   <li>Geo: {@code assets/servo_dungeons/geo/boss_{registryName}.geo.json} (fallback: boss_placeholder.geo.json)</li>
 *   <li>Texture: {@code assets/servo_dungeons/textures/entity/boss_{registryName}.png} (fallback: boss_placeholder.png)</li>
 *   <li>Animation: {@code assets/servo_dungeons/animations/boss_{registryName}.animation.json} (fallback: boss_placeholder.animation.json)</li>
 * </ul>
 */
public class DungeonBossModel extends GeoModel<AbstractDungeonBoss> {

    @Override
    public ResourceLocation getModelResource(AbstractDungeonBoss boss) {
        // All bosses use the placeholder model until real models are created
        return ResourceLocation.fromNamespaceAndPath(ServoDungeons.MOD_ID, "geo/boss_placeholder.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AbstractDungeonBoss boss) {
        // All bosses use the placeholder texture until real textures are created
        return ResourceLocation.fromNamespaceAndPath(ServoDungeons.MOD_ID, "textures/entity/boss_placeholder.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractDungeonBoss boss) {
        // All bosses use the placeholder animation until real animations are created
        return ResourceLocation.fromNamespaceAndPath(ServoDungeons.MOD_ID, "animations/boss_placeholder.animation.json");
    }
}
