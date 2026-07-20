package com.nekotune.minecraftjourneys.shared.systems.stamina.packet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public final class MaxStaminaPacket {
    
    public final record ToClient(int value) implements CustomPacketPayload {
        public static final ResourceLocation RESOURCE = ResourceLocation.fromNamespaceAndPath(
                MinecraftJourneys.MOD_ID, "player/stamina/value_max/to_client");

        public ToClient() {
            this(1);
        }

        public static final Codec<ToClient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("value").forGetter(d -> d.value)).apply(instance, ToClient::new));

        public static final TypeAndCodec<FriendlyByteBuf, ToClient> PAYLOAD = new CustomPacketPayload.TypeAndCodec<>(
                new CustomPacketPayload.Type<>(RESOURCE),
                StreamCodec.composite(
                        ByteBufCodecs.INT, d -> d.value,
                        ToClient::new));

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PAYLOAD.type();
        }
    }
}
