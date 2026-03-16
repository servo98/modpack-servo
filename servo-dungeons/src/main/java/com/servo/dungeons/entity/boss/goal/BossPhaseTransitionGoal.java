package com.servo.dungeons.entity.boss.goal;

import com.servo.dungeons.entity.boss.AbstractDungeonBoss;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Handles the invulnerability window during a phase transition.
 * While the boss is transitioning, this goal takes priority: the boss stops moving
 * and attacking, and remains invulnerable until the transition completes.
 * This goal has the highest priority to interrupt any other active goals.
 */
public class BossPhaseTransitionGoal extends Goal {

    private final AbstractDungeonBoss boss;

    public BossPhaseTransitionGoal(AbstractDungeonBoss boss) {
        this.boss = boss;
        // Take over all flags to ensure no other goals run during transition
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return boss.isInTransition();
    }

    @Override
    public boolean canContinueToUse() {
        return boss.isInTransition();
    }

    @Override
    public void start() {
        boss.getNavigation().stop();
    }

    @Override
    public void tick() {
        // Boss remains stationary during transition.
        // The AbstractDungeonBoss.aiStep() handles the countdown.
        boss.getNavigation().stop();
    }

    @Override
    public boolean isInterruptable() {
        // This goal cannot be interrupted by lower-priority goals
        return false;
    }
}
