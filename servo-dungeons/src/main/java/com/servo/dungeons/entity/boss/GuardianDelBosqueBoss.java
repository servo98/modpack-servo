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
 * Chapter 1 Boss: Guardian del Bosque.
 * A 3-block tall humanoid made of roots, moss, and bark with glowing green eyes.
 *
 * <p>Phase 1 (100%-50%): Basic melee (Zarpazo), ranged root lash (Latigazo de Raiz),
 * AoE poison spore (Espora Toxica).</p>
 *
 * <p>Phase 2 (50%-0%): Speed increase, spawns Retonos (saplings) that must be killed
 * near the boss to cancel its root shield defense. Enhanced melee and root eruption AoE.</p>
 *
 * <p>Stats: 800 HP | 5.0 Dmg | 0.28 Speed | 4 Armor | 0.6 KB Res | 2 Phases</p>
 */
public class GuardianDelBosqueBoss extends AbstractDungeonBoss {

    // TODO: Implement Retonos (sapling minions) as custom entity - 20 HP, 3.0 dmg, 0.35 speed
    // TODO: Root shield defense (50% dmg reduction every 20s for 5s) - cancelled by killing Retono near boss
    // TODO: Espora Toxica AoE (2.0/s Poison for 4s in radius 4) with 1.5s telegraph
    // TODO: Erupcion de Raices (phase 2) - 6.0 dmg + vertical knockback, targeted on random player
    // TODO: Speed boost in phase 2 (0.28 -> 0.34)

    public GuardianDelBosqueBoss(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level, 1, new float[]{0.5f}, 5.0, BossEvent.BossBarColor.GREEN);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBossAttributes(800, 5.0, 0.28, 4, 0.6);
    }

    @Override
    protected void registerGoals() {
        // Priority 0: Phase transition (highest)
        this.goalSelector.addGoal(0, new BossPhaseTransitionGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        // Attacks
        this.goalSelector.addGoal(2, new BossMeleeAttackGoal(this, 1.0, 3.0, 40));
        this.goalSelector.addGoal(3, new BossRangedAttackGoal(this, 4.0, 8.0, 120, 0.8f));

        // Minion spawning (phase 2 only) - uses Zombies as placeholder for Retonos
        // TODO: Replace EntityType.ZOMBIE with custom Retono entity
        this.goalSelector.addGoal(4, new BossSpawnMinionsGoal(this,
                () -> EntityType.ZOMBIE, 2, 4, 600, 2));

        // Passive behaviors
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // Targeting
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (newPhase == 2) {
            // TODO: Apply speed buff (0.28 -> 0.34)
            // TODO: Play root emergence effects
            // TODO: Apply Slowness I 2s to all players in arena
        }
    }
}
