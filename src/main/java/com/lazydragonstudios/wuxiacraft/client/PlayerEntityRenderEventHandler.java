package com.lazydragonstudios.wuxiacraft.client;

import com.lazydragonstudios.wuxiacraft.capabilities.ClientAnimationState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerEntityRenderEventHandler {

	/**
	 * This event will cancel the basic render when the remote player is meditating
	 *
	 * @param event a description what is happening
	 */
	@SubscribeEvent
	public static void onSinglePlayerRender(RenderLivingEvent.Pre<AbstractClientPlayer, ? extends Model> event) {
		if (!(event.getEntity() instanceof RemotePlayer)) return; //we don't want local players so far
		var player = Minecraft.getInstance().player;
		if (player == event.getEntity()) return; //means in some weird way that the entity is the local player
		var animationState = ClientAnimationState.get((Player) event.getEntity());
		if(animationState.isMeditating()) {
			event.setCanceled(true);
		}
	}

}
