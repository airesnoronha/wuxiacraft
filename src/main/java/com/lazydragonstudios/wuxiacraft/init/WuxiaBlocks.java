package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.blocks.*;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.formation.FormationMaterialTier;
import com.lazydragonstudios.wuxiacraft.formation.FormationStat;
import com.lazydragonstudios.wuxiacraft.formation.FormationSystemStat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.math.BigDecimal;
import java.util.HashMap;

public class WuxiaBlocks {

	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(Block.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<Block> TECHNIQUE_INSCRIBER = BLOCKS.register("technique_inscriber",
			() -> new TechniqueInscriber(BlockBehaviour.Properties.of(Material.WOOD).strength(3f)));

	public static RegistryObject<Block> FORMATION_CORE_BASE = BLOCKS.register("formation_core_base",
			() -> new FormationCoreBaseBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f)));

	public static RegistryObject<Block> RUNEMAKING_TABLE = BLOCKS.register("runemaking_table",
			() -> new RunemakingTableBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f)));

	public static RegistryObject<Block> OAK_FORMATION_CORE = BLOCKS.register("oak_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), Blocks.OAK_LOG));

	public static RegistryObject<Block> BIRCH_FORMATION_CORE = BLOCKS.register("birch_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), Blocks.BIRCH_LOG));

	public static RegistryObject<Block> SPRUCE_FORMATION_CORE = BLOCKS.register("spruce_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), Blocks.SPRUCE_LOG));

	public static RegistryObject<Block> JUNGLE_FORMATION_CORE = BLOCKS.register("jungle_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), Blocks.JUNGLE_LOG));

	public static RegistryObject<Block> ACACIA_FORMATION_CORE = BLOCKS.register("acacia_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), Blocks.ACACIA_LOG));

	public static RegistryObject<Block> DARK_OAK_FORMATION_CORE = BLOCKS.register("dark_oak_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), Blocks.DARK_OAK_LOG));

	public static RegistryObject<Block> STONE_FORMATION_CORE = BLOCKS.register("stone_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), 2, Blocks.STONE));

	public static RegistryObject<Block> COPPER_FORMATION_CORE = BLOCKS.register("copper_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), 3, Blocks.COPPER_BLOCK));

	public static RegistryObject<Block> IRON_FORMATION_CORE = BLOCKS.register("iron_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), 3, Blocks.IRON_BLOCK));

	public static RegistryObject<Block> LAPIS_FORMATION_CORE = BLOCKS.register("lapis_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), 2, Blocks.LAPIS_BLOCK));

	public static RegistryObject<Block> GOLD_FORMATION_CORE = BLOCKS.register("gold_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), 4, Blocks.GOLD_BLOCK));

	public static RegistryObject<Block> DIAMOND_FORMATION_CORE = BLOCKS.register("diamond_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), 5, Blocks.DIAMOND_BLOCK));

	public static RegistryObject<Block> EMERALD_FORMATION_CORE = BLOCKS.register("emerald_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f), 5, Blocks.EMERALD_BLOCK));

	public static HashMap<FormationMaterialTier, RegistryObject<Block>> GENERATION_RUNES = new HashMap<>();
	public static HashMap<FormationMaterialTier, RegistryObject<Block>> BARRIER_RUNES = new HashMap<>();
	public static HashMap<System, HashMap<FormationMaterialTier, RegistryObject<Block>>> ENERGY_RUNES = new HashMap<>();
	public static HashMap<System, HashMap<FormationMaterialTier, RegistryObject<Block>>> CULTIVATION_RUNES = new HashMap<>();

	static {
		for (var material : FormationMaterialTier.values()) {
			GENERATION_RUNES.put(material, BLOCKS.register(material.name().toLowerCase() + "_generation_rune",
					() -> new StatRuneBlock(BlockBehaviour.Properties.of(material.blockMaterial).strength(material.blockStrength))
							.addStat(FormationStat.ENERGY_GENERATION, material.materialModifier)
			));
			BARRIER_RUNES.put(material, BLOCKS.register(material.name().toLowerCase() + "_barrier_rune",
					() -> new StatRuneBlock(BlockBehaviour.Properties.of(material.blockMaterial).strength(material.blockStrength))
							.addStat(FormationStat.ENERGY_COST, material.materialModifier.multiply(new BigDecimal("8")))
							.addStat(FormationStat.BARRIER_RANGE, BigDecimal.TEN.add(material.materialModifier.multiply(new BigDecimal("2"))))
							.addStat(FormationStat.BARRIER_MAX_AMOUNT, new BigDecimal("20").add(material.materialModifier.multiply(new BigDecimal("2"))))
							.addStat(FormationStat.BARRIER_STRENGTH, material.materialModifier)
							.addStat(FormationStat.BARRIER_REGEN, material.materialModifier.multiply(new BigDecimal("0.6")))
			));
			for (var system : System.values()) {
				ENERGY_RUNES.putIfAbsent(system, new HashMap<>());
				CULTIVATION_RUNES.putIfAbsent(system, new HashMap<>());
				ENERGY_RUNES.get(system).put(material, BLOCKS.register(system.name().toLowerCase() + "_" + material.name().toLowerCase() + "_energy_rune",
						() -> new StatRuneBlock(BlockBehaviour.Properties.of(material.blockMaterial).strength(material.blockStrength))
								.addStat(FormationStat.ENERGY_COST, material.materialModifier.multiply(new BigDecimal("4")))
								.addStat(system, FormationSystemStat.ENERGY_REGEN, material.materialModifier.multiply(new BigDecimal("0.005")))
								.addStat(system, FormationSystemStat.ENERGY_REGEN_RUNE_COUNT, BigDecimal.ONE)
								.addStat(system, FormationSystemStat.ENERGY_REGEN_RANGE, new BigDecimal("4").add(material.materialModifier))
				));
				CULTIVATION_RUNES.get(system).put(material, BLOCKS.register(system.name().toLowerCase() + "_" + material.name().toLowerCase() + "_cultivation_rune",
						() -> new StatRuneBlock(BlockBehaviour.Properties.of(material.blockMaterial).strength(material.blockStrength))
								.addStat(FormationStat.ENERGY_COST, material.materialModifier.multiply(new BigDecimal("3")))
								.addStat(system, FormationSystemStat.CULTIVATION_SPEED, material.materialModifier.multiply(new BigDecimal("0.5")))
								.addStat(system, FormationSystemStat.CULTIVATION_RUNE_COUNT, BigDecimal.ONE)
				));
			}
		}
	}

}
