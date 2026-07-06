package com.nekotune.minecraftjourneys.datagen.util.bountifulfares;

import net.hecco.bountifulfares.BountifulFares;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;

public final class BFDataGenUtility {
    
    // Templates for block state datagen
    private static final class ResourceTemplates {
        public static final ResourceLocation FRUIT_LOG = ResourceLocation.fromNamespaceAndPath(
                BountifulFares.MOD_ID, "block/template_fruit_log");
        public static final ResourceLocation FRUIT_LOG_SIDE = ResourceLocation.fromNamespaceAndPath(
                BountifulFares.MOD_ID, "block/template_fruit_log_side");
        public static final ResourceLocation FRUIT_LOG_OTHERSIDE = ResourceLocation.fromNamespaceAndPath(
                BountifulFares.MOD_ID, "block/template_fruit_log_otherside");
        public static final ResourceLocation FRUIT_LOG_NOSIDE = ResourceLocation.fromNamespaceAndPath(
                BountifulFares.MOD_ID, "block/template_fruit_log_noside");
        public static final ResourceLocation FRUIT_LOG_ITEM = ResourceLocation.fromNamespaceAndPath(
                BountifulFares.MOD_ID, "item/template_fruit_log");
        public static final ResourceLocation FRUIT_WOOD_SIDE = ResourceLocation.fromNamespaceAndPath(
                BountifulFares.MOD_ID, "block/template_fruit_wood_side");
        public static final ResourceLocation FRUIT_WOOD_OTHERSIDE = ResourceLocation.fromNamespaceAndPath(
                BountifulFares.MOD_ID, "block/template_fruit_wood_otherside");
        public static final ResourceLocation FRUIT_WOOD_ITEM = ResourceLocation.fromNamespaceAndPath(
                BountifulFares.MOD_ID, "item/template_fruit_wood");
        public static final ResourceLocation FRUIT_LEAVES = ResourceLocation.fromNamespaceAndPath(
                BountifulFares.MOD_ID, "block/leaves_with_overlay");
    }
    
    public static class BlockStateProviderHelper {

        /**
         * Helper to build the blockstate/model for a fruit leaves block.
         * @param provider The block state provider.
         * @param block The block to generate a blockstate/model for.
         * @return The item's parent model.
         */
        public static ModelFile fruitLeaves(BlockStateProvider provider, Block block) {
            final String blockName = BuiltInRegistries.BLOCK.getKey(block).getPath();
            final String prefix = "flowering_";
            final boolean isFlowering = blockName.startsWith(prefix);
            final ModelFile model;
            if (isFlowering) {
                final ResourceLocation overlayTexture = provider.blockTexture(block);
                final ResourceLocation allTexture = ResourceLocation.fromNamespaceAndPath(
                        overlayTexture.getNamespace(), "block/" + blockName.substring(prefix.length()));
                model = provider.models().withExistingParent(blockName, ResourceTemplates.FRUIT_LEAVES)
                        .texture("all", allTexture)
                        .texture("overlay", overlayTexture)
                        .renderType("cutout_mipped");
            } else {
                final ResourceLocation ownTexture = provider.blockTexture(block);
                model = provider.models().leaves(blockName, ownTexture).renderType("cutout_mipped");
            }
            return model;
        }

        /**
         * Helper to build the blockstate/model for a fruit log block.
         * @param provider The block state provider.
         * @param block The block to generate a blockstate/model for.
         * @return The item's parent model.
         */
        @SuppressWarnings("unchecked")
        public static ModelFile fruitLog(BlockStateProvider provider, Block block) {
            final String blockName = BuiltInRegistries.BLOCK.getKey(block).getPath();
            final boolean isWood = blockName.endsWith("_wood");
            final ResourceLocation ownTexture = provider.blockTexture(block);
            final ResourceLocation texture = isWood
                    ? ResourceLocation.fromNamespaceAndPath(ownTexture.getNamespace(),
                            "block/" + blockName.substring(0, blockName.length() - "_wood".length()) + "_log")
                    : ownTexture;

            final ModelFile base;
            final ModelFile noside;
            final ModelFile side;
            final ModelFile otherside;
            if (isWood) {
                final String logName = blockName.substring(0, blockName.length() - "_wood".length()) + "_log";
                base = provider.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(ownTexture.getNamespace(), "block/" + logName));
                noside = provider.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(ownTexture.getNamespace(), "block/" + logName + "_noside"));
                side = provider.models().withExistingParent(blockName + "_side", ResourceTemplates.FRUIT_WOOD_SIDE)
                        .texture("texture", texture);
                otherside = provider.models().withExistingParent(blockName + "_otherside", ResourceTemplates.FRUIT_WOOD_OTHERSIDE)
                        .texture("texture", texture);
            } else {
                base = provider.models().withExistingParent(blockName, ResourceTemplates.FRUIT_LOG)
                        .texture("texture", texture);
                side = provider.models().withExistingParent(blockName + "_side", ResourceTemplates.FRUIT_LOG_SIDE)
                        .texture("texture", texture);
                otherside = provider.models().withExistingParent(blockName + "_otherside", ResourceTemplates.FRUIT_LOG_OTHERSIDE)
                        .texture("texture", texture);
                noside = provider.models().withExistingParent(blockName + "_noside", ResourceTemplates.FRUIT_LOG_NOSIDE)
                        .texture("texture", texture);
            }

