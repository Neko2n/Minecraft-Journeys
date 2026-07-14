package com.nekotune.minecraftjourneys.data.datagen.loot.modifier;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.data.datagen.MJDatapackEntriesProvider;
import com.nekotune.minecraftjourneys.data.tags.MJBlockTags;
import com.nekotune.minecraftjourneys.data.tags.MJEntityTags;
import com.nekotune.minecraftjourneys.data.tags.MJItemTags;
import com.nekotune.minecraftjourneys.shared.definitions.loot.condition.MatchBlock;
import com.nekotune.minecraftjourneys.shared.definitions.loot.condition.MatchEntityType;
import com.nekotune.minecraftjourneys.shared.definitions.loot.modifier.AddItemModifier;
import com.nekotune.minecraftjourneys.shared.definitions.loot.modifier.RemoveItemModifier;
import com.nekotune.minecraftjourneys.shared.registries.content.MJItems;

import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

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
                MJItems.GRASS.get()));
        this.add("grass_to_grass_plants_extra", new AddItemModifier(
                new LootItemCondition[]{
                    ExplosionCondition.survivesExplosion().build(),
                    MatchBlock.blockMatches(dropsGrassExtra).build(),
                    LootItemRandomChanceCondition.randomChance(0.6f).build()
                },
                MJItems.GRASS.get()));
        final var isKnife = ItemPredicate.Builder.item().of(MJItemTags.Tools.KNIVES);
        this.add("grass_to_grass_plants_knife", new AddItemModifier(
                new LootItemCondition[]{
                    MatchTool.toolMatches(isKnife).build(),
                    MatchBlock.blockMatches(dropsGrass)
                            .or(MatchBlock.blockMatches(dropsGrassExtra))
                            .build()
                },
                MJItems.GRASS.get()));

        // Remove default rabbit hide drops to avoid overlap
        // Order-sensitive
        this.add("remove_vanilla_rabbit_hide", new RemoveItemModifier(
                new LootItemCondition[]{
                    MatchEntityType.any().build()
                },
                Items.RABBIT_HIDE));
        // Add pelt (rabbit hide) to all furry animals
        final var dropsPelt = EntityTypePredicate.of(MJEntityTags.DROPS_PELT);
        this.add("pelt_to_pelt_entities", new AddItemModifier(
                new LootItemCondition[]{
                    MatchEntityType.entityTypeMatches(dropsPelt).build()
                },
                Items.RABBIT_HIDE,
                0, 2));
        // Drop a guaranteed extra pelt when killed by a knife
        this.add("pelt_to_pelt_entities_knife", new AddItemModifier(
                new LootItemCondition[]{
                    MatchTool.toolMatches(isKnife).build(),
                    MatchEntityType.entityTypeMatches(dropsPelt).build()
                },
                Items.RABBIT_HIDE));
    }
}
