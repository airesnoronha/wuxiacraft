package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {
	public ItemBase(String item_name) {
		setUnlocalizedName(item_name);
		setRegistryName(item_name);
		setCreativeTab(Items.WUXIACRAFT_GENERAL);

		Items.ITEMS.add(this);
	}

	@Override
	public void registerModels() {
		WuxiaCraft.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
