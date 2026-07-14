package com.nekotune.minecraftjourneys.data.datagen.tags;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.data.tags.MJItemTags;
import com.nekotune.minecraftjourneys.shared.registries.content.MJItems;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.data.event.GatherDataEvent;

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
            .addTag(MJItemTags.Tools.SPEARS);
        tag(MJItemTags.KNIFE_ENCHANTABLE)
            .addTag(MJItemTags.Tools.KNIVES);
        tag(MJItemTags.MATTOCK_ENCHANTABLE)
            .addTag(MJItemTags.Tools.MATTOCKS);
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

        // Tools
        tag(MJItemTags.Tools.SPEARS)
            .add(MJItems.WOODEN_SPEAR.get())
            .add(MJItems.INCENDIARY_SPEAR.get())
            .add(MJItems.STONE_SPEAR.get())
            .add(MJItems.FLINT_SPEAR.get())
            .add(MJItems.BONE_SPEAR.get())
            .add(MJItems.OBSIDIAN_SPEAR.get());
        tag(MJItemTags.Tools.KNIVES)
            .add(MJItems.STONE_KNIFE.get())
            .add(MJItems.FLINT_KNIFE.get())
            .add(MJItems.BONE_KNIFE.get())
            .add(MJItems.OBSIDIAN_KNIFE.get());
        tag(MJItemTags.Tools.MATTOCKS)
            .add(MJItems.STONE_MATTOCK.get())
            .add(MJItems.FLINT_MATTOCK.get())
            .add(MJItems.BONE_MATTOCK.get())
            .add(MJItems.OBSIDIAN_MATTOCK.get());
        
        // Armor
        tag(MJItemTags.Armors.CLOTH)
            .add(MJItems.CLOTH_HELMET.get())
            .add(MJItems.CLOTH_CHESTPLATE.get())
            .add(MJItems.CLOTH_LEGGINGS.get())
            .add(MJItems.CLOTH_BOOTS.get());

        // Vanilla Tags
        tag(ItemTags.DYEABLE)
            .addTag(MJItemTags.Armors.CLOTH);
        tag(ItemTags.HORSE_TEMPT_ITEMS)
            .add(MJItems.GRASS.get());
        tag(ItemTags.SHEEP_FOOD)
            .add(MJItems.GRASS.get());
        tag(ItemTags.COW_FOOD)
            .add(MJItems.GRASS.get());
    }
}
