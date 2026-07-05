package com.nekotune.minecraftjourneys.client;

import java.util.Collection;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.registry.content.MJBlocks;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.Holder;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = MinecraftJourneys.MODID, value = Dist.CLIENT)
public class BlockColorsHandler {
    
    @SubscribeEvent
    static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        final Collection<Block> blocks = MJBlocks.BLOCKS.getEntries()
                .stream().map(Holder::value).toList();
        blocks.forEach(block -> {
            final boolean isLeaves = block instanceof LeavesBlock;
            if (!isLeaves) return;
            event.register((state, level, pos, tintIndex) ->
                    level != null && pos != null
                            ? BiomeColors.getAverageFoliageColor(level, pos)
                            : FoliageColor.getDefaultColor(),
                    block);
        });
        
    }

    @SubscribeEvent
    static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        final Collection<Block> blocks = MJBlocks.BLOCKS.getEntries()
                .stream().map(Holder::value).toList();
        blocks.forEach(block -> {
            final boolean isLeaves = block instanceof LeavesBlock;
            if (!isLeaves) return;
            event.register((stack, tintIndex) ->
                    FoliageColor.getDefaultColor(),
                    block);
        });
    }
}
