package com.nekotune.minecraftjourneys.shared.registry.content;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.RegistryHandler.RegisterDeferred;
import com.nekotune.minecraftjourneys.shared.definition.loot.AddItemModifier;

import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class MJLootModifiers {
    
    @RegisterDeferred
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MinecraftJourneys.MOD_ID);
    
    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", () -> AddItemModifier.CODEC);
}
