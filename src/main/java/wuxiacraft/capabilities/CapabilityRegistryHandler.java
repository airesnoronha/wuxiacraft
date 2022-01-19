package wuxiacraft.capabilities;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityRegistryHandler {

	@SubscribeEvent
	public void RegisterCapabilities(RegisterCapabilitiesEvent event) {
	}

}
