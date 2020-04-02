package com.airesnor.wuxiacraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Blocks {

	public static final CreativeTabs BLOCKS_TAB = new CreativeTabs("wuxiacraft.blocks") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ItemBlock.getItemFromBlock(IRON_CAULDRON));
		}
	};

	public static final List<Block> BLOCKS = new ArrayList<>();

	public static Block NATURAL_ODDITY_ORE = new NaturalOddityOre("natural_oddity_ore");
	public static Block IRON_CAULDRON = new Cauldron("iron_cauldron");
	public static Block SPIRIT_STONE_STACK_BLOCK = new SpiritStoneStackBlock("spirit_stone_stack_block");

}
