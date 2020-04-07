package com.airesnor.wuxiacraft.items;

public class ItemDagger extends ItemBase {

	public ItemDagger(String item_name) {
		super(item_name);
		setMaxDamage(ToolMaterial.GOLD.getMaxUses());
		setMaxStackSize(1);
	}



}
