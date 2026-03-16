package com.servo.core.gacha;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.EnumMap;
import java.util.Map;

/**
 * Per-player gacha pity state. Tracks pull counters independently for each
 * {@link GachaMachineType}. Attached to players via NeoForge AttachedData.
 *
 * <p>Pity mechanics:
 * <ul>
 *     <li>Each pull without Epic+ adds +2% to the Epic chance</li>
 *     <li>At 50 pulls without Epic+, hard pity triggers: guaranteed Legendary (100%)</li>
 *     <li>Counter resets to 0 when Epic or Legendary is obtained</li>
 * </ul>
 */
public class GachaPityData {

    /** Bonus added per pull without Epic+ (2% = 0.02). */
    public static final float PITY_BONUS_PER_PULL = 0.02f;

    /** Number of pulls that triggers hard pity (guaranteed Legendary). */
    public static final int HARD_PITY_THRESHOLD = 50;

    /**
     * Codec for serialization. Encodes the pull counters as a map of
     * GachaMachineType (string) -> Integer.
     */
    public static final Codec<GachaPityData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(GachaMachineType.CODEC, Codec.INT)
                            .fieldOf("pull_counters")
                            .forGetter(GachaPityData::getPullCountersAsMap)
            ).apply(instance, GachaPityData::fromMap)
    );

    private final EnumMap<GachaMachineType, Integer> pullCounters;

    public GachaPityData() {
        this.pullCounters = new EnumMap<>(GachaMachineType.class);
        for (GachaMachineType type : GachaMachineType.values()) {
            pullCounters.put(type, 0);
        }
    }

    private GachaPityData(EnumMap<GachaMachineType, Integer> counters) {
        this.pullCounters = counters;
    }

    /**
     * Creates a GachaPityData from a deserialized map. Fills in missing machine
     * types with 0 (forward-compatible if new machines are added).
     */
    static GachaPityData fromMap(Map<GachaMachineType, Integer> map) {
        EnumMap<GachaMachineType, Integer> counters = new EnumMap<>(GachaMachineType.class);
        for (GachaMachineType type : GachaMachineType.values()) {
            counters.put(type, map.getOrDefault(type, 0));
        }
        return new GachaPityData(counters);
    }

    /**
     * Returns the pull counters as a regular map (for Codec serialization).
     */
    Map<GachaMachineType, Integer> getPullCountersAsMap() {
        return new EnumMap<>(pullCounters);
    }

    // === Query Methods ===

    /**
     * Returns the number of pulls since last Epic+ for the given machine type.
     */
    public int getPullCount(GachaMachineType type) {
        return pullCounters.getOrDefault(type, 0);
    }

    /**
     * Increments the pull counter for the given machine type.
     * Called when a pull does NOT result in Epic or higher.
     */
    public void recordPull(GachaMachineType type) {
        pullCounters.merge(type, 1, Integer::sum);
    }

    /**
     * Resets the pull counter to 0 for the given machine type.
     * Called when an Epic or Legendary result is obtained.
     */
    public void resetPity(GachaMachineType type) {
        pullCounters.put(type, 0);
    }

    /**
     * Calculates the bonus added to the Epic chance from pity.
     * Each pull without Epic+ adds +2%.
     *
     * @return bonus as a float (e.g., 10 pulls = 0.20)
     */
    public float calculateEpicBonus(GachaMachineType type) {
        return getPullCount(type) * PITY_BONUS_PER_PULL;
    }

    /**
     * Returns true if the player has hit hard pity (50+ pulls without Epic+).
     * At hard pity, the next pull is guaranteed Legendary.
     */
    public boolean isHardPity(GachaMachineType type) {
        return getPullCount(type) >= HARD_PITY_THRESHOLD;
    }

    /**
     * Returns the effective rates for a machine type, adjusted for pity.
     *
     * <p>If hard pity is reached, Legendary is 100% (and Epic is 0% since
     * the result is guaranteed Legendary). Otherwise, the Epic bonus is
     * added to the base Epic rate. Legendary rate is unchanged until hard pity.
     */
    public EffectiveRates getEffectiveRates(GachaMachineType type) {
        if (isHardPity(type)) {
            return new EffectiveRates(0.0f, 1.0f, true);
        }
        float epicRate = type.getBaseEpicRate() + calculateEpicBonus(type);
        // Cap epic rate at a reasonable value (shouldn't exceed remaining probability)
        epicRate = Math.min(epicRate, 1.0f - type.getBaseLegendaryRate());
        return new EffectiveRates(epicRate, type.getBaseLegendaryRate(), false);
    }

    /**
     * Holds the calculated effective rates after pity adjustments.
     *
     * @param epicRate      adjusted Epic chance (0.0 - 1.0)
     * @param legendaryRate adjusted Legendary chance (0.0 - 1.0)
     * @param hardPity      true if this is a hard pity pull
     */
    public record EffectiveRates(float epicRate, float legendaryRate, boolean hardPity) {
        /**
         * Combined chance of Epic or higher.
         */
        public float epicPlusRate() {
            return epicRate + legendaryRate;
        }
    }
}
