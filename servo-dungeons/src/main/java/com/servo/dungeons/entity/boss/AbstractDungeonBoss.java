package com.servo.dungeons.entity.boss;

import com.servo.dungeons.ServoDungeons;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Abstract base class for all 8 dungeon boss entities.
 * Provides multiplayer scaling, phase management, boss bar, GeckoLib animation,
 * and invulnerability during phase transitions.
 */
public abstract class AbstractDungeonBoss extends Monster implements GeoEntity {

    // === GeckoLib animation references ===
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    protected static final RawAnimation ATTACK_MELEE = RawAnimation.begin().thenPlay("attack_melee");
    protected static final RawAnimation ATTACK_RANGED = RawAnimation.begin().thenPlay("attack_ranged");
    protected static final RawAnimation ATTACK_SPECIAL_1 = RawAnimation.begin().thenPlay("attack_special_1");
    protected static final RawAnimation STAGGER_ANIM = RawAnimation.begin().thenPlay("stagger");
    protected static final RawAnimation SPAWN_ANIM = RawAnimation.begin().thenPlay("spawn");
    protected static final RawAnimation PHASE_TRANSITION = RawAnimation.begin().thenPlay("phase_transition");
    protected static final RawAnimation ENRAGE_ANIM = RawAnimation.begin().thenPlay("enrage");
    protected static final RawAnimation DEATH_ANIM = RawAnimation.begin().thenPlay("death");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // === Boss state ===
    /** The chapter this boss belongs to (1-8). */
    protected final int chapter;

    /** Current phase (1-indexed). Phase 1 = start. */
    protected int currentPhase = 1;

    /** HP percentage thresholds that trigger phase transitions.
     *  For a 2-phase boss: {0.5f} means phase 2 starts at 50% HP.
     *  For a 3-phase boss: {0.65f, 0.30f} means phase 2 at 65%, phase 3 at 30%. */
    protected final float[] phaseThresholds;

    /** True during a phase transition (boss is invulnerable). */
    protected boolean isTransitioning = false;

    /** Ticks remaining in transition invulnerability. */
    protected int transitionTicks = 0;

    /** Duration in ticks of the transition animation. */
    protected static final int TRANSITION_DURATION = 60; // 3 seconds

    /** Base damage for this boss (before multiplayer scaling). */
    protected final double baseDamage;

    /** The number of players at spawn time (used for scaling). Fixed for the fight's duration. */
    protected int scaledPlayerCount = 1;

    /** Whether death animation is playing. */
    protected boolean isDying = false;

    /** Ticks remaining in death animation before removal. */
    protected int deathAnimTicks = 0;

    /** Duration of the death animation in ticks. */
    protected static final int DEATH_ANIM_DURATION = 80; // 4 seconds

    /** Boss bar shown to nearby players. */
    private final ServerBossEvent bossBar;

    /**
     * @param entityType the entity type
     * @param level the world
     * @param chapter the chapter number (1-8)
     * @param phaseThresholds HP% thresholds for phase transitions (descending order)
     * @param baseDamage base melee damage before scaling
     * @param bossBarColor boss bar color
     */
    protected AbstractDungeonBoss(EntityType<? extends Monster> entityType, Level level,
                                  int chapter, float[] phaseThresholds, double baseDamage,
                                  BossEvent.BossBarColor bossBarColor) {
        super(entityType, level);
        this.chapter = chapter;
        this.phaseThresholds = phaseThresholds;
        this.baseDamage = baseDamage;
        this.setPersistenceRequired();
        this.xpReward = 0; // Loot handled via data-driven loot tables

        this.bossBar = new ServerBossEvent(
                this.getDisplayName(),
                bossBarColor,
                BossEvent.BossBarOverlay.NOTCHED_10
        );
        this.bossBar.setVisible(true);
    }

    // ==================== Multiplayer Scaling ====================

