package com.nekotune.minecraftjourneys.data.datagen.assets;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.item.tool.KnifeItem;
import com.nekotune.minecraftjourneys.shared.definitions.item.tool.MultitoolItem;
import com.nekotune.minecraftjourneys.shared.definitions.item.tool.SpearItem;
import com.nekotune.minecraftjourneys.shared.registries.content.MJItems;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.data.event.GatherDataEvent;

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
            if (registered.contains(BuiltInRegistries.ITEM.getKey(item))) return;

            // Spears have special item models
            if (item instanceof SpearItem) {
                ItemModelBuilder throwingModel = spearThrowingModel(item);
                model(item, "modpack:item/spear_in_hand")
                        .override()
                        .predicate(ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, "throwing"), 1)
                        .model(throwingModel)
                        .end();
                return;
            }

            // Hand-held items (tools, weapons, etc.)
            if (item instanceof MultitoolItem
                    || item instanceof KnifeItem) {
                handheldItem(item);
                return;
            }

            // All other items
            basicItem(item);
        });
    }
    
    @Override
    public ItemModelBuilder handheldItem(Item item) {
        return model(item, "item/handheld");
    }

    @Override
    public ItemModelBuilder basicItem(Item item) {
        return model(item, "item/generated");
    }

    private ItemModelBuilder model(Item item, final String modelPath) {
        ResourceLocation location = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        final String texturePath = getTexturePath(item);
        return getBuilder(location.toString())
                .parent(new ModelFile.UncheckedModelFile(modelPath))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(
                        location.getNamespace(), texturePath));
    }

    private ItemModelBuilder spearThrowingModel(Item item) {
        ResourceLocation location = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        final String texturePath = getTexturePath(item);
        return getBuilder(location.getPath() + "_throwing")
                .parent(new ModelFile.UncheckedModelFile("modpack:item/spear_throwing"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(
                        location.getNamespace(), texturePath));
    }

    private static String getTexturePath(Item item) {
        ResourceLocation modelLocation = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        String modifier = "";
        if (item instanceof ArmorItem) {
            modifier = "armor/";
        } else if (item instanceof TieredItem) {
            modifier = "tool/";
        } else {
            modifier = "material/";
        }
        return "item/" + modifier + modelLocation.getPath();
    }
}
