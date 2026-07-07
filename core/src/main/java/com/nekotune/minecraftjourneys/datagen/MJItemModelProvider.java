package com.nekotune.minecraftjourneys.datagen;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.shared.registry.content.MJItems;
import com.nekotune.minecraftjourneys.shared.registry.util.MJResourceLocations.Paths;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@DataGenProvider(dist = Dist.CLIENT)
public class MJItemModelProvider extends ItemModelProvider {

    /**
     * Keeps track of which items have already been given an
     * explicit item model.
     * Populated automatically by {@link #getBuilder}.
     */
    private final Set<ResourceLocation> registered = new HashSet<>();

    public MJItemModelProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(),
                MinecraftJourneys.MOD_ID,
                event.getExistingFileHelper());
    }

    @Override
    public ItemModelBuilder getBuilder(String path) {
        registered.add(
                path.contains(":") ? ResourceLocation.parse(path) : ResourceLocation.fromNamespaceAndPath(modid, path));
        return super.getBuilder(path);
    }

    @Override
    protected void registerModels() {

        // Any items which didn't get an explicit model are given a generic flat model.
        // Block items are skipped, as they're given models by BlockStateProvider.
        final Stream<Item> items = MJItems.ITEMS.getEntries().stream().map(Holder::value);
        items.forEach(item -> {
            if (item instanceof BlockItem) return;
            final ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
            if (registered.contains(key)) return;
            basicItem(item);
        });
    }

    @Override
    public ItemModelBuilder basicItem(Item item) {
        ResourceLocation resource = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        String modifier = "";
        if (item instanceof ArmorItem) {
            modifier = Paths.ARMOR;
        } else if (item instanceof TieredItem) {
            modifier = Paths.TOOL;
        }
        final String path = "item/" + modifier + resource.getPath();
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(resource.getNamespace(), path));
    }
}
