package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.combat.WuxiaDamageSource;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator.SkillActivatorAspect;
import com.lazydragonstudios.wuxiacraft.init.WuxiaElements;
import com.lazydragonstudios.wuxiacraft.init.WuxiaMobEffects;
import com.lazydragonstudios.wuxiacraft.networking.TurnSemiDeadStateMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
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
		ICultivation cultivation = Cultivation.get(player);
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CultivationSyncMessage(cultivation));
		cultivation.calculateStats();
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		var player = event.getPlayer();
		syncClientCultivation((ServerPlayer) player);
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
				new TurnSemiDeadStateMessage(Cultivation.get(player).isSemiDead(),
						//since these two are useless when ppl is alive, might as well just leave it there as if dead
						new TranslatableComponent("wuxiacraft.death.login"), player.getServer().isHardcore()));
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
		handleClientSync(player, cultivation);
		if (cultivation.isSemiDead()) {
			handleSemiDead(player, cultivation);
		} else {
			handleSkillCasting(player, cultivation);
			handleBodyEnergyRegen(player, bodyData);
			handleEnergyRegen(player, cultivation);
			handleEnergyOverflow(cultivation);
			handleNaturalHealing(cultivation, bodyData);
			handleExerciseEnergies(cultivation, bodyData, essenceData);
			handleLowEnergyPunishments(player, bodyData, divineData);
			handleCustomPotionEffects(player, essenceData);
		}
		player.level.getProfiler().pop();
	}

	private static void handleCustomPotionEffects(Player player, SystemContainer essenceContainer) {
		if (player.hasEffect(WuxiaMobEffects.SPIRITUAL_RESONANCE.get())) {
			var effectInstance = player.getEffect(WuxiaMobEffects.SPIRITUAL_RESONANCE.get());
			if (effectInstance == null) return;
			essenceContainer.addEnergy(new BigDecimal("0.001").multiply(BigDecimal.TEN.pow(effectInstance.getAmplifier())));
		}

	}

	private static void handleSemiDead(Player player, ICultivation cultivation) {
		//TODO add a game rule for the cooldown, it's 5 mins now
		cultivation.advanceSemiDead(20 * 300);
		if (player.level.isClientSide()) return;
		if (!cultivation.isSemiDead()) {
			WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
					new TurnSemiDeadStateMessage(false, new TextComponent(""), false));
		}
	}

	private static void handleClientSync(Player player, ICultivation cultivation) {
		//Sync the cultivation with the client every so often
		cultivation.advanceTimer();
		if (cultivation.getTimer() >= 100) {
			cultivation.resetTimer();
			if (!player.level.isClientSide()) {
				syncClientCultivation((ServerPlayer) player);
				for (var system : System.values()) {
					var systemData = cultivation.getSystemData(system);
					systemData.techniqueData.grid.fixProficiencies(cultivation.getAspects());
				}
			}
		}
	}

	private static void handleSkillCasting(Player player, ICultivation cultivation) {
		var skillData = cultivation.getSkills();
		var selectedSkill = skillData.getSkillAt(skillData.selectedSkill);
		if (cultivation.isCombat()) {
			if (skillData.casting && selectedSkill.getStatValue(SkillStat.CURRENT_COOLDOWN).compareTo(BigDecimal.ZERO) <= 0) {
				if (selectedSkill.getSkillChain().size() > 0) {
					if (selectedSkill.getSkillChain().getFirst() instanceof SkillActivatorAspect activator) {
						selectedSkill.addStat(SkillStat.CURRENT_CASTING, BigDecimal.ONE);
						//casting >= cast_time
						if (selectedSkill.getStatValue(SkillStat.CURRENT_CASTING)
								.compareTo(selectedSkill.getStatValue(SkillStat.CAST_TIME)) >= 0) {
							selectedSkill.setStat(SkillStat.CURRENT_CASTING, BigDecimal.ZERO);
							selectedSkill.setStat(SkillStat.CURRENT_COOLDOWN, selectedSkill.getStatValue(SkillStat.COOLDOWN));
							activator.activate.test(player, selectedSkill);
						}
					}
				}
			} else {
				if (selectedSkill.getStatValue(SkillStat.CURRENT_CASTING).compareTo(BigDecimal.ZERO) > 0) {
					selectedSkill.addStat(SkillStat.CURRENT_CASTING, new BigDecimal("-1"));
				}
			}
		}
		for (int i = 0; i < 10; i++) {
			var skill = skillData.getSkillAt(i);
			if (skill.getStatValue(SkillStat.CURRENT_COOLDOWN).compareTo(BigDecimal.ZERO) > 0) {
				skill.addStat(SkillStat.CURRENT_COOLDOWN, new BigDecimal("-1"));
			}
		}
	}

	private static void handleBodyEnergyRegen(Player player, SystemContainer bodyData) {
		//Body energy regen depends on food
		if (player.getFoodData().getFoodLevel() > 15) {
			BigDecimal hunger_modifier = new BigDecimal("1");
			if (player.getFoodData().getFoodLevel() >= 18) hunger_modifier = hunger_modifier.add(new BigDecimal("0.3"));
			if (player.getFoodData().getFoodLevel() >= 20) hunger_modifier = hunger_modifier.add(new BigDecimal("0.3"));
			BigDecimal finalEnergyRegen = bodyData.getStat(PlayerSystemStat.ENERGY_REGEN).multiply(hunger_modifier);
			//bodyEnergy < bodyMaxEnergy * 0.7 (70%)
			boolean canRegenBodyEnergy = bodyData.getStat(PlayerSystemStat.ENERGY).compareTo(bodyData.getStat(PlayerSystemStat.MAX_ENERGY).multiply(new BigDecimal("0.7"))) < 0;
			if (canRegenBodyEnergy) {
				bodyData.addEnergy(finalEnergyRegen);
				bodyData.setStat(PlayerSystemStat.ENERGY, bodyData.getStat(PlayerSystemStat.ENERGY).min(bodyData.getStat(PlayerSystemStat.MAX_ENERGY).multiply(new BigDecimal("0.7"))));
				player.causeFoodExhaustion(finalEnergyRegen.floatValue());
			}
		}
	}

	private static void handleEnergyRegen(Player player, ICultivation cultivation) {
		//others don't
		for (var system : System.values()) {
			var systemData = cultivation.getSystemData(system);
			if (system != System.BODY) { //body already regenerated at that point
				systemData.addEnergy(systemData.getStat(PlayerSystemStat.ENERGY_REGEN));
			}
			//kill if above 150%
			if (systemData.getStat(PlayerSystemStat.ENERGY).compareTo(systemData.getStat(PlayerSystemStat.MAX_ENERGY).multiply(new BigDecimal("1.5"))) > 0) {
				killPlayerWithExplosion(player, systemData,
						"wuxiacraft.energy_excess." + system.name().toLowerCase(),
						//energy * 3 * max_health -> just to guarantee death
						systemData.getStat(PlayerSystemStat.ENERGY).multiply(new BigDecimal("3")).multiply(cultivation.getStat(PlayerStat.MAX_HEALTH)));
				//or regulate it slowly to 100%
			} else if (systemData.getStat(PlayerSystemStat.ENERGY).compareTo(systemData.getStat(PlayerSystemStat.MAX_ENERGY)) > 0) {
				systemData.consumeEnergy(systemData.getStat(PlayerSystemStat.ENERGY_REGEN));
			}
		}
	}

	private static void handleEnergyOverflow(ICultivation cultivation) {
		for (var system : System.values()) {
			var systemData = cultivation.getSystemData(system);
			var energy = systemData.getStat(PlayerSystemStat.ENERGY);
			var max_energy = systemData.getStat(PlayerSystemStat.MAX_ENERGY);
			if (energy.compareTo(max_energy) > 0) {
				var energy_regen = systemData.getStat(PlayerSystemStat.ENERGY_REGEN);
				energy = energy.subtract(energy_regen.multiply(new BigDecimal("2"))).max(max_energy);
				systemData.setStat(PlayerSystemStat.ENERGY, energy);
			}
		}
	}

	private static void handleNaturalHealing(ICultivation cultivation, SystemContainer bodyData) {
		//Healing part yaay
		if (cultivation.getStat(PlayerStat.HEALTH).compareTo(cultivation.getStat(PlayerStat.MAX_HEALTH)) < 0) {
			BigDecimal energy_used = cultivation.getStat(PlayerStat.HEALTH_REGEN_COST);
			//Won't heal when energy is below 10%
			if (bodyData.getStat(PlayerSystemStat.ENERGY).subtract(energy_used).compareTo(bodyData.getStat(PlayerSystemStat.MAX_ENERGY).multiply(new BigDecimal("0.1"))) >= 0) {
				BigDecimal amount_healed = cultivation.getStat(PlayerStat.HEALTH_REGEN);
				if (bodyData.consumeEnergy(energy_used)) { // this is the correct way to use consume energy ever
					cultivation.setStat(PlayerStat.HEALTH, cultivation.getStat(PlayerStat.MAX_HEALTH).min(cultivation.getStat(PlayerStat.HEALTH).add(amount_healed)));
				}
			}
		}
	}

	private static void handleExerciseEnergies(ICultivation cultivation, SystemContainer bodyData, SystemContainer essenceData) {
		//if player is exercising, add a little of essence to him
		if (cultivation.isExercising() && (
				bodyData.techniqueData.modifier.isValidTechnique() ||
						essenceData.techniqueData.modifier.isValidTechnique()
		)) {
			if (bodyData.consumeEnergy(cultivation.getStat(PlayerStat.EXERCISE_COST))) {
				essenceData.addEnergy(cultivation.getStat(PlayerStat.EXERCISE_CONVERSION));
			}
		}
	}

	private static void handleLowEnergyPunishments(Player player, SystemContainer bodyData, SystemContainer divineData) {
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
	}

	private static void killPlayerWithExplosion(Player player, SystemContainer systemData, String deathMessage, BigDecimal amount) {
		systemData.setStat(PlayerSystemStat.ENERGY, BigDecimal.ZERO);
		var interaction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(player.level, player) ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE;
		player.level.explode(null, player.getX(), player.getY(), player.getZ(), 5f, false, interaction);
		player.hurt(new WuxiaDamageSource(deathMessage, WuxiaElements.PHYSICAL.get(), player, amount).setInstantDeath(), amount.floatValue());
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
			event.getPlayer().removeTag("PLEASE_RTP_ME");
			var rtpX = event.getPlayer().getPersistentData().getDouble("rtp_to_x");
			var rtpY = event.getPlayer().getPersistentData().getDouble("rtp_to_y");
			var rtpZ = event.getPlayer().getPersistentData().getDouble("rtp_to_z");
			event.getPlayer().teleportTo(rtpX, rtpY, rtpZ);
			event.getPlayer().getPersistentData().remove("rtp_to_x");
			event.getPlayer().getPersistentData().remove("rtp_to_y");
			event.getPlayer().getPersistentData().remove("rtp_to_z");
		}
	}

	private static Vec3 findSafeRTP(Player player) {
		Vec3 result = new Vec3(0, 0, 0);
		var level = player.level;
		var playerPositions = new HashSet<Point>();
		Point playerDeathPosition = new Point(player.getBlockX(), player.getBlockZ());
		for (var p : level.players()) {
			playerPositions.add(new Point(p.getBlockX(), p.getBlockZ()));
		}
		WuxiaCraft.LOGGER.info("Player: " + player.getDisplayName() + " is random teleporting!");
		int attempts = 30;
		int spawnX = player.level.getLevelData().getXSpawn();
		int spawnZ = player.level.getLevelData().getZSpawn();
		Point newPosition = null;
		for (int i = 0; i < attempts; i++) {
			boolean isNearSomeone = false;
			int newX = spawnX + player.getRandom().nextInt(20000) - 10000;
			int newZ = spawnZ + player.getRandom().nextInt(20000) - 10000;
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
			var chunk = player.level.getChunkAt(new BlockPos(newPosition.x, 64, newPosition.y));
			int newY = chunk.getMaxBuildHeight();
			for (int i = chunk.getMaxBuildHeight(); i >= 0; i--) {
				var state = chunk.getBlockState(new BlockPos(newPosition.x, i, newPosition.y));
				newY = i;
				if (!state.getBlock().equals(Blocks.AIR)) {
					break;
				}
			}
			result = new Vec3(newPosition.x, newY, newPosition.y);
		}
		return result;
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
		oldCultivation.setSemiDeadState(false);
		if (event.isWasDeath()) {
			//oldCultivation.setSkillCooldown(0);
			if (event.getOriginal().getTags().contains("PLEASE_RTP_ME")) {
				event.getPlayer().addTag("PLEASE_RTP_ME");
				var rtpPos = findSafeRTP(event.getOriginal());
				event.getPlayer().getPersistentData().putDouble("rtp_to_x", rtpPos.x);
				event.getPlayer().getPersistentData().putDouble("rtp_to_y", rtpPos.y);
				event.getPlayer().getPersistentData().putDouble("rtp_to_z", rtpPos.z);
			}
			oldCultivation.setStat(PlayerStat.LIVES, oldCultivation.getStat(PlayerStat.LIVES).subtract(BigDecimal.ONE));
			if (oldCultivation.getStat(PlayerStat.LIVES).compareTo(BigDecimal.ZERO) == 0) {
				oldCultivation = new Cultivation();
			} else {
				oldCultivation.setStat(PlayerStat.HEALTH, PlayerStat.HEALTH.defaultValue);
				var bodyData = oldCultivation.getSystemData(System.BODY);
				var divineData = oldCultivation.getSystemData(System.DIVINE);
				var essenceData = oldCultivation.getSystemData(System.ESSENCE);
				bodyData.setStat(PlayerSystemStat.ENERGY, new BigDecimal("7"));
				divineData.setStat(PlayerSystemStat.ENERGY, new BigDecimal("10"));
				essenceData.setStat(PlayerSystemStat.ENERGY, new BigDecimal("0"));
			}
		}
		event.getOriginal().invalidateCaps();
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
