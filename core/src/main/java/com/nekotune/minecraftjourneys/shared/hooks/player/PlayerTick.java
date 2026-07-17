package com.nekotune.minecraftjourneys.shared.hooks.player;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.systems.PlayerStamina;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public final class PlayerTick {
    
    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Post event) {
        final Player player = event.getEntity();
        PlayerStamina.get(player).tick();
    } 
}
