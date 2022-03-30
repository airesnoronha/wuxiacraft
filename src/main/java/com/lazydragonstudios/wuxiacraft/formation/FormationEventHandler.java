package com.lazydragonstudios.wuxiacraft.formation;

import com.lazydragonstudios.wuxiacraft.blocks.entity.FormationCore;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.event.CultivatingEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.math.BigDecimal;
import java.util.LinkedList;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FormationEventHandler {

	@SubscribeEvent
	public static void onPlayerCultivate(CultivatingEvent event) {
		var player = event.getPlayer();
		var cultivation = Cultivation.get(player);
		var system = event.getSystem();
		var formationPos = cultivation.getFormation();
		if (formationPos == null) return;
		var distSqr = formationPos.distSqr(player.getX(), player.getY(), player.getZ(), true);
		if (distSqr > 16d * 16d) return;
		var blockEntity = player.level.getBlockEntity(formationPos);
		if (!(blockEntity instanceof FormationCore core)) return;
		if (!core.isActive()) return;
		var formationSpeed = core.getStat(system, FormationSystemStat.CULTIVATION_SPEED);
		var cultivationSpeed = BigDecimal.ONE.add(cultivation.getSystemData(system).getStat(PlayerSystemStat.CULTIVATION_SPEED));
		//amount = amount + min(formationSpeed, 2.5 * player_cult_speed);
		event.setAmount(event.getAmount().add(formationSpeed.min(new BigDecimal("4").multiply(cultivationSpeed))));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		event.player.level.getProfiler().push("playerFormationTick");
		var cultivation = Cultivation.get(event.player);
		var formationPos = cultivation.getFormation();
		if (formationPos == null) return;
		var blockEntity = event.player.level.getBlockEntity(formationPos);
		if (!(blockEntity instanceof FormationCore core)) return;
		if (!core.isActive()) return;
		var distToFormation = formationPos.distSqr(event.player.getX(), event.player.getY(), event.player.getZ(), true);

		for (var system : System.values()) {
			var regenDist = core.getStat(system, FormationSystemStat.ENERGY_REGEN_RANGE).doubleValue();
			var regenAmount = core.getStat(system, FormationSystemStat.ENERGY_REGEN);
			var systemData = cultivation.getSystemData(system);
			if (distToFormation <= regenDist * regenDist) {
				if (systemData.getStat(PlayerSystemStat.ENERGY_REGEN).compareTo(new BigDecimal("0.005")) >= 0) {
					systemData.addEnergy(regenAmount);
				}
				if (system == System.ESSENCE) {
					if (cultivation.isExercising()) {
						systemData.addEnergy(regenAmount);
					}
				}
			}
		}
		event.player.level.getProfiler().pop();
	}

	@SubscribeEvent
	public static void onPlayerMayBreak(BlockEvent.BreakEvent event) {
		var breaker = event.getPlayer();
		if (breaker == null) return;
		int chunkRadius = 4;
		Level level = breaker.level;
		var chunk = level.getChunkAt(event.getPos());
		var activeFormationCores = new LinkedList<FormationCore>();
		for (int cx = -chunkRadius; cx <= chunkRadius; cx++) {
			for (int cz = -chunkRadius; cz <= chunkRadius; cz++) {
				var currentChunkPos = new ChunkPos(chunk.getPos().x + cx, chunk.getPos().z + cz);
				var currentChunk = level.getChunk(currentChunkPos.x, currentChunkPos.z);
				var blockEntities = currentChunk.getBlockEntities();
				for (var blockEntity : blockEntities.values()) {
					if (!(blockEntity instanceof FormationCore core)) continue;
					if (core.isActive()) {
						activeFormationCores.add(core);
					}
				}
			}
		}
		for (var core : activeFormationCores) {
			if (breaker == core.getOwner()) continue;
			var barrierAmount = core.getStat(FormationStat.BARRIER_AMOUNT);
			if (barrierAmount.compareTo(BigDecimal.ZERO) <= 0) continue;
			var barrierRange = core.getStat(FormationStat.BARRIER_RANGE).doubleValue();
			var distSqr = event.getPos().distSqr(core.getBlockPos());
			if (distSqr <= barrierRange * barrierRange) {
				event.setCanceled(true);
				break;
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		var interactive = event.getPlayer();
		if (interactive == null) return;
		int chunkRadius = 4;
		Level level = interactive.level;
		var chunk = level.getChunkAt(event.getPos());
		var activeFormationCores = new LinkedList<FormationCore>();
		for (int cx = -chunkRadius; cx <= chunkRadius; cx++) {
			for (int cz = -chunkRadius; cz <= chunkRadius; cz++) {
				var currentChunkPos = new ChunkPos(chunk.getPos().x + cx, chunk.getPos().z + cz);
				var currentChunk = level.getChunk(currentChunkPos.x, currentChunkPos.z);
				var blockEntities = currentChunk.getBlockEntities();
				for (var blockEntity : blockEntities.values()) {
					if (!(blockEntity instanceof FormationCore core)) continue;
					if (core.isActive()) {
						activeFormationCores.add(core);
					}
				}
			}
		}
		for (var core : activeFormationCores) {
			if (interactive == core.getOwner()) continue;
			var barrierAmount = core.getStat(FormationStat.BARRIER_AMOUNT);
			if (barrierAmount.compareTo(BigDecimal.ZERO) <= 0) continue;
			var barrierRange = core.getStat(FormationStat.BARRIER_RANGE).doubleValue();
			var distSqr = event.getPos().distSqr(core.getBlockPos());
			if (distSqr <= barrierRange * barrierRange) {
				event.setCanceled(true);
				break;
			}
		}
	}

	@SubscribeEvent
	public static void onAttackOwner(AttackEntityEvent event) {
		var target = event.getTarget();
		if (!(target instanceof Player targetPlayer)) return;
		var targetCultivation = Cultivation.get(targetPlayer);
		var formationPos = targetCultivation.getFormation();
		if (formationPos == null) return;
		var blockEntity = targetPlayer.getLevel().getBlockEntity(formationPos);
		if (!(blockEntity instanceof FormationCore core)) return;
		if (core.getStat(FormationStat.BARRIER_AMOUNT).compareTo(BigDecimal.ZERO) <= 0) return;
		var barrierRange = core.getStat(FormationStat.BARRIER_RANGE).doubleValue();
		var distSqr = formationPos.distSqr(targetPlayer.getPosition(0), true);
		if (distSqr <= barrierRange * barrierRange) {
			event.setCanceled(true);
		}
	}

}
