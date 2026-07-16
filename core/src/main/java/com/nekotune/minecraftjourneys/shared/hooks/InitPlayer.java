package com.nekotune.minecraftjourneys.shared.hooks;

import com.google.common.eventbus.Subscribe;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.systems.PlayerStamina;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public class InitPlayer {
    
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        PlayerStamina.init(event.getEntity());
    }

    @Subscribe
    public static void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
        PlayerStamina.unlink(event.getEntity());
    }
}
