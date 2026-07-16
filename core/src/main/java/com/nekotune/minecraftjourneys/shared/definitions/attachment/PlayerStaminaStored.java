package com.nekotune.minecraftjourneys.shared.definitions.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nekotune.minecraftjourneys.MinecraftJourneys;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PlayerStaminaStored(int maxStamina) implements CustomPacketPayload {
    public PlayerStaminaStored(int maxStamina) {
        this.maxStamina = maxStamina;
    }
    public PlayerStaminaStored() {
        this(1);
    }

    public static final Codec<PlayerStaminaStored> CODEC = RecordCodecBuilder.create(instance -> 
            instance.group(
                    Codec.INT.fieldOf("maxStamina").forGetter(d -> d.maxStamina)
            ).apply(instance, PlayerStaminaStored::new));
    
    public static final StreamCodec<ByteBuf, PlayerStaminaStored> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, d -> d.maxStamina,
        PlayerStaminaStored::new
    );

    public static final CustomPacketPayload.Type<PlayerStaminaStored> PAYLOAD_TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, "player_stamina"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return PAYLOAD_TYPE;
	}
}
