package com.nekotune.minecraftjourneys.client.hooks.register;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.client.gui.hud.MJExperienceHud;
import com.nekotune.minecraftjourneys.client.gui.hud.MJStaminaHud;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID, value = Dist.CLIENT)
@OnlyIn(value = Dist.CLIENT)
public final class RegisterGuiLayers {

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        new MJStaminaHud().register(event);
        new MJExperienceHud().register(event);
    }
}
