package com.lazydragonstudios.wuxiacraft.client;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEventHandler {

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event) {
	}
	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Post event) {
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
	}

}
