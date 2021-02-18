package wuxiacraft.handler;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.network.CultivationSyncMessage;
import wuxiacraft.network.EnergyMessage;
import wuxiacraft.network.WuxiaPacketHandler;
import wuxiacraft.util.CultivationUtils;

import java.util.UUID;

@Mod.EventBusSubscriber
public class CultivationEventHandler {

	@SubscribeEvent
	public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();

		// Little code to almost kill Fruit on log in because I'm nice
		if (player.getUniqueID().equals(UUID.fromString("6b143647-21b9-447e-a5a7-cd48808ec30a"))) {
			player.setPositionAndUpdate(player.getPosX(), player.getPosY() + 200, player.getPosZ());
			player.setHealth(1);
		}
	}

	/**
	 * Syncs between client and server every 100 ticks.
	 * Add energy to players every tick
	 *
	 * @param event A description of what's happening
	 */
	@SubscribeEvent
	public void onCultivatorUpdate(LivingEvent.LivingUpdateEvent event) {
		if (!(event.getEntity() instanceof PlayerEntity)) return;

		PlayerEntity player = (PlayerEntity) event.getEntity();
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);

		cultivation.advanceTimer();

		if (cultivation.getTickerTime() == 100 && player.world.isRemote) {
			WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new CultivationSyncMessage(cultivation, player.getUniqueID()));
		}

		//A little bit from the soul modifier since soul increase perception
		cultivation.addEnergy(cultivation.getMaxEnergy() * 0.00025 + cultivation.getDivineModifier() * 0.003);
	}

	/**
	 * A method to reduce the amount of energy messages to the server
	 */
	private double accumulatedFlyCost = 0;

	/**
	 * Calculates if players should fly and takes away the energy cost
	 *
	 * @param event A description of what's happening
	 */
	@SubscribeEvent
	public void onHandleFlightClientSide(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		PlayerEntity player = event.player;
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
		boolean canFly = ((CultivationLevel.EssenceLevel) cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel()).flight;
		if (canFly && player.abilities.isFlying && cultivation.getEnergy() <= 0) {
			player.abilities.isFlying = false;
		}
		if (canFly) {
			player.abilities.allowFlying = true;
			player.abilities.setFlySpeed((float) player.getAttributeValue(Attributes.MOVEMENT_SPEED));
		}
		player.sendPlayerAbilities();

		if (player.abilities.isFlying) {
			double distance = Math.sqrt(Math.pow(player.lastTickPosX - player.getPosX(), 2) + Math.pow(player.lastTickPosY - player.getPosY(), 2) + Math.pow(player.lastTickPosZ - player.getPosZ(), 2));
			double totalRem = 0;
			double fly_cost = 250;
			double dist_cost = 132;
			totalRem += fly_cost;
			totalRem += distance * dist_cost;
			if (!player.isCreative()) {
				cultivation.addEnergy(-totalRem);
				accumulatedFlyCost += totalRem;
				if (cultivation.getTickerTime() % 10 == 0) {
					WuxiaPacketHandler.INSTANCE.sendToServer(new EnergyMessage(accumulatedFlyCost, 1, player.getUniqueID()));
					accumulatedFlyCost = 0;
				}
			}
		}

	}

}
