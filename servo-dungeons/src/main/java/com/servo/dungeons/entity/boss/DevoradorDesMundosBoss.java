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
 * Chapter 8 Boss: Devorador de Mundos (World Devourer) - FINAL BOSS.
 * Starts as a 3-block dark sphere, grows per phase into a 10+ block leviathan with
 * tentacles, multiple eyes, and energy cracks. Cosmic entity in void dimension.
 *
 * <p>Phase 1 (100%-75%): COMBAT PURE. Azote de Tentaculo (20.0 sweep), Rayo Cosmico
 * (25.0 hitscan), Nova Oscura (15.0 AoE + Wither II), Tentaculos Rastreadores (8.0 each, 4 tracking).</p>
 *
 * <p>Phase 2 (75%-50%): CREATE. Arena transforms into chaotic factory. Boss invulnerable to direct
 * attacks. Activate 3 machines in correct order (red/blue/green). Each sequence = Deployer hits boss 10% HP.
 * 3 sequences complete the phase. Gear Throws, Piston Slams, Toxic Steam.</p>
 *
 * <p>Phase 3 (50%-25%): COOKING. Factory disappears. Cosmic Cooking Pot appears. Boss regenerates
 * 1% HP/5s. Cook "Cosmic Antidote" from 3 tentacle ingredients. Each antidote: 5% HP + stun 8s DPS window.</p>
 *
 * <p>Phase 4 (25%-0%): ALL THREE PILLARS. F1 attacks + F2 machines + F3 cooking (regen 2% HP/5s).
 * Platforms disappear every 30s. 5-min enrage timer (999 damage).</p>
 *
 * <p>Stats: 10400 HP | 40.9 Dmg | 0.30 Speed | 18 Armor | 1.0 KB Res | 4 Phases</p>
 */
public class DevoradorDesMundosBoss extends AbstractDungeonBoss {

    // TODO: Phase 1 - COMBAT:
    //       - Azote de Tentaculo (20.0 sweep, 6 blocks frontal, 4s CD, 1.2s telegraph)
    //       - Rayo Cosmico (25.0 hitscan, 25 blocks, 8s CD, 2.5s eye charge telegraph)
    //       - Nova Oscura (15.0 AoE + Wither II 3s, radius 8, 15s CD, 2s contraction)
    //       - Tentaculos Rastreadores (8.0 each, 4 tentacles track for 5s, 20s CD, 15 HP each cuttable)
    // TODO: Phase 2 - CREATE:
    //       - Arena transforms into factory
    //       - Invulnerable to direct attacks
    //       - 3 machines, activate in correct color order (red -> blue -> green, changes each time)
    //       - Each correct sequence: Deployer hits boss for 10% HP
    //       - 3 sequences complete the phase
    //       - Engranaje Lanzado (12.0 projectile, 20 blocks, 3s CD)
    //       - Piston Slam (18.0 line, 15 blocks, 8s CD)
    //       - Vapor Toxico (5.0/s zone 4s, 6x6, 12s CD)
    // TODO: Phase 3 - COOKING:
    //       - Factory disappears, Cosmic Cooking Pot in center
    //       - Boss regenerates 1% HP/5s
    //       - Cut Tentaculos de Ingrediente (0 dmg, 30 HP, 3 every 15s, colored)
    //       - Cook "Antidoto Cosmico" from 3 ingredients -> 5% HP damage + stun 8s
    //       - Absorcion Vital (drains HP, 15 blocks channel 5s, 20s CD)
    //       - Onda de Hambre (10.0 + Hunger III 5s, radius 10, 12s CD)
    // TODO: Phase 4 - ALL:
    //       - All 3 mechanics active simultaneously
    //       - Regen increases to 2% HP/5s
    //       - Platforms disappear every 30s
    //       - 5-min enrage timer -> 999 damage attack
    // TODO: Size growth per phase (sphere -> medium -> organic -> leviathan)

    public DevoradorDesMundosBoss(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level, 8, new float[]{0.75f, 0.50f, 0.25f}, 40.9, BossEvent.BossBarColor.PURPLE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBossAttributes(10400, 40.9, 0.30, 18, 1.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BossPhaseTransitionGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        // Melee: Azote de Tentaculo
        this.goalSelector.addGoal(2, new BossMeleeAttackGoal(this, 1.0, 6.0, 80));
        // Ranged: Rayo Cosmico / Nova Oscura
        this.goalSelector.addGoal(3, new BossRangedAttackGoal(this, 6.0, 25.0, 160, 0.6f));

        // Tentaculos Rastreadores (tracking tentacles)
        // TODO: Replace with custom Tracking Tentacle entity (8.0 dmg, 15 HP, tracks 5s)
        this.goalSelector.addGoal(4, new BossSpawnMinionsGoal(this,
                () -> EntityType.VEX, 4, 8, 400, 1));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 30.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        switch (newPhase) {
            case 2 -> {
                // TODO: Transform arena into factory
                // TODO: Boss becomes invulnerable to direct attacks
                // TODO: Spawn 3 color machines
                // TODO: Growth animation (sphere -> medium)
            }
            case 3 -> {
                // TODO: Factory disappears
                // TODO: Spawn Cosmic Cooking Pot
                // TODO: Start HP regeneration (1% per 5s)
                // TODO: Growth animation (medium -> organic)
            }
            case 4 -> {
                // TODO: All 3 mechanics active
                // TODO: Increase regen to 2% per 5s
                // TODO: Start platform destruction timer (every 30s)
                // TODO: Start 5-min enrage timer
                // TODO: Growth animation (organic -> leviathan)
            }
        }
    }
}
