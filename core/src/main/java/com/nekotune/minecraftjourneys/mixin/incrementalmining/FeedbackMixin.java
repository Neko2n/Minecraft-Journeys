package com.nekotune.minecraftjourneys.mixin.incrementalmining;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @see ToolRequirements
 */
@Mixin(targets = "com.jdynamo.incrementalmining.events.MiningFeedback")
public class FeedbackMixin {

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