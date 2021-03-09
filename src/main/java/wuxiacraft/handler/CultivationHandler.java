package wuxiacraft.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.SystemStats;
import wuxiacraft.network.CultivationSyncMessage;
import wuxiacraft.network.WuxiaPacketHandler;

import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CultivationHandler {

	@SubscribeEvent
	public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		ICultivation cultivation = Cultivation.get(player);

		if (!player.world.isRemote) {
			WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new CultivationSyncMessage(cultivation));
		}
		// Little code to almost kill Fruit on log in because I'm nice
		if (player.getUniqueID().equals(UUID.fromString("6b143647-21b9-447e-a5a7-cd48808ec30a"))) {
			player.setPositionAndUpdate(player.getPosX(), player.getPosY() + 200, player.getPosZ());
			player.setHealth(1);
		}
	}

	/**
	 * Syncs between client and server every 100 ticks.
	 * Add energy to players every tick
	 * Applies low resources penalties
	 *
	 * @param event A description of what's happening
	 */
	@SubscribeEvent
	public static void onCultivatorUpdate(LivingEvent.LivingUpdateEvent event) {
		if (!(event.getEntity() instanceof PlayerEntity)) return;

		PlayerEntity player = (PlayerEntity) event.getEntity();
		ICultivation cultivation = Cultivation.get(player);

		if (player.abilities.isFlying) {
			player.fallDistance = 0;
		}

		cultivation.calculateFinalModifiers();
		cultivation.advanceTimer();

		SystemStats bodyStats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
		SystemStats divineStats = cultivation.getStatsBySystem(CultivationLevel.System.DIVINE);
		SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		//sync with client
		if (cultivation.getTickerTime() >= 100 && !player.world.isRemote) {
			WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new CultivationSyncMessage(cultivation));
			cultivation.resetTickerTimer();
		}
		//lower skill cool down
		if (cultivation.getSkillCooldown() > 0)
			cultivation.lowerCoolDown();
		//refill energies
		if (player.getFoodStats().getFoodLevel() > 15 && bodyStats.getEnergy() < cultivation.getMaxBodyEnergy() * 0.7) {
			double hunger_mod = 1;
			if (player.getFoodStats().getFoodLevel() >= 18) hunger_mod += 0.3;
			if (player.getFoodStats().getFoodLevel() >= 20) hunger_mod += 0.3;
			bodyStats.addEnergy(hunger_mod * cultivation.getBodyEnergyRegen() * cultivation.getMaxBodyEnergy());
			//it ok because it's just regen, so it won't halve if it's past middle point
			bodyStats.setEnergy(Math.min(cultivation.getMaxBodyEnergy() * 0.7, bodyStats.getEnergy()));
			player.addExhaustion((float) (hunger_mod * cultivation.getBodyEnergyRegen() * cultivation.getMaxBodyEnergy() * 0.4));
		} else { //if above middle point set try to set max to protect from overflow
			bodyStats.setEnergy(Math.min(cultivation.getMaxBodyEnergy(), bodyStats.getEnergy()));
		}
		divineStats.addEnergy(cultivation.getDivineEnergyRegen() * cultivation.getMaxDivineEnergy());
		divineStats.setEnergy(Math.min(cultivation.getMaxDivineEnergy(), divineStats.getEnergy()));
		essenceStats.addEnergy(cultivation.getEssenceEnergyRegen() * cultivation.getMaxEssenceEnergy());
		essenceStats.setEnergy(Math.min(cultivation.getMaxEssenceEnergy(), essenceStats.getEnergy()));

		//auto healing
		if (cultivation.getHP() < cultivation.getFinalModifiers().maxHealth) {
			double energy_used = cultivation.getMaxBodyEnergy() * cultivation.getHealingAmount();
			if (bodyStats.getEnergy() + energy_used >= energy_used + cultivation.getMaxBodyEnergy() * 0.1) { // won't heal when blood energy is less than 10%
				double amount_healed = energy_used / cultivation.getHealingCost();
				cultivation.setHP(Math.min(cultivation.getFinalModifiers().maxHealth, cultivation.getHP() + amount_healed));
				bodyStats.addEnergy(-energy_used);
			}
		}
		//apply penalties for low resources aka poor resource management
		if (cultivation.getTickerTime() % 10 == 0) {
			if (bodyStats.getEnergy() < cultivation.getMaxBodyEnergy() * 0.1) {
				double relativeAmount = bodyStats.getEnergy() / cultivation.getMaxBodyEnergy();
				int amplifier = 0;
				if (relativeAmount < 0.08) amplifier = 1;
				if (relativeAmount < 0.06) amplifier = 2;
				if (relativeAmount < 0.04) amplifier = 3;
				if (relativeAmount < 0.02) amplifier = 4;
				player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 15, amplifier, true, false));
				player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 15, amplifier, true, false));
				if (relativeAmount < 0.005) {
					cultivation.setHP(cultivation.getHP() - 1);
				}
			}
			if (divineStats.getEnergy() < cultivation.getMaxDivineEnergy() * 0.01) {
				double relativeAmount = bodyStats.getEnergy() / cultivation.getMaxBodyEnergy();
				int amplifier = 0;
				if (relativeAmount < 0.08) amplifier = 1;
				if (relativeAmount < 0.06) amplifier = 2;
				if (relativeAmount < 0.04) amplifier = 3;
				if (relativeAmount < 0.02) {
					amplifier = 4;
					player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 15, 4, false, false));
				}
				player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 15, amplifier / 2, false, false));
				player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 15, amplifier, false, false));
				if (relativeAmount < 0.005) {
					cultivation.setHP(cultivation.getHP() - 1);
				}
			}
		}

	}

	/**
	 * When players wake up they'll recover blood energy and mental energy, because player rested
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
		ICultivation cultivation = Cultivation.get(event.getPlayer());
		cultivation.getStatsBySystem(CultivationLevel.System.BODY).setEnergy(cultivation.getMaxBodyEnergy());
		cultivation.getStatsBySystem(CultivationLevel.System.DIVINE).setEnergy(cultivation.getMaxDivineEnergy());
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
			oldCultivation.setSkillCooldown(0);
			oldCultivation.setHP(20);
			oldCultivation.getStatsBySystem(CultivationLevel.System.BODY).setBase(0);
			oldCultivation.getStatsBySystem(CultivationLevel.System.BODY).setEnergy(5);
			oldCultivation.getStatsBySystem(CultivationLevel.System.BODY).setSubLevel((oldCultivation.getStatsBySystem(CultivationLevel.System.BODY).getSubLevel() / 3) * 3);
			oldCultivation.getStatsBySystem(CultivationLevel.System.DIVINE).setBase(0);
			oldCultivation.getStatsBySystem(CultivationLevel.System.DIVINE).setEnergy(10);
			oldCultivation.getStatsBySystem(CultivationLevel.System.DIVINE).setSubLevel((oldCultivation.getStatsBySystem(CultivationLevel.System.DIVINE).getSubLevel() / 3) * 3);
			oldCultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).setBase(0);
			oldCultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).setEnergy(0);
			oldCultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).setSubLevel((oldCultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getSubLevel() / 3) * 3);
		}
		newCultivation.copyFrom(oldCultivation);
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new CultivationSyncMessage(newCultivation));
	}

	@SubscribeEvent
	public static void onPlayerCanFy(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		PlayerEntity player = event.player;
		ICultivation cultivation = Cultivation.get(player);
		boolean canFly = false;
		if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() instanceof CultivationLevel.EssenceLevel)
			canFly = ((CultivationLevel.EssenceLevel) cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel()).flight;
		if (canFly && player.abilities.isFlying && cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getEnergy() <= 1) {
			player.abilities.isFlying = false;
		}
		if (canFly) {
			player.abilities.allowFlying = true;
		}
	}

}
