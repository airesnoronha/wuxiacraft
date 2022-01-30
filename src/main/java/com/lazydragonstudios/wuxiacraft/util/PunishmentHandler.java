package com.lazydragonstudios.wuxiacraft.util;

import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.lazydragonstudios.wuxiacraft.capabilities.CultivationProvider;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;

import java.math.BigDecimal;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PunishmentHandler {

	@SubscribeEvent
	public static void onAnnoyingPeopleLogIn(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayer player = (ServerPlayer) event.getEntity();
		// Little code to almost kill Fruit on log in because I'm nice
		if (player.getUUID().equals(UUID.fromString("6b143647-21b9-447e-a5a7-cd48808ec30a"))) {
			player.setPos(player.xo, player.yo + 200, player.zo);
			player.getCapability(CultivationProvider.CULTIVATION_PROVIDER).orElse(new Cultivation()).setPlayerStat(PlayerStat.HEALTH, new BigDecimal("1"));
		}
		// EnwoH that prick
		if (player.getUUID().equals(UUID.fromString("623bdf9f-cb3d-4696-8089-f4edc3728378"))) {
			player.sendMessage(new TextComponent("This mod was designed for people, not pigs"), UUID.randomUUID());
			player.disconnect();
		}
	}
}