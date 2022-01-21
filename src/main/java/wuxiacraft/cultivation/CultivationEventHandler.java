package wuxiacraft.cultivation;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import wuxiacraft.networking.CultivationSyncMessage;
import wuxiacraft.networking.WuxiaPacketHandler;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CultivationEventHandler {

	/**
	 * A helper procedure to simplify the line actually because this might get repeated a lot in this file
	 * @param player the player to be synchronized
	 */
	public static void syncClientCultivation(ServerPlayer player) {
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CultivationSyncMessage(Cultivation.get(player)));
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		var player = event.getPlayer();
		syncClientCultivation((ServerPlayer) player);
	}

	public static void onCultivatorUpdate(LivingEvent.LivingUpdateEvent event) {
		if(!(event.getEntity() instanceof Player player)) return;
		player.level.getProfiler().push("playerCultivationUpdate");
		//defining variables I'm sure I'm gonna use a lot inside here
		ICultivation cultivation = Cultivation.get(player);
		var bodyData = cultivation.getSystemData(Cultivation.System.BODY);
		var divineData = cultivation.getSystemData(Cultivation.System.DIVINE);
		var essenceData = cultivation.getSystemData(Cultivation.System.ESSENCE);

		//Sync the cultivation with the client every so often
		cultivation.advanceTimer();
		if(cultivation.getTimer() >= 100) {
			cultivation.resetTimer();
			if(!player.level.isClientSide()) {
				syncClientCultivation((ServerPlayer) player);
			}
		}

		//Body energy regen depends on food
		if(player.getFoodData().getFoodLevel() > 15 && !bodyData.hasEnergy(bodyData.getMaxEnergy() * 0.7) ) {
			double hunger_modifier = 1;
			if(player.getFoodData().getFoodLevel() >= 18) hunger_modifier += 0.3;
			if(player.getFoodData().getFoodLevel() >= 20) hunger_modifier += 0.3;
			bodyData.addEnergy(hunger_modifier * bodyData.getEnergyRegen());
		}
		//others don't
		for(var system : Cultivation.System.values()) {
			var systemData = cultivation.getSystemData(system);
			if(system != Cultivation.System.BODY) {
				systemData.energy += systemData.getEnergyRegen();
			}
			systemData.energy = Math.min(systemData.energy, systemData.getMaxEnergy());
		}
		//Healing part yaay
		if(cultivation.getHealth() < cultivation.getMaxHealth()) {
			// TODO add a health regen variable and health regen cost variable
			double energy_used = bodyData.getMaxEnergy() * cultivation.getMaxHealth() * 0.01;
			//Won't heal when energy is below 10%
			if(bodyData.energy + energy_used >= energy_used + bodyData.getMaxEnergy() * 0.1) {
				double amount_healed = energy_used * 0.1;
				if(bodyData.consumeEnergy(energy_used)) { // this is the correct way to use consume energy ever
					cultivation.setHealth(Math.min(cultivation.getMaxHealth(), cultivation.getHealth() + amount_healed));
				}
			}

			//TODO punishment for low energy >>> poor resource management

		}
		player.level.getProfiler().pop();
	}

	/**
	 * Restores peoples cultivation after death, with some penalties
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent
	public static void onPlayerDeath(PlayerEvent.Clone event) {
		ICultivation newCultivation = Cultivation.get(event.getPlayer());
		ICultivation oldCultivation = Cultivation.get(event.getOriginal());
		if (event.isWasDeath()) {
			//oldCultivation.setSkillCooldown(0);
			oldCultivation.setHealth(20);
			var bodyData = oldCultivation.getSystemData(Cultivation.System.BODY);
			var divineData = oldCultivation.getSystemData(Cultivation.System.DIVINE);
			var essenceData = oldCultivation.getSystemData(Cultivation.System.ESSENCE);
			bodyData.cultivationBase = 0;
			bodyData.energy = 0;
			divineData.cultivationBase = 0;
			divineData.energy = 10;
			essenceData.cultivationBase = 0;
			essenceData.energy = 0;
		}
		newCultivation.deserialize(oldCultivation.serialize());
		syncClientCultivation((ServerPlayer) event.getPlayer());
	}

	/**
	 * When players wake up they'll recover blood energy and mental energy, because player rested
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
		ICultivation cultivation = Cultivation.get(event.getPlayer());
		var bodyData = cultivation.getSystemData(Cultivation.System.BODY);
		var divineData = cultivation.getSystemData(Cultivation.System.DIVINE);
		bodyData.energy = bodyData.getMaxEnergy();
		divineData.energy = divineData.getMaxEnergy();
	}

}
