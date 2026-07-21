package com.nekotune.minecraftjourneys.mixin.incrementalmining;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.jdynamo.incrementalmining.mining.MiningTargets;
import com.nekotune.minecraftjourneys.shared.definitions.item.tool.KnifeItem;
import com.nekotune.minecraftjourneys.shared.logic.ToolRequirements;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @see ToolRequirements
 */
@Mixin(targets = "com.jdynamo.incrementalmining.events.MiningRules")
public class RulesMixin {

    @Inject(method = "shouldUseVanillaBreaking", at = @At("HEAD"), cancellable = true)
    private static void modpack$shouldUseVanillaBreaking(
            BlockState state, ServerPlayer player, BlockPos pos, ItemStack tool,
            CallbackInfoReturnable<Boolean> cir) {
        
        // Use vanilla breaking for wrong tools to allow for disabling the event
        if (!player.hasCorrectToolForDrops(state, player.level(), pos)) {
            cir.setReturnValue(true);
            return;
        }

        // Knives use vanilla breaking for foliage to break things faster
        if (tool.getItem() instanceof KnifeItem
                && MiningTargets.usesVanillaFoliageBreaking(state, tool)) {
            cir.setReturnValue(true);
        }
    }
}