package com.airesnor.wuxiacraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class Blocks {
	public static final List<Block> BLOCKS = new ArrayList<>();

	public static Block NATURAL_ODDITY_ORE = new NaturalOddityOre("natural_oddity_ore");
	public static Block IRON_CAULDRON = new Cauldron("cauldron");

}
