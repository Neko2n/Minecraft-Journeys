package com.nekotune.minecraftjourneys.client.gui;

import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public interface IGuiLayer {

    /**
     * Register this GUI layer to the NeoForge RegisterGuiLayersEvent
     * @param event The event instance to register with
     */
    public abstract void register(RegisterGuiLayersEvent event);
}
