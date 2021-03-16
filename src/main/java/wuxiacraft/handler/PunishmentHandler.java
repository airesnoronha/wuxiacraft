package wuxiacraft.handler;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PunishmentHandler {

	@SubscribeEvent
	public static void onAnnoyingPeopleLogIn(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
		// Little code to almost kill Fruit on log in because I'm nice
		if (player.getUniqueID().equals(UUID.fromString("6b143647-21b9-447e-a5a7-cd48808ec30a"))) {
			player.setPositionAndUpdate(player.getPosX(), player.getPosY() + 200, player.getPosZ());
			player.setHealth(1);
		}
		// EnwoH that prick
		if (player.getUniqueID().equals(UUID.fromString("623bdf9f-cb3d-4696-8089-f4edc3728378"))) {
			player.sendMessage(new StringTextComponent("This mod was designed for people, not pigs"), Util.DUMMY_UUID);
			player.disconnect();
		}
	}
}
