package com.nekotune.minecraftjourneys.datagen;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.registry.content.MJBlocks;
import com.nekotune.minecraftjourneys.registry.tags.MJBlockTags;

import net.hecco.bountifulfares.registry.tags.BFBlockTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.BlockTags;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@DataGenProvider(dist = Dist.DEDICATED_SERVER)
public class MJBlockTagsProvider extends BlockTagsProvider {

    public MJBlockTagsProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(),
                event.getLookupProvider(),
                MinecraftJourneys.MODID,
                event.getExistingFileHelper());
    }

    @Override
    protected void addTags(Provider provider) {

        // Mining type tags
        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(MJBlocks.BFPearBlocks.HANGING_PEAR.get())
            .add(MJBlocks.BFPearBlocks.PEAR_BLOCK.get())
            .add(MJBlocks.BFPearBlocks.PEAR_LOG.get())
            .add(MJBlocks.BFPearBlocks.PEAR_WOOD.get())
            .add(MJBlocks.BFPearBlocks.STRIPPED_PEAR_LOG.get())
            .add(MJBlocks.BFPearBlocks.STRIPPED_PEAR_WOOD.get());

        // Bountiful fares tags
        tag(BFBlockTags.HANGING_FRUIT)
            .add(MJBlocks.BFPearBlocks.HANGING_PEAR.get());
        tag(MJBlockTags.PEAR_LEAVES)
            .add(MJBlocks.BFPearBlocks.PEAR_LEAVES.get())
            .add(MJBlocks.BFPearBlocks.FLOWERING_PEAR_LEAVES.get());
    }
}
