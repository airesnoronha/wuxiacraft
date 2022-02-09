package com.lazydragonstudios.wuxiacraft.client;

import com.lazydragonstudios.wuxiacraft.client.gui.WuxiaDeathScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderHUDEventHandler {

	/**
	 * Disable vanilla health rendering
	 * Comes first because I'm using wuxia health anyway
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPreRenderHUD(RenderGameOverlayEvent.PreLayer event) {
		if (Minecraft.getInstance().player == null) return;
		if (event.getOverlay() == ForgeIngameGui.PLAYER_HEALTH_ELEMENT) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onDeathScreen(ScreenOpenEvent event) {
		if (event.getScreen() instanceof DeathScreen vanillaDeathScreen) {
			var player = Minecraft.getInstance().player;
			if (player == null) return;
			event.setScreen(new WuxiaDeathScreen(vanillaDeathScreen.getNarrationMessage(), player.level.getLevelData().isHardcore()));
		}
	}

}
