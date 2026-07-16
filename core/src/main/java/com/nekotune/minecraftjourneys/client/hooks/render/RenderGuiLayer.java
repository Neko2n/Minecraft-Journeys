package com.nekotune.minecraftjourneys.client.hooks.render;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.client.gui.hud.MJExperienceHud;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;

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

    @SubscribeEvent
    public static void onRenderGuiLayerPost(RenderGuiLayerEvent.Post event) {
        final var resource = event.getName();
        final var graphics = event.getGuiGraphics();
        final var tick = event.getPartialTick();
        final var layer = event.getLayer();

        // Link custom experience bar to the vanilla experience bar
        if (resource.equals(MJExperienceHud.EXPERIENCE_BAR)) {
            NeoForge.EVENT_BUS.post(new RenderGuiLayerEvent.Post(
                    graphics, tick, VanillaGuiLayers.EXPERIENCE_BAR, layer));
        } else if (resource.equals(MJExperienceHud.EXPERIENCE_LEVEL)) {
            NeoForge.EVENT_BUS.post(new RenderGuiLayerEvent.Post(
                    graphics, tick, VanillaGuiLayers.EXPERIENCE_LEVEL, layer));
        }
    }
}
