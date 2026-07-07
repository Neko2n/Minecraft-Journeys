package com.nekotune.minecraftjourneys.datagen;

import com.davigj.wholecloth.core.registry.WCItems;
import com.farcr.nomansland.common.registry.blocks.NMLBlocks;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.shared.registry.content.MJItems;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@DataGenProvider(dist = Dist.DEDICATED_SERVER)
public class MJRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public MJRecipeProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(),
                event.getLookupProvider());
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        // Cloth armor
        final ItemLike CLOTH = WCItems.CLOTH_SCRAP.get();
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Equipment.CLOTH_HELMET.get())
                .pattern("XXX")
                .pattern("X X")
                .define('X', CLOTH)
                .unlockedBy(getHasName(CLOTH), has(CLOTH))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Equipment.CLOTH_CHESTPLATE.get())
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', CLOTH)
                .unlockedBy(getHasName(CLOTH), has(CLOTH))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Equipment.CLOTH_LEGGINGS.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .define('X', CLOTH)
                .unlockedBy(getHasName(CLOTH), has(CLOTH))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Equipment.CLOTH_BOOTS.get())
                .pattern("X X")
                .pattern("X X")
                .define('X', CLOTH)
                .unlockedBy(getHasName(CLOTH), has(CLOTH))
                .save(recipeOutput);

        // Mattocks
        final ItemLike PEBBLES = NMLBlocks.PEBBLES.get();
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.Equipment.STONE_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', PEBBLES)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(PEBBLES), has(PEBBLES))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, PEBBLES, 4)
                .requires(ItemTags.STONE_TOOL_MATERIALS)
                .unlockedBy(getHasName(PEBBLES), has(ItemTags.STONE_TOOL_MATERIALS))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.Equipment.FLINT_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', Items.FLINT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.Equipment.BONE_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', Items.BONE)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.Equipment.OBSIDIAN_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', Blocks.OBSIDIAN)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Blocks.OBSIDIAN), has(Blocks.OBSIDIAN))
                .save(recipeOutput);
    }
}
