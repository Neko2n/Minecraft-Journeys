package com.nekotune.minecraftjourneys.shared.registries.serializers;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.loot.condition.MatchBlock;
import com.nekotune.minecraftjourneys.shared.definitions.loot.condition.MatchEntityType;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJLootConditions {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.LOOT_CONDITION_TYPE.key(), MinecraftJourneys.MOD_ID);    

    public static final Supplier<LootItemConditionType> MATCH_BLOCK;
    public static final Supplier<LootItemConditionType> MATCH_ENTITY_TYPE;

    static {
        MATCH_BLOCK = register("match_block", MatchBlock.CODEC);
        MATCH_ENTITY_TYPE = register("match_entity_type", MatchEntityType.CODEC);
    }

    private static Supplier<LootItemConditionType> register(String name, MapCodec<? extends LootItemCondition> codec) {
        return LOOT_CONDITION_SERIALIZERS.register(name,
                () -> new LootItemConditionType(codec));
    }
}
