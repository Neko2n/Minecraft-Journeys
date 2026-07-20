package com.nekotune.minecraftjourneys.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nekotune.minecraftjourneys.shared.systems.stamina.PlayerStamina;

import net.minecraft.client.player.LocalPlayer;

@Mixin(targets = "net.minecraft.client.player.LocalPlayer")
public class LocalPlayerMixins {
    
    @Inject(method = "hasEnoughFoodToStartSprinting", at = @At("HEAD"), cancellable = true)
    private void modpack$hasEnoughFoodToStartSprinting(CallbackInfoReturnable<Boolean> ci) {
        final LocalPlayer player = (LocalPlayer) (Object) this;
        final PlayerStamina stamina = PlayerStamina.get(player);
        ci.setReturnValue(player.isPassenger() || player.mayFly() || stamina.getValue() > 0);
    }
}
