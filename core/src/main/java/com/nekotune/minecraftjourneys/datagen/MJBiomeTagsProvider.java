package com.nekotune.minecraftjourneys.datagen;

import com.farcr.nomansland.common.registry.worldgen.NMLBiomes;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.shared.registry.tags.MJBiomeTags;

import net.hecco.bountifulfares.registry.tags.BFBiomeTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.neoforged.api.distmarker.Dist;
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

        // Custom tags
        tag(MJBiomeTags.HAS_PEAR_TREES)
            .add(NMLBiomes.AUTUMNAL_FOREST)
            .add(NMLBiomes.BOREAL_FOREST)
            .add(NMLBiomes.MAPLE_FOREST)
            .add(NMLBiomes.MAPLE_GROVE)
            .add(NMLBiomes.OLD_GROWTH_FOREST)
            .add(NMLBiomes.OLD_GROWTH_FOREST_CLEARING)
            .add(NMLBiomes.OLD_GROWTH_FOREST_EDGE);
        
        // Bountiful fares compatibility
        tag(BFBiomeTags.HAS_APPLE_TREES)
            .add(NMLBiomes.OLD_GROWTH_FOREST)
            .add(NMLBiomes.OLD_GROWTH_FOREST_CLEARING)
            .add(NMLBiomes.OLD_GROWTH_FOREST_EDGE);
        tag(BFBiomeTags.HAS_WILD_WHEAT)
            .add(NMLBiomes.PRAIRIE)
            .add(NMLBiomes.AUTUMNAL_FOREST)
            .add(NMLBiomes.BOREAL_FOREST)
            .add(NMLBiomes.MAPLE_FOREST)
            .add(NMLBiomes.MAPLE_GROVE)
            .add(NMLBiomes.OLD_GROWTH_FOREST)
            .add(NMLBiomes.OLD_GROWTH_FOREST_CLEARING)
            .add(NMLBiomes.OLD_GROWTH_FOREST_EDGE)
            .add(NMLBiomes.DESERT_RIVER);
        tag(BFBiomeTags.HAS_WILD_BEETROOT)
            .add(NMLBiomes.PRAIRIE)
            .add(NMLBiomes.OLD_GROWTH_FOREST)
            .add(NMLBiomes.OLD_GROWTH_FOREST_CLEARING)
            .add(NMLBiomes.OLD_GROWTH_FOREST_EDGE);
        tag(BFBiomeTags.HAS_WILD_POTATOES)
            .add(NMLBiomes.PRAIRIE)
            .add(NMLBiomes.AUTUMNAL_FOREST)
            .add(NMLBiomes.BOREAL_FOREST)
            .add(NMLBiomes.MAPLE_FOREST)
            .add(NMLBiomes.MAPLE_GROVE)
            .add(NMLBiomes.OLD_GROWTH_FOREST)
            .add(NMLBiomes.OLD_GROWTH_FOREST_CLEARING)
            .add(NMLBiomes.OLD_GROWTH_FOREST_EDGE);
        tag(BFBiomeTags.HAS_WILD_MAIZE)
            .add(NMLBiomes.BAYOU);
        tag(BFBiomeTags.HAS_WILD_LEEKS)
            .add(NMLBiomes.BLACKWATER_RIVER);
        tag(BFBiomeTags.HAS_LARGE_WILD_CARROTS)
            .add(NMLBiomes.PRAIRIE);
        tag(BFBiomeTags.HAS_LARGE_WILD_LEEKS)
            .add(NMLBiomes.LUSH_RIVER);
        tag(BFBiomeTags.HAS_CHAMOMILE)
            .add(NMLBiomes.PRAIRIE);
        tag(BFBiomeTags.HAS_HONEYSUCKLE)
            .add(NMLBiomes.OLD_GROWTH_FOREST)
            .add(NMLBiomes.OLD_GROWTH_FOREST_CLEARING)
            .add(NMLBiomes.OLD_GROWTH_FOREST_EDGE);
        tag(BFBiomeTags.HAS_VIOLET_BELLFLOWER)
            .add(NMLBiomes.DARK_TAIGA);
    }
}
