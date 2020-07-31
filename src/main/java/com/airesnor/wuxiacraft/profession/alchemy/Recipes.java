package com.airesnor.wuxiacraft.profession.alchemy;

import com.airesnor.wuxiacraft.items.ItemHerb;
import com.airesnor.wuxiacraft.items.WuxiaItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Recipes {


    public static final List<Recipe> RECIPES = new ArrayList<>();

    public static List<Recipe> getRecipeCandidatesByInput(List<Pair<Float, Item>> inputs) {
        List<Recipe> candidates = new ArrayList<>();
        List<Pair<Float, Item>> herbInputs = new ArrayList<>();
        List<Pair<Float, Item>> nonHerbInputs = new ArrayList<>();
        int[] totalInputElements = new int[5];
        for (int i = 0; i < inputs.size(); i++) {
            Pair<Float, Item> input = inputs.get(i);
            if (input.getRight() instanceof ItemHerb) {
                herbInputs.add(input);
                int[] herbElements = ((ItemHerb) input.getRight()).getHerbElements();
                for (int k = 0; k < totalInputElements.length && herbElements.length == totalInputElements.length; k++) {
                    totalInputElements[k] += herbElements[k];
                }
            } else {
                nonHerbInputs.add(input);
            }
        }
        for (Recipe recipe : RECIPES) {
            if (recipe.checkRecipe(herbInputs, nonHerbInputs, totalInputElements)) {
                candidates.add(recipe);
            }
        }
        return candidates;
    }

    public static Recipe getDefinitiveRecipe(List<Pair<Float, Item>> inputs, List<Recipe> candidates, float temperature, boolean changeRecipe) {
        Recipe definitiveRecipe = null;
        int index = 0;
        List<Pair<Float, Item>> nonHerbInputs = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            Pair<Float, Item> input = inputs.get(i);
            if (!(input.getRight() instanceof ItemHerb)) {
                nonHerbInputs.add(input);
            }
        }
        for (int i = 0; i < candidates.size(); i++) {
            Recipe recipe = candidates.get(i);
            if (nonHerbInputs.size() > 0) {
                if (nonHerbInputs.get(nonHerbInputs.size() - 1).getRight() == recipe.getCatalyst() && nonHerbInputs.size() == recipe.getRecipeItemCount()) {
                    definitiveRecipe = recipe;
                    index = i;
                }
            } else if (nonHerbInputs.size() == 0) {
                definitiveRecipe = recipe;
                index = i;
            }
        }
        if (candidates.size() > 1 && changeRecipe) {
            Recipe recipe = candidates.get(index);
            int cycles = 0;
            while ((temperature < recipe.getCookTemperatureMin() || temperature > recipe.getCookTemperatureMax()) && cycles < candidates.size()) {
                if (index == candidates.size() - 1) {
                    index = 0;
                } else {
                    index++;
                }
                if (candidates.get(index) != null) {
                    recipe = candidates.get(index);
                    definitiveRecipe = recipe;
                    cycles++;
                }
            }
        }
        return definitiveRecipe;
    }

    public static final Recipe RECIPE_BODY_REFINEMENT_PILL = new Recipe("body_refinement_pill_recipe",
            540f, 800f, 30, WuxiaItems.BODY_REFINEMENT_PILL, 5, 1)
            .setElements(1, 2, 3, 4, 5); // explodes, works, cold

    public static final Recipe RECIPE_TRAINING_PILL = new Recipe("training_pill_recipe",
            600f, 850f, 35, WuxiaItems.TRAINING_PILL, 4, 1)
            .setElements(1, 1, 1, 1, 1)
            .addRecipeItem(300f, 600f, Items.NETHER_WART); // works, doesn't work without the nether wart,

    public static final Recipe RECIPE_REINFORCEMENT_PILL = new Recipe("reinforcement_pill_recipe",
            540f, 800f, 40, WuxiaItems.REINFORCEMENT_PILL, 3, 1)
            .setElements(2, 3, 4, 5, 6); // works, cold and hot

    public static final Recipe RECIPE_BODY_CREATION_PILL = new Recipe("body_creation_pill_recipe",
            800f, 1000f, 40, WuxiaItems.BODY_CREATION_PILL, 3, 1)
            .setElements(2, 3, 4, 5, 6); // works, cold and hot
}
