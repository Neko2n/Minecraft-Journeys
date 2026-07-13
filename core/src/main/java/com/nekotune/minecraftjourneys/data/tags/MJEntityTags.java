package com.nekotune.minecraftjourneys.data.tags;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class MJEntityTags {

    public static final TagKey<EntityType<?>> DROPS_PELT = createTag("drops_pelt");
    
    private static TagKey<EntityType<?>> createTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, name));
    }
}
