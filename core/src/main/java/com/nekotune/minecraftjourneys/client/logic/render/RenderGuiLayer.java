package com.nekotune.minecraftjourneys.client.logic.render;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public class RenderGuiLayer {
    private static final Set<ResourceLocation> DISABLED = new HashSet<ResourceLocation>(List.of(
        VanillaGuiLayers.EXPERIENCE_LEVEL,
        VanillaGuiLayers.EXPERIENCE_BAR
    ));

    @SubscribeEvent
    public static void onRenderGuiLayerPre(RenderGuiLayerEvent.Pre event) {
        final ResourceLocation resource = event.getName();
        if (DISABLED.contains(resource)) {
            event.setCanceled(true);
            return;
        }
    }
}
