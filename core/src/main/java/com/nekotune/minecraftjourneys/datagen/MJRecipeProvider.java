package com.nekotune.minecraftjourneys.datagen;

import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
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
    protected void buildRecipes(RecipeOutput recipeOutput) {}
}
