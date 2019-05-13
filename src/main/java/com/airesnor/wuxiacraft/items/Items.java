package com.airesnor.wuxiacraft.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Items {

	/**
	 * Contains all the items to be registered
	 */
	public static final List<Item> ITEMS = new ArrayList<>();

	/**
	 * A test item, not in use right now
	 */
	public static final Item CRUNCHYBAR = new ItemBase("crunchy_bar");
	public static final Item NATURAL_ADDITY_LOW = new ItemBase("natural_oddity_low").setCreativeTab(CreativeTabs.MATERIALS);

}
