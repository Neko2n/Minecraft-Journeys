package com.nekotune.minecraftjourneys.shared.hooks;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.attachment.PlayerStaminaStored;
import com.nekotune.minecraftjourneys.shared.systems.PlayerStamina;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public class RegisterPayloads {
    
    @SubscribeEvent
    public static void onRegisterPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // Player stamina
        registrar.playToClient(
            PlayerStaminaStored.PAYLOAD_TYPE,
            PlayerStaminaStored.STREAM_CODEC,
            new MainThreadPayloadHandler<>(PlayerStamina.ClientPayloadHandler::acceptPayload));
    }
}
