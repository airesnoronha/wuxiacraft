package wuxiacraft.capabilities;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wuxiacraft.cultivation.ICultivation;

public class CapabilityRegistryHandler {

	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.register(ICultivation.class);
	}
}
