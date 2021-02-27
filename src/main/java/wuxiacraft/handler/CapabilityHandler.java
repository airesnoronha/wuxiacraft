package wuxiacraft.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.capabilities.CultivationProvider;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityHandler {

	@SubscribeEvent
	public static void AttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
		if(!(event.getObject() instanceof PlayerEntity)) return;
		event.addCapability(new ResourceLocation(WuxiaCraft.MOD_ID, "cultivation_capability"), new CultivationProvider());
	}

}
