package com.airesnor.wuxiacraft.alchemy;

import akka.japi.Pair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

	private List<Pair<Integer, Item>> recipeOrder;

	public Recipe() {
		recipeOrder = new ArrayList<>();
	}

	public Recipe addRecipeItem(int temperature, Item item) {
		this.recipeOrder.add(Pair.apply(temperature, item));
		return this;
	}


}
