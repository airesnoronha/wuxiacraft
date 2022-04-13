package com.lazydragonstudios.wuxiacraft.client.render;

import com.lazydragonstudios.wuxiacraft.capabilities.ClientAnimationState;
import com.lazydragonstudios.wuxiacraft.client.render.renderer.AnimatedPlayerRenderer;
import com.lazydragonstudios.wuxiacraft.client.render.renderer.AuraRenderer;
import com.lazydragonstudios.wuxiacraft.client.render.renderer.GhostRenderer;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerEntityRenderEventHandler {

	/**
	 * This event will cancel the basic render when the remote player is meditating
	 * And replace it with an animation
	 *
	 * @param event a description what is happening
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onSinglePlayerRender(RenderLivingEvent.Pre<AbstractClientPlayer, ? extends Model> event) {
		if (!(event.getEntity() instanceof Player)) return; //we don't want local players so far
		var animationState = ClientAnimationState.get((Player) event.getEntity());
		if (animationState.isMeditating() || animationState.isExercising() || animationState.isSemiDead() || animationState.isSwordFlight()) {
			event.setCanceled(true);
		}
		if (!event.isCanceled()) return;
		//this is when we canceled the rendering to render it ourselves to add animations
		var renderer = (AnimatedPlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(WuxiaEntities.ANIMATED_PLAYER_ENTITY.get());
		if (renderer == null) return;
		renderer.render((AbstractClientPlayer) event.getEntity(), event.hashCode(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRenderGhost(RenderLivingEvent.Post<AbstractClientPlayer, ? extends Model> event) {
		if (!(event.getEntity() instanceof AbstractClientPlayer target)) return;
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		if (!cultivation.isDivineSense()) return;
		var renderer = (GhostRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(WuxiaEntities.GHOST_ENTITY.get());
		if (renderer == null) return;
		var targetCultivation = Cultivation.get(target);
		var range = cultivation.getStat(PlayerStat.DETECTION_RANGE).doubleValue();
		if (player.distanceTo(target) > range) return;
		var detectionStrength = cultivation.getStat(PlayerStat.DETECTION_STRENGTH);
		var detectionResistance = targetCultivation.getStat(PlayerStat.DETECTION_RESISTANCE);
		if (detectionStrength.compareTo(detectionResistance) <= 0) return;
		renderer.render(target, event.hashCode(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
	}

	@SubscribeEvent
	public static void onRenderAura(RenderLivingEvent.Post<AbstractClientPlayer, ? extends Model> event) {
		if (!(event.getEntity() instanceof AbstractClientPlayer target)) return;
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var renderer = (AuraRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(WuxiaEntities.AURA_ENTITY.get());
		if (renderer == null) return;
		var cultivation = Cultivation.get(player);
		if (!cultivation.isCombat()) return;
		//rel = max(barrier/maxBarrier, 0.3);
		if (cultivation.getStat(PlayerStat.BARRIER).compareTo(BigDecimal.ZERO) <= 0) return;
		var barrierRelative = cultivation.getStat(PlayerStat.BARRIER)
				.divide(cultivation.getStat(PlayerStat.MAX_BARRIER), RoundingMode.HALF_UP).max(new BigDecimal("0.3")).floatValue();
		event.getPoseStack().pushPose();
		event.getPoseStack().scale(barrierRelative, barrierRelative, barrierRelative);
		renderer.render(target, event.hashCode(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
		event.getPoseStack().popPose();
	}

	@SubscribeEvent
	public static void onRenderHand(RenderHandEvent event) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var animationState = ClientAnimationState.get(player);
		if(animationState.isSwordFlight()) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onPlayerTickAddAnimation(TickEvent.PlayerTickEvent event) {
		if (event.side != LogicalSide.CLIENT) return;
		if (event.phase != TickEvent.Phase.END) return;
		var player = event.player;
		if (player == null) return;
		var animationState = ClientAnimationState.get(player);
		animationState.advanceAnimationFrame();
	}

}
