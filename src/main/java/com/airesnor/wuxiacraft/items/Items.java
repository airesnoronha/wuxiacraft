package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.blocks.Blocks;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Items {

	/**
	 * Contains all the items to be registered
	 */
	public static final List<Item> ITEMS = new ArrayList<>();

	public static final CreativeTabs SCROLLS = new CreativeTabs("wuxiacraft.scrolls") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(BODY_STRENGTH_SCROLL);
		}
	};

	/**
	 * A test item, not in use right now
	 */
	public static final Item NATURAL_ODDITY_LOW = new ItemBase("natural_oddity_low").setCreativeTab(CreativeTabs.MATERIALS);

	//pellets
	public static final Item BODY_REFINEMENT_PELLET = new ItemProgressPellet("body_refinement_pellet", 30f, 100);
	public static final Item ENERGY_RECOVERY_PELLET = new ItemEnergyPellet("energy_recovery_pellet", 15f);

	//Scrolls
	public static final Item BODY_STRENGTH_SCROLL = new ItemScroll(Techniques.BODY_STRENGTH);
	public static final Item LIGHT_FEET_SCROLL = new ItemScroll(Techniques.LIGHT_FEET);
	public static final Item ASSASSIN_MANUAL_SCROLL = new ItemScroll(Techniques.ASSASSIN_MANUAL);
	public static final Item BASIC_MEDICINE_SCROLL = new ItemScroll(Techniques.BASIC_MEDICINE);
	public static final Item SWORD_HEART_SCROLL = new ItemScroll(Techniques.SWORD_HEART);
	public static final Item AXE_RAGE_SCROLL = new ItemScroll(Techniques.AXE_RAGE);
	public static final Item FIRE_BENDING_SCROLL = new ItemScroll(Techniques.FIRE_BENDING);
	public static final Item MOUNTAIN_RAISER_SCROLL = new ItemScroll(Techniques.MOUNTAIN_RAISER);
	public static final Item METAL_MANIPULATION_SCROLL = new ItemScroll(Techniques.METAL_MANIPULATION);
	public static final Item SURGING_WAVES_SCROLL = new ItemScroll(Techniques.SURGING_WAVES);
	public static final Item BOTANICAL_GROWTH_SCROLL = new ItemScroll(Techniques.BOTANICAL_GROWTH);

	//FANS
	public static final Item FEATHER_FAN = new ItemFan("feather_fan").setMaxStrength(10f).setStrength(1f);

}
