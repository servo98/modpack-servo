package com.servo.core.gacha;

import com.servo.core.ServoCore;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * Registers the gacha pity tracker as a NeoForge AttachmentType on players.
 *
 * <p>The attachment persists across sessions (serialized via Codec) and is
 * per-player. Each player gets their own {@link GachaPityData} with independent
 * counters for each {@link GachaMachineType}.
 *
 * <p>Usage from other code:
 * <pre>{@code
 *     GachaPityData pity = player.getData(GachaPityAttachment.GACHA_PITY);
 *     // or use the helper: GachaPityHelper.getPityData(player)
 * }</pre>
 */
public class GachaPityAttachment {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ServoCore.MOD_ID);

    /**
     * The gacha pity attachment. Attached to players, persists via Codec.
     * Default value: fresh GachaPityData with all counters at 0.
     */
    public static final Supplier<AttachmentType<GachaPityData>> GACHA_PITY =
            ATTACHMENT_TYPES.register("gacha_pity", () ->
                    AttachmentType.builder(GachaPityData::new)
                            .serialize(GachaPityData.CODEC)
                            .build()
            );

    /**
     * Registers the attachment types to the mod event bus.
     * Called from {@link ServoCore} constructor.
     */
    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
