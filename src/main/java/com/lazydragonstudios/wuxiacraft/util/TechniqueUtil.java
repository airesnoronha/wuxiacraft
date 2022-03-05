package com.lazydragonstudios.wuxiacraft.util;

import com.lazydragonstudios.wuxiacraft.init.WuxiaElements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.HashMap;

public class TechniqueUtil {

	private static final HashMap<Item, HashMap<ResourceLocation, BigDecimal>> DEVOURING_ITEM_TO_REWARD = new HashMap<>();

	public static void initDevouringData() {
		addDevouringData(Items.DIRT, WuxiaElements.EARTH.getId(), new BigDecimal("0.9"));
		addDevouringData(Items.STONE, WuxiaElements.EARTH.getId(), new BigDecimal("1.4"));
		addDevouringData(Items.COBBLESTONE, WuxiaElements.EARTH.getId(), new BigDecimal("1.3"));
		addDevouringData(Items.GRASS_BLOCK, WuxiaElements.EARTH.getId(), new BigDecimal("0.7"));
		addDevouringData(Items.GRASS_BLOCK, WuxiaElements.WOOD.getId(), new BigDecimal("0.4"));
		addDevouringData(Items.COAL, WuxiaElements.EARTH.getId(), new BigDecimal("6"));
		addDevouringData(Items.COAL, WuxiaElements.FIRE.getId(), new BigDecimal("1"));
		addDevouringData(Items.CHARCOAL, WuxiaElements.WOOD.getId(), new BigDecimal("4"));
		addDevouringData(Items.CHARCOAL, WuxiaElements.FIRE.getId(), new BigDecimal("1"));
		addDevouringData(Items.STONE_BUTTON, WuxiaElements.EARTH.getId(), new BigDecimal("1"));
		addDevouringData(Items.BRICK, WuxiaElements.EARTH.getId(), new BigDecimal("1"));
		addDevouringData(Items.BRICK, WuxiaElements.FIRE.getId(), new BigDecimal("1"));
		addDevouringData(Items.CLAY, WuxiaElements.EARTH.getId(), new BigDecimal("1"));
		addDevouringData(Items.CLAY, WuxiaElements.WATER.getId(), new BigDecimal("1"));
		addDevouringData(Items.ANVIL, WuxiaElements.METAL.getId(), new BigDecimal("20"));
		addDevouringData(Items.GLOWSTONE_DUST, WuxiaElements.LIGHT.getId(), new BigDecimal("1"));
		addDevouringData(Items.GLOWSTONE, WuxiaElements.LIGHT.getId(), new BigDecimal("2"));
		addDevouringData(Items.ACACIA_WOOD, WuxiaElements.WOOD.getId(), new BigDecimal("3"));
		addDevouringData(Items.BIRCH_WOOD, WuxiaElements.WOOD.getId(), new BigDecimal("3"));
		addDevouringData(Items.DARK_OAK_WOOD, WuxiaElements.WOOD.getId(), new BigDecimal("3"));
		addDevouringData(Items.JUNGLE_WOOD, WuxiaElements.WOOD.getId(), new BigDecimal("3"));
		addDevouringData(Items.OAK_WOOD, WuxiaElements.WOOD.getId(), new BigDecimal("3"));
		addDevouringData(Items.SPRUCE_WOOD, WuxiaElements.WOOD.getId(), new BigDecimal("3"));
		addDevouringData(Items.SPRUCE_WOOD, WuxiaElements.WOOD.getId(), new BigDecimal("0.5"));
		addDevouringData(Items.ACACIA_PLANKS, WuxiaElements.WOOD.getId(), new BigDecimal("1"));
		addDevouringData(Items.BIRCH_PLANKS, WuxiaElements.WOOD.getId(), new BigDecimal("1"));
		addDevouringData(Items.DARK_OAK_PLANKS, WuxiaElements.WOOD.getId(), new BigDecimal("1"));
		addDevouringData(Items.JUNGLE_PLANKS, WuxiaElements.WOOD.getId(), new BigDecimal("1"));
		addDevouringData(Items.OAK_PLANKS, WuxiaElements.WOOD.getId(), new BigDecimal("1"));
		addDevouringData(Items.SPRUCE_PLANKS, WuxiaElements.WOOD.getId(), new BigDecimal("1"));
		addDevouringData(Items.IRON_INGOT, WuxiaElements.METAL.getId(), new BigDecimal("1"));
		addDevouringData(Items.IRON_BLOCK, WuxiaElements.METAL.getId(), new BigDecimal("7")); //loss intended, in many cases
		addDevouringData(Items.IRON_ORE, WuxiaElements.METAL.getId(), new BigDecimal("0.8"));
		addDevouringData(Items.IRON_ORE, WuxiaElements.EARTH.getId(), new BigDecimal("0.6"));
		addDevouringData(Items.COPPER_ORE, WuxiaElements.METAL.getId(), new BigDecimal("0.6"));
		addDevouringData(Items.COPPER_ORE, WuxiaElements.EARTH.getId(), new BigDecimal("0.6"));
		addDevouringData(Items.COAL_ORE, WuxiaElements.EARTH.getId(), new BigDecimal("6"));
		addDevouringData(Items.COAL_ORE, WuxiaElements.FIRE.getId(), new BigDecimal("0.6"));
		addDevouringData(Items.DIAMOND, WuxiaElements.EARTH.getId(), new BigDecimal("32"));
		addDevouringData(Items.EMERALD, WuxiaElements.EARTH.getId(), new BigDecimal("38"));
	}

	private static void addDevouringData(Item item, ResourceLocation element, BigDecimal value) {
		DEVOURING_ITEM_TO_REWARD.putIfAbsent(item, new HashMap<>());
		DEVOURING_ITEM_TO_REWARD.get(item).put(element, value);
	}

	@Nonnull
	public static HashMap<ResourceLocation, BigDecimal> getDevouringDataPerItem(Item item) {
		return DEVOURING_ITEM_TO_REWARD.getOrDefault(item, new HashMap<>());
	}


}
