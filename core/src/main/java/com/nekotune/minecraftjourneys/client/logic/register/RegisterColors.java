package com.nekotune.minecraftjourneys.client.logic.register;

import java.util.Collection;
import java.util.HashSet;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.item.armor.MJArmorMaterials;
import com.nekotune.minecraftjourneys.shared.registries.content.MJBlocks;
import com.nekotune.minecraftjourneys.shared.registries.content.MJItems;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.Holder;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ItemLike;
import net.hecco.bountifulfares.definition.block.custom.FruitLogBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID, value = Dist.CLIENT)
@OnlyIn(value = Dist.CLIENT)
public class RegisterColors {

    private static final HashSet<ItemLike> BLACKLIST = new HashSet<>();

    /**
     * Prevents certain items from being automatically given colors.
     * @param item The item to verify against.
     * @return True if the given item is blacklisted, false otherwise.
     */
    private static boolean isBlacklisted(ItemLike item) {
        if (BLACKLIST.isEmpty()) {
            // Add blacklist items here
        }
        return BLACKLIST.contains(item);
    }

    /**
     * Determines what kinds of items should automatically be given colors.
     * @param item The item to verify against.
     * @return True if the given item should be colored, false otherwise.
     */
    private static boolean shouldColor(ItemLike item) {
        if (isBlacklisted(item)) return false;
        if (item instanceof LeavesBlock) return true;
        if (item instanceof FruitLogBlock) return true;
        return false;
    }
    
    /**
     * Changes the default tint for cloth armor, overriding the default leather brown.
     */
    private static final IClientItemExtensions CLOTH_ARMOR_EXTENSIONS = new IClientItemExtensions() {
        @Override
        public int getDefaultDyeColor(ItemStack stack) {
            return FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(stack, MJArmorMaterials.CLOTH_COLOR));
        }
    };
    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(
                CLOTH_ARMOR_EXTENSIONS,
                MJItems.CLOTH_HELMET.get(),
                MJItems.CLOTH_CHESTPLATE.get(),
                MJItems.CLOTH_LEGGINGS.get(),
                MJItems.CLOTH_BOOTS.get());
    }

    @SubscribeEvent
    static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        final Collection<Block> blocks = MJBlocks.BLOCKS.getEntries()
                .stream().map(Holder::value).toList();
        blocks.forEach(block -> {
            if (!shouldColor(block)) return;
            event.register((state, level, pos, tintIndex) ->
                    level != null && pos != null
                            ? BiomeColors.getAverageFoliageColor(level, pos)
                            : FoliageColor.getDefaultColor(),
                    block);
        });
    }

    @SubscribeEvent
    static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {

        // Cloth armor coloring
        event.register(
                (stack, tintIndex) -> tintIndex > 0 ? -1 : DyedItemColor.getOrDefault(stack, MJArmorMaterials.CLOTH_COLOR),
                MJItems.CLOTH_HELMET.get(),
                MJItems.CLOTH_CHESTPLATE.get(),
                MJItems.CLOTH_LEGGINGS.get(),
                MJItems.CLOTH_BOOTS.get());

        // Generic foliage tint colors for blocks
        final Collection<Block> blocks = MJBlocks.BLOCKS.getEntries()
                .stream().map(Holder::value).toList();
        blocks.forEach(block -> {
            if (!shouldColor(block)) return;
            event.register((stack, tintIndex) ->
                    FoliageColor.getDefaultColor(),
                    block);
        });
    }
}
