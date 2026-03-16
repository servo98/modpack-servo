package com.servo.dungeons.entity.boss.goal;

import com.servo.dungeons.entity.boss.AbstractDungeonBoss;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

/**
 * Spawns minion mobs on a cooldown, up to a maximum concurrent count.
 * Used by bosses like Guardian del Bosque (Retonos), Coloso Mecanico (Drones), etc.
 * Tracks spawned minions and removes references to dead ones.
 */
public class BossSpawnMinionsGoal extends Goal {

    private final AbstractDungeonBoss boss;
    private final Supplier<EntityType<? extends Mob>> minionType;
    private final int spawnCount;
    private final int maxAlive;
    private final int cooldownTicks;
    private final int minPhase;
    private int cooldownTimer;
    private final List<Mob> activeMinions = new ArrayList<>();

    /**
     * @param boss the boss entity
     * @param minionType supplier for the minion EntityType
     * @param spawnCount how many minions to spawn per activation
     * @param maxAlive maximum concurrent living minions
     * @param cooldownTicks ticks between spawn waves
     * @param minPhase minimum phase number before this goal activates (1 = always)
     */
    public BossSpawnMinionsGoal(AbstractDungeonBoss boss, Supplier<EntityType<? extends Mob>> minionType,
                                 int spawnCount, int maxAlive, int cooldownTicks, int minPhase) {
        this.boss = boss;
        this.minionType = minionType;
        this.spawnCount = spawnCount;
        this.maxAlive = maxAlive;
        this.cooldownTicks = cooldownTicks;
        this.minPhase = minPhase;
        this.setFlags(EnumSet.noneOf(Goal.Flag.class)); // Doesn't block other goals
    }

    @Override
    public boolean canUse() {
        if (boss.isInTransition()) return false;
        if (boss.getCurrentPhase() < minPhase) return false;
        if (cooldownTimer > 0) return false;

        // Clean up dead minions
        activeMinions.removeIf(mob -> !mob.isAlive());

        return activeMinions.size() < maxAlive;
    }

    @Override
    public boolean canContinueToUse() {
        return false; // Single-shot: spawn and done
    }

    @Override
    public void start() {
        if (!(boss.level() instanceof ServerLevel serverLevel)) return;

        // Clean up dead minions
        activeMinions.removeIf(mob -> !mob.isAlive());

        int toSpawn = Math.min(spawnCount, maxAlive - activeMinions.size());

        for (int i = 0; i < toSpawn; i++) {
            Mob minion = (Mob) minionType.get().create(serverLevel);
            if (minion != null) {
                // Spawn near the boss with some spread
                BlockPos bossPos = boss.blockPosition();
                double offsetX = (boss.getRandom().nextDouble() - 0.5) * 6.0;
                double offsetZ = (boss.getRandom().nextDouble() - 0.5) * 6.0;
                minion.setPos(bossPos.getX() + offsetX, bossPos.getY(), bossPos.getZ() + offsetZ);
                serverLevel.addFreshEntity(minion);
                activeMinions.add(minion);
            }
        }

        cooldownTimer = cooldownTicks;
    }

    @Override
    public void tick() {
        cooldownTimer = Math.max(cooldownTimer - 1, 0);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    /**
     * Kill all active minions. Called when the boss dies.
     */
    public void killAllMinions() {
        for (Mob minion : activeMinions) {
            if (minion.isAlive()) {
                minion.kill();
            }
        }
        activeMinions.clear();
    }

    /**
     * @return the current list of active (living) minions
     */
    public List<Mob> getActiveMinions() {
        activeMinions.removeIf(mob -> !mob.isAlive());
        return activeMinions;
    }
}
