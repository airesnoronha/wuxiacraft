package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.formation.FormationCoreBlock;
import com.airesnor.wuxiacraft.items.WuxiaItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

@SuppressWarnings("unused")
public class WuxiaBlocks {

	public static final CreativeTabs BLOCKS_TAB = new CreativeTabs("wuxiacraft.blocks") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ItemBlock.getItemFromBlock(IRON_CAULDRON));
		}
	};

	public static final List<Block> BLOCKS = new ArrayList<>();

	public static final Block NATURAL_ODDITY_ORE = new NaturalOddityOre("natural_oddity_ore");

	public static final Block FIERY_STONE = new BlockBase("fiery_stone", Material.ROCK, true, 5.0f, 6.0f, 2); // 2 is iron level
	public static final Block ICY_STONE = new BlockBase("icy_stone", Material.ROCK, true, 5.0f, 6.0f, 2);
	public static final Block METALLIC_STONE = new BlockBase("metallic_stone", Material.IRON, true, 5.0f, 6.0f, 2);

	public static final Block IRON_CAULDRON = new Cauldron("iron_cauldron");
	public static final Block SPIRIT_STONE_STACK_BLOCK = new SpiritStoneStackBlock("spirit_stone_stack_block");

	public static final Block PAINT_RUNE = new BlockRune("paint_rune");
	public static final Map<String, Block> BLOOD_RUNES = new HashMap<>();

	public static void initBloodRunes() {
		for (CultivationLevel level : CultivationLevel.REGISTERED_LEVELS.values()) {
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

	public static final Block WEAK_LIFE_STONE_VEIN = new SpiritVeinOre("weak_life_stone_vein").setDroppedItem(WuxiaItems.WEAK_LIFE_STONE);
	public static final Block SOUL_STONE_VEIN = new SpiritVeinOre("soul_stone_vein").setDroppedItem(WuxiaItems.SOUL_STONE);
	public static final Block PRIMORDIAL_STONE = new SpiritVeinOre("primordial_stone_vein").setDroppedItem(WuxiaItems.PRIMORDIAL_STONE);
	public static final Block FIVE_ELEMENT_PURE_CRYSTAL_VEIN = new SpiritVeinOre("five_element_pure_crystal_vein").setDroppedItem(WuxiaItems.FIVE_ELEMENT_PURE_CRYSTAL);
	public static final Block PURE_QI_CRYSTAL_VEIN = new SpiritVeinOre("pure_qi_crystal_vein").setDroppedItem(WuxiaItems.PURE_QI_CRYSTAL);
	public static final Block EARTH_LAW_CRYSTAL_VEIN = new SpiritVeinOre("earth_law_crystal_vein").setDroppedItem(WuxiaItems.EARTH_LAW_CRYSTAL);
	public static final Block SKY_LAW_CRYSTAL_VEIN = new SpiritVeinOre("sky_law_crystal_vein").setDroppedItem(WuxiaItems.SKY_LAW_CRYSTAL);
	public static final Block HEAVENLY_STONE_VEIN = new SpiritVeinOre("heavenly_stone_vein").setDroppedItem(WuxiaItems.HEAVENLY_STONE);
	public static final Block RAINBOW_LAW_STONE_VEIN = new SpiritVeinOre("rainbow_law_stone_vein").setDroppedItem(WuxiaItems.RAINBOW_LAW_STONE);
	public static final Block SKY_AND_EARTH_CRYSTAL_VEIN = new SpiritVeinOre("sky_and_earth_crystal_vein").setDroppedItem(WuxiaItems.SKY_AND_EARTH_CRYSTAL);
	public static final Block LAW_NEXUS_STONE_VEIN = new SpiritVeinOre("law_nexus_stone_vein").setDroppedItem(WuxiaItems.LAW_NEXUS_STONE);
	public static final Block WAR_CRYSTAL_VEIN = new SpiritVeinOre("war_crystal_vein").setDroppedItem(WuxiaItems.WAR_CRYSTAL);
	public static final Block GOLD_SPIRIT_STONE_VEIN = new SpiritVeinOre("gold_spirit_stone_vein").setDroppedItem(WuxiaItems.GOLD_SPIRIT_STONE);
	public static final Block YIN_YANG_STONE_VEIN = new SpiritVeinOre("yin_yang_stone_vein").setDroppedItem(WuxiaItems.YIN_YANG_STONE);
	public static final Block TRANSCENDENT_CRYSTAL_VEIN = new SpiritVeinOre("transcendent_crystal_vein").setDroppedItem(WuxiaItems.TRANSCENDENT_CRYSTAL);
	public static final Block IMMORTALITY_STONE_VEIN = new SpiritVeinOre("immortality_stone_vein").setDroppedItem(WuxiaItems.IMMORTALITY_STONE);
	public static final Block ASCENDED_IMMORTALITY_STONE_VEIN = new SpiritVeinOre("ascended_immortality_stone_vein").setDroppedItem(WuxiaItems.ASCENDED_IMMORTALITY_STONE);
	public static final Block IMMORTAL_WILL_STONE_VEIN = new SpiritVeinOre("immortal_will_stone_vein").setDroppedItem(WuxiaItems.IMMORTAL_WILL_STONE);
	public static final Block STELLAR_STONE_VEIN = new SpiritVeinOre("stellar_stone_vein").setDroppedItem(WuxiaItems.STELLAR_STONE);
	public static final Block DIVINE_ORIGIN_STONE_VEIN = new SpiritVeinOre("divine_origin_stone_vein").setDroppedItem(WuxiaItems.DIVINE_ORIGIN_STONE);
	public static final Block BOUNDLESS_VOID_CRYSTAL_VEIN = new SpiritVeinOre("boundless_void_crystal_vein").setDroppedItem(WuxiaItems.BOUNDLESS_VOID_CRYSTAL);
	public static final Block PRIMORDIAL_CHAOS_STONE_VEIN = new SpiritVeinOre("primordial_chaos_stone_vein").setDroppedItem(WuxiaItems.PRIMORDIAL_CHAOS_STONE);

	public static final Block MAGICAL_GRINDER = new MagicalGrinder("magical_grinder");

}
