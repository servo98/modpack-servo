package com.servo.core.gacha;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

/**
 * The four gacha machine tiers available in the modpack.
 * Each machine has its own base rates for Epic and Legendary pulls,
 * and each player tracks pity counters independently per machine.
 *
 * <p>Base rates (before pity bonus):
 * <ul>
 *     <li>GREEN (Basic): Epic 5%, Legendary 3%</li>
 *     <li>BLUE (Advanced): Epic 7%, Legendary 3%</li>
 *     <li>PURPLE (Superior): Epic 9%, Legendary 4%</li>
 *     <li>PINK (Furniture): Epic 4%, Legendary 1%</li>
 * </ul>
 */
public enum GachaMachineType implements StringRepresentable {
    GREEN("green", 0.05f, 0.03f),
    BLUE("blue", 0.07f, 0.03f),
    PURPLE("purple", 0.09f, 0.04f),
    PINK("pink", 0.04f, 0.01f);

    public static final Codec<GachaMachineType> CODEC = StringRepresentable.fromEnum(GachaMachineType::values);
    public static final StreamCodec<ByteBuf, GachaMachineType> STREAM_CODEC =
            ByteBufCodecs.idMapper(i -> values()[i], GachaMachineType::ordinal);

    private final String id;
    private final float baseEpicRate;
    private final float baseLegendaryRate;

    GachaMachineType(String id, float baseEpicRate, float baseLegendaryRate) {
        this.id = id;
        this.baseEpicRate = baseEpicRate;
        this.baseLegendaryRate = baseLegendaryRate;
    }

    public String getId() {
        return id;
    }

    public float getBaseEpicRate() {
        return baseEpicRate;
    }

    public float getBaseLegendaryRate() {
        return baseLegendaryRate;
    }

    /**
     * Combined base chance of getting Epic or higher (before pity).
     */
    public float getBaseEpicPlusRate() {
        return baseEpicRate + baseLegendaryRate;
    }

    @Override
    public String getSerializedName() {
        return id;
    }

    /**
     * Looks up a machine type by its string ID. Returns null if not found.
     */
    public static GachaMachineType byId(String id) {
        for (GachaMachineType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return null;
    }
}
