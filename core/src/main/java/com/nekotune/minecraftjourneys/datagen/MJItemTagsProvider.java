package com.nekotune.minecraftjourneys.datagen;

import com.farcr.nomansland.common.registry.items.NMLItems;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.shared.registry.content.MJBlocks;
import com.nekotune.minecraftjourneys.shared.registry.content.MJItems;
import com.nekotune.minecraftjourneys.shared.registry.tags.MJItemTags;

import net.hecco.bountifulfares.registry.tags.BFItemTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@DataGenProvider(dist = Dist.DEDICATED_SERVER)
public class MJItemTagsProvider extends ItemTagsProvider {

    public MJItemTagsProvider(GatherDataEvent event, MJBlockTagsProvider blockTags) {
        super(event.getGenerator().getPackOutput(),
                event.getLookupProvider(),
                blockTags.contentsGetter(),
                MinecraftJourneys.MOD_ID,
                event.getExistingFileHelper());
    }

    @Override
    protected void addTags(Provider provider) {
 
        // Enchants
        tag(MJItemTags.SPEAR_ENCHANTABLE)
            .addTag(MJItemTags.Equipment.SPEARS);
        tag(MJItemTags.KNIFE_ENCHANTABLE)
            .addTag(MJItemTags.Equipment.KNIVES);
        tag(MJItemTags.MATTOCK_ENCHANTABLE)
            .addTag(MJItemTags.Equipment.MATTOCKS);
        tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
            .addTag(MJItemTags.KNIFE_ENCHANTABLE);
        tag(ItemTags.MINING_ENCHANTABLE)
            .addTag(MJItemTags.MATTOCK_ENCHANTABLE);
        tag(ItemTags.DURABILITY_ENCHANTABLE)
            .addTag(MJItemTags.SPEAR_ENCHANTABLE)
            .addTag(MJItemTags.KNIFE_ENCHANTABLE)
            .addTag(MJItemTags.MATTOCK_ENCHANTABLE);
        tag(ItemTags.FIRE_ASPECT_ENCHANTABLE)
            .addTag(MJItemTags.SPEAR_ENCHANTABLE);

        // Equipment
        tag(MJItemTags.Equipment.SPEARS)
            .add(MJItems.Equipment.WOODEN_SPEAR.get())
            .add(MJItems.Equipment.STONE_SPEAR.get())
            .add(MJItems.Equipment.FLINT_SPEAR.get())
            .add(MJItems.Equipment.BONE_SPEAR.get())
            .add(MJItems.Equipment.OBSIDIAN_SPEAR.get());
        tag(MJItemTags.Equipment.KNIVES)
            .add(MJItems.Equipment.STONE_KNIFE.get())
            .add(MJItems.Equipment.FLINT_KNIFE.get())
            .add(MJItems.Equipment.BONE_KNIFE.get())
            .add(MJItems.Equipment.OBSIDIAN_KNIFE.get());
        tag(MJItemTags.Equipment.MATTOCKS)
            .add(MJItems.Equipment.STONE_MATTOCK.get())
            .add(MJItems.Equipment.FLINT_MATTOCK.get())
            .add(MJItems.Equipment.BONE_MATTOCK.get())
            .add(MJItems.Equipment.OBSIDIAN_MATTOCK.get());
        tag(ItemTags.DYEABLE)
            .add(MJItems.Equipment.CLOTH_HELMET.get())
            .add(MJItems.Equipment.CLOTH_CHESTPLATE.get())
            .add(MJItems.Equipment.CLOTH_LEGGINGS.get())
            .add(MJItems.Equipment.CLOTH_BOOTS.get());

        // Bountiful fares compatibility
        tag(MJItemTags.PEAR_LOGS)
            .add(MJBlocks.BFPearBlocks.PEAR_LOG.asItem())
            .add(MJBlocks.BFPearBlocks.PEAR_WOOD.asItem())
            .add(MJBlocks.BFPearBlocks.STRIPPED_PEAR_LOG.asItem())
            .add(MJBlocks.BFPearBlocks.STRIPPED_PEAR_WOOD.asItem());
        tag(BFItemTags.FRUIT_LOGS).addTag(MJItemTags.PEAR_LOGS);
        tag(ItemTags.LOGS_THAT_BURN).addTag(MJItemTags.PEAR_LOGS);
        tag(MJItemTags.PEAR_LEAVES)
            .add(MJBlocks.BFPearBlocks.PEAR_LEAVES.asItem());
            // .add(MJBlocks.BFPearBlocks.FLOWERING_PEAR_LEAVES.asItem());
        tag(ItemTags.LEAVES).addTag(MJItemTags.PEAR_LEAVES);
        tag(BFItemTags.C_FRUIT)
            .add(NMLItems.PEAR.get());
    }
}
