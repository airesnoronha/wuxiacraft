package com.lazydragonstudios.wuxiacraft.init;

import java.util.Random;

import com.google.common.collect.ImmutableSet;
import com.lazydragonstudios.wuxiacraft.WuxiaCraft;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WuxiaProfessions {

	public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, WuxiaCraft.MOD_ID);

	public static RegistryObject<VillagerProfession> CULTIVATOR = PROFESSIONS
			.register("cultivator",
					() -> new VillagerProfession("cultivator",
							WuxiaPoiTypes.TECHNIQUE_INSCRIBER.get(),
							ImmutableSet.of(),
							ImmutableSet.of(),
							SoundEvents.VILLAGER_WORK_CARTOGRAPHER
					)
			);


	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event) {

		if (event.getType() == WuxiaProfessions.CULTIVATOR.get()) {
			for (var manualLocation : WuxiaDefaultTechniqueManuals.getAllKeys()) {
				var manualSupplier = WuxiaDefaultTechniqueManuals.getDefaultManual(manualLocation);
				if (manualSupplier == null) continue;
				event.getTrades().get(1).add(
						new ItemTrade(
								new ItemStack(Items.EMERALD, 10),
								manualSupplier.get(),
								2,
								15
						)
				);
			}
			event.getTrades().get(1).add(
					new ItemTrade(
							new ItemStack(Items.EMERALD, 1),
							new ItemStack(Items.BOOK, 1),
							15,
							5
					)
			);
			event.getTrades().get(1).add(
					new ItemTrade(
							new ItemStack(Items.EMERALD, 1),
							new ItemStack(Items.INK_SAC, 1),
							15,
							5
					)
			);
		}

	}

	static class ItemTrade implements ItemListing {

		private ItemStack cost;
		private ItemStack product;

		private int xpGained;
		private int stock;

		public ItemTrade(ItemStack cost, ItemStack product, int stock, int xpGained) {
			this.cost = cost;
			this.product = product;
			this.stock = stock;
			this.xpGained = xpGained;
		}

		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			return new MerchantOffer(cost, product, stock, xpGained, 0F);
		}
	}
}