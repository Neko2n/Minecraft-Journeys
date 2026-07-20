package com.nekotune.minecraftjourneys.shared.systems.stamina.packet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public final class StaminaPacket {
    
    public final record ToClient(float value, float tweenTime) implements CustomPacketPayload {
        public static final ResourceLocation RESOURCE = ResourceLocation.fromNamespaceAndPath(
                MinecraftJourneys.MOD_ID, "player/stamina/value/to_client");

        public static final Codec<ToClient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("value").forGetter(d -> d.value),
                Codec.FLOAT.fieldOf("tweenTime").forGetter(d -> d.tweenTime)).apply(instance, ToClient::new));

        public static final TypeAndCodec<FriendlyByteBuf, ToClient> PAYLOAD = new CustomPacketPayload.TypeAndCodec<>(
                new CustomPacketPayload.Type<>(RESOURCE),
                StreamCodec.composite(
                        ByteBufCodecs.FLOAT, d -> d.value,
                        ByteBufCodecs.FLOAT, d -> d.tweenTime,
                        ToClient::new));

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PAYLOAD.type();
        }
    }

    public final record ToServer(float value) implements CustomPacketPayload {
        public static final ResourceLocation RESOURCE = ResourceLocation.fromNamespaceAndPath(
                MinecraftJourneys.MOD_ID, "player/stamina/value/to_server");

        public static final Codec<ToServer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("value").forGetter(d -> d.value)).apply(instance, ToServer::new));

        public static final TypeAndCodec<FriendlyByteBuf, ToServer> PAYLOAD = new CustomPacketPayload.TypeAndCodec<>(
                new CustomPacketPayload.Type<>(RESOURCE),
                StreamCodec.composite(
                        ByteBufCodecs.FLOAT, d -> d.value,
                        ToServer::new));

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PAYLOAD.type();
        }
    }
}
