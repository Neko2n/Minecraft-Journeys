package com.nekotune.minecraftjourneys.shared.registries.serializers;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.loot.modifier.AddItemModifier;
import com.nekotune.minecraftjourneys.shared.definitions.loot.modifier.RemoveItemModifier;

import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class MJLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MinecraftJourneys.MOD_ID);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_ITEM;
    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> REMOVE_ITEM;

    static {
        ADD_ITEM = register("add_item", () -> AddItemModifier.CODEC);
        REMOVE_ITEM = register("remove_item", () -> RemoveItemModifier.CODEC);
    }
    private static <C extends MapCodec<? extends IGlobalLootModifier>> Supplier<C> register(String name, Supplier<C> codec) {
        return LOOT_MODIFIER_SERIALIZERS.register(name, codec);
    }
}
