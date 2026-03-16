package com.servo.dungeons.entity.boss.goal;

import com.servo.dungeons.entity.boss.AbstractDungeonBoss;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Basic melee attack goal for dungeon bosses.
 * Approaches the target and attacks with configurable damage and cooldown.
 * Triggers the "melee" animation on attack via GeckoLib.
 */
public class BossMeleeAttackGoal extends Goal {

    private final AbstractDungeonBoss boss;
    private final double speedModifier;
    private final double attackRange;
    private final int attackCooldownTicks;
    private int cooldownTimer;
    private int ticksUntilNextPathRecalc;

    /**
     * @param boss the boss entity
     * @param speedModifier movement speed multiplier when approaching target
     * @param attackRange melee range in blocks
     * @param attackCooldownTicks ticks between attacks
     */
    public BossMeleeAttackGoal(AbstractDungeonBoss boss, double speedModifier, double attackRange, int attackCooldownTicks) {
        this.boss = boss;
        this.speedModifier = speedModifier;
        this.attackRange = attackRange;
        this.attackCooldownTicks = attackCooldownTicks;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = boss.getTarget();
        return target != null && target.isAlive() && !boss.isInTransition();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        cooldownTimer = 0;
        ticksUntilNextPathRecalc = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = boss.getTarget();
        if (target == null) return;

        // Look at target
        boss.getLookControl().setLookAt(target, 30.0F, 30.0F);

        double distSq = boss.distanceToSqr(target);
        double reachSq = attackRange * attackRange;

        // Path recalculation
        ticksUntilNextPathRecalc = Math.max(ticksUntilNextPathRecalc - 1, 0);
        if (ticksUntilNextPathRecalc <= 0) {
            ticksUntilNextPathRecalc = 10;
            if (distSq > reachSq) {
                boss.getNavigation().moveTo(target, speedModifier);
            }
        }

        // Cooldown
        cooldownTimer = Math.max(cooldownTimer - 1, 0);

        // Attack if in range and off cooldown
        if (distSq <= reachSq && cooldownTimer <= 0) {
            cooldownTimer = attackCooldownTicks;
            boss.triggerAnim("attacks", "melee");
            float damage = (float) boss.getScaledDamage();
            target.hurt(boss.damageSources().mobAttack(boss), damage);
        }
    }

    @Override
    public void stop() {
        boss.getNavigation().stop();
    }
}
