package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {
	public ItemBase(String item_name) {
		setUnlocalizedName(item_name);
		setRegistryName(item_name);
		setCreativeTab(CreativeTabs.FOOD);

		Items.ITEMS.add(this);
	}

	@Override
	public void registerModels() {
		WuxiaCraft.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
