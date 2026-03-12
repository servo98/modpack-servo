package com.servo.packaging.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

/**
 * Data component stored on ShippingBox items.
 * Tracks what item is packed inside and how many.
 */
public record BoxContents(ResourceLocation itemId, int count, String category) {

    public static final Codec<BoxContents> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("item_id").forGetter(BoxContents::itemId),
                    Codec.INT.fieldOf("count").forGetter(BoxContents::count),
                    Codec.STRING.fieldOf("category").forGetter(BoxContents::category)
            ).apply(instance, BoxContents::new)
    );

    public static final StreamCodec<ByteBuf, BoxContents> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, BoxContents::itemId,
            ByteBufCodecs.INT, BoxContents::count,
            ByteBufCodecs.STRING_UTF8, BoxContents::category,
            BoxContents::new
    );
}
