package com.nekotune.minecraftjourneys.shared.registry.content;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.RegistryHandler.RegisterDeferred;
import com.nekotune.minecraftjourneys.shared.definition.loot.MatchBlock;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJLootItemConditions {

    @RegisterDeferred
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.LOOT_CONDITION_TYPE.key(), MinecraftJourneys.MOD_ID);    

    public static final Supplier<LootItemConditionType> MATCH_BLOCK = register(
            "block_tag", MatchBlock.CODEC);
    
    private static Supplier<LootItemConditionType> register(String name, MapCodec<? extends LootItemCondition> codec) {
        return LOOT_CONDITION_SERIALIZERS.register(name,
                () -> new LootItemConditionType(codec));
    }
}
