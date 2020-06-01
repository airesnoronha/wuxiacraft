package com.airesnor.wuxiacraft.items;

import net.minecraft.item.Item;

public class ItemBase extends Item {
	public ItemBase(String item_name) {
		setUnlocalizedName(item_name);
		setRegistryName(item_name);
		setCreativeTab(WuxiaItems.WUXIACRAFT_GENERAL);

		WuxiaItems.ITEMS.add(this);
	}
}
