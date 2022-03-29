package com.lazydragonstudios.wuxiacraft.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RuneMakingRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RuneMakingRecipe> {

	public RuneMakingRecipeSerializer() {
	}

	@Override
	public RuneMakingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
		JsonElement jsonelement = GsonHelper.isArrayNode(pJson, "ingredient") ? GsonHelper.getAsJsonArray(pJson, "ingredient") : GsonHelper.getAsJsonObject(pJson, "ingredient");
		if (!pJson.has("rune"))
			throw new com.google.gson.JsonSyntaxException("Missing rune, expected to find an integer or object");
		int rune = GsonHelper.getAsInt(pJson, "rune");
		if (!pJson.has("mining_level"))
			throw new com.google.gson.JsonSyntaxException("Missing mining_level, expected to find a string or object");
		ResourceLocation miningLevel = new ResourceLocation(GsonHelper.getAsString(pJson, "mining_level"));
		Tier requiredTier = TierSortingRegistry.byName(miningLevel);
		if (requiredTier == null)
			throw new com.google.gson.JsonSyntaxException("Mining level not found, expected to find it registered in the sorted tiers registry");
		Ingredient ingredient = Ingredient.fromJson(jsonelement);
		if (!pJson.has("result"))
			throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
		return new RuneMakingRecipe(pRecipeId, rune, requiredTier, ingredient, result);
	}

	@Nullable
	@Override
	public RuneMakingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
		int rune = buf.readInt();
		var miningLevel = buf.readResourceLocation();
		var requiredTier = TierSortingRegistry.byName(miningLevel);
		if (requiredTier == null) return null;
		var ingredient = Ingredient.fromNetwork(buf);
		var result = buf.readItem();
		return new RuneMakingRecipe(recipeId, rune, requiredTier, ingredient, result);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, RuneMakingRecipe recipe) {
		buf.writeInt(recipe.rune);
		var miningLevel = TierSortingRegistry.getName(recipe.requiredMiningLevel);
		if (miningLevel == null) return;
		buf.writeResourceLocation(miningLevel);
		recipe.blockIngredient.toNetwork(buf);
		buf.writeItem(recipe.result);
	}
}
