package com.nekotune.minecraftjourneys.data.datagen.recipes;

import com.davigj.wholecloth.core.registry.WCItems;
import com.farcr.nomansland.common.registry.blocks.NMLBlocks;
import com.farcr.nomansland.common.registry.items.NMLItems;
import com.nekotune.minecraftjourneys.shared.registries.content.MJItems;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class MJRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public MJRecipeProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(),
                event.getLookupProvider());
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        final ItemLike cloth = WCItems.CLOTH_SCRAP.get();
        final ItemLike pebbles = NMLBlocks.PEBBLES.get();
        final ItemLike grass = MJItems.GRASS.get();
        final ItemLike resin = NMLItems.RESIN.get();
        final ItemLike rope = net.mehvahdjukaar.supplementaries.reg.ModRegistry.ROPE_ITEM.get();
        final ItemLike sack = net.mehvahdjukaar.supplementaries.reg.ModRegistry.SACK_ITEM.get();

        final Ingredient torchFuel = CompoundIngredient.of(
                Ingredient.of(ItemTags.COALS),
                Ingredient.of(grass, resin));
        
        // Cloth armor
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.CLOTH_HELMET.get())
                .pattern("XXX")
                .pattern("X X")
                .define('X', cloth)
                .unlockedBy(getHasName(cloth), has(cloth))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.CLOTH_CHESTPLATE.get())
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', cloth)
                .unlockedBy(getHasName(cloth), has(cloth))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.CLOTH_LEGGINGS.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .define('X', cloth)
                .unlockedBy(getHasName(cloth), has(cloth))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.CLOTH_BOOTS.get())
                .pattern("X X")
                .pattern("X X")
                .define('X', cloth)
                .unlockedBy(getHasName(cloth), has(cloth))
                .save(recipeOutput);
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, pebbles, 4)
                .requires(ItemTags.STONE_TOOL_MATERIALS)
                .unlockedBy(getHasName(pebbles), has(ItemTags.STONE_TOOL_MATERIALS))
                .save(recipeOutput);

        // Mattocks
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.STONE_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', pebbles)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(pebbles), has(pebbles))
                .group("Mattocks")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.FLINT_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', Items.FLINT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                .group("Mattocks")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.BONE_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', Items.BONE)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                .group("Mattocks")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.OBSIDIAN_MATTOCK.get())
                .pattern("XXX")
                .pattern(" SX")
                .pattern(" S ")
                .define('X', Blocks.OBSIDIAN)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Blocks.OBSIDIAN), has(Blocks.OBSIDIAN))
                .group("Mattocks")
                .save(recipeOutput);
        
        // Spears
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.WOODEN_SPEAR.get())
                .pattern(" S")
                .pattern("S ")
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .group("Spears")
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, MJItems.INCENDIARY_SPEAR.get(), 1)
                .requires(MJItems.WOODEN_SPEAR.get())
                .requires(Ingredient.of(
                        WCItems.CLOTH_SCRAP.get(),
                        MJItems.GRASS.get()))
                .requires(NMLItems.RESIN_OIL_BOTTLE.get())
                .unlockedBy(getHasName(resin), has(resin))
                .group("Spears")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.STONE_SPEAR.get())
                .pattern("  X")
                .pattern(" S ")
                .pattern("S  ")
                .define('X', pebbles)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(pebbles), has(pebbles))
                .group("Spears")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.FLINT_SPEAR.get())
                .pattern("  X")
                .pattern(" S ")
                .pattern("S  ")
                .define('X', Items.FLINT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                .group("Spears")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.BONE_SPEAR.get())
                .pattern("  X")
                .pattern(" S ")
                .pattern("S  ")
                .define('X', Items.BONE)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                .group("Spears")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.OBSIDIAN_SPEAR.get())
                .pattern("  X")
                .pattern(" S ")
                .pattern("S  ")
                .define('X', Blocks.OBSIDIAN)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Blocks.OBSIDIAN), has(Blocks.OBSIDIAN))
                .group("Spears")
                .save(recipeOutput);
        
        // Knives
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.STONE_KNIFE.get())
                .pattern(" X")
                .pattern("S ")
                .define('X', pebbles)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(pebbles), has(pebbles))
                .group("Knives")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.FLINT_KNIFE.get())
                .pattern(" X")
                .pattern("S ")
                .define('X', Items.FLINT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                .group("Knives")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.BONE_KNIFE.get())
                .pattern(" X")
                .pattern("S ")
                .define('X', Items.BONE)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                .group("Knives")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MJItems.OBSIDIAN_KNIFE.get())
                .pattern(" X")
                .pattern("S ")
                .define('X', Blocks.OBSIDIAN)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Blocks.OBSIDIAN), has(Blocks.OBSIDIAN))
                .group("Knives")
                .save(recipeOutput);
        
        // Grass recipes
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.TORCH)
                .pattern("G")
                .pattern("S")
                .define('G', torchFuel)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(grass), has(grass))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CAMPFIRE)
                .pattern("GG")
                .pattern("LL")
                .define('G', torchFuel)
                .define('L', ItemTags.LOGS_THAT_BURN)
                .unlockedBy(getHasName(grass), has(grass))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, rope, 1)
                .requires(grass, 3)
                .unlockedBy(getHasName(grass), has(grass))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NMLBlocks.THATCH.asItem(), 2)
                .pattern("GG")
                .pattern("GG")
                .define('G', grass)
                .unlockedBy(getHasName(grass), has(grass))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, sack, 1)
                .pattern("GRG")
                .pattern("G G")
                .pattern("GGG")
                .define('G', grass)
                .define('R', rope)
                .unlockedBy(getHasName(grass), has(grass))
                .save(recipeOutput);
    }
}
