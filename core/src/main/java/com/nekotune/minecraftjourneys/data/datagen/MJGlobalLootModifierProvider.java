package com.nekotune.minecraftjourneys.data.datagen;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.data.tags.MJBlockTags;
import com.nekotune.minecraftjourneys.data.tags.MJItemTags;
import com.nekotune.minecraftjourneys.shared.definition.loot.AddItemModifier;
import com.nekotune.minecraftjourneys.shared.definition.loot.MatchBlock;
import com.nekotune.minecraftjourneys.shared.registry.content.MJItems;

import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@DataGenProvider(dist = Dist.DEDICATED_SERVER)
public class MJGlobalLootModifierProvider extends GlobalLootModifierProvider {
    
    public MJGlobalLootModifierProvider(GatherDataEvent event, MJDatapackEntriesProvider datapackEntries) {
        super(event.getGenerator().getPackOutput(), datapackEntries.getRegistryProvider(), MinecraftJourneys.MOD_ID);
    }

    @Override
    protected void start() {

        // Add grass to grass plants (anything marked as DROPS_GRASS)
        final var dropsGrass = BlockPredicate.Builder.block().of(MJBlockTags.DROPS_GRASS);
        final var dropsGrassExtra = BlockPredicate.Builder.block().of(MJBlockTags.DROPS_GRASS_EXTRA);
        this.add("grass_to_grass_plants", new AddItemModifier(
                new LootItemCondition[]{
                    ExplosionCondition.survivesExplosion().build(),
                    MatchBlock.blockMatches(dropsGrass).build(),
                    LootItemRandomChanceCondition.randomChance(0.3f).build()
                },
                MJItems.Materials.GRASS.get()));
        this.add("grass_to_grass_plants_extra", new AddItemModifier(
                new LootItemCondition[]{
                    ExplosionCondition.survivesExplosion().build(),
                    MatchBlock.blockMatches(dropsGrassExtra).build(),
                    LootItemRandomChanceCondition.randomChance(0.6f).build()
                },
                MJItems.Materials.GRASS.get()));
        final var isKnife = ItemPredicate.Builder.item().of(MJItemTags.Equipment.KNIVES);
        this.add("grass_to_grass_plants_knife", new AddItemModifier(
                new LootItemCondition[]{
                    MatchTool.toolMatches(isKnife).build(),
                    MatchBlock.blockMatches(dropsGrass)
                            .or(MatchBlock.blockMatches(dropsGrassExtra))
                            .build()
                },
                MJItems.Materials.GRASS.get()));

    }
}
