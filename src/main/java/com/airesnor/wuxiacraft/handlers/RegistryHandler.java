package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.Blocks;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.entities.mobs.GiantAnt;
import com.airesnor.wuxiacraft.entities.mobs.GiantBee;
import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import com.airesnor.wuxiacraft.entities.skills.FireThrowable;
import com.airesnor.wuxiacraft.entities.skills.SwordBeamThrowable;
import com.airesnor.wuxiacraft.entities.skills.WaterBladeThrowable;
import com.airesnor.wuxiacraft.entities.skills.WaterNeedleThrowable;
import com.airesnor.wuxiacraft.entities.tileentity.CauldronTileEntity;
import com.airesnor.wuxiacraft.entities.tileentity.GrinderTileEntity;
import com.airesnor.wuxiacraft.entities.tileentity.SpiritStoneStackTileEntity;
import com.airesnor.wuxiacraft.formation.FormationTileEntity;
import com.airesnor.wuxiacraft.items.IHasModel;
import com.airesnor.wuxiacraft.items.Items;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class RegistryHandler {

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		WuxiaCraft.logger.info("Registering items.");
		event.getRegistry().registerAll(Items.ITEMS.toArray(new Item[0]));

	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		WuxiaCraft.logger.info("Registering blocks.");
		event.getRegistry().registerAll(Blocks.BLOCKS.toArray(new Block[0]));

		GameRegistry.registerTileEntity(CauldronTileEntity.class, new ResourceLocation(WuxiaCraft.MOD_ID, "cauldron_tile_entity"));
		GameRegistry.registerTileEntity(SpiritStoneStackTileEntity.class, new ResourceLocation(WuxiaCraft.MOD_ID, "spirit_stone_stack_tile_entity"));
		GameRegistry.registerTileEntity(FormationTileEntity.class, new ResourceLocation(WuxiaCraft.MOD_ID, "formation_tile_entity"));
		GameRegistry.registerTileEntity(GrinderTileEntity.class, new ResourceLocation(WuxiaCraft.MOD_ID, "grinder_tile_entity"));
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Item item : Items.ITEMS) {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModels();
			}
		}
		for (Block block : Blocks.BLOCKS) {
			if (block instanceof IHasModel) {
				((IHasModel) block).registerModels();
			}
		}
	}

	@SubscribeEvent
	public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {
		EntityEntry giantAntEntity = EntityEntryBuilder.create()
				.entity(GiantAnt.class)
				.id(new ResourceLocation(WuxiaCraft.MOD_ID, "giant_ant"),0)
				.name("giant_ant")
				.tracker(80, 3, false)
				.egg(0xDA5917, 0xC35D35)
				.spawn(EnumCreatureType.MONSTER, 35, 1, 3,
						Biomes.BEACH,
						Biomes.BIRCH_FOREST,
						Biomes.DESERT,
						Biomes.DESERT_HILLS,
						Biomes.COLD_TAIGA,
						Biomes.EXTREME_HILLS,
						Biomes.FOREST,
						Biomes.SAVANNA,
						Biomes.SAVANNA_PLATEAU,
						Biomes.TAIGA,
						Biomes.TAIGA_HILLS,
						Biomes.PLAINS,
						Biomes.JUNGLE_EDGE,
						Biomes.MESA
				)
				.build();
		EntityEntry giantBeeEntity = EntityEntryBuilder.create()
				.entity(GiantBee.class)
				.id(new ResourceLocation(WuxiaCraft.MOD_ID, "giant_bee"),2)
				.name("giant_bee")
				.tracker(80, 3, false)
				.egg(0xFACB27, 0x202020)
				.spawn(EnumCreatureType.MONSTER, 35, 1, 2,
						Biomes.BEACH,
						Biomes.BIRCH_FOREST,
						Biomes.DESERT,
						Biomes.DESERT_HILLS,
						Biomes.COLD_TAIGA,
						Biomes.EXTREME_HILLS,
						Biomes.FOREST,
						Biomes.SAVANNA,
						Biomes.SAVANNA_PLATEAU,
						Biomes.TAIGA,
						Biomes.TAIGA_HILLS,
						Biomes.PLAINS,
						Biomes.JUNGLE_EDGE,
						Biomes.MESA
				)
				.build();
		EntityEntry wanderingCultivatorEntity = EntityEntryBuilder.create()
				.entity(WanderingCultivator.class)
				.id(new ResourceLocation(WuxiaCraft.MOD_ID, "wandering_cultivator"),1)
				.name("wandering_cultivator")
				.tracker(150, 3, false)
				.egg(0x202020, 0xFACB27)
				.spawn(EnumCreatureType.MONSTER, 15, 1, 1,
						Biomes.BEACH,
						Biomes.BIRCH_FOREST,
						Biomes.DESERT,
						Biomes.DESERT_HILLS,
						Biomes.COLD_TAIGA,
						Biomes.EXTREME_HILLS,
						Biomes.FOREST,
						Biomes.SAVANNA,
						Biomes.SAVANNA_PLATEAU,
						Biomes.TAIGA,
						Biomes.TAIGA_HILLS,
						Biomes.PLAINS,
						Biomes.JUNGLE_EDGE,
						Biomes.MESA
				)
				.build();
		EntityEntry fireThrowable = EntityEntryBuilder.create()
				.entity(FireThrowable.class)
				.id(new ResourceLocation(WuxiaCraft.MOD_ID, "fire_throwable"), 3)
				.name("fire_throwable")
				.tracker(200, 10, true)
				.build();
		EntityEntry waterNeedleThrowable = EntityEntryBuilder.create()
				.entity(WaterNeedleThrowable.class)
				.id(new ResourceLocation(WuxiaCraft.MOD_ID, "water_needle_throwable"), 4)
				.name("water_needle_throwable")
				.tracker(200, 10, true)
				.build();
		EntityEntry waterBladeThrowable = EntityEntryBuilder.create()
				.entity(WaterBladeThrowable.class)
				.id(new ResourceLocation(WuxiaCraft.MOD_ID, "water_blade_throwable"), 5)
				.name("water_blade_throwable")
				.tracker(200, 10, true)
				.build();
		EntityEntry swordBeamThrowable = EntityEntryBuilder.create()
				.entity(SwordBeamThrowable.class)
				.id(new ResourceLocation(WuxiaCraft.MOD_ID, "sword_beam_throwable"), 6)
				.name("sword_beam_throwable")
				.tracker(200, 10, true)
				.build();
		event.getRegistry().register(giantAntEntity);
		event.getRegistry().register(giantBeeEntity);
		event.getRegistry().register(wanderingCultivatorEntity);
		event.getRegistry().register(fireThrowable);
		event.getRegistry().register(waterNeedleThrowable);
		event.getRegistry().register(waterBladeThrowable);
		event.getRegistry().register(swordBeamThrowable);
	}

	@SubscribeEvent
	public void onPotionRegister(RegistryEvent.Register<Potion> event) {
		event.getRegistry().register(Skills.ENLIGHTENMENT);
	}

}
