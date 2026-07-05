package com.nekotune.minecraftjourneys.datagen;

import com.farcr.nomansland.common.registry.NMLTags;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.shared.registry.tags.MJBiomeTags;

import net.hecco.bountifulfares.registry.tags.BFBiomeTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@DataGenProvider(dist = Dist.DEDICATED_SERVER)
public class MJBiomeTagsProvider extends BiomeTagsProvider {

    public MJBiomeTagsProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(),
                event.getLookupProvider(),
                MinecraftJourneys.MOD_ID,
                event.getExistingFileHelper());
    }

    @Override
    protected void addTags(Provider provider) {

        tag(MJBiomeTags.HAS_PEAR_TREES).addTag(Tags.Biomes.IS_CONIFEROUS_TREE);
        tag(BFBiomeTags.HAS_APPLE_TREES).addTag(Tags.Biomes.IS_DECIDUOUS_TREE);
    }
}
