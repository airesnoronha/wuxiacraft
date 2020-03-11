package com.airesnor.wuxiacraft.alchemy;

import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Recipe {

	private List<Triple<Float, Float, Item>> recipeOrder;
	private float cookTemperatureMin;
	private float cookTemperatureMax;
	private int cookTime;

	private String name;

	private Item output;
	private int yields;
	private int yieldRange;

	public Recipe(String name, float tempMin, float tempMax, int cookTime, Item output, int yields, int yieldRange) {
		recipeOrder = new ArrayList<>();
		this.name = name;
		this.cookTemperatureMin = tempMin;
		this.cookTemperatureMax = tempMax;
		this.cookTime = cookTime;
		this.output = output;
		this.yields = yields;
		this.yieldRange = yieldRange;

		Recipes.RECIPES.add(this);
	}

	public Recipe addRecipeItem(float minTemperature, float maxTemperature, Item item) {
		this.recipeOrder.add(Triple.of(minTemperature, maxTemperature, item));
		return this;
	}

	public boolean checkRecipe(List<Pair<Float, Item>> inputs) {
		boolean isThis = false;
		for (int i = 0; i < inputs.size(); i++) {
			if (inputs.size() <= recipeOrder.size()) {
				Triple<Float, Float, Item> element = recipeOrder.get(i);
				Pair<Float, Item> input = inputs.get(i);
				if (input.getRight() == element.getRight()) {
					isThis = MathUtils.between(input.getLeft(), element.getLeft(), element.getMiddle());
				} else {
					isThis = false;
					break;
				}
			}
		}
		return isThis;
	}

	public float getCookTemperatureMin() {
		return cookTemperatureMin;
	}

	public float getCookTemperatureMax() {
		return cookTemperatureMax;
	}

	public int getCookTime() {
		return cookTime;
	}

	public String getName() {
		return I18n.format("wuxiacraft.recipes.name." + this.getUnlocalizedName());
	}

	public String getUnlocalizedName() {
		return name;
	}

	public Item getCatalyst() {
		return recipeOrder.get(recipeOrder.size() - 1).getRight();
	}

	public Item getOutput() {
		return this.output;
	}

	public int getRecipeItemCount() {
		return this.recipeOrder.size();
	}

	public int getYieldResult() {
		Random random = new Random();
		int range = random.nextInt(this.yieldRange * 2) - this.yieldRange;
		return this.yields + range;
	}
}