package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.SpiritVeinOre;
import com.airesnor.wuxiacraft.blocks.WuxiaBlocks;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.items.ItemHerb;
import com.airesnor.wuxiacraft.items.WuxiaHerbs;
import com.airesnor.wuxiacraft.world.dimensions.biomes.WuxiaBiomes;
import com.airesnor.wuxiacraft.entities.mobs.GiantAnt;
import com.airesnor.wuxiacraft.entities.mobs.GiantBee;
import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import com.airesnor.wuxiacraft.entities.skills.FireThrowable;
import com.airesnor.wuxiacraft.entities.skills.SwordBeamThrowable;
import com.airesnor.wuxiacraft.entities.skills.WaterBladeThrowable;
import com.airesnor.wuxiacraft.entities.skills.WaterNeedleThrowable;
import com.airesnor.wuxiacraft.entities.tileentity.*;
import com.airesnor.wuxiacraft.formation.FormationTileEntity;
import com.airesnor.wuxiacraft.items.ItemScroll;
import com.airesnor.wuxiacraft.items.WuxiaItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
		event.getRegistry().registerAll(WuxiaItems.ITEMS.toArray(new Item[0]));
		event.getRegistry().registerAll(WuxiaHerbs.HERBS.toArray(new Item[0]));

	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		WuxiaCraft.logger.info("Registering blocks.");
		event.getRegistry().registerAll(WuxiaBlocks.BLOCKS.toArray(new Block[0]));

		GameRegistry.registerTileEntity(CauldronTileEntity.class, new ResourceLocation(WuxiaCraft.MOD_ID, "cauldron_tile_entity"));
		GameRegistry.registerTileEntity(SpiritStoneStackTileEntity.class, new ResourceLocation(WuxiaCraft.MOD_ID, "spirit_stone_stack_tile_entity"));
		GameRegistry.registerTileEntity(FormationTileEntity.class, new ResourceLocation(WuxiaCraft.MOD_ID, "formation_tile_entity"));
		GameRegistry.registerTileEntity(GrinderTileEntity.class, new ResourceLocation(WuxiaCraft.MOD_ID, "grinder_tile_entity"));
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		WuxiaCraft.proxy.registerItemRenderer(Item.getItemFromBlock(WuxiaBlocks.MAGICAL_GRINDER), 0, "inventory");
		ClientRegistry.bindTileEntitySpecialRenderer(GrinderTileEntity.class, new GrinderTESR());
		WuxiaCraft.proxy.registerCustomModelLocation(WuxiaItems.BLOOD_BOTTLE, 0, "inventory", "wuxiacraft:blood_bottle");
		WuxiaCraft.proxy.registerCustomModelLocation(WuxiaItems.RECIPE_SCROLL, 0, "inventory", "wuxiacraft:recipe_scroll");

		for (Item item : WuxiaItems.ITEMS) {
			if (item instanceof ItemScroll) {
				WuxiaCraft.proxy.registerScrollModel(item, 0, "inventory");
			} else {
				WuxiaCraft.proxy.registerItemRenderer(item, 0, "inventory");
			}
		}
		for (Item item : WuxiaHerbs.HERBS) {
			WuxiaCraft.proxy.registerItemRenderer(item, 0, "inventory");
		}
		for (Block block : WuxiaBlocks.BLOCKS) {
			if (block instanceof SpiritVeinOre) {
				WuxiaCraft.proxy.registerCustomModelLocation(ItemBlock.getItemFromBlock(block), 0, "inventory", "wuxiacraft:spirit_vein_ore");
			} else {
				WuxiaCraft.proxy.registerItemRenderer(Item.getItemFromBlock(block), 0, "inventory");
			}
		}
		ClientRegistry.bindTileEntitySpecialRenderer(CauldronTileEntity.class, new CauldronTESR());
		ClientRegistry.bindTileEntitySpecialRenderer(SpiritStoneStackTileEntity.class, new SpiritStoneStackTESR());
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
						Biomes.MESA,
						WuxiaBiomes.MINING,
						WuxiaBiomes.EARTH,
						WuxiaBiomes.FIRE,
						WuxiaBiomes.METAL,
						WuxiaBiomes.WATER,
						WuxiaBiomes.WOOD
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
						Biomes.MESA,
						WuxiaBiomes.MINING,
						WuxiaBiomes.EARTH,
						WuxiaBiomes.FIRE,
						WuxiaBiomes.METAL,
						WuxiaBiomes.WATER,
						WuxiaBiomes.WOOD
				)
				.build();
		EntityEntry wanderingCultivatorEntity = EntityEntryBuilder.create()
				.entity(WanderingCultivator.class)
				.id(new ResourceLocation(WuxiaCraft.MOD_ID, "wandering_cultivator"),1)
				.name("wandering_cultivator")
				.tracker(150, 3, false)
				.egg(0x202020, 0xFACB27)
				.spawn(EnumCreatureType.MONSTER, 5, 1, 1,
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
				.spawn(EnumCreatureType.MONSTER, 12, 1, 1,
						Biomes.HELL,
						WuxiaBiomes.MINING,
						WuxiaBiomes.EARTH,
						WuxiaBiomes.FIRE,
						WuxiaBiomes.METAL,
						WuxiaBiomes.WATER,
						WuxiaBiomes.WOOD,
						WuxiaBiomes.EXTREMEQI)
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
		EntityEntry soulArrowThrowable = EntityEntryBuilder.create()
				.entity(SwordBeamThrowable.class)
				.id(new ResourceLocation(WuxiaCraft.MOD_ID, "soul_arrow_throwable"), 6)
				.name("soul_arrow_throwable")
				.tracker(200, 10, true)
				.build();
		event.getRegistry().register(giantAntEntity);
		event.getRegistry().register(giantBeeEntity);
		event.getRegistry().register(wanderingCultivatorEntity);
		event.getRegistry().register(fireThrowable);
		event.getRegistry().register(waterNeedleThrowable);
		event.getRegistry().register(waterBladeThrowable);
		event.getRegistry().register(swordBeamThrowable);
		event.getRegistry().register(soulArrowThrowable);
	}

	//ain't an event
	public static void registerSmeltingRecipes() {
		FurnaceRecipes.instance().addSmelting(WuxiaItems.DUST_IRON, new ItemStack(Items.IRON_INGOT), 2);
		FurnaceRecipes.instance().addSmelting(WuxiaItems.TINY_DUST_IRON, new ItemStack(Items.IRON_NUGGET), 2);
		FurnaceRecipes.instance().addSmelting(WuxiaItems.DUST_GOLD, new ItemStack(Items.GOLD_INGOT), 2);
		FurnaceRecipes.instance().addSmelting(WuxiaItems.TINY_DUST_GOLD, new ItemStack(Items.GOLD_NUGGET), 2);
		FurnaceRecipes.instance().addSmelting(WuxiaItems.DUST_DIAMOND, new ItemStack(Items.DIAMOND), 2);
	}

	@SubscribeEvent
	public void onPotionRegister(RegistryEvent.Register<Potion> event) {
		event.getRegistry().register(Skills.ENLIGHTENMENT);
	}

	public static final SoundEvent fartSound = new SoundEvent(new ResourceLocation(WuxiaCraft.MOD_ID, "fart")).setRegistryName("fart");

	@SubscribeEvent
	public void onRegisterSound(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(new SoundEvent(new ResourceLocation(WuxiaCraft.MOD_ID, "fart")).setRegistryName("fart"));
	}

}
