package com.airesnor.wuxiacraft.alchemy;

import com.airesnor.wuxiacraft.items.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Recipes {

	public static final List<Recipe> RECIPES = new ArrayList<>();

	public static List<Recipe> getRecipeCandidatesByInput(List<Pair<Float, Item>> input) {
		List<Recipe> candidates = new ArrayList<>();
		for (Recipe recipe : RECIPES) {
			if (recipe.checkRecipe(input)) {
				candidates.add(recipe);
			}
		}
		return candidates;
	}

	public static Recipe getDefinitiveRecipe(List<Pair<Float, Item>> input, List<Recipe> candidates) {
		Recipe definitiveRecipe = null;
		for (Recipe recipe : candidates) {
			if (input.get(input.size() - 1).getRight() == recipe.getCatalyst() && input.size() == recipe.getRecipeItemCount()) {
				definitiveRecipe = recipe;
			}
		}
		return definitiveRecipe;
	}

	public static final Recipe RECIPE_BODY_REFINEMENT_PILL = new Recipe("body_refinement_pill_recipe",
			540f, 800f, 600, Items.BODY_REFINEMENT_PILL, 5, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(200f, 400f, net.minecraft.init.Items.FLINT)
			.addRecipeItem(450f, 800f, ItemBlock.getItemFromBlock(Blocks.STONE))
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_ENERGY_RECOVERY_PILL = new Recipe("energy_recovery_pill_recipe",
			320f, 480f, 450, Items.ENERGY_RECOVERY_PILL, 5, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(600f, 800f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(200f, 450f, net.minecraft.init.Items.APPLE)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

	public static final Recipe RECIPE_MINOR_HEALING_PILL = new Recipe("minor_healing_pill_recipe",
			680f, 820f, 550, Items.MINOR_HEALING_PILL, 4, 4)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 600f, net.minecraft.init.Items.SPECKLED_MELON)
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_MINOR_RECOVERY_PILL = new Recipe("minor_recovery_pill_recipe",
			580f, 670f, 700, Items.MINOR_RECOVERY_PILL, 4, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(500, 800f, net.minecraft.init.Items.GHAST_TEAR)
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_BIRD_GRACE_PILL = new Recipe("bird_grace_pill_recipe",
			300f, 450f, 340, Items.BIRD_GRACE_PILL, 6, 3)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(150f, 450f, net.minecraft.init.Items.FEATHER)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_BULL_RAGE_PILL = new Recipe("bull_rage_pill_recipe",
			600f, 820f, 480, Items.BULL_RAGE_PILL, 4, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(800f, 1000f, net.minecraft.init.Items.BLAZE_POWDER)
			.addRecipeItem(450f, 650f, net.minecraft.init.Items.LEATHER)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_CAT_VISION_PILL = new Recipe("cat_vision_pill_recipe",
			300f, 740f, 520, Items.CAT_VISION_PILL, 7, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(550f, 700f, net.minecraft.init.Items.GOLDEN_CARROT)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_FIRE_WALKER_PILL = new Recipe("fire_walker_pill_recipe",
			900f, 1150f, 700, Items.FIRE_WALKER_PILL, 6, 3)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(750f, 900f, net.minecraft.init.Items.MAGMA_CREAM)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_FISH_SOUL_PILL = new Recipe("fish_soul_pill_recipe",
			150f, 500f, 600, Items.FISH_SOUL_PILL, 8, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(150f, 350f, net.minecraft.init.Items.FISH)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_JUMPING_RABBIT_PILL = new Recipe("jumping_rabbit_pill_recipe",
			350f, 580f, 380, Items.JUMPING_RABBIT_PILL, 5, 3)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(150f, 350f, net.minecraft.init.Items.RABBIT_FOOT)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_RUNNERS_ESSENCE_PILL = new Recipe("runners_essence_pill_recipe",
			580f, 740f, 340, Items.RUNNERS_ESSENCE_PILL, 4, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR)
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_TURTLE_SHELL_PILL = new Recipe("turtle_shell_pill_recipe",
			800f, 1100f, 820, Items.TURTLE_SHELL_PILL, 3, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 650f, net.minecraft.init.Items.LEATHER)
			.addRecipeItem(1100f, 1550f, net.minecraft.init.Items.IRON_INGOT)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_MINOR_FASTING_PILL = new Recipe("minor_fasting_pill_recipe",
			150f, 350f, 550, Items.MINOR_FASTING_PILL, 3, 1)
			.addRecipeItem(150f, 300f, net.minecraft.init.Items.COOKED_BEEF)
			.addRecipeItem(150f, 300f, net.minecraft.init.Items.COOKED_FISH)
			.addRecipeItem(150f, 300f, net.minecraft.init.Items.COOKED_CHICKEN)
			.addRecipeItem(200f, 350f, net.minecraft.init.Items.POTATO)
			.addRecipeItem(200f, 350f, net.minecraft.init.Items.CARROT)
			.addRecipeItem(200f, 200f, net.minecraft.init.Items.MELON)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

}
