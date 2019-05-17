package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Items {

	/**
	 * Contains all the items to be registered
	 */
	public static final List<Item> ITEMS = new ArrayList<>();

	public static final CreativeTabs SCROLLS = new CreativeTabs("wuxiacraft.scrolls") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(BODY_STRENGTH_SCROLL);
		}
	};

	/**
	 * A test item, not in use right now
	 */
	public static final Item CRUNCHYBAR = new ItemBase("crunchy_bar");
	public static final Item NATURAL_ADDITY_LOW = new ItemBase("natural_oddity_low").setCreativeTab(CreativeTabs.MATERIALS);
	public static final Item BODY_STRENGTH_SCROLL = new ItemScroll(Techniques.BODY_STRENGTH);
	public static final Item LIGHT_FEET_SCROLL = new ItemScroll(Techniques.LIGHT_FEET);
	public static final Item ASSASSIN_MANUAL_SCROLL = new ItemScroll(Techniques.ASSASSIN_MANUAL);

}
