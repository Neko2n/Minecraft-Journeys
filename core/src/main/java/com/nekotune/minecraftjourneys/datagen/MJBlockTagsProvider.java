package com.nekotune.minecraftjourneys.datagen;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.shared.registry.content.MJBlocks;
import com.nekotune.minecraftjourneys.shared.registry.tags.MJBlockTags;

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
                MinecraftJourneys.MOD_ID,
                event.getExistingFileHelper());
    }

    @Override
    protected void addTags(Provider provider) {

        // Mining type tags
        tag(MJBlockTags.MINEABLE_WITH_MULTITOOL)
            .addTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(BlockTags.MINEABLE_WITH_AXE)
            .addTag(BlockTags.MINEABLE_WITH_SHOVEL);
        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(MJBlocks.BFPearBlocks.HANGING_PEAR.get())
            .add(MJBlocks.BFPearBlocks.PEAR_BLOCK.get());

        // Wood tags
        tag(BlockTags.LOGS_THAT_BURN)
            .add(MJBlocks.BFPearBlocks.PEAR_LOG.get())
            .add(MJBlocks.BFPearBlocks.PEAR_WOOD.get())
            .add(MJBlocks.BFPearBlocks.STRIPPED_PEAR_LOG.get())
            .add(MJBlocks.BFPearBlocks.STRIPPED_PEAR_WOOD.get());

        // Bountiful fares tags
        tag(BFBlockTags.HANGING_FRUIT)
            .add(MJBlocks.BFPearBlocks.HANGING_PEAR.get());
        tag(MJBlockTags.PEAR_LEAVES)
            .add(MJBlocks.BFPearBlocks.PEAR_LEAVES.get());
            // .add(MJBlocks.BFPearBlocks.FLOWERING_PEAR_LEAVES.get());
        
        /* Block tags which derive from other tags */
        tag(BlockTags.LEAVES).addTag(MJBlockTags.PEAR_LEAVES);
    }
}
