package com.lazydragonstudios.wuxiacraft.client;

import com.lazydragonstudios.wuxiacraft.client.gui.WuxiaSemiDeadScreen;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientSideEventHandler {

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		if (event.side != LogicalSide.CLIENT) return;
		var cultivation = Cultivation.get(event.player);
		if (cultivation.isSemiDead()) {
			var mc = Minecraft.getInstance();
			if (!(mc.screen instanceof WuxiaSemiDeadScreen)) {
				mc.screen = new WuxiaSemiDeadScreen();
			}
		}
	}
}
