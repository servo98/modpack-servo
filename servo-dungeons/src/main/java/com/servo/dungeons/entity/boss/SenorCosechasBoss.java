package com.servo.dungeons.entity.boss;

import com.servo.dungeons.entity.boss.goal.BossMeleeAttackGoal;
import com.servo.dungeons.entity.boss.goal.BossPhaseTransitionGoal;
import com.servo.dungeons.entity.boss.goal.BossRangedAttackGoal;
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
 * Chapter 6 Boss: Senor de las Cosechas.
 * A 6-block corrupted treant with withered crops, dead wheat crown, bone-scythe root arms.
 *
 * <p>Arena: Dead crop field (30x30) with 16 farmland plots (4x4 each) in a grid, dead central tree.</p>
 *
 * <p>Phase 1 (100%-60%): Segadora (scythe sweep), Raiz Subterranea (root erupts under player),
 * Esporas de Cosecha (AoE + Hunger III). Every 10s absorbs from plots: corrupted crops heal 2% HP,
 * player-planted real crops damage boss 5% HP. Channeling is a DPS window.</p>
 *
 * <p>Phase 2 (60%-30%): Corrupted crops regrow every 7s. Gains Ola de Raices from central tree
 * (arena-wide except plots; stand on real-crop plot = safe).</p>
 *
 * <p>Phase 3 (30%-0%): Merges with central tree, becomes 8 blocks tall, immobile.
 * Continuous absorption. Gains Raiz Tsunami (wave patterns) and constant Esporas Finales
 * (2.0/s to all, cancelled by planting 8+ real crops).</p>
 *
 * <p>Stats: 5000 HP | 22.4 Dmg | 0.26 Speed | 12 Armor | 0.85 KB Res | 3 Phases</p>
 */
public class SenorCosechasBoss extends AbstractDungeonBoss {

    // TODO: Crop plot mechanic
    //       - 16 plots with corrupted crops
    //       - Every 10s: boss channels 3s (DPS window while channeling)
    //         - Corrupted crop plot: heals boss 2% HP (max 32% if untouched)
    //         - Empty plot: nothing
    //         - Real crop plot (player-planted): damages boss 5% max HP
    //       - Players must harvest corrupted and plant real seeds
    // TODO: Segadora (15.0 sweep, 5 blocks frontal 120 degrees, 5s CD, 1.5s telegraph)
    // TODO: Raiz Subterranea (10.0 + root 2s, erupts under player 20 blocks range, 8s CD, 2s earth telegraph)
    // TODO: Esporas de Cosecha (3.0/s zone 6s + Hunger III, radius 6 AoE, 15s CD)
    // TODO: Phase 2: Corrupted crops regrow every 7s
    // TODO: Phase 2: Ola de Raices (12.0 from central tree, hits entire arena EXCEPT plots, 15s CD)
    //       - Standing on plot with real crop = safe; corrupted crop plot = 2/s damage
    // TODO: Phase 3: Merge with central tree (8 blocks, immobile)
    //       - Continuous absorption (not by cycle)
    //       - Real crop plots do 1% HP/3s passively
    //       - Raiz Tsunami (8.0 wave patterns across arena, 8s CD)
    //       - Esporas Finales (2.0/s to all, constant, cancelled by planting 8+ real crops)

    public SenorCosechasBoss(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level, 6, new float[]{0.60f, 0.30f}, 22.4, BossEvent.BossBarColor.YELLOW);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBossAttributes(5000, 22.4, 0.26, 12, 0.85);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BossPhaseTransitionGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        // Melee: Segadora scythe sweep
        this.goalSelector.addGoal(2, new BossMeleeAttackGoal(this, 0.9, 5.0, 100));
        // Ranged: Raiz Subterranea
        this.goalSelector.addGoal(3, new BossRangedAttackGoal(this, 5.0, 20.0, 160, 0.45f));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 20.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (newPhase == 2) {
            // TODO: Corrupted crops regrow every 7s
            // TODO: Enable Ola de Raices attack from central tree
        } else if (newPhase == 3) {
            // TODO: Merge with central tree animation
            // TODO: Boss becomes 8 blocks, immobile
            // TODO: Switch to continuous absorption
            // TODO: Enable Esporas Finales (constant AoE)
            // TODO: Remove movement goals
        }
    }
}
