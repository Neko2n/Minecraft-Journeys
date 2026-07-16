package com.nekotune.minecraftjourneys.shared.registries;

import java.util.function.Supplier;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.attachment.PlayerStaminaStored;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class MJAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MinecraftJourneys.MOD_ID);
    
    public static final Supplier<AttachmentType<PlayerStaminaStored>> PLAYER_STAMINA_DATA = ATTACHMENT_TYPES.register(
            "stamina", () -> AttachmentType.builder(() -> new PlayerStaminaStored())
            .serialize(PlayerStaminaStored.CODEC)
            .sync(PlayerStaminaStored.STREAM_CODEC)
            .build());
}
