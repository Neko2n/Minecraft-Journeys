package com.nekotune.minecraftjourneys.shared.definitions.loot.modifier;

import java.security.InvalidParameterException;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AddItemModifier extends LootModifier {
    public static final MapCodec<AddItemModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            LootModifier.codecStart(instance).and(BuiltInRegistries.ITEM
                    .byNameCodec().fieldOf("item")
                    .forGetter(o -> o.item))
            .apply(instance, AddItemModifier::new));
    protected static final RandomSource random = RandomSource.create();
    protected final Item item;
    protected int minCount;
    protected int maxCount;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
        this.minCount = 1;
        this.maxCount = 1;
    }
    
    /**
     * @throws InvalidParameterException If count < 1.
     */
    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, int count) {
        super(conditionsIn);
        this.item = item;
        if (count < 1) throw new InvalidParameterException("count must be >= 1");
        this.minCount = count;
        this.maxCount = count;
    }
    
    /**
     * @throws InvalidParameterException If max < min, min < 0, or max < 1.
     */
    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, int minCount, int maxCount) {
        super(conditionsIn);
        this.item = item;
        if (minCount > maxCount) throw new InvalidParameterException("max must be >= min");
        if (minCount < 0) throw new InvalidParameterException("min must be >= 0");
        if (maxCount < 1) throw new InvalidParameterException("max must be >= 1");
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }
        final int count = random.nextInt(minCount, maxCount + 1);
        final ItemStack stack = new ItemStack(this.item);
        stack.setCount(count);
        generatedLoot.add(stack);
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
