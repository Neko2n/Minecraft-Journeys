package com.nekotune.minecraftjourneys.datagen;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@DataGenProvider(dist = Dist.DEDICATED_SERVER)
public class MJEnchantmentTagsProvider extends EnchantmentTagsProvider {

    public MJEnchantmentTagsProvider(GatherDataEvent event, MJDatapackEntriesProvider datapackEntries) {
        super(event.getGenerator().getPackOutput(), datapackEntries.getRegistryProvider(),
                MinecraftJourneys.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(Provider provider) {

        // tag(EnchantmentTags.IN_ENCHANTING_TABLE)
        //     .add(MJEnchantments.PIERCING);
    }
    
}
