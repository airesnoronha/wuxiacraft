package com.lazydragonstudios.wuxiacraft.capabilities;

import com.lazydragonstudios.wuxiacraft.cultivation.ICultivation;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityRegistryHandler {

	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.register(ICultivation.class);
	}
}
