package com.servo.dungeons.entity.boss;

import com.servo.dungeons.entity.boss.goal.BossPhaseTransitionGoal;
import com.servo.dungeons.entity.boss.goal.BossRangedAttackGoal;
import com.servo.dungeons.entity.boss.goal.BossSpawnMinionsGoal;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Chapter 7 Boss: Nucleo del Dungeon.
 * A 4-block living crystal that floats, with reflective surface, orbiting fragments, light pulses.
 *
 * <p>Arena: Hexagonal room (radius 18). Obsidian/crystal floor. 6 crystal pillars at vertices.
 * Void below (fall = death). Destroying a pillar deals 5% max HP but reduces safe floor.</p>
 *
 * <p>Phase 1 (100%-75%): Rayo Prismatico (hitscan laser, break LOS with pillar),
 * Lluvia de Shards (5 shards in 8x8 area), Pulso Cristalino (radius 6 AoE). Full arena.</p>
 *
 * <p>Phase 2 (75%-50%): 3 floor sections disappear. Spawns 2 Espejos (mirrors reflect attacks
 * from front, attack from behind, 40 HP, respawn every 20s). Shard rain: 8 shards.</p>
 *
 * <p>Phase 3 (50%-25%): Arena halved. Pillars emit pulses (3 dmg/8s nearby).
 * Nucleo cycles invulnerability (2s invuln, 4s vulnerable). Gains Secuencia Cristalina.</p>
 *
 * <p>Phase 4 (25%-0%): Only central platform. Full damage always. Every 15s destroys a section.
 * Gains Implosion (20.0 + pull). Enrage timer ~3 min.</p>
 *
 * <p>Stats: 7200 HP | 30.3 Dmg | 0.20 Speed | 16 Armor | 1.0 KB Res | 4 Phases</p>
 */
public class NucleoDelDungeonBoss extends AbstractDungeonBoss {

    // TODO: Floating movement (no standard pathfinding, custom hover + teleport behavior)
    // TODO: 6 Crystal pillars (destructible, 5% max HP direct damage, reduces safe floor)
    // TODO: Rayo Prismatico (15.0 hitscan, 20 blocks, 5s CD, 2s charge, break LOS with pillar)
    // TODO: Lluvia de Shards (8.0/shard, 5 shards in 8x8, 10s CD, 2s shadow telegraph)
    // TODO: Pulso Cristalino (6.0 AoE radius 6, 12s CD, 1.5s white pulse telegraph)
    // TODO: Phase 2: 3 floor sections disappear, 2 Espejos (mirrors) - reflect front attacks, 40 HP
    //       - Shard rain increases to 8 shards
    //       - Rayo Prismatico bounces off mirrors
    // TODO: Phase 3: Arena halved, pillars emit 3 dmg/8s pulses nearby
    //       - Invulnerability cycle (2s invuln, 4s vulnerable, visual indicator)
    //       - Secuencia Cristalina (10.0/wave x3, crystal lines on floor, 15s CD, 1.5s telegraph)
    // TODO: Phase 4: Central platform only, full damage always
    //       - Every 15s destroys a platform section
    //       - Implosion (20.0 + pull radius 10, 20s CD, 3s contraction telegraph)
    //       - Enrage timer ~3 min (escalating damage after)

    public NucleoDelDungeonBoss(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level, 7, new float[]{0.75f, 0.50f, 0.25f}, 30.3, BossEvent.BossBarColor.WHITE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBossAttributes(7200, 30.3, 0.20, 16, 1.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BossPhaseTransitionGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        // Primarily ranged boss (floats, doesn't melee normally)
        this.goalSelector.addGoal(2, new BossRangedAttackGoal(this, 3.0, 20.0, 100, 0.5f));
        // Secondary ranged (Pulso Cristalino - shorter range)
        this.goalSelector.addGoal(3, new BossRangedAttackGoal(this, 0.0, 6.0, 240, 0.2f));

        // Espejos (mirror copies, phase 2+)
        // TODO: Replace with custom Mirror entity (reflects front attacks, 40 HP, attack from behind)
        this.goalSelector.addGoal(4, new BossSpawnMinionsGoal(this,
                () -> EntityType.VEX, 2, 4, 400, 2));

        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 24.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        switch (newPhase) {
            case 2 -> {
                // TODO: Remove 3 floor sections
                // TODO: Spawn initial 2 Espejos
                // TODO: Increase shard rain to 8
            }
            case 3 -> {
                // TODO: Halve remaining arena
                // TODO: Activate pillar damage pulses
                // TODO: Enable invulnerability cycling
                // TODO: Enable Secuencia Cristalina attack
            }
            case 4 -> {
                // TODO: Collapse to central platform only
                // TODO: Boss descends to ground level (melee accessible)
                // TODO: Disable invulnerability cycling (full damage always)
                // TODO: Start platform destruction timer (every 15s)
                // TODO: Enable Implosion attack
                // TODO: Start enrage timer (~3 min)
            }
        }
    }
}
