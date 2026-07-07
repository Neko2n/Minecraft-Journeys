package com.nekotune.minecraftjourneys.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nekotune.minecraftjourneys.shared.ToolRequirementHandler;

/**
 * @see ToolRequirementHandler
 */
public class IncrementalMiningMixins {

    
    @Mixin(targets = "com.jdynamo.incrementalmining.events.MiningRules")
    public static class Rules {

        @Inject(method = "shouldUseVanillaBreaking", at = @At("HEAD"), cancellable = true)
        private static void modpack$useVanillaBreakingForWrongTool(
                BlockState state, ServerPlayer player, BlockPos pos, ItemStack tool,
                CallbackInfoReturnable<Boolean> cir) {
            if (!player.hasCorrectToolForDrops(state, player.level(), pos)) {
                cir.setReturnValue(true);
            }
        }
    }
    
    @Mixin(targets = "com.jdynamo.incrementalmining.events.MiningFeedback")
    public static class Feedback {

        private static final float WRONG_TOOL_PITCH = 1.5F;

        @ModifyArg(
                method = "sendImpactSounds",
                at = @At(
                        value = "INVOKE",
                        target = "Lcom/jdynamo/incrementalmining/events/MiningFeedback;sendSoundPacketToNearbyPlayers(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"
                ),
                index = 5
        )
        private static float modpack$boostPitchForWrongTool(float pitch,
                @Local(argsOnly = true) BlockPos pos,
                @Local(argsOnly = true) BlockState state,
                @Local(argsOnly = true) ServerPlayer player) {
            final boolean hasCorrectTool = player.hasCorrectToolForDrops(state, player.level(), pos);
            return hasCorrectTool ? pitch : pitch * WRONG_TOOL_PITCH;
        }
    }
}