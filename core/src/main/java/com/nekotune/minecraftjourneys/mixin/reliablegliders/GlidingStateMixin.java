package com.nekotune.minecraftjourneys.mixin.reliablegliders;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.evandev.reliable_gliders.api.GlidingState;
import com.nekotune.minecraftjourneys.shared.logic.stamina.PlayerStamina;

import net.minecraft.world.entity.player.Player;

/**
 * @see GlidingState
 */
@Mixin(targets = "com.evandev.reliable_gliders.api.GlidingState")
public class GlidingStateMixin {
    
    // Cannot glide without stamina
	@Inject(method = "isGliding", at = @At("HEAD"), cancellable = true)
    public static void modpack$isGliding(Player player, CallbackInfoReturnable<Boolean> cir) {
        final var stamina = PlayerStamina.get(player);
        if (stamina.isEmpty()) {
            GlidingState.setGliding(player, false);
            cir.setReturnValue(false);
        }
    }
}
