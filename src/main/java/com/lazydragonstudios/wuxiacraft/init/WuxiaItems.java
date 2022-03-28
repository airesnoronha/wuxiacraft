package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.item.SpatialItem;
import com.lazydragonstudios.wuxiacraft.formation.FormationMaterialTier;
import com.lazydragonstudios.wuxiacraft.item.SpiritStone;
import com.lazydragonstudios.wuxiacraft.item.TechniqueManual;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.HashMap;

@SuppressWarnings("unused")
public class WuxiaItems {

	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(Item.class, WuxiaCraft.MOD_ID);

	public static CreativeModeTab WUXIACRAFT_TAB = new CreativeModeTab("wuxiacraft") {
		@Nonnull
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(SPIRIT_STONE_1.get());
		}
	};

	public static CreativeModeTab FORMATIONS_TAB = new CreativeModeTab("wuxiacraft") {
		@Nonnull
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(DIAMOND_FORMATION_CORE.get());
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

	public static RegistryObject<Item> FORMATION_CORE_BASE = ITEMS.register("formation_core_base",
			() -> new BlockItem(WuxiaBlocks.FORMATION_CORE_BASE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> OAK_FORMATION_CORE = ITEMS.register("oak_formation_core",
			() -> new BlockItem(WuxiaBlocks.OAK_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> BIRCH_FORMATION_CORE = ITEMS.register("birch_formation_core",
			() -> new BlockItem(WuxiaBlocks.BIRCH_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> SPRUCE_FORMATION_CORE = ITEMS.register("spruce_formation_core",
			() -> new BlockItem(WuxiaBlocks.SPRUCE_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> JUNGLE_FORMATION_CORE = ITEMS.register("jungle_formation_core",
			() -> new BlockItem(WuxiaBlocks.JUNGLE_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> ACACIA_FORMATION_CORE = ITEMS.register("acacia_formation_core",
			() -> new BlockItem(WuxiaBlocks.ACACIA_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> DARK_OAK_FORMATION_CORE = ITEMS.register("dark_oak_formation_core",
			() -> new BlockItem(WuxiaBlocks.DARK_OAK_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> STONE_FORMATION_CORE = ITEMS.register("stone_formation_core",
			() -> new BlockItem(WuxiaBlocks.STONE_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> COPPER_FORMATION_CORE = ITEMS.register("copper_formation_core",
			() -> new BlockItem(WuxiaBlocks.COPPER_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> IRON_FORMATION_CORE = ITEMS.register("iron_formation_core",
			() -> new BlockItem(WuxiaBlocks.IRON_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> LAPIS_FORMATION_CORE = ITEMS.register("lapis_formation_core",
			() -> new BlockItem(WuxiaBlocks.LAPIS_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> GOLD_FORMATION_CORE = ITEMS.register("gold_formation_core",
			() -> new BlockItem(WuxiaBlocks.GOLD_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> DIAMOND_FORMATION_CORE = ITEMS.register("diamond_formation_core",
			() -> new BlockItem(WuxiaBlocks.DIAMOND_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static RegistryObject<Item> EMERALD_FORMATION_CORE = ITEMS.register("emerald_formation_core",
			() -> new BlockItem(WuxiaBlocks.EMERALD_FORMATION_CORE.get(), new Item.Properties().tab(FORMATIONS_TAB)));

	public static HashMap<FormationMaterialTier, RegistryObject<Item>> GENERATION_RUNES = new HashMap<>();
	public static HashMap<FormationMaterialTier, RegistryObject<Item>> BARRIER_RUNES = new HashMap<>();
	public static HashMap<System, HashMap<FormationMaterialTier, RegistryObject<Item>>> ENERGY_RUNES = new HashMap<>();
	public static HashMap<System, HashMap<FormationMaterialTier, RegistryObject<Item>>> CULTIVATION_RUNES = new HashMap<>();

	static {
		for (var material : FormationMaterialTier.values()) {
			GENERATION_RUNES.put(material, ITEMS.register(material.name().toLowerCase() + "_generation_rune",
					() -> new BlockItem(WuxiaBlocks.GENERATION_RUNES.get(material).get(), new Item.Properties().tab(FORMATIONS_TAB))
			));
			BARRIER_RUNES.put(material, ITEMS.register(material.name().toLowerCase() + "_barrier_rune",
					() -> new BlockItem(WuxiaBlocks.BARRIER_RUNES.get(material).get(), new Item.Properties().tab(FORMATIONS_TAB))
			));
			for (var system : System.values()) {
				ENERGY_RUNES.putIfAbsent(system, new HashMap<>());
				CULTIVATION_RUNES.putIfAbsent(system, new HashMap<>());
				ENERGY_RUNES.get(system).put(material, ITEMS.register(system.name().toLowerCase() + "_" + material.name().toLowerCase() + "_energy_rune",
						() -> new BlockItem(WuxiaBlocks.ENERGY_RUNES.get(system).get(material).get(), new Item.Properties().tab(FORMATIONS_TAB))
				));
				CULTIVATION_RUNES.get(system).put(material, ITEMS.register(system.name().toLowerCase() + "_" + material.name().toLowerCase() + "_cultivation_rune",
						() -> new BlockItem(WuxiaBlocks.CULTIVATION_RUNES.get(system).get(material).get(), new Item.Properties().tab(FORMATIONS_TAB))
				));
			}
		}
	}

	// Test Item
	public static RegistryObject<Item> SPATIAL_RING = ITEMS.register("spatial_ring",
			() -> new SpatialItem(new Item.Properties().tab(WUXIACRAFT_TAB), 3, 9));

}
