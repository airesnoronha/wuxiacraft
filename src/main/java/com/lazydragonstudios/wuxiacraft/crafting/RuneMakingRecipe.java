package com.lazydragonstudios.wuxiacraft.crafting;

import com.lazydragonstudios.wuxiacraft.blocks.entity.RunemakingTable;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRecipeTypes;
import com.lazydragonstudios.wuxiacraft.item.RuneStencil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.TierSortingRegistry;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RuneMakingRecipe implements Recipe<Container> {

	public static RecipeType<RuneMakingRecipe> recipeType;

	public int rune;
	public ResourceLocation id;
	public Tier requiredMiningLevel;
	public Ingredient blockIngredient;
	public ItemStack result;

	public RuneMakingRecipe(ResourceLocation id, int rune, Tier requiredMiningLevel, Ingredient blockIngredient, ItemStack result) {
		this.id = id;
		this.rune = rune;
		this.requiredMiningLevel = requiredMiningLevel;
		this.blockIngredient = blockIngredient;
		this.result = result;
	}

	@Override
	public boolean matches(Container pContainer, Level pLevel) {
		if(!(pContainer instanceof RunemakingTable table)) return false;
		if(table.getSelectedRune() != this.rune) return false;
		ItemStack stencilItemStack = pContainer.getItem(0);
		if (!(stencilItemStack.getItem() instanceof RuneStencil stencil)) return false;
		var stencilTier = stencil.getTier();
		if (TierSortingRegistry.getTiersLowerThan(this.requiredMiningLevel).contains(stencilTier)) return false;
		ItemStack blockItemStack = pContainer.getItem(1);
		return blockIngredient.test(blockItemStack);
	}

	@Override
	public ItemStack assemble(Container pContainer) {
		return this.result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return this.result;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return WuxiaRecipeTypes.RUNEMAKING_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return recipeType;
	}
}
