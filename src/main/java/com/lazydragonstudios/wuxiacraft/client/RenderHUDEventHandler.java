package com.lazydragonstudios.wuxiacraft.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderHUDEventHandler {

	/**
	 * Disable vanilla health rendering
	 * Comes first because I'm using wuxia health anyways
	 * @param event a description of what is happening
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPreRenderHUD(RenderGameOverlayEvent.PreLayer event) {
		if(Minecraft.getInstance().player == null) return;
		if(event.getOverlay() == ForgeIngameGui.PLAYER_HEALTH_ELEMENT) {
			event.setCanceled(true);
		}
	}


}
