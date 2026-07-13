package com.nekotune.minecraftjourneys.client.render.entity;

import com.nekotune.minecraftjourneys.shared.registries.content.MJEntityTypes;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT)
@OnlyIn(value = Dist.CLIENT)
public class EntityRenderersHandler {

    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MJEntityTypes.THROWN_SPEAR.get(), ThrownSpearRenderer::new);
    }
}