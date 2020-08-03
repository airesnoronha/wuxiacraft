package com.airesnor.wuxiacraft.profession.alchemy;

import com.airesnor.wuxiacraft.items.WuxiaItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class RecipesOld {

	public static final List<Recipe> RECIPES = new ArrayList<>();

	public static List<Recipe> getRecipeCandidatesByInput(List<Pair<Float, Item>> input) {
		List<Recipe> candidates = new ArrayList<>();
		for (Recipe recipe : RECIPES) {
			//if (recipe.checkRecipe(input)) {
			//	candidates.add(recipe);
			//}
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

	//Progress Pills
	public static final Recipe RECIPE_BODY_REFINEMENT_PILL = new Recipe("body_refinement_pill_recipe",
			540f, 800f, 30, WuxiaItems.BODY_REFINEMENT_PILL, 5, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(200f, 400f, net.minecraft.init.Items.FLINT)
			.addRecipeItem(450f, 800f, ItemBlock.getItemFromBlock(Blocks.STONE))
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_TRAINING_PILL = new Recipe("training_pill_recipe",
			600f, 850f, 35, WuxiaItems.TRAINING_PILL, 4, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 800f, ItemBlock.getItemFromBlock(Blocks.STONE))
			.addRecipeItem(500f, 800f, ItemBlock.getItemFromBlock(Blocks.STONEBRICK))
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_REINFORCEMENT_PILL = new Recipe("reinforcement_pill_recipe",
			680f, 900f, 40, WuxiaItems.REINFORCEMENT_PILL, 3, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 800f, ItemBlock.getItemFromBlock(Blocks.STONEBRICK))
			.addRecipeItem(620f, 820f, net.minecraft.init.Items.BONE)
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_BODY_CREATION_PILL = new Recipe("body_creation_pill_recipe",
			750f, 900f, 45, WuxiaItems.BODY_CREATION_PILL, 3, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 800f, ItemBlock.getItemFromBlock(Blocks.STONEBRICK))
			.addRecipeItem(1000f, 1150f, ItemBlock.getItemFromBlock(Blocks.BONE_BLOCK))
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_SOUL_REFINEMENT_PILL = new Recipe("soul_refinement_pill_recipe",
			600f, 850f, 45, WuxiaItems.SOUL_REFINEMENT_PILL, 4, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.SNOWBALL)
			.addRecipeItem(1100f, 1550f, net.minecraft.init.Items.QUARTZ)
			.addRecipeItem(1400f, 1800f, ItemBlock.getItemFromBlock(Blocks.MAGMA))
			.addRecipeItem(800f, 900f, ItemBlock.getItemFromBlock(Blocks.GLOWSTONE));

	public static final Recipe RECIPE_SOUL_ASPECT_PILL = new Recipe("soul_aspect_pill_recipe",
			600f, 850f, 45, WuxiaItems.SOUL_ASPECT_PILL, 4, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(300f, 600f, ItemBlock.getItemFromBlock(Blocks.SNOW))
			.addRecipeItem(1100f, 1550f, net.minecraft.init.Items.QUARTZ)
			.addRecipeItem(1400f, 1800f, ItemBlock.getItemFromBlock(Blocks.MAGMA))
			.addRecipeItem(800f, 900f, ItemBlock.getItemFromBlock(Blocks.GLOWSTONE));

	public static final Recipe RECIPE_WANDERING_GHOST_PILL = new Recipe("wandering_ghost_pill_recipe",
			800f, 1000f, 45, WuxiaItems.WANDERING_GHOST_PILL, 4, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(300f, 600f, ItemBlock.getItemFromBlock(Blocks.SNOW))
			.addRecipeItem(1100f, 1550f, ItemBlock.getItemFromBlock(Blocks.QUARTZ_BLOCK))
			.addRecipeItem(1400f, 1500f, ItemBlock.getItemFromBlock(Blocks.SOUL_SAND))
			.addRecipeItem(800f, 900f, ItemBlock.getItemFromBlock(Blocks.GLOWSTONE));

	public static final Recipe RECIPE_QI_PATHS_REFINEMENT_PILL = new Recipe("qi_paths_refinement_pill_recipe",
			900f, 1050f, 50, WuxiaItems.QI_PATHS_REFINEMENT_PILL, 3, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(1100f, 1550f, net.minecraft.init.Items.GOLDEN_APPLE)
			.addRecipeItem(900f, 1000f, net.minecraft.init.Items.SHULKER_SHELL)
			.addRecipeItem(800f, 900f, ItemBlock.getItemFromBlock(Blocks.GLOWSTONE));

	public static final Recipe RECIPE_MERIDIAN_INJECTION_PILL = new Recipe("meridian_injection_pill_recipe",
			930f, 1000f, 50, WuxiaItems.MERIDIAN_INJECTION_PILL, 3, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(1100f, 1550f, net.minecraft.init.Items.GOLDEN_APPLE)
			.addRecipeItem(900f, 1000f, net.minecraft.init.Items.SHULKER_SHELL)
			.addRecipeItem(1050f, 1100f, net.minecraft.init.Items.ENDER_PEARL)
			.addRecipeItem(800f, 900f, ItemBlock.getItemFromBlock(Blocks.GLOWSTONE));

	public static final Recipe RECIPE_DANTIAN_CONDENSING_PILL = new Recipe("dantian_condensing_pill_recipe",
			1000f, 1200f, 50, WuxiaItems.DANTIAN_CONDENSING_PILL, 3, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(1050f, 1100f, net.minecraft.init.Items.ENDER_PEARL)
			.addRecipeItem(1300f, 1550f, ItemBlock.getItemFromBlock(Blocks.OBSIDIAN))
			.addRecipeItem(900f, 1000f, net.minecraft.init.Items.SHULKER_SHELL)
			.addRecipeItem(800f, 900f, ItemBlock.getItemFromBlock(Blocks.GLOWSTONE));

	//Energy Pills
	public static final Recipe RECIPE_ENERGY_RECOVERY_PILL = new Recipe("energy_recovery_pill_recipe",
			320f, 480f, 22, WuxiaItems.ENERGY_RECOVERY_PILL, 5, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(600f, 800f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(200f, 450f, net.minecraft.init.Items.APPLE)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

	public static final Recipe RECIPE_MINOR_ENERGY_REPLENISHING_PILL = new Recipe("minor_energy_replenishing_pill_recipe",
			340f, 500f, 25, WuxiaItems.MINOR_ENERGY_REPLENISHING_PILL, 5, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(600f, 800f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(200f, 420f, net.minecraft.init.Items.EGG)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

	public static final Recipe RECIPE_ENERGY_STEAM_PILL = new Recipe("energy_stream_pill_recipe",
			360f, 520f, 26, WuxiaItems.ENERGY_STREAM_PILL, 5, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(600f, 800f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(200f, 450f, net.minecraft.init.Items.POTATO)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

	public static final Recipe RECIPE_LESSER_ENERGY_REPLENISHING_PILL = new Recipe("lesser_energy_replenishing_pill_recipe",
			380f, 540f, 27, WuxiaItems.LESSER_ENERGY_REPLENISHING_PILL, 5, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(600f, 800f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(200f, 450f, net.minecraft.init.Items.BAKED_POTATO)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

	public static final Recipe RECIPE_EARTH_QI_PILL = new Recipe("earth_qi_pill_recipe",
			400f, 560f, 30, WuxiaItems.EARTH_QI_PILL, 5, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(600f, 800f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(200f, 450f, net.minecraft.init.Items.GOLDEN_APPLE)
			.addRecipeItem(1400f, 1600f, ItemBlock.getItemFromBlock(Blocks.OBSIDIAN))
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

	public static final Recipe RECIPE_SMALLER_ENERGY_REPLENISHING_PILL = new Recipe("smaller_energy_replenishing_pill_recipe",
			420f, 580f, 30, WuxiaItems.SMALL_ENERGY_REPLENISHING_PILL, 5, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(600f, 800f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(200f, 450f, net.minecraft.init.Items.GOLDEN_APPLE)
			.addRecipeItem(1100f, 1550f, ItemBlock.getItemFromBlock(Blocks.QUARTZ_BLOCK))
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

	public static final Recipe RECIPE_HEAVEN_QI_PILL = new Recipe("heaven_qi_pill_recipe",
			440f, 600f, 30, WuxiaItems.HEAVEN_QI_PILL, 5, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(600f, 800f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(1400f, 1600f, ItemBlock.getItemFromBlock(Blocks.OBSIDIAN))
			.addRecipeItem(1300f, 1400f, ItemBlock.getItemFromBlock(Blocks.END_STONE))
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

	public static final Recipe RECIPE_SMALL_ENERGY_REPLENISHING_PILL = new Recipe("small_energy_replenishing_pill_recipe",
			460f, 580f, 30, WuxiaItems.SMALL_ENERGY_REPLENISHING_PILL, 4, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(600f, 800f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(1400f, 1600f, ItemBlock.getItemFromBlock(Blocks.OBSIDIAN))
			.addRecipeItem(1300f, 1400f, ItemBlock.getItemFromBlock(Blocks.END_STONE))
			.addRecipeItem(900f, 1000f, net.minecraft.init.Items.SHULKER_SHELL)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

	public static final Recipe RECIPE_ENERGY_GATHERING_PILL = new Recipe("energy_gathering_pill_recipe",
			480f, 540f, 31, WuxiaItems.ENERGY_GATHERING_PILL, 4, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(600f, 800f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(1400f, 1600f, ItemBlock.getItemFromBlock(Blocks.OBSIDIAN))
			.addRecipeItem(1300f, 1400f, ItemBlock.getItemFromBlock(Blocks.END_STONE))
			.addRecipeItem(900f, 1000f, net.minecraft.init.Items.SHULKER_SHELL)
			.addRecipeItem(1150f, 1250f, net.minecraft.init.Items.ENDER_PEARL)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

	//Healing Pills
	public static final Recipe RECIPE_MINOR_HEALING_PILL = new Recipe("minor_healing_pill_recipe",
			680f, 820f, 27, WuxiaItems.MINOR_HEALING_PILL, 4, 4)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 600f, net.minecraft.init.Items.SPECKLED_MELON)
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_INVIGORATE_BLOOD_PILL = new Recipe("invigorate_blood_pill_recipe",
			700f, 840f, 30, WuxiaItems.INVIGORATE_BLOOD_PILL, 4, 4)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 600f, net.minecraft.init.Items.SPECKLED_MELON)
			.addRecipeItem(1100f, 1550f, net.minecraft.init.Items.GOLDEN_APPLE)
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_LESSER_HEALING_PILL = new Recipe("lesser_healing_pill_recipe",
			720f, 850f, 35, WuxiaItems.LESSER_HEALING_PILL, 4, 4)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 600f, net.minecraft.init.Items.SPECKLED_MELON)
			.addRecipeItem(1100f, 1550f, net.minecraft.init.Items.GOLDEN_APPLE)
			.addRecipeItem(800f, 1000f, net.minecraft.init.Items.BLAZE_POWDER)
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_RECONSTRUCT_BODY_PILL = new Recipe("reconstruct_body_pill_recipe",
			720f, 850f, 35, WuxiaItems.RECONSTRUCT_BODY_PILL, 4, 4)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 600f, net.minecraft.init.Items.SPECKLED_MELON)
			.addRecipeItem(1100f, 1550f, net.minecraft.init.Items.GOLDEN_APPLE)
			.addRecipeItem(1000f, 1100f, net.minecraft.init.Items.BLAZE_ROD)
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_SMALLER_HEALING_PILL = new Recipe("smaller_healing_pill_recipe",
			740f, 850f, 35, WuxiaItems.SMALLER_HEALING_PILL, 4, 4)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 600f, net.minecraft.init.Items.SPECKLED_MELON)
			.addRecipeItem(1100f, 1550f, net.minecraft.init.Items.GOLDEN_APPLE)
			.addRecipeItem(800f, 1000f, net.minecraft.init.Items.BLAZE_ROD)
			.addRecipeItem(550f, 800f, ItemBlock.getItemFromBlock(Blocks.GLOWSTONE));

	public static final Recipe RECIPE_HEALTH_INCARNATE_PILL = new Recipe("health_incarnate_pill_recipe",
			740f, 850f, 35, WuxiaItems.HEALTH_INCARNATE_PILL, 4, 4)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 600f, net.minecraft.init.Items.SPECKLED_MELON)
			.addRecipeItem(500, 800f, net.minecraft.init.Items.GHAST_TEAR)
			.addRecipeItem(800f, 1000f, net.minecraft.init.Items.BLAZE_ROD)
			.addRecipeItem(550f, 800f, ItemBlock.getItemFromBlock(Blocks.GLOWSTONE));


	public static final Recipe RECIPE_PHYSICIAN_MIRACLE_PILL = new Recipe("physician_miracle_pill_recipe",
			750f, 840f, 35, WuxiaItems.PHYSICIAN_MIRACLE_PILL, 4, 4)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 600f, net.minecraft.init.Items.SPECKLED_MELON)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR)
			.addRecipeItem(500, 800f, net.minecraft.init.Items.GHAST_TEAR)
			.addRecipeItem(800f, 1000f, net.minecraft.init.Items.BLAZE_ROD)
			.addRecipeItem(550f, 800f, ItemBlock.getItemFromBlock(Blocks.GLOWSTONE));

	public static final Recipe RECIPE_SMALL_HEALING_PILL = new Recipe("small_healing_pill_recipe",
			760f, 830f, 35, WuxiaItems.SMALL_HEALING_PILL, 4, 4)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 600f, net.minecraft.init.Items.SPECKLED_MELON)
			.addRecipeItem(500, 800f, net.minecraft.init.Items.GHAST_TEAR)
			.addRecipeItem(800f, 1000f, net.minecraft.init.Items.BLAZE_ROD)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(550f, 800f, ItemBlock.getItemFromBlock(Blocks.GLOWSTONE));

	public static final Recipe RECIPE_ALMOST_RESURRECT_PILL = new Recipe("almost_resurrect_pill_recipe",
			750f, 800f, 35, WuxiaItems.ALMOST_RESURRECT_PILL, 4, 4)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 600f, net.minecraft.init.Items.SPECKLED_MELON)
			.addRecipeItem(500, 800f, net.minecraft.init.Items.GHAST_TEAR)
			.addRecipeItem(1100f, 1150f, net.minecraft.init.Items.ENDER_EYE)
			.addRecipeItem(800f, 1000f, net.minecraft.init.Items.BLAZE_ROD)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE)
			.addRecipeItem(550f, 800f, ItemBlock.getItemFromBlock(Blocks.GLOWSTONE));

	//Effect Pills
	public static final Recipe RECIPE_MINOR_RECOVERY_PILL = new Recipe("minor_recovery_pill_recipe",
			580f, 670f, 35, WuxiaItems.MINOR_RECOVERY_PILL, 4, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(500, 800f, net.minecraft.init.Items.GHAST_TEAR)
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST);

	public static final Recipe RECIPE_BIRD_GRACE_PILL = new Recipe("bird_grace_pill_recipe",
			300f, 450f, 17, WuxiaItems.BIRD_GRACE_PILL, 6, 3)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(150f, 450f, net.minecraft.init.Items.FEATHER)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_BULL_RAGE_PILL = new Recipe("bull_rage_pill_recipe",
			600f, 820f, 24, WuxiaItems.BULL_RAGE_PILL, 4, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(800f, 1000f, net.minecraft.init.Items.BLAZE_POWDER)
			.addRecipeItem(450f, 650f, net.minecraft.init.Items.LEATHER)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_CAT_VISION_PILL = new Recipe("cat_vision_pill_recipe",
			300f, 740f, 26, WuxiaItems.CAT_VISION_PILL, 7, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(550f, 700f, net.minecraft.init.Items.GOLDEN_CARROT)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_FIRE_WALKER_PILL = new Recipe("fire_walker_pill_recipe",
			900f, 1150f, 35, WuxiaItems.FIRE_WALKER_PILL, 6, 3)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(750f, 900f, net.minecraft.init.Items.MAGMA_CREAM)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_FISH_SOUL_PILL = new Recipe("fish_soul_pill_recipe",
			150f, 500f, 30, WuxiaItems.FISH_SOUL_PILL, 8, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(150f, 350f, net.minecraft.init.Items.FISH)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_JUMPING_RABBIT_PILL = new Recipe("jumping_rabbit_pill_recipe",
			350f, 580f, 19, WuxiaItems.JUMPING_RABBIT_PILL, 5, 3)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(150f, 350f, net.minecraft.init.Items.RABBIT_FOOT)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_RUNNERS_ESSENCE_PILL = new Recipe("runners_essence_pill_recipe",
			580f, 740f, 17, WuxiaItems.RUNNERS_ESSENCE_PILL, 4, 2)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR)
			.addRecipeItem(550f, 800f, net.minecraft.init.Items.GLOWSTONE_DUST)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_TURTLE_SHELL_PILL = new Recipe("turtle_shell_pill_recipe",
			800f, 1100f, 41, WuxiaItems.TURTLE_SHELL_PILL, 3, 1)
			.addRecipeItem(300f, 600f, net.minecraft.init.Items.NETHER_WART)
			.addRecipeItem(450f, 650f, net.minecraft.init.Items.LEATHER)
			.addRecipeItem(1100f, 1550f, net.minecraft.init.Items.IRON_INGOT)
			.addRecipeItem(400f, 700f, net.minecraft.init.Items.REDSTONE);

	public static final Recipe RECIPE_MINOR_FASTING_PILL = new Recipe("minor_fasting_pill_recipe",
			150f, 350f, 27, WuxiaItems.MINOR_FASTING_PILL, 3, 1)
			.addRecipeItem(150f, 300f, net.minecraft.init.Items.COOKED_BEEF)
			.addRecipeItem(150f, 300f, net.minecraft.init.Items.COOKED_FISH)
			.addRecipeItem(150f, 300f, net.minecraft.init.Items.COOKED_CHICKEN)
			.addRecipeItem(200f, 350f, net.minecraft.init.Items.POTATO)
			.addRecipeItem(200f, 350f, net.minecraft.init.Items.CARROT)
			.addRecipeItem(200f, 200f, net.minecraft.init.Items.MELON)
			.addRecipeItem(100f, 340f, net.minecraft.init.Items.SUGAR);

}
