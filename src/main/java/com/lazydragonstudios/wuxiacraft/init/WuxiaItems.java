package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.item.SpiritStone;
import com.lazydragonstudios.wuxiacraft.item.TechniqueManual;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class WuxiaItems {

	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(Item.class, WuxiaCraft.MOD_ID);

	public static CreativeModeTab WUXIACRAFT_TAB = new CreativeModeTab("wuxiacraft") {
		@Nonnull
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(SPIRIT_STONE_1.get());
		}
	};

	public static RegistryObject<Item> TECHNIQUE_INSCRIBER = ITEMS.register("technique_inscriber",
			() -> new BlockItem(WuxiaBlocks.TECHNIQUE_INSCRIBER.get(), new Item.Properties().tab(WUXIACRAFT_TAB)));

	public static RegistryObject<Item> SPIRIT_STONE_1 = ITEMS.register("spirit_stone_1",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB), 0));

	public static RegistryObject<Item> SPIRIT_STONE_2 = ITEMS.register("spirit_stone_2",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB), 1));

	public static RegistryObject<Item> SPIRIT_STONE_3 = ITEMS.register("spirit_stone_3",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB), 2));

	public static RegistryObject<Item> SPIRIT_STONE_4 = ITEMS.register("spirit_stone_4",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB), 3));

	public static RegistryObject<Item> SPIRIT_STONE_5 = ITEMS.register("spirit_stone_5",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB), 4));

	public static RegistryObject<Item> SPIRIT_STONE_6 = ITEMS.register("spirit_stone_6",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB), 5));

	public static RegistryObject<Item> SPIRIT_STONE_7 = ITEMS.register("spirit_stone_7",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB), 6));

	public static RegistryObject<Item> SPIRIT_STONE_8 = ITEMS.register("spirit_stone_8",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB), 7));

	public static RegistryObject<Item> SPIRIT_STONE_9 = ITEMS.register("spirit_stone_9",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB), 8));

	public static RegistryObject<Item> BODY_MANUAL = ITEMS.register("body_manual",
			() -> new TechniqueManual(new Item.Properties().stacksTo(1), System.BODY));

	public static RegistryObject<Item> DIVINE_MANUAL = ITEMS.register("divine_manual",
			() -> new TechniqueManual(new Item.Properties().stacksTo(1), System.DIVINE));

	public static RegistryObject<Item> ESSENCE_MANUAL = ITEMS.register("essence_manual",
			() -> new TechniqueManual(new Item.Properties().stacksTo(1), System.ESSENCE));

}
