package com.lazydragonstudios.wuxiacraft.capabilities;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityAttachingHandler {

	@SubscribeEvent
	public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if(event.getObject().getType() != EntityType.PLAYER) return;
		event.addCapability(new ResourceLocation(WuxiaCraft.MOD_ID, "cultivation_capability"), new CultivationProvider());
	}

}
