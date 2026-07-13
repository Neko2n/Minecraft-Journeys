package com.nekotune.minecraftjourneys.data.datagen.loot;

import java.util.Collections;
import java.util.List;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@OnlyIn(value = Dist.DEDICATED_SERVER)
public class MJLootTableProvider extends LootTableProvider {

    public MJLootTableProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), Collections.emptySet(),
                fetchSubProviders(), event.getLookupProvider());
    }

    private static final List<SubProviderEntry> fetchSubProviders() {
        return List.of(
            new SubProviderEntry(provider -> new MJBlockLootSubProvider(provider),
                    LootContextParamSets.BLOCK),
            new SubProviderEntry(provider -> new MJEntityLootSubProvider(provider),
                    LootContextParamSets.ENTITY)
        );
    }
}
