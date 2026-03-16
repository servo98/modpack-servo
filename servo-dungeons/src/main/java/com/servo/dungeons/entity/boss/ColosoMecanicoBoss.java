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
 * Chapter 3 Boss: Coloso Mecanico.
 * A 5-block tall robot with gears, pistons, oxidized copper, a red eye, and steam vents.
 *
 * <p>Phase 1 (100%-65%): Punetazo (punch + knockback), Piston Slam (frontal AoE),
 * Rayo de Vapor (steam ray line). Takes only 25% damage normally; hit active orange panels for 3x damage.
 * 4 Sabotage Nodes in arena corners (single-use, activate all panels for 6s).</p>
 *
 * <p>Phase 2 (65%-30%): Arm breaks into cannon. Loses Punetazo, gains Canon de Chatarra.
 * Panels rotate faster (every 5s). Spawns Repair Drones every 20s (heal boss 3% if not killed in 5s).</p>
 *
 * <p>Phase 3 (30%-0%): Overload. All panels permanently active (3x dmg always). Speed 0.18->0.30.
 * +40% damage. Gains Descarga Electrica (arena-wide AoE, hide behind pillars).</p>
 *
 * <p>Stats: 1600 HP | 9.1 Dmg | 0.18 Speed | 14 Armor | 1.0 KB Res | 3 Phases</p>
 */
public class ColosoMecanicoBoss extends AbstractDungeonBoss {

    // TODO: Panel mechanic - 4 panels (back, chest, left arm, right arm)
    //       - Normal hits: 25% damage (high armor)
    //       - Active panel hit: 3x damage
    //       - Panel rotates every 8s (phase 1) / 5s (phase 2) / always active (phase 3)
    //       - Visual: active panel glows orange
    // TODO: 4 Sabotage Nodes in arena corners (interactable, 3s channel, single-use, all panels active 6s)
    // TODO: TNT mechanic: TNT near active panel = 40 direct damage (ignore armor), max 2 effective per fight
    // TODO: Piston Slam (12.0 AoE + Slowness II, radius 5 frontal, 10s CD, 2s charge telegraph)
    // TODO: Rayo de Vapor (4.0/s line 3s, 12 blocks, 15s CD, red eye telegraph 2s)
    // TODO: Phase 2: Canon de Chatarra (7.0 + small explosion, 20 block slow projectile, 5s CD)
    // TODO: Phase 2: Repair Drones (30 HP, heal boss 3% if alive 5s near boss, max 4)
    // TODO: Phase 3: Descarga Electrica (6.0 AoE entire arena, 12s CD, 3s charge, hide behind pillars)
    // TODO: Phase 3: Speed increase 0.18 -> 0.30, damage +40%

    public ColosoMecanicoBoss(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level, 3, new float[]{0.65f, 0.30f}, 9.1, BossEvent.BossBarColor.YELLOW);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBossAttributes(1600, 9.1, 0.18, 14, 1.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BossPhaseTransitionGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        // Melee: Punetazo
        this.goalSelector.addGoal(2, new BossMeleeAttackGoal(this, 0.8, 4.0, 60));
        // Ranged: Rayo de Vapor / Canon placeholder
        this.goalSelector.addGoal(3, new BossRangedAttackGoal(this, 5.0, 12.0, 200, 0.8f));

        // Phase 2+ drones (placeholder: VEX as repair drone stand-in)
        // TODO: Replace with custom Repair Drone entity
        this.goalSelector.addGoal(4, new BossSpawnMinionsGoal(this,
                () -> EntityType.VEX, 2, 4, 400, 2));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (newPhase == 2) {
            // TODO: Break arm animation, reconfigure to cannon
            // TODO: Armor decreases from 14 to 10
            // TODO: Panel rotation speed increases to 5s
        } else if (newPhase == 3) {
            // TODO: All panels permanently active (3x damage always)
            // TODO: Speed increase to 0.30
            // TODO: Damage +40%
            // TODO: Alarm sound, panels open animation
        }
    }
}
