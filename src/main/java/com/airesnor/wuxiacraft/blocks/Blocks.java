package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.formation.FormationCoreBlock;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Blocks {

	public static final CreativeTabs BLOCKS_TAB = new CreativeTabs("wuxiacraft.blocks") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ItemBlock.getItemFromBlock(IRON_CAULDRON));
		}
	};

	public static final List<Block> BLOCKS = new ArrayList<>();

	public static final Block NATURAL_ODDITY_ORE = new NaturalOddityOre("natural_oddity_ore");
	public static final Block IRON_CAULDRON = new Cauldron("iron_cauldron");
	public static final Block SPIRIT_STONE_STACK_BLOCK = new SpiritStoneStackBlock("spirit_stone_stack_block");

	public static final Block PAINT_RUNE = new BlockRune("paint_rune");
	public static final Map<String, Block> BLOOD_RUNES = new HashMap<>();
	static {
		for(CultivationLevel level : CultivationLevel.values()) {
			BLOOD_RUNES.put(level.getUName(), new BlockRune("blood_rune_" + level.getUName()));
		}
	}

	public static final Block FORMATION_CORE = new FormationCoreBlock("formation_core");

}
