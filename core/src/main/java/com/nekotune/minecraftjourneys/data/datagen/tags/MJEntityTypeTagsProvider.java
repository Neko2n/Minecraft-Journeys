package com.nekotune.minecraftjourneys.data.datagen.tags;

import com.farcr.nomansland.common.registry.entities.NMLEntities;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.data.tags.MJEntityTags;
import com.ninni.spawn.registry.SpawnEntityType;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class MJEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public MJEntityTypeTagsProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(),
                event.getLookupProvider(),
                MinecraftJourneys.MOD_ID,
                event.getExistingFileHelper());
    }

    @Override
    protected void addTags(Provider provider) {
        
        tag(MJEntityTags.DROPS_PELT)
            .add(EntityType.RABBIT)
            .add(EntityType.FOX)
            .add(EntityType.WOLF)
            .add(SpawnEntityType.SEAL.get())
            .add(NMLEntities.DEER.get());
            // Add bewearagers from Species
    }
}
