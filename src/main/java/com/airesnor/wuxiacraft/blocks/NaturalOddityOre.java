package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.items.Items;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

public class NaturalOddityOre extends BlockBase {
	public NaturalOddityOre(String name) {
		super(name, Material.ROCK);
		this.setHardness(50f);
		this.setResistance(25f);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.NATURAL_ADDITY_LOW;
	}

	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(10) == 1 ? 2 : 1;
	}
}
