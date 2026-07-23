package com.nekotune.minecraftjourneys.client;

import com.nekotune.minecraftjourneys.shared.logic.stamina.GUIAnimationProperties;
import com.nekotune.minecraftjourneys.shared.logic.stamina.PlayerStamina;

import foundry.imgui.neoforge.api.event.RenderImGuiEventsNeoforge;
import imgui.ImGui;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@OnlyIn(value = Dist.CLIENT)
public final class MJImGui {
    private static boolean playerLoaded = false;
    private static final class ImVariables {
        public static final ImFloat animLength = new ImFloat(1f);
        public static final ImInt maxStamina = new ImInt(1);
    }

    @SubscribeEvent
    public static void onRenderImGui(RenderImGuiEventsNeoforge.Pre event) {
        final var minecraft = Minecraft.getInstance();

        if (playerLoaded) {
            
            if (ImGui.begin("Stamina")) {
                final PlayerStamina stamina = PlayerStamina.get(minecraft.player);
                ImGui.inputFloat("Tween Length", ImVariables.animLength);
                ImGui.text("Add");
                final var guiAnimation = new GUIAnimationProperties.Builder()
                        .length(ImVariables.animLength.get())
                        .build();
                if (ImGui.arrowButton("stamina_add", 1)) {
                    stamina.setValue(stamina.getValue() + 1f, guiAnimation);
                } else if (ImGui.arrowButton("stamina_sub", 3)) {
                    stamina.setValue(stamina.getValue() - 1f, guiAnimation);
                }
                if (ImGui.inputInt("Max Stamina", ImVariables.maxStamina)) {
                    stamina.setMaxValue(ImVariables.maxStamina.get());
                } else {
                    ImVariables.maxStamina.set(stamina.getMaxValue());
                }
                ImGui.text("Delta: " + String.format("%.6f", stamina.getDeltaStamina()));
            }
            ImGui.end();
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(final EntityJoinLevelEvent event) {
        if (event.getEntity().is(Minecraft.getInstance().player)) {
            playerLoaded = true;
        }
    }

    @SubscribeEvent
    public static void onClientLoggingOut(final ClientPlayerNetworkEvent.LoggingOut event) {
        playerLoaded = false;
    }
}