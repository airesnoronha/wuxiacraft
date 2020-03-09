package com.airesnor.wuxiacraft.alchemy;

import net.minecraft.item.Item;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Recipes {

    public static final List<Recipe> RECIPES = new ArrayList<>();

    public static List<Recipe> getRecipeCandidatesByInput(List<Pair<Float, Item>> input) {
        List<Recipe> candidates = new ArrayList<>();
        for(Recipe recipe : RECIPES) {
            if(recipe.checkRecipe(input)) {
                candidates.add(recipe);
            }
        }
        return candidates;
    }

}
