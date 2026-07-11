package com.nekotune.minecraftjourneys.datagen;

import com.davigj.wholecloth.core.registry.WCItems;
import com.farcr.nomansland.common.registry.blocks.NMLBlocks;
import com.farcr.nomansland.common.registry.items.NMLItems;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.shared.registry.content.MJItems;
import com.nekotune.minecraftjourneys.shared.registry.content.MJItems.Armors;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
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
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Armors.CLOTH_HELMET.get())
                .pattern("XXX")
                .pattern("X X")
                .define('X', CLOTH)
                .unlockedBy(getHasName(CLOTH), has(CLOTH))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Armors.CLOTH_CHESTPLATE.get())
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', CLOTH)
                .unlockedBy(getHasName(CLOTH), has(CLOTH))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Armors.CLOTH_LEGGINGS.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .define('X', CLOTH)
                .unlockedBy(getHasName(CLOTH), has(CLOTH))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Armors.CLOTH_BOOTS.get())
                .pattern("X X")
                .pattern("X X")
                .define('X', CLOTH)
                .unlockedBy(getHasName(CLOTH), has(CLOTH))
                .save(recipeOutput);
        
        final ItemLike PEBBLES = NMLBlocks.PEBBLES.get();
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, PEBBLES, 4)
                .requires(ItemTags.STONE_TOOL_MATERIALS)
                .unlockedBy(getHasName(PEBBLES), has(ItemTags.STONE_TOOL_MATERIALS))
                .save(recipeOutput);

        // Mattocks
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.Tools.STONE_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', PEBBLES)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(PEBBLES), has(PEBBLES))
                .group("Mattocks")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.Tools.FLINT_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', Items.FLINT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                .group("Mattocks")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.Tools.BONE_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', Items.BONE)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                .group("Mattocks")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.Tools.OBSIDIAN_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', Blocks.OBSIDIAN)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Blocks.OBSIDIAN), has(Blocks.OBSIDIAN))
                .group("Mattocks")
                .save(recipeOutput);
        
        // Spears
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Tools.WOODEN_SPEAR.get())
                .pattern(" S")
                .pattern("S ")
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .group("Spears")
                .save(recipeOutput);
        final Item RESIN = NMLItems.RESIN.get();
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, MJItems.Tools.INCENDIARY_SPEAR.get(), 1)
                .requires(MJItems.Tools.WOODEN_SPEAR.get())
                .requires(WCItems.CLOTH_SCRAP.get())
                .requires(NMLItems.RESIN_OIL_BOTTLE.get())
                .unlockedBy(getHasName(RESIN), has(RESIN))
                .group("Spears")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Tools.STONE_SPEAR.get())
                .pattern("  X")
                .pattern(" S ")
                .pattern("S  ")
                .define('X', PEBBLES)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(PEBBLES), has(PEBBLES))
                .group("Spears")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Tools.FLINT_SPEAR.get())
                .pattern("  X")
                .pattern(" S ")
                .pattern("S  ")
                .define('X', Items.FLINT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                .group("Spears")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Tools.BONE_SPEAR.get())
                .pattern("  X")
                .pattern(" S ")
                .pattern("S  ")
                .define('X', Items.BONE)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                .group("Spears")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Tools.OBSIDIAN_SPEAR.get())
                .pattern("  X")
                .pattern(" S ")
                .pattern("S  ")
                .define('X', Blocks.OBSIDIAN)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Blocks.OBSIDIAN), has(Blocks.OBSIDIAN))
                .group("Spears")
                .save(recipeOutput);
        
        // Knives
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Tools.STONE_KNIFE.get())
                .pattern(" X")
                .pattern("S ")
                .define('X', PEBBLES)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(PEBBLES), has(PEBBLES))
                .group("Knives")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Tools.FLINT_KNIFE.get())
                .pattern(" X")
                .pattern("S ")
                .define('X', Items.FLINT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                .group("Knives")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Tools.BONE_KNIFE.get())
                .pattern(" X")
                .pattern("S ")
                .define('X', Items.BONE)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                .group("Knives")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.Tools.OBSIDIAN_KNIFE.get())
                .pattern(" X")
                .pattern("S ")
                .define('X', Blocks.OBSIDIAN)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Blocks.OBSIDIAN), has(Blocks.OBSIDIAN))
                .group("Knives")
                .save(recipeOutput);
    }
}
