package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.init.WuxiaTechniqueAspects;
import com.lazydragonstudios.wuxiacraft.util.TechniqueUtil;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TechniqueHandler {

	private static void sendSuccessLearning(Player player, ResourceLocation aspect) {
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.sendMessage(new TranslatableComponent("wuxiacraft.learn_successful")
							.append(new TranslatableComponent("wuxiacraft.techinque" + aspect + ".name")),
					Util.NIL_UUID);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onStruckByLightning(LivingDamageEvent event) {
		if (event.getSource() != DamageSource.LIGHTNING_BOLT) return;
		if (!(event.getEntityLiving() instanceof Player player)) return;
		ICultivation cultivation = Cultivation.get(player);
		var aspects = cultivation.getAspects();
		aspects.learnAspect(WuxiaTechniqueAspects.SPARK.getId(), cultivation);
		sendSuccessLearning(player, WuxiaTechniqueAspects.START.getId());
	}

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		var player = event.getPlayer();
		ICultivation cultivation = Cultivation.get(player);
		var aspects = cultivation.getAspects();
		if (!aspects.knowsAspect(WuxiaTechniqueAspects.ESSENCE_GATHERING.getId())
				&& !aspects.knowsAspect(WuxiaTechniqueAspects.BODY_GATHERING.getId())
				&& !aspects.knowsAspect(WuxiaTechniqueAspects.DIVINE_GATHERING.getId())
		) return;
		HashMap<ResourceLocation, Double> aspectsPerBlock = TechniqueUtil.getAspectChancePerBlock(event.getState().getBlock());
		for (var aspect : aspectsPerBlock.keySet()) {
			if (Math.random() * aspectsPerBlock.get(aspect) < 1d) {
				aspects.learnAspect(aspect, cultivation);
				sendSuccessLearning(player, aspect);
			}
		}
	}
}
