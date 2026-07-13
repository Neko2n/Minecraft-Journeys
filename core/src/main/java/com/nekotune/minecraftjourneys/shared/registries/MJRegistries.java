package com.nekotune.minecraftjourneys.shared.registries;

import com.nekotune.minecraftjourneys.shared.registries.content.MJBlocks;
import com.nekotune.minecraftjourneys.shared.registries.content.MJEnchantmentEffects;
import com.nekotune.minecraftjourneys.shared.registries.content.MJEntityTypes;
import com.nekotune.minecraftjourneys.shared.registries.content.MJItems;
import com.nekotune.minecraftjourneys.shared.registries.serializers.MJLootConditions;
import com.nekotune.minecraftjourneys.shared.registries.serializers.MJLootModifiers;

import net.neoforged.bus.api.IEventBus;

public final class MJRegistries {
    
    public static void registerAll(IEventBus modEventBus) {
        MJSoundEvents.SOUND_EVENTS.register(modEventBus);
        
        MJLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        MJLootConditions.LOOT_CONDITION_SERIALIZERS.register(modEventBus);
        
        MJItems.ITEMS.register(modEventBus);
        MJBlocks.BLOCKS.register(modEventBus);
        MJEntityTypes.ENTITY_TYPES.register(modEventBus);
        MJEnchantmentEffects.ENTITY_ENCHANTMENT_EFFECTS.register(modEventBus);
    }
}
