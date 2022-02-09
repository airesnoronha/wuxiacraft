package com.lazydragonstudios.wuxiacraft.client.render;

import com.lazydragonstudios.wuxiacraft.capabilities.ClientAnimationState;
import com.lazydragonstudios.wuxiacraft.client.render.AnimatedPlayerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

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
		if (animationState.isMeditating() || animationState.isExercising()) {
			event.setCanceled(true);
		}
		if (!event.isCanceled()) return;
		//this is when we canceled the rendering to render it ourselves to add animations
		var renderer = (AnimatedPlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(AnimatedPlayerRenderer.animatedEntityType);
		if (renderer == null) return;
		renderer.render((AbstractClientPlayer) event.getEntity(), event.hashCode(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRenderGhost(RenderLivingEvent.Post<AbstractClientPlayer, ? extends Model> event) {
		if (!(event.getEntity() instanceof AbstractClientPlayer target)) return;
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		if (!player.isCrouching()) return;
		var renderer = (GhostRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(GhostRenderer.ghostEntityType);
		if (renderer == null) return;
		renderer.render(target, event.hashCode(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
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
