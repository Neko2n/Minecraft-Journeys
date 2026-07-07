package com.nekotune.minecraftjourneys.shared.registry.misc;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

import com.davigj.wholecloth.core.registry.WCItems;
import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class MJArmorMaterials {

    /**
     * Default tint for undyed cloth armor, matching Whole Cloth's cloth scrap item texture.
     */
    public static final int CLOTH_COLOR = 0xFFA5BFC3;

    public static final Holder<ArmorMaterial> CLOTH = register(
            "cloth", () -> new ArmorMaterial(
                    armorValues(1, 3, 2, 1),
                    15,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(WCItems.CLOTH_SCRAP.get()),
                    List.of(
                        new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, "cloth"), "", true),
                        new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, "cloth"), "_overlay", true)
                    ),
                    0.0F,
                    0.0F));

    private static EnumMap<ArmorItem.Type, Integer> armorValues(final int helmet, final int chestplate, final int leggings, final int boots) {
        final EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.BOOTS, Integer.valueOf(boots));
        map.put(ArmorItem.Type.LEGGINGS, Integer.valueOf(leggings));
        map.put(ArmorItem.Type.CHESTPLATE, Integer.valueOf(chestplate));
        map.put(ArmorItem.Type.HELMET, Integer.valueOf(helmet));
        map.put(ArmorItem.Type.BODY, Integer.valueOf(chestplate));
        return map;
    }

    private static Holder<ArmorMaterial> register(String name, Supplier<ArmorMaterial> material) {
        Holder<ArmorMaterial> holder = Registry.registerForHolder(
            BuiltInRegistries.ARMOR_MATERIAL,
            ResourceLocation.withDefaultNamespace(name),
            material.get()
        );
        return holder;
    }
}
