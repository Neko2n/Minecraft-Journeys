package com.nekotune.minecraftjourneys.shared.registry.worldgen;

import java.util.List;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definition.block.pear.HangingPearBlock;
import com.nekotune.minecraftjourneys.shared.registry.content.MJBlocks;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class MJConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PEAR_KEY = registerKey("pear");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        register(context, PEAR_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(MJBlocks.BFPearBlocks.PEAR_LOG.get()),
                new StraightTrunkPlacer(5, 1, 0),
                BlockStateProvider.simple(MJBlocks.BFPearBlocks.PEAR_LEAVES.get()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 4),
                new TwoLayersFeatureSize(0, 0, 0))
                .dirt(BlockStateProvider.simple(Blocks.GRASS_BLOCK))
                .ignoreVines()
                .decorators(List.of(
                        new AttachedToLeavesDecorator(
                                0.5F, 1, 0,
                                new RandomizedIntStateProvider(
                                        BlockStateProvider.simple(MJBlocks.BFPearBlocks.HANGING_PEAR.get()
                                                .defaultBlockState().setValue(HangingPearBlock.AGE, 4)),
                                        HangingPearBlock.AGE,
                                        UniformInt.of(0, 4)),
                                1,
                                List.of(Direction.DOWN)),
                        new BeehiveDecorator(0.05F)))
                .build());
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            F feature,
            FC configuration) {
        context.register(key, new ConfiguredFeature<FC, F>(feature, configuration));
    }
}
