package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.combat.WuxiaDamageSource;
import com.lazydragonstudios.wuxiacraft.init.WuxiaElements;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.networking.CultivationSyncMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CultivationEventHandler {

	/**
	 * A helper procedure to simplify the line actually because this might get repeated a lot in this file
	 *
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

	//TODO add drop back realm when not have stabilized realm
	@SubscribeEvent
	public static void onCultivatorUpdate(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		var player = event.player;
		if (player == null) return;
		player.level.getProfiler().push("playerCultivationUpdate");
		//defining variables I'm sure I'm gonna use a lot inside here
		ICultivation cultivation = Cultivation.get(player);
		var bodyData = cultivation.getSystemData(System.BODY);
		var divineData = cultivation.getSystemData(System.DIVINE);
		var essenceData = cultivation.getSystemData(System.ESSENCE);

		//Sync the cultivation with the client every so often
		cultivation.advanceTimer();
		if (cultivation.getTimer() >= 100) {
			cultivation.resetTimer();
			if (!player.level.isClientSide()) {
				syncClientCultivation((ServerPlayer) player);
			}
		}

		//Body energy regen depends on food
		if (player.getFoodData().getFoodLevel() > 15) {
			BigDecimal hunger_modifier = new BigDecimal("1");
			if (player.getFoodData().getFoodLevel() >= 18) hunger_modifier = hunger_modifier.add(new BigDecimal("0.3"));
			if (player.getFoodData().getFoodLevel() >= 20) hunger_modifier = hunger_modifier.add(new BigDecimal("0.3"));
			BigDecimal finalEnergyRegen = bodyData.getStat(PlayerSystemStat.ENERGY_REGEN).multiply(hunger_modifier);
			//bodyEnergy <= bodyMaxEnergy * 0.7 (70%)
			boolean canRegenBodyEnergy = bodyData.getStat(PlayerSystemStat.ENERGY).compareTo(bodyData.getStat(PlayerSystemStat.MAX_ENERGY).multiply(new BigDecimal("0.7"))) <= 0;
			if (canRegenBodyEnergy) {
				bodyData.addEnergy(finalEnergyRegen);
				bodyData.setStat(PlayerSystemStat.ENERGY, bodyData.getStat(PlayerSystemStat.ENERGY).min(bodyData.getStat(PlayerSystemStat.MAX_ENERGY).multiply(new BigDecimal("0.7"))));
				player.causeFoodExhaustion(finalEnergyRegen.floatValue());
			}
		}
		//others don't
		for (var system : System.values()) {
			var systemData = cultivation.getSystemData(system);
			if (system != System.BODY) { //body already regenerated at that point
				systemData.addEnergy(systemData.getStat(PlayerSystemStat.ENERGY_REGEN));
			}
			//kill if above 150%
			if (systemData.getStat(PlayerSystemStat.ENERGY).compareTo(systemData.getStat(PlayerSystemStat.MAX_ENERGY).multiply(new BigDecimal("1.5"))) > 0) {
				killPlayerWithExplosion(player,
						"wuxiacraft.energy_excess." + system.name().toLowerCase(),
						//energy * 3 * max_health -> just to guarantee death
						systemData.getStat(PlayerSystemStat.ENERGY).multiply(new BigDecimal("3")).multiply(cultivation.getPlayerStat(PlayerStat.MAX_HEALTH)));
				//or regulate it slowly to 100%
			} else if (systemData.getStat(PlayerSystemStat.ENERGY).compareTo(systemData.getStat(PlayerSystemStat.MAX_ENERGY)) > 0) {
				systemData.consumeEnergy(systemData.getStat(PlayerSystemStat.ENERGY_REGEN));
			}
		}
		//Healing part yaay
		if (cultivation.getPlayerStat(PlayerStat.HEALTH).compareTo(cultivation.getPlayerStat(PlayerStat.MAX_HEALTH)) < 0) {
			BigDecimal energy_used = cultivation.getPlayerStat(PlayerStat.HEALTH_REGEN_COST);
			//Won't heal when energy is below 10%
			if (bodyData.getStat(PlayerSystemStat.ENERGY).subtract(energy_used).compareTo(bodyData.getStat(PlayerSystemStat.MAX_ENERGY).multiply(new BigDecimal("0.1"))) >= 0) {
				BigDecimal amount_healed = cultivation.getPlayerStat(PlayerStat.HEALTH_REGEN);
				if (bodyData.consumeEnergy(energy_used)) { // this is the correct way to use consume energy ever
					cultivation.setPlayerStat(PlayerStat.HEALTH, cultivation.getPlayerStat(PlayerStat.MAX_HEALTH).min(cultivation.getPlayerStat(PlayerStat.HEALTH).add(amount_healed)));
				}
			}
		}

		//if player is exercising, add a little of essence to him
		if (cultivation.isExercising() && (
				bodyData.techniqueData.modifier.isValidTechnique() ||
						essenceData.techniqueData.modifier.isValidTechnique()
		)) {
			if (bodyData.consumeEnergy(cultivation.getPlayerStat(PlayerStat.EXERCISE_COST))) {
				essenceData.addEnergy(cultivation.getPlayerStat(PlayerStat.EXERCISE_CONVERSION));
			}
		}

		// punishment for low energy >>> poor resource management
		if (!bodyData.hasEnergy(bodyData.getStat(PlayerSystemStat.MAX_ENERGY).multiply(new BigDecimal("0.1")))) {
			double relativeAmount = bodyData.getStat(PlayerSystemStat.ENERGY).divide(bodyData.getStat(PlayerSystemStat.MAX_ENERGY), RoundingMode.HALF_UP).doubleValue();
			int amplifier = 0;
			if (relativeAmount < 0.08) amplifier = 1;
			if (relativeAmount < 0.06) amplifier = 2;
			if (relativeAmount < 0.04) amplifier = 3;
			if (relativeAmount < 0.02) amplifier = 4;
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 15, amplifier, true, false));
			player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 15, amplifier, true, false));
			if (relativeAmount < 0.005) {
				player.hurt(DamageSource.WITHER, 2);
			}
		}
		if (!divineData.hasEnergy(divineData.getStat(PlayerSystemStat.MAX_ENERGY).multiply(new BigDecimal("0.1")))) {
			double relativeAmount = divineData.getStat(PlayerSystemStat.ENERGY).divide(divineData.getStat(PlayerSystemStat.MAX_ENERGY), RoundingMode.HALF_UP).doubleValue();
			int amplifier = 0;
			if (relativeAmount < 0.08) amplifier = 1;
			if (relativeAmount < 0.06) amplifier = 2;
			if (relativeAmount < 0.04) amplifier = 3;
			if (relativeAmount < 0.02) amplifier = 4;
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 15, amplifier, true, false));
			player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 15, amplifier, true, false));
			if (relativeAmount < 0.005) {
				player.hurt(DamageSource.WITHER, 2);
			}
		}
		player.level.getProfiler().pop();
	}

	private static void killPlayerWithExplosion(Player player, String deathMessage, BigDecimal amount) {
		player.hurt(new WuxiaDamageSource(deathMessage, WuxiaElements.PHYSICAL.get(), player, amount), amount.floatValue());
	}

	/**
	 * This fires after a player has properly spawned after something
	 * Fixing the Clone event sync issues
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent
	public static void onPlayerResurrect(PlayerEvent.PlayerRespawnEvent event) {
		syncClientCultivation((ServerPlayer) event.getPlayer());
		if (event.getPlayer().getTags().contains("PLEASE_RTP_ME")) {
			var level = event.getPlayer().level;
			var playerPositions = new HashSet<Point>();
			Point playerDeathPosition = new Point(event.getPlayer().getBlockX(), event.getPlayer().getBlockZ());
			for (var p : level.players()) {
				playerPositions.add(new Point(p.getBlockX(), p.getBlockZ()));
			}
			int attempts = 30;
			int spawnX = event.getPlayer().level.getLevelData().getXSpawn();
			int spawnZ = event.getPlayer().level.getLevelData().getZSpawn();
			Point newPosition = null;
			for (int i = 0; i < attempts; i++) {
				boolean isNearSomeone = false;
				int newX = spawnX + event.getPlayer().getRandom().nextInt(20000) - 10000;
				int newZ = spawnZ + event.getPlayer().getRandom().nextInt(20000) - 10000;
				var point = new Point(newX, newZ);
				//I'm using the y variable from point as the z coordinate
				if (point.distance(playerDeathPosition.x, playerDeathPosition.y) >= 1000) {
					for (var p : playerPositions) {
						if (p.distance(point.x, point.y) < 400) {
							isNearSomeone = true;
							break;
						}
					}
				} else {
					isNearSomeone = true;
				}
				if (!isNearSomeone) {
					newPosition = point;
					break;
				}
			}
			if (newPosition != null) {
				var chunk = event.getPlayer().level.getChunkAt(new BlockPos(newPosition.x, 64, newPosition.y));
				int newY = chunk.getMaxBuildHeight();
				for (int i = chunk.getMaxBuildHeight(); i >= 0; i--) {
					var state = chunk.getBlockState(new BlockPos(newPosition.x, i, newPosition.y));
					if (!state.getBlock().equals(Blocks.AIR)) {
						newY = i + 1;
						break;
					}
				}
				event.getPlayer().teleportTo(newPosition.x, newY, newPosition.y);
			}
			event.getPlayer().removeTag("PLEASE_RTP_ME");
		}
	}

	/**
	 * Restores peoples cultivation after death, with some penalties
	 * This fires right after players press respawn
	 * Sync issues with this because client might get the sync package before actually spawning
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent
	public static void onPlayerDeath(PlayerEvent.Clone event) {
		event.getOriginal().reviveCaps();
		ICultivation oldCultivation = Cultivation.get(event.getOriginal());
		ICultivation newCultivation = Cultivation.get(event.getPlayer());
		if (event.isWasDeath()) {
			//oldCultivation.setSkillCooldown(0);
			if (event.getOriginal().getTags().contains("PLEASE_RTP_ME")) {
				event.getPlayer().addTag("PLEASE_RTP_ME");
			}
			oldCultivation.setPlayerStat(PlayerStat.LIVES, oldCultivation.getPlayerStat(PlayerStat.LIVES).subtract(BigDecimal.ONE));
			if (oldCultivation.getPlayerStat(PlayerStat.LIVES).compareTo(BigDecimal.ZERO) == 0) {
				oldCultivation.deserialize(new Cultivation().serialize());
			} else {
				oldCultivation.setPlayerStat(PlayerStat.HEALTH, PlayerStat.HEALTH.defaultValue);
				var bodyData = oldCultivation.getSystemData(System.BODY);
				var divineData = oldCultivation.getSystemData(System.DIVINE);
				var essenceData = oldCultivation.getSystemData(System.ESSENCE);
				bodyData.setStat(PlayerSystemStat.ENERGY, new BigDecimal("7"));
				divineData.setStat(PlayerSystemStat.ENERGY, new BigDecimal("10"));
				essenceData.setStat(PlayerSystemStat.ENERGY, new BigDecimal("0"));
			}
			event.getOriginal().invalidateCaps();
		}
		newCultivation.deserialize(oldCultivation.serialize());
	}

	/**
	 * When players wake up they'll recover blood energy and mental energy, because player rested
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
		ICultivation cultivation = Cultivation.get(event.getPlayer());
		var bodyData = cultivation.getSystemData(System.BODY);
		var divineData = cultivation.getSystemData(System.DIVINE);
		bodyData.setStat(PlayerSystemStat.ENERGY, bodyData.getStat(PlayerSystemStat.MAX_ENERGY));
		divineData.setStat(PlayerSystemStat.ENERGY, divineData.getStat(PlayerSystemStat.MAX_ENERGY));
		if (!event.getPlayer().level.isClientSide()) {
			syncClientCultivation((ServerPlayer) event.getPlayer());
		}
	}

}
