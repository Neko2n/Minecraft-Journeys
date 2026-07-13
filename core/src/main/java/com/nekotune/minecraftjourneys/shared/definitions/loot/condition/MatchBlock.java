package com.nekotune.minecraftjourneys.shared.definitions.loot.condition;

import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nekotune.minecraftjourneys.shared.registries.serializers.MJLootConditions;

import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

public record MatchBlock(Optional<BlockPredicate> predicate) implements LootItemCondition {
    
    public static final MapCodec<MatchBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(BlockPredicate.CODEC
                    .optionalFieldOf("predicate")
                    .forGetter(MatchBlock::predicate))
            .apply(instance, MatchBlock::new)
    );

    @Override
    public LootItemConditionType getType() {
        return MJLootConditions.MATCH_BLOCK.get();
    }

    @Override
    public boolean test(LootContext context) {
        if (predicate().isEmpty()) return true;
        final Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return false;
        final ServerLevel level = context.getLevel();
        final BlockPos blockPos = BlockPos.containing(origin);
        return predicate().get().matches(level, blockPos);
    }

    public static LootItemCondition.Builder blockMatches(BlockPredicate.Builder predicate) {
        return () -> new MatchBlock(Optional.of(predicate.build()));
    }
}

