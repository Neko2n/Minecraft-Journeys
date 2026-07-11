package com.nekotune.minecraftjourneys.shared.registry.content;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.registry.tags.MJItemTags;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;

public class MJEnchantments {
    // Migrated to vanilla's minecraft:piercing enchantment (see data/minecraft/enchantment/piercing.json).
    // Left here for reference in case a similar custom enchantment is needed later.
    // public static final ResourceKey<Enchantment> PIERCING = createKey("piercing");

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        // var enchantments = context.lookup(Registries.ENCHANTMENT);
        // var items = context.lookup(Registries.ITEM);

        // register(context, PIERCING, Enchantment.enchantment(Enchantment.definition(
        //     items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
        //     items.getOrThrow(MJItemTags.SPEAR_ENCHANTABLE),
        //     5,
        //     2,
        //     Enchantment.dynamicCost(5, 8),
        //     Enchantment.dynamicCost(25, 8),
        //     2,
        //     EquipmentSlotGroup.MAINHAND))
        //     .withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER,
        //             EnchantmentTarget.VICTIM, new PiercingEnchantmentEffect()));
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }

    private static ResourceKey<Enchantment> createKey(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, name));
    }
}
