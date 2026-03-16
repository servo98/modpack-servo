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
 * Chapter 2 Boss: Bestia Glotona.
 * An obese 2.5-block creature with grey-green skin and a massive dislocated jaw.
 *
 * <p>Phase 1 (100%-40%): Embestida (charge), Mordisco (bite + steals food from inventory),
 * Vomito Acido (acid pool). Core mechanic: Saciedad meter (0-100%). Players feed the boss
 * food items to manage its hunger. Poisoned food creates DPS windows.</p>
 *
 * <p>Phase 2 (40%-0%): Saciedad drains 2x faster. Boss grows 30%. Gains Devorar attack
 * (grabs and chews player under 6 HP). Poisoned food window extended to 6s.</p>
 *
 * <p>Stats: 1200 HP | 6.8 Dmg | 0.22 Speed | 6 Armor | 0.9 KB Res | 2 Phases</p>
 */
public class BestiaGlotonaBoss extends AbstractDungeonBoss {

    // TODO: Implement Saciedad meter (0-100%, starts at 100%, drains 2%/s)
    //       - At 0%: Frenzy mode (2x dmg, 2x speed for 15s)
    //       - Normal food: +20% Saciedad
    //       - Poisoned food (Rotten Flesh, Suspicious Stew, Poisonous Potato): +10% + Nausea 4s + 1.5x dmg taken
    //       - High quality food (Feast-tier): +40% + heals boss 5% HP (bad move)
    //       - Saciedad > 80% = Resistance I (bad, let it drop)
    //       - Saciedad < 20% = +50% dmg, +30% speed (dangerous)
    //       - Optimal zone: 20%-60%
    // TODO: Embestida (8.0 dmg + strong knockback, 10 blocks line, 8s CD, 1.5s telegraph)
    // TODO: Mordisco (6.8 dmg + steals 1 food from inventory, 2 blocks melee, 3s CD)
    // TODO: Vomito Acido (3.0/s acid pool 5s + Nausea, 3x3 in front, 10s CD)
    // TODO: Phase 2 - Devorar (4.0/s for 3s, immobilizes player < 6 HP, 15s CD, interrupted by hitting boss)
    // TODO: Phase 2 - Boss grows 30%, Saciedad drains 4%/s, acid pools 5x5 for 8s
    // TODO: Right-click food throwing mechanic

    public BestiaGlotonaBoss(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level, 2, new float[]{0.4f}, 6.8, BossEvent.BossBarColor.GREEN);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBossAttributes(1200, 6.8, 0.22, 6, 0.9);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BossPhaseTransitionGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        // Melee: Mordisco (bite) - short range, fast cooldown
        this.goalSelector.addGoal(2, new BossMeleeAttackGoal(this, 1.0, 2.5, 60));
        // Ranged: Embestida / Vomito placeholder
        this.goalSelector.addGoal(3, new BossRangedAttackGoal(this, 4.0, 10.0, 160, 1.2f));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (newPhase == 2) {
            // TODO: Saciedad drops to 0%, boss grows 30%, Saciedad drain rate doubles
            // TODO: Play massive vomit animation
            // TODO: Scale entity size (setScale or GeckoLib bone transform)
        }
    }
}
