package com.airesnor.wuxiacraft.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PreClientEvents {

	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent event) {
		//Manually add the mod textures because my OBJBlockModelLoader won't do that, shame
		event.getMap().registerSprite(new ResourceLocation("wuxiacraft:blocks/iron_cauldron"));
	}
}
