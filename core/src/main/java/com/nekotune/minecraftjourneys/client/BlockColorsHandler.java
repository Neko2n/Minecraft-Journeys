package com.nekotune.minecraftjourneys.client;

import java.util.Collection;
import java.util.HashSet;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.registry.content.MJBlocks;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.Holder;
import net.minecraft.world.level.FoliageColor;
import net.hecco.bountifulfares.definition.block.custom.FruitLogBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID, value = Dist.CLIENT)
@OnlyIn(value = Dist.CLIENT)
public class BlockColorsHandler {

    private static final HashSet<Block> BLACKLIST = new HashSet<>();

    /**
     * Prevents certain blocks from being automatically given colors.
     * @param block The block to verify against.
     * @return True if the given block is blacklisted, false otherwise.
     */
    private static boolean isBlacklisted(Block block) {
        if (BLACKLIST.isEmpty()) {
            BLACKLIST.add(MJBlocks.BFPearBlocks.PEAR_LEAVES.get());
        }
        return BLACKLIST.contains(block);
    }

    /**
     * Determines what kinds of blocks should automatically be given colors.
     * @param block The block to verify against.
     * @return True if the given block should be colored, false otherwise.
     */
    private static boolean shouldColor(Block block) {
        if (isBlacklisted(block)) return false;
        if (block instanceof LeavesBlock) return true;
        if (block instanceof FruitLogBlock) return true;
        return false;
    }
    
    @SubscribeEvent
    static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        final Collection<Block> blocks = MJBlocks.BLOCKS.getEntries()
                .stream().map(Holder::value).toList();
        blocks.forEach(block -> {
            if (!shouldColor(block)) return;
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
            if (!shouldColor(block)) return;
            event.register((stack, tintIndex) ->
                    FoliageColor.getDefaultColor(),
                    block);
        });
    }
}
