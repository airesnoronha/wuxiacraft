package com.airesnor.wuxiacraft.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.Random;

public class SpiritVeinOre extends BlockBase {

	private Item droppedItem;

	public SpiritVeinOre(String name, Material materialIn) {
		super(name, materialIn);
		this.setResistance(25f);
		this.setLightLevel(3f);
		this.setHardness(120f);
		this.setHarvestLevel("pickaxe", 3);
	}

	@Override
	@Nonnull
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this.droppedItem;
	}

	@Override
	public int quantityDropped(Random random) {
		return super.quantityDropped(random);
	}

	public SpiritVeinOre setDroppedItem(Item drop) {
		this.droppedItem = drop;
		return this;
	}
}
