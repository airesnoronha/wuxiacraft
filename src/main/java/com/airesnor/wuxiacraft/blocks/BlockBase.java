package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.items.IHasModel;
import com.airesnor.wuxiacraft.items.Items;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel {
	public BlockBase(String name, Material materialIn) {
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(Blocks.BLOCKS_TAB);

		Blocks.BLOCKS.add(this);
		Items.ITEMS.add(new ItemBlock(this).setRegistryName(name));
	}

	@Override
	public void registerModels() {
		WuxiaCraft.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
