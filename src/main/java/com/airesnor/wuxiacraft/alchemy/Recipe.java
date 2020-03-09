package com.airesnor.wuxiacraft.alchemy;

import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.item.Item;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

	private List<Triple<Float, Float, Item>> recipeOrder;

	public Recipe() {
		recipeOrder = new ArrayList<>();
	}

	public Recipe addRecipeItem(float minTemperature, float maxTemperature, Item item) {
		this.recipeOrder.add(Triple.of(minTemperature, maxTemperature, item));
		return this;
	}

	public boolean checkRecipe (List<Pair<Float, Item>> inputs) {
		boolean isThis = false;
		for(int i = 0; i< inputs.size(); i++) {
			Triple<Float, Float, Item> element = recipeOrder.get(i);
			Pair<Float, Item> input = inputs.get(i);
			if(input.getRight() == element.getRight()) {
				isThis = MathUtils.between(input.getLeft(), element.getLeft(), element.getMiddle());
			}
		}
		return isThis;
	}

}
