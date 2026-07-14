package com.nekotune.minecraftjourneys.data.datagen.tags;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class MJBiomeTagsProvider extends BiomeTagsProvider {

    public MJBiomeTagsProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(),
                event.getLookupProvider(),
                MinecraftJourneys.MOD_ID,
                event.getExistingFileHelper());
    }

    @Override
    protected void addTags(Provider provider) {}
}
