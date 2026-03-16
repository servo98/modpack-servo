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
 * Chapter 5 Boss: El Arquitecto.
 * A 4-block holographic humanoid with a blue-cyan digital grid, visual glitching.
 *
 * <p>Phase 1 (100%-60%): Rayo Digital (hitscan laser), Muro de Datos (creates destructible walls),
 * Cortocircuito (AoE near terminals + paralysis). Has Firewall: only takes 10% damage unless players
 * hack 3 of 6 terminals (4s channel each), which drops Firewall for 12s DPS window.</p>
 *
 * <p>Phase 2 (60%-30%): Needs 4 terminals. Builds Digital Turrets (3 dmg/2s, 30 HP).
 * Rayo Digital bounces once off data walls.</p>
 *
 * <p>Phase 3 (30%-0%): Firewall permanently down. Teleports every 8s. Splits into 3 copies
 * (1 real with brighter particles, 2 fakes with 1 HP but real damage). Gains Reinicio del Sistema
 * (freezes ALL players 1.5s, arena-wide, 20s CD).</p>
 *
 * <p>Stats: 3400 HP | 16.6 Dmg | 0.24 Speed | 10 Armor | 0.8 KB Res | 3 Phases</p>
 */
public class ElArquitectoBoss extends AbstractDungeonBoss {

    // TODO: Firewall mechanic - boss takes only 10% damage until Firewall is down
    //       - 6 Terminals in arena walls (interactable blocks)
    //       - Hack 3 terminals (4s channel each, don't move) to drop Firewall 12s
    //       - Boss attacks active terminals to interrupt
    //       - Solo mode: only 2 terminals needed, boss attacks terminals slower
    // TODO: Rayo Digital (10.0 hitscan, 15 blocks, 4s CD, 1.5s telegraph with cyan line)
    // TODO: Muro de Datos (creates 3x3 destructible wall, 20 HP/block, 8s CD)
    // TODO: Cortocircuito (8.0 + paralysis 2s, radius 4 centered on Terminal, 10s CD)
    // TODO: Phase 2: Digital Turrets (3 dmg/2s, 30 HP, 2 per cycle)
    // TODO: Phase 2: Rayo bounces once off data walls
    // TODO: Phase 3: Split into 3 copies (1 real + 2 fakes with 1 HP, real has brighter particles)
    // TODO: Phase 3: Teleport every 8s (glitch animation)
    // TODO: Phase 3: Reinicio del Sistema (freeze ALL 1.5s, 20s CD, unavoidable)

    public ElArquitectoBoss(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level, 5, new float[]{0.60f, 0.30f}, 16.6, BossEvent.BossBarColor.BLUE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBossAttributes(3400, 16.6, 0.24, 10, 0.8);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BossPhaseTransitionGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        // Melee: when players get close
        this.goalSelector.addGoal(2, new BossMeleeAttackGoal(this, 1.0, 3.0, 60));
        // Ranged: Rayo Digital hitscan
        this.goalSelector.addGoal(3, new BossRangedAttackGoal(this, 5.0, 15.0, 80, 0.6f));

        // Turrets in phase 2+ (placeholder: SHULKER for turret behavior)
        // TODO: Replace with custom Digital Turret entity
        this.goalSelector.addGoal(4, new BossSpawnMinionsGoal(this,
                () -> EntityType.SHULKER, 2, 4, 400, 2));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 20.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (newPhase == 2) {
            // TODO: Increase terminals needed from 3 to 4
            // TODO: Start spawning Digital Turrets
        } else if (newPhase == 3) {
            // TODO: Firewall permanently deactivated (full damage always)
            // TODO: Split into 3 copies
            // TODO: Begin teleportation cycle (every 8s)
            // TODO: Glitch massive visual effect, room darkens
        }
    }
}
