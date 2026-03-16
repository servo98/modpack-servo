package com.servo.dungeons.entity.boss.goal;

import com.servo.dungeons.entity.boss.AbstractDungeonBoss;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Ranged attack goal for dungeon bosses.
 * Fires a projectile or applies AoE damage at the target's position.
 * Currently implemented as a direct damage hit (placeholder for future projectile logic).
 * Triggers the "ranged" animation on attack via GeckoLib.
 */
public class BossRangedAttackGoal extends Goal {

    private final AbstractDungeonBoss boss;
    private final double minRange;
    private final double maxRange;
    private final int attackCooldownTicks;
    private final float damageMultiplier;
    private int cooldownTimer;

    /**
     * @param boss the boss entity
     * @param minRange minimum distance to target before using this attack
     * @param maxRange maximum distance to target for this attack
     * @param attackCooldownTicks ticks between attacks
     * @param damageMultiplier multiplier applied to the boss's scaled damage
     */
    public BossRangedAttackGoal(AbstractDungeonBoss boss, double minRange, double maxRange,
                                 int attackCooldownTicks, float damageMultiplier) {
        this.boss = boss;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.attackCooldownTicks = attackCooldownTicks;
        this.damageMultiplier = damageMultiplier;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = boss.getTarget();
        if (target == null || !target.isAlive() || boss.isInTransition()) return false;

        double dist = boss.distanceTo(target);
        return dist >= minRange && dist <= maxRange && cooldownTimer <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return false; // Single-shot: execute once then stop
    }

    @Override
    public void start() {
        LivingEntity target = boss.getTarget();
        if (target == null) return;

        boss.getLookControl().setLookAt(target, 30.0F, 30.0F);
        boss.triggerAnim("attacks", "ranged");

        // TODO: Replace with actual projectile entity when models are ready.
        //       For now, apply direct damage after a short delay simulation.
        float damage = (float) (boss.getScaledDamage() * damageMultiplier);
        target.hurt(boss.damageSources().mobAttack(boss), damage);

        cooldownTimer = attackCooldownTicks;
    }

    @Override
    public void tick() {
        cooldownTimer = Math.max(cooldownTimer - 1, 0);
    }

    @Override
    public void stop() {
        // No cleanup needed
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
