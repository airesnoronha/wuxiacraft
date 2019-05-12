package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilitiesHandler {

	public final ResourceLocation CULTIVATION_CAP = new ResourceLocation(WuxiaCraft.MODID, "cultivation");

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if(!(event.getObject() instanceof EntityPlayer)) return;

		event.addCapability(CULTIVATION_CAP, new CultivationProvider());
	}

}
