package com.nekotune.minecraftjourneys.client.gui.hud.stamina;

import java.util.UUID;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.logic.stamina.StaminaEvent;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID, value = Dist.CLIENT)
@OnlyIn(value = Dist.CLIENT)
public final class EventHooks {

    private static boolean isClientPlayer(final UUID playerId) {
        final var clientPlayer = Minecraft.getInstance().player;
        return clientPlayer != null && playerId.equals(clientPlayer.getUUID());
    }

    @SubscribeEvent
    public static void onStaminaChanged(StaminaEvent.ChangedEvent event) {
        if (!isClientPlayer(event.getPlayerId())) return;
        MJStaminaHud.instance.ifPresent((final var hud) -> {
            final float tweenTime = event.getGuiAnimation().tweenTime();
            if (tweenTime == 0) return;
            hud.deltaAnimation.tickLength = tweenTime * 20f;
            hud.deltaAnimation.deltaStamina = event.getStamina().getValue() - event.getPreviousValue();
            hud.deltaAnimation.shouldStart = true;
        });
    }

    @SubscribeEvent
    public static void onStaminaTickPost(StaminaEvent.TickEvent.Post event) {
        if (!isClientPlayer(event.getPlayerId())) return;
        MJStaminaHud.instance.ifPresent((final var hud) -> {
            // TODO: Stamina drain animation
        });
    }
}