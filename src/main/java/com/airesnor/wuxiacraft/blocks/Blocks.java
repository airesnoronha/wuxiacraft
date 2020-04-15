package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.formation.FormationCoreBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

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

	//Training posts
	public static final Map<String, Block> TRAINING_POSTS = new HashMap<>();
	static {
		List<Triple<String, Double, Material>> tiers =new ArrayList<>();
		tiers.add(Triple.of("stick", 0.5, Material.WOOD));
		tiers.add(Triple.of("stone", 1.25, Material.ROCK));
		tiers.add(Triple.of("iron", 2.5, Material.IRON));
		tiers.add(Triple.of("diamond", 4.0, Material.IRON));
		for(BlockPlanks.EnumType wood: BlockPlanks.EnumType.values()) {
			for(Triple<String, Double, Material> triple : tiers) {
				String name = "training_post_"+wood.getName()+"_"+triple.getLeft();
				TRAINING_POSTS.put(name, new BlockTrainingPost(name, triple.getRight(), triple.getMiddle()));
			}
		}
	}

}
