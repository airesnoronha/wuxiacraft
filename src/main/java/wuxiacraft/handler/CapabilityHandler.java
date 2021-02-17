package wuxiacraft.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.capabilities.CultivationProvider;

public class CapabilityHandler {

	@SubscribeEvent
	public void AttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
		if(!(event.getObject() instanceof PlayerEntity)) return;
		event.addCapability(new ResourceLocation(WuxiaCraft.MOD_ID, "cultivation_capability"), new CultivationProvider());
	}

}
