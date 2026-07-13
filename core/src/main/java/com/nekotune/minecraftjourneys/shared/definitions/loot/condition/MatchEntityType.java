package com.nekotune.minecraftjourneys.shared.definitions.loot.condition;

import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nekotune.minecraftjourneys.shared.registries.serializers.MJLootConditions;

import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record MatchEntityType(Optional<EntityTypePredicate> predicate) implements LootItemCondition {
    
    public static final MapCodec<MatchEntityType> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(EntityTypePredicate.CODEC
                    .optionalFieldOf("predicate")
                    .forGetter(MatchEntityType::predicate))
            .apply(instance, MatchEntityType::new)
    );

    @Override
    public LootItemConditionType getType() {
        return MJLootConditions.MATCH_ENTITY_TYPE.get();
    }

    @Override
    public boolean test(LootContext context) {
        if (predicate().isEmpty()) return true;
        final Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity == null) return false;
        return predicate().get().matches(entity.getType());
    }
    
    public static LootItemCondition.Builder any() {
        return () -> new MatchEntityType(Optional.of(null));
    }

    public static LootItemCondition.Builder entityTypeMatches(EntityTypePredicate predicate) {
        return () -> new MatchEntityType(Optional.of(predicate));
    }
}
