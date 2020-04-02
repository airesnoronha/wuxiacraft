package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.Blocks;
import com.airesnor.wuxiacraft.entities.mobs.GiantAnt;
import com.airesnor.wuxiacraft.entities.mobs.GiantBee;
import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import com.airesnor.wuxiacraft.entities.tileentity.CauldronTileEntity;
import com.airesnor.wuxiacraft.entities.tileentity.SpiritStoneStackTileEntity;
import com.airesnor.wuxiacraft.items.IHasModel;
import com.airesnor.wuxiacraft.items.Items;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
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
		event.getRegistry().register(giantAntEntity);
		event.getRegistry().register(giantBeeEntity);
		event.getRegistry().register(wanderingCultivatorEntity);
	}

}
