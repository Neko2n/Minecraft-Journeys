package com.nekotune.minecraftjourneys.datagen;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.registry.content.MJBlocks;

import net.hecco.bountifulfares.registry.tags.BFItemTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@DataGenProvider(dist = Dist.DEDICATED_SERVER)
public class MJItemTagsProvider extends ItemTagsProvider {

    public MJItemTagsProvider(GatherDataEvent event, MJBlockTagsProvider blockTags) {
        super(event.getGenerator().getPackOutput(),
                event.getLookupProvider(),
                blockTags.contentsGetter(),
                MinecraftJourneys.MODID,
                event.getExistingFileHelper());
    }

    @Override
    protected void addTags(Provider provider) {

        // Bountiful fares tags
        tag(BFItemTags.FRUIT_LOGS)
            .add(MJBlocks.BFPearBlocks.PEAR_LOG.get().asItem())
            .add(MJBlocks.BFPearBlocks.PEAR_WOOD.get().asItem())
            .add(MJBlocks.BFPearBlocks.STRIPPED_PEAR_LOG.get().asItem())
            .add(MJBlocks.BFPearBlocks.STRIPPED_PEAR_WOOD.get().asItem());
    }
}
