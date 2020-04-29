package com.airesnor.wuxiacraft.utils;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.items.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class GrinderRecipes {

	private static final GrinderRecipes INSTANCE = new GrinderRecipes();

	public static GrinderRecipes getInstance() {
		return INSTANCE;
	}

	private final Map<ItemStack, ItemStack[]> outputs = new HashMap<>();
	private final Map<ItemStack, float[]> chances = new HashMap<>();
	private final Map<ItemStack, Double> costs = new HashMap<>();

	private GrinderRecipes() {
		this.addGrinding(new ItemStack(ItemBlock.getItemFromBlock(Blocks.IRON_ORE)),
				new ItemStack[]{
						new ItemStack(Items.DUST_IRON, 2),
						new ItemStack(Items.DUST_IRON),
						new ItemStack(Items.DUST_GOLD),
						ItemStack.EMPTY},
				new float[]{1f, 0.1f, 0.005f, 0f},
				70.0);
		this.addGrinding(new ItemStack(ItemBlock.getItemFromBlock(Blocks.GOLD_ORE)),
				new ItemStack[]{
						new ItemStack(Items.DUST_GOLD, 2),
						new ItemStack(Items.DUST_IRON),
						new ItemStack(Items.DUST_GOLD),
						ItemStack.EMPTY},
				new float[]{1f, 0.1f, 0.005f, 0f},
				80.0);
	}

	public void addGrinding(ItemStack input, ItemStack[] output, float[] chances, double energy) {
		if (output.length != 4) {
			WuxiaCraft.logger.error("Grinder recipe output is not a 4 sized array. Skipping");
			return;
		}
		if (chances.length != 4) {
			WuxiaCraft.logger.error("Grinder recipe chances is not a 4 sized array. Skipping");
			return;
		}
		if (energy < 0) {
			WuxiaCraft.logger.error("Grinder recipe energy was negative. Skipping");
			return;
		}
		this.outputs.put(input, output);
		this.chances.put(input, chances);
		this.costs.put(input, energy);
	}

	@Nonnull
	public ItemStack[] getOutputFromInput(ItemStack input) {
		for (Map.Entry<ItemStack, ItemStack[]> entry : this.outputs.entrySet()) {
			if (this.compareItemStacks(input, entry.getKey())) {
				return entry.getValue();
			}
		}
		return new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
	}

	@Nonnull
	public float[] getChancesFromInput(ItemStack input) {
		for (Map.Entry<ItemStack, float[]> entry : this.chances.entrySet()) {
			if (this.compareItemStacks(input, entry.getKey())) {
				return entry.getValue();
			}
		}
		return new float[]{0, 0, 0, 0};
	}

	public double getCostFromInput(ItemStack input) {
		for (Map.Entry<ItemStack, Double> entry : this.costs.entrySet()) {
			if (this.compareItemStacks(input, entry.getKey())) {
				return entry.getValue();
			}
		}
		return 0.0;
	}


	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}

}