    /**
     * Scale the boss's HP and damage for the given player count.
     * HP formula: base * (1 + (players-1) * 0.3)
     * Dmg formula: base * (1 + (players-1) * 0.15)
     * Must be called immediately after spawning, before the fight begins.
     *
     * @param playerCount the number of players in the arena at spawn time
     */
    public void scaleForPlayers(int playerCount) {
        this.scaledPlayerCount = Math.max(1, playerCount);

        double hpMultiplier = 1.0 + (scaledPlayerCount - 1) * 0.3;
        double scaledMaxHealth = getBaseMaxHealth() * hpMultiplier;

        // Apply scaled health
        var healthAttr = this.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttr != null) {
            healthAttr.setBaseValue(scaledMaxHealth);
        }
        this.setHealth((float) scaledMaxHealth);

        ServoDungeons.LOGGER.info("Boss {} scaled for {} players: HP={}, Dmg={}",
                this.getType().getDescriptionId(), scaledPlayerCount,
                String.format("%.0f", scaledMaxHealth),
                String.format("%.1f", getScaledDamage()));
    }

    /**
     * @return the base max HP before any scaling (the value from the stats table)
     */
    protected double getBaseMaxHealth() {
        return this.getAttributeBaseValue(Attributes.MAX_HEALTH);
    }

    /**
     * @return the scaled max health for the current player count
     */
    public double getScaledMaxHealth() {
        double hpMultiplier = 1.0 + (scaledPlayerCount - 1) * 0.3;
        return getBaseMaxHealth() * hpMultiplier;
    }

    /**
     * @return the scaled damage for the current player count
     */
    public double getScaledDamage() {
        double dmgMultiplier = 1.0 + (scaledPlayerCount - 1) * 0.15;
        return baseDamage * dmgMultiplier;
    }

    // ==================== Phase Management ====================

    /**
     * @return the current phase number (1-indexed)
     */
    public int getCurrentPhase() {
        return currentPhase;
    }

    /**
     * @return the total number of phases for this boss
     */
    public int getTotalPhases() {
        return phaseThresholds.length + 1;
    }

    /**
     * Determine which phase should be active based on current HP percentage.
     */
    private int calculateExpectedPhase() {
        float hpPercent = this.getHealth() / this.getMaxHealth();
        int phase = 1;
        for (float threshold : phaseThresholds) {
            if (hpPercent <= threshold) {
                phase++;
            }
        }
        return phase;
    }

    /**
     * Called when a phase transition completes. Override in subclasses to reconfigure AI goals,
     * apply buffs, spawn minions, etc.
     *
     * @param newPhase the phase number that was just entered
     */
    protected abstract void onPhaseTransition(int newPhase);

    /**
     * Begin a phase transition. The boss becomes invulnerable and plays the transition animation.
     */
    private void beginPhaseTransition(int newPhase) {
        this.isTransitioning = true;
        this.transitionTicks = TRANSITION_DURATION;
        this.triggerAnim("phase", "phase_transition");

        ServoDungeons.LOGGER.debug("Boss {} entering phase {} (HP: {}/{})",
                this.getType().getDescriptionId(), newPhase,
                String.format("%.0f", this.getHealth()),
                String.format("%.0f", this.getMaxHealth()));
    }

    // ==================== Boss Bar ====================

    /**
     * Update the boss bar color based on the current phase.
     */
    private void updateBossBarColor() {
        int totalPhases = getTotalPhases();
        if (totalPhases <= 2) {
            this.bossBar.setColor(currentPhase == 1 ? BossEvent.BossBarColor.GREEN : BossEvent.BossBarColor.RED);
        } else {
            switch (currentPhase) {
                case 1 -> this.bossBar.setColor(BossEvent.BossBarColor.GREEN);
                case 2 -> this.bossBar.setColor(BossEvent.BossBarColor.YELLOW);
                case 3 -> this.bossBar.setColor(BossEvent.BossBarColor.RED);
                default -> this.bossBar.setColor(BossEvent.BossBarColor.PURPLE);
            }
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossBar.removePlayer(player);
    }

    // ==================== Tick & Combat ====================

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            // Update boss bar progress
            this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());

            // Handle transition countdown
            if (isTransitioning) {
                transitionTicks--;
                if (transitionTicks <= 0) {
                    isTransitioning = false;
                    int expectedPhase = calculateExpectedPhase();
                    if (expectedPhase > currentPhase) {
                        currentPhase = expectedPhase;
                        updateBossBarColor();
                        onPhaseTransition(currentPhase);
                    }
                }
                // Don't do phase checks while already transitioning
                return;
            }

            // Check for phase transition
            int expectedPhase = calculateExpectedPhase();
            if (expectedPhase > currentPhase && !isDying) {
                beginPhaseTransition(expectedPhase);
            }

            // Handle death animation
            if (isDying) {
                deathAnimTicks--;
                if (deathAnimTicks <= 0) {
                    this.remove(RemovalReason.KILLED);
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Invulnerable during phase transitions
        if (isTransitioning) {
            return false;
        }
        // Invulnerable during death animation
        if (isDying) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void tickDeath() {
        // Override to prevent vanilla death behavior — we handle it with our animation
        if (!isDying) {
            isDying = true;
            deathAnimTicks = DEATH_ANIM_DURATION;
            this.triggerAnim("phase", "death");
            // Clear the boss bar
            this.bossBar.removeAllPlayers();
            ServoDungeons.LOGGER.info("Boss {} defeated!", this.getType().getDescriptionId());
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // Bosses should never despawn from distance
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    /**
     * @return the chapter number this boss belongs to
     */
    public int getChapter() {
        return chapter;
    }

    /**
     * @return true if the boss is currently in a phase transition (invulnerable)
     */
    public boolean isInTransition() {
        return isTransitioning;
    }

    // ==================== GeckoLib ====================

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Controller 1: Movement — always active
        controllers.add(new AnimationController<>(this, "movement", 5, this::movementController));

        // Controller 2: Attacks — triggered from server
        controllers.add(new AnimationController<>(this, "attacks", 0, state -> PlayState.STOP)
                .triggerableAnim("melee", ATTACK_MELEE)
                .triggerableAnim("ranged", ATTACK_RANGED)
                .triggerableAnim("special1", ATTACK_SPECIAL_1)
                .triggerableAnim("stagger", STAGGER_ANIM));

        // Controller 3: Phase/state — transitions and cinematics
        controllers.add(new AnimationController<>(this, "phase", 10, state -> PlayState.STOP)
                .triggerableAnim("spawn", SPAWN_ANIM)
                .triggerableAnim("phase_transition", PHASE_TRANSITION)
                .triggerableAnim("enrage", ENRAGE_ANIM)
                .triggerableAnim("death", DEATH_ANIM));
    }

    /**
     * Movement animation controller: plays walk when moving, idle when stationary.
     */
    private PlayState movementController(AnimationState<AbstractDungeonBoss> state) {
        if (state.isMoving()) {
            return state.setAndContinue(WALK_ANIM);
        }
        return state.setAndContinue(IDLE_ANIM);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // ==================== Attributes ====================

    /**
     * Create default boss attribute supplier. Subclasses should call this or build their own.
     *
     * @param maxHealth base HP
     * @param damage base melee damage
     * @param speed movement speed
     * @param armor armor value
     * @param knockbackResistance knockback resistance (0.0-1.0)
     * @return the attribute builder
     */
    public static AttributeSupplier.Builder createBossAttributes(double maxHealth, double damage,
                                                                   double speed, double armor,
                                                                   double knockbackResistance) {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, maxHealth)
                .add(Attributes.ATTACK_DAMAGE, damage)
                .add(Attributes.MOVEMENT_SPEED, speed)
                .add(Attributes.ARMOR, armor)
                .add(Attributes.KNOCKBACK_RESISTANCE, knockbackResistance)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }
}