            // Properties for determining model shape
            final var states = block.getStateDefinition();
            final Property<Boolean> north = (Property<Boolean>) states.getProperty("north");
            final Property<Boolean> east = (Property<Boolean>) states.getProperty("east");
            final Property<Boolean> south = (Property<Boolean>) states.getProperty("south");
            final Property<Boolean> west = (Property<Boolean>) states.getProperty("west");
            final Property<Boolean> up = (Property<Boolean>) states.getProperty("up");
            final Property<Boolean> down = (Property<Boolean>) states.getProperty("down");
            final Property<Direction.Axis> axis = (Property<Direction.Axis>) states.getProperty("axis");
            final Property<Boolean> leafy = (Property<Boolean>) states.getProperty("leafy");
            final MultiPartBlockStateBuilder builder = provider.getMultipartBuilder(block);

            // Branch connections
            builder.part().modelFile(otherside).rotationX(90).addModel().condition(north, true).end();
            builder.part().modelFile(side).rotationX(90).rotationY(90).addModel().condition(east, true).end();
            builder.part().modelFile(side).rotationX(270).addModel().condition(south, true).end();
            builder.part().modelFile(otherside).rotationX(270).rotationY(90).addModel().condition(west, true).end();
            builder.part().modelFile(side).addModel().condition(up, true).end();
            builder.part().modelFile(otherside).rotationX(180).addModel().condition(down, true).end();

            // X-axis
            builder.part().modelFile(base).rotationX(90).rotationY(90).addModel()
                    .condition(axis, Direction.Axis.X).end();
            builder.part().modelFile(noside).rotationX(90).rotationY(90).addModel()
                    .condition(axis, Direction.Axis.X).condition(east, false).end();
            builder.part().modelFile(noside).rotationX(270).rotationY(90).addModel()
                    .condition(axis, Direction.Axis.X).condition(west, false).end();
            builder.part().modelFile(side).rotationX(90).rotationY(90).addModel()
                    .condition(axis, Direction.Axis.X).condition(east, false).condition(west, false).end();
            builder.part().modelFile(side).rotationX(90).rotationY(90).addModel()
                    .condition(axis, Direction.Axis.X).condition(down, false).condition(east, false)
                    .condition(north, false).condition(south, false).condition(up, false).end();
            builder.part().modelFile(otherside).rotationX(270).rotationY(90).addModel()
                    .condition(axis, Direction.Axis.X).condition(east, false).condition(west, false).end();
            builder.part().modelFile(otherside).rotationX(270).rotationY(90).addModel()
                    .condition(axis, Direction.Axis.X).condition(down, false).condition(north, false)
                    .condition(south, false).condition(up, false).condition(west, false).end();

            // Y-axis
            builder.part().modelFile(base).addModel()
                    .condition(axis, Direction.Axis.Y).end();
            builder.part().modelFile(noside).addModel()
                    .condition(axis, Direction.Axis.Y).condition(down, true).condition(up, false).end();
            builder.part().modelFile(noside).rotationX(180).addModel()
                    .condition(axis, Direction.Axis.Y).condition(down, false).condition(up, true).end();
            builder.part().modelFile(side).addModel()
                    .condition(axis, Direction.Axis.Y).condition(down, false).condition(up, false).end();
            builder.part().modelFile(side).addModel()
                    .condition(axis, Direction.Axis.Y).condition(east, false).condition(north, false)
                    .condition(south, false).condition(up, false).condition(west, false).end();
            builder.part().modelFile(otherside).rotationX(180).addModel()
                    .condition(axis, Direction.Axis.Y).condition(down, false).condition(up, false).end();
            builder.part().modelFile(otherside).rotationX(180).addModel()
                    .condition(axis, Direction.Axis.Y).condition(down, false).condition(east, false)
                    .condition(north, false).condition(south, false).condition(west, false).end();

            // Z-axis
            builder.part().modelFile(base).rotationX(270).addModel()
                    .condition(axis, Direction.Axis.Z).end();
            builder.part().modelFile(noside).rotationX(270).rotationY(180).addModel()
                    .condition(axis, Direction.Axis.Z).condition(north, false).end();
            builder.part().modelFile(noside).rotationX(270).addModel()
                    .condition(axis, Direction.Axis.Z).condition(south, false).end();
            builder.part().modelFile(otherside).rotationX(90).addModel()
                    .condition(axis, Direction.Axis.Z).condition(north, false).condition(south, false).end();
            builder.part().modelFile(otherside).rotationX(90).addModel()
                    .condition(axis, Direction.Axis.Z).condition(down, false).condition(east, false)
                    .condition(north, false).condition(up, false).condition(west, false).end();
            builder.part().modelFile(side).rotationX(270).addModel()
                    .condition(axis, Direction.Axis.Z).condition(north, false).condition(south, false).end();
            builder.part().modelFile(side).rotationX(270).addModel()
                    .condition(axis, Direction.Axis.Z).condition(down, false).condition(east, false)
                    .condition(south, false).condition(up, false).condition(west, false).end();

            // Leafy overlay (when the block is surrounded by leaves)
            if (leafy != null) {
                final String treeName = texture.getPath().replaceFirst("^block/(stripped_)?", "")
                        .replaceFirst("_(log|wood)$", "");
                final ModelFile leaves = provider.models().getExistingFile(
                        ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), "block/" + treeName + "_leaves"));
                builder.part().modelFile(leaves).uvLock(true).addModel().condition(leafy, true).end();
            }

            return provider.models().withExistingParent(blockName + "_item", isWood ? ResourceTemplates.FRUIT_WOOD_ITEM : ResourceTemplates.FRUIT_LOG_ITEM)
                    .texture("texture", texture);
        }

    }
}
