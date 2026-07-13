package com.nekotune.minecraftjourneys.data.datagen.tags;

import com.farcr.nomansland.common.registry.NMLTags;
import com.farcr.nomansland.common.registry.worldgen.NMLBiomes;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.data.tags.MJBiomeTags;

import net.hecco.bountifulfares.registry.tags.BFBiomeTags;
import net.hibiscus.naturespirit.registration.NSBiomes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@OnlyIn(value = Dist.DEDICATED_SERVER)
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
            .add(NMLBiomes.OLD_GROWTH_FOREST_EDGE)
            .add(NSBiomes.MAPLE_WOODLANDS);
        
        // No man's land compatibility
        tag(NMLTags.FeatureAddition.HAS_PEBBLES)
            .add(NSBiomes.REDWOOD_FOREST);
        
        // Bountiful fares compatibility
        tag(BFBiomeTags.HAS_APPLE_TREES)
            .add(NMLBiomes.OLD_GROWTH_FOREST)
            .add(NMLBiomes.OLD_GROWTH_FOREST_CLEARING)
            .add(NMLBiomes.OLD_GROWTH_FOREST_EDGE);
        tag(BFBiomeTags.HAS_ORANGE_TREES)
            .add(NSBiomes.SUGI_FOREST)
            .add(NSBiomes.WINDSWEPT_SUGI_FOREST)
            .add(NSBiomes.BLOOMING_SUGI_FOREST);
        tag(BFBiomeTags.HAS_PLUM_TREES)
            .add(NSBiomes.SUGI_FOREST)
            .add(NSBiomes.WINDSWEPT_SUGI_FOREST)
            .add(NSBiomes.BLOOMING_SUGI_FOREST)
            .add(NSBiomes.WISTERIA_FOREST);
        tag(BFBiomeTags.HAS_WILD_WHEAT)
            .add(NMLBiomes.PRAIRIE)
            .add(NMLBiomes.AUTUMNAL_FOREST)
            .add(NMLBiomes.BOREAL_FOREST)
            .add(NMLBiomes.MAPLE_FOREST)
            .add(NMLBiomes.MAPLE_GROVE)
            .add(NMLBiomes.OLD_GROWTH_FOREST)
            .add(NMLBiomes.OLD_GROWTH_FOREST_CLEARING)
            .add(NMLBiomes.OLD_GROWTH_FOREST_EDGE)
            .add(NMLBiomes.DESERT_RIVER)
            .add(NSBiomes.FLOWERING_SHRUBLAND);
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
            .add(NMLBiomes.OLD_GROWTH_FOREST_EDGE)
            .add(NSBiomes.WISTERIA_FOREST);
        tag(BFBiomeTags.HAS_WILD_LEEKS)
            .add(NMLBiomes.BLACKWATER_RIVER)
            .add(NMLBiomes.LUSH_RIVER);
        tag(BFBiomeTags.HAS_WILD_MAIZE)
            .add(NMLBiomes.LUSH_RIVER);
        tag(BFBiomeTags.HAS_LARGE_WILD_WHEAT)
            .add(NSBiomes.OAK_SAVANNA)
            .add(NSBiomes.SHRUBLAND);
        tag(BFBiomeTags.HAS_LARGE_WILD_CARROTS)
            .add(NMLBiomes.PRAIRIE);
        tag(BFBiomeTags.HAS_CHAMOMILE)
            .add(NMLBiomes.PRAIRIE)
            .add(NSBiomes.BLOOMING_SUGI_FOREST)
            .add(NSBiomes.BLOOMING_HIGHLANDS)
            .add(NSBiomes.WISTERIA_FOREST)
            .add(NSBiomes.FLOWERING_SHRUBLAND);
        tag(BFBiomeTags.HAS_HONEYSUCKLE)
            .add(NMLBiomes.OLD_GROWTH_FOREST)
            .add(NMLBiomes.OLD_GROWTH_FOREST_CLEARING)
            .add(NMLBiomes.OLD_GROWTH_FOREST_EDGE)
            .add(NSBiomes.BLOOMING_SUGI_FOREST)
            .add(NSBiomes.WISTERIA_FOREST);
        tag(BFBiomeTags.HAS_VIOLET_BELLFLOWER)
            .add(NMLBiomes.DARK_TAIGA)
            .add(NSBiomes.ASPEN_FOREST)
            .add(NSBiomes.BLOOMING_SUGI_FOREST)
            .add(NSBiomes.WISTERIA_FOREST);
    }
}
