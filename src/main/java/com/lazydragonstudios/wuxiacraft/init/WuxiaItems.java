package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.item.SpiritStone;
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
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB)));

	public static RegistryObject<Item> SPIRIT_STONE_2 = ITEMS.register("spirit_stone_2",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB)));

	public static RegistryObject<Item> SPIRIT_STONE_3 = ITEMS.register("spirit_stone_3",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB)));

	public static RegistryObject<Item> SPIRIT_STONE_4 = ITEMS.register("spirit_stone_4",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB)));

	public static RegistryObject<Item> SPIRIT_STONE_5 = ITEMS.register("spirit_stone_5",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB)));

	public static RegistryObject<Item> SPIRIT_STONE_6 = ITEMS.register("spirit_stone_6",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB)));

	public static RegistryObject<Item> SPIRIT_STONE_7 = ITEMS.register("spirit_stone_7",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB)));

	public static RegistryObject<Item> SPIRIT_STONE_8 = ITEMS.register("spirit_stone_8",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB)));

	public static RegistryObject<Item> SPIRIT_STONE_9 = ITEMS.register("spirit_stone_9",
			() -> new SpiritStone(new Item.Properties().tab(WUXIACRAFT_TAB)));

}
