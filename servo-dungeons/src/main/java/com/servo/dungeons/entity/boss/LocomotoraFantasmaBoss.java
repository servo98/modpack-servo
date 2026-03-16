package com.servo.dungeons.entity.boss;

import com.servo.dungeons.entity.boss.goal.BossMeleeAttackGoal;
import com.servo.dungeons.entity.boss.goal.BossPhaseTransitionGoal;
import com.servo.dungeons.entity.boss.goal.BossRangedAttackGoal;
import com.servo.dungeons.entity.boss.goal.BossSpawnMinionsGoal;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Chapter 4 Boss: Locomotora Fantasma.
 * A 15-block spectral train with a demonic face, ghost passengers, purple aura, floating.
 *
 * <p>Unique arena: Circular rail track (radius 20 blocks). Floating platforms around it.
 * The train circles continuously. Below the track: ghost fog (2 dmg/s), ladders to climb back.</p>
 *
 * <p>Phase 1 (100%-50%): Silbido Sonico (AoE knockback on train, crouch reduces 70%),
 * Vapor Fantasmal (steam cloud + Blindness behind chimney), Pasajeros Fantasma spawns.
 * Decouple wagons by hitting hooks (5 hits each) to reduce ghost spawns.</p>
 *
 * <p>Phase 2 (50%-0%): Train accelerates 1.5x. Track sections appear/disappear (5s cycle).
 * Ghosts explode on death (3 AoE radius 2). Gains Embate Frontal (15.0 dmg from train front).</p>
 *
 * <p>Stats: 2400 HP | 12.3 Dmg | 0.32 Speed | 8 Armor | 0.7 KB Res | 2 Phases</p>
 */
public class LocomotoraFantasmaBoss extends AbstractDungeonBoss {

    // TODO: Train movement on circular rail - custom movement logic, NOT standard pathfinding
    // TODO: Silbido Sonico (6.0 + knockback on train, 8s CD, 1.5s telegraph, crouch = 70% less KB)
    // TODO: Vapor Fantasmal (4.0/s cloud 3s + Blindness 2s, behind chimney, 10s CD)
    // TODO: Pasajeros Fantasma (spawn 3-4 ghosts every 15s from wagons, 10 HP each, apply Slowness)
    // TODO: Wagon decouple mechanic (5 hits on hook = detach, 3 wagons initially)
    // TODO: Phase 2: Train 1.5x speed, track sections disappear (5s off/5s on)
    // TODO: Phase 2: Ghosts explode on death (3 AoE radius 2)
    // TODO: Phase 2: Embate Frontal (15.0 from train front, 8 blocks, 12s CD, 2s red glow telegraph)
    // TODO: Ghost fog below track (2 dmg/s if fallen, ladders to climb)
    // TODO: Floating platforms (8 around track) for ranged combat

    public LocomotoraFantasmaBoss(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level, 4, new float[]{0.5f}, 12.3, BossEvent.BossBarColor.PURPLE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBossAttributes(2400, 12.3, 0.32, 8, 0.7);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BossPhaseTransitionGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        // Melee: direct contact damage
        this.goalSelector.addGoal(2, new BossMeleeAttackGoal(this, 1.2, 4.0, 60));
        // Ranged: Silbido / Vapor
        this.goalSelector.addGoal(3, new BossRangedAttackGoal(this, 3.0, 15.0, 160, 0.5f));

        // Ghost passenger spawning
        // TODO: Replace with custom Ghost Passenger entity
        this.goalSelector.addGoal(4, new BossSpawnMinionsGoal(this,
                () -> EntityType.VEX, 3, 6, 300, 1));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 20.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (newPhase == 2) {
            // TODO: Train accelerates 1.5x
            // TODO: 1 wagon explodes
            // TODO: Track sections start appearing/disappearing cycle
        }
    }
}
