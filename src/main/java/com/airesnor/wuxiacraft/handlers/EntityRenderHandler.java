package com.airesnor.wuxiacraft.handlers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityRenderHandler {

	private static int animationStep =  0;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void playerAura (RenderLivingEvent.Post<EntityPlayer> event) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(event.getX(), event.getY(), event.getZ());
		
		GlStateManager.popMatrix();
	}

}
