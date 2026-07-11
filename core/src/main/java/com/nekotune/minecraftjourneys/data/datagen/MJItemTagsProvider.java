package com.nekotune.minecraftjourneys.data.datagen;

import com.farcr.nomansland.common.registry.items.NMLItems;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.data.tags.MJItemTags;
import com.nekotune.minecraftjourneys.shared.registry.content.MJBlocks;
import com.nekotune.minecraftjourneys.shared.registry.content.MJItems;
import com.nekotune.minecraftjourneys.shared.registry.content.MJItems.Armors;

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
        tag(MJItemTags.LOYALTY_ENCHANTABLE)
            .addTag(ItemTags.TRIDENT_ENCHANTABLE)
            .addTag(MJItemTags.SPEAR_ENCHANTABLE);
        tag(MJItemTags.PIERCING_ENCHANTABLE)
            .addTag(ItemTags.CROSSBOW_ENCHANTABLE)
            .addTag(MJItemTags.SPEAR_ENCHANTABLE);

        // Equipment
        tag(MJItemTags.Equipment.SPEARS)
            .add(MJItems.Tools.WOODEN_SPEAR.get())
            .add(MJItems.Tools.INCENDIARY_SPEAR.get())
            .add(MJItems.Tools.STONE_SPEAR.get())
            .add(MJItems.Tools.FLINT_SPEAR.get())
            .add(MJItems.Tools.BONE_SPEAR.get())
            .add(MJItems.Tools.OBSIDIAN_SPEAR.get());
        tag(MJItemTags.Equipment.KNIVES)
            .add(MJItems.Tools.STONE_KNIFE.get())
            .add(MJItems.Tools.FLINT_KNIFE.get())
            .add(MJItems.Tools.BONE_KNIFE.get())
            .add(MJItems.Tools.OBSIDIAN_KNIFE.get());
        tag(MJItemTags.Equipment.MATTOCKS)
            .add(MJItems.Tools.STONE_MATTOCK.get())
            .add(MJItems.Tools.FLINT_MATTOCK.get())
            .add(MJItems.Tools.BONE_MATTOCK.get())
            .add(MJItems.Tools.OBSIDIAN_MATTOCK.get());
        tag(ItemTags.DYEABLE)
            .add(Armors.CLOTH_HELMET.get())
            .add(Armors.CLOTH_CHESTPLATE.get())
            .add(Armors.CLOTH_LEGGINGS.get())
            .add(Armors.CLOTH_BOOTS.get());

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

        // General materials
        tag(ItemTags.HORSE_TEMPT_ITEMS)
            .add(MJItems.Materials.GRASS.get());
        tag(ItemTags.SHEEP_FOOD)
            .add(MJItems.Materials.GRASS.get());
        tag(ItemTags.COW_FOOD)
            .add(MJItems.Materials.GRASS.get());
    }
}
