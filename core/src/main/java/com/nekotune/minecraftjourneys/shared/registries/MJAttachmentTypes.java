package com.nekotune.minecraftjourneys.shared.registries;

import java.util.function.Supplier;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.logic.stamina.packet.MaxStaminaPacket;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class MJAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MinecraftJourneys.MOD_ID);
    
    public static final Supplier<AttachmentType<MaxStaminaPacket.ToClient>> PLAYER_MAX_STAMINA = ATTACHMENT_TYPES.register(
            MaxStaminaPacket.ToClient.RESOURCE.getPath(), () -> AttachmentType.builder(() -> new MaxStaminaPacket.ToClient())
            .serialize(MaxStaminaPacket.ToClient.CODEC)
            .sync(MaxStaminaPacket.ToClient.PAYLOAD.codec())
            .build());
}
