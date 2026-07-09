package com.nekotune.minecraftjourneys.client;

import java.util.stream.Stream;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definition.item.gear.SpearItem;
import com.nekotune.minecraftjourneys.shared.registry.content.MJItems;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = Dist.CLIENT)
@OnlyIn(value = Dist.CLIENT)
public class ItemPropertiesHandler {
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            final Stream<Item> items = MJItems.ITEMS.getEntries().stream().map(Holder::value);
            items.forEach(item -> {
                if (item instanceof SpearItem) {
                    ItemProperties.register(item,
                            ResourceLocation.fromNamespaceAndPath(
                                    MinecraftJourneys.MOD_ID,
                                    "throwing"),
                            (stack, level, entity, seed) ->
                                    entity != null && entity.isUsingItem()
                                    && entity.getUseItem() == stack ? 1.0F : 0.0F);
                }
            });
        });
    }
}
