package com.lazydragonstudios.wuxiacraft.formation;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.blocks.entity.FormationCore;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.event.CultivatingEvent;
import com.lazydragonstudios.wuxiacraft.init.WuxiaConfigs;
import com.lazydragonstudios.wuxiacraft.item.FormationBarrierBadge;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
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

	public static final TagKey<Item> INTERACT_TAG = ItemTags.create(new ResourceLocation(WuxiaCraft.MOD_ID, "interact_badge"));
	public static final TagKey<Item> BREAK_TAG = ItemTags.create(new ResourceLocation(WuxiaCraft.MOD_ID, "break_badge"));

	@SubscribeEvent
	public static void onPlayerCultivate(CultivatingEvent event) {
		var player = event.getPlayer();
		var cultivation = Cultivation.get(player);
		var system = event.getSystem();
		var formationPos = cultivation.getFormation();
		if (formationPos == null) return;
		var distSqr = formationPos.distToCenterSqr(player.getX(), player.getY(), player.getZ());
		if (distSqr > 16d * 16d) return;
		var blockEntity = player.level.getBlockEntity(formationPos);
		if (!(blockEntity instanceof FormationCore core)) return;
		if (!core.isActive()) return;
		var formationSpeed = core.getStat(system, FormationSystemStat.CULTIVATION_SPEED);
		var formationRuneCount = core.getStat(event.getSystem(), FormationSystemStat.CULTIVATION_RUNE_COUNT);
		var cultivationSpeed = BigDecimal.ONE.add(cultivation.getSystemData(system).getStat(PlayerSystemStat.CULTIVATION_SPEED))
				.multiply(BigDecimal.valueOf(WuxiaConfigs.CULTIVATION_SPEED_MULTIPLIER.get()));
		//amount = amount + min(formationSpeed, (3 + 0.5 * runeCount) * player_cult_speed);
		event.setAmount(event.getAmount().add(formationSpeed.min(new BigDecimal("3").add(new BigDecimal("0.5").multiply(formationRuneCount)).multiply(cultivationSpeed))));
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
		var distToFormation = formationPos.distToCenterSqr(event.player.getX(), event.player.getY(), event.player.getZ());

		for (var system : System.values()) {
			var regenDist = core.getStat(system, FormationSystemStat.ENERGY_REGEN_RANGE).doubleValue();
			var formationRegenAmount = core.getStat(system, FormationSystemStat.ENERGY_REGEN);
			var formationEnergyRunes = core.getStat(system, FormationSystemStat.ENERGY_REGEN_RUNE_COUNT);
			var systemData = cultivation.getSystemData(system);
			var currentEnergy = systemData.getStat(PlayerSystemStat.ENERGY);
			var maxEnergy = systemData.getStat(PlayerSystemStat.MAX_ENERGY);
			var energyRegen = systemData.getStat(PlayerSystemStat.ENERGY_REGEN);
			var regenAmount = formationRegenAmount.min(energyRegen.multiply(formationEnergyRunes));
			if (distToFormation <= regenDist * regenDist) {
				if (energyRegen.compareTo(new BigDecimal("0.005")) >= 0 &&
						//currentEnergy + regenAmount <= 110% of maxEnergy
						currentEnergy.add(regenAmount).compareTo(maxEnergy.multiply(new BigDecimal("1.1"))) <= 0) {
					systemData.addEnergy(formationRegenAmount);
				}
				if (system == System.ESSENCE) {
					if (cultivation.isExercising()) {
						systemData.addEnergy(formationRegenAmount);
					}
				}
			}
		}
		event.player.level.getProfiler().pop();
	}

	private static LinkedList<FormationCore> getActiveFormationCoresNearby(LevelChunk chunk, Level level) {
		int chunkRadius = 5;
		LinkedList<FormationCore> activeFormationCores = new LinkedList<>();
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
		return activeFormationCores;
	}

	@SubscribeEvent
	public static void onPlayerMayBreak(BlockEvent.BreakEvent event) {
		var breaker = event.getPlayer();
		if (breaker == null) return;
		Level level = breaker.level;
		var chunk = level.getChunkAt(event.getPos());
		var activeFormationCores = getActiveFormationCoresNearby(chunk, level);
		for (var core : activeFormationCores) {
			if (breaker == core.getOwner()) continue;
			var barrierAmount = core.getStat(FormationStat.BARRIER_AMOUNT);
			if (barrierAmount.compareTo(BigDecimal.ZERO) <= 0) continue;
			var barrierRange = core.getStat(FormationStat.BARRIER_RANGE).doubleValue();
			var distSqr = event.getPos().distSqr(core.getBlockPos());
			if (distSqr <= barrierRange * barrierRange) {
				var badge = getItemBadge(breaker, core.getBlockPos());
				if (badge.is(BREAK_TAG)) continue;
				event.setCanceled(true);
				break;
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		var interactive = event.getPlayer();
		if (interactive == null) return;
		Level level = interactive.level;
		var chunk = level.getChunkAt(event.getPos());
		var activeFormationCores = getActiveFormationCoresNearby(chunk, level);
		for (var core : activeFormationCores) {
			if (interactive == core.getOwner()) continue;
			var barrierAmount = core.getStat(FormationStat.BARRIER_AMOUNT);
			if (barrierAmount.compareTo(BigDecimal.ZERO) <= 0) continue;
			var barrierRange = core.getStat(FormationStat.BARRIER_RANGE).doubleValue();
			var distSqr = event.getPos().distSqr(core.getBlockPos());
			if (distSqr <= barrierRange * barrierRange) {
				var badge = getItemBadge(interactive, core.getBlockPos());
				if (badge.is(INTERACT_TAG)) continue;
				event.setCanceled(true);
				break;
			}
		}
	}

	private static ItemStack getItemBadge(Player player, BlockPos formationPos) {
		var inv = player.getInventory();
		for (var itemStack : inv.items) {
			if (!(itemStack.getItem() instanceof FormationBarrierBadge)) continue;
			var tag = itemStack.getTag();
			if (tag == null) continue;
			if (!tag.contains("formation")) continue;
			var formationTag = tag.getCompound("formation");
			var x = formationTag.getInt("x");
			var y = formationTag.getInt("y");
			var z = formationTag.getInt("z");
			var blockPos = new BlockPos(x, y, z);
			if (blockPos.compareTo(formationPos) == 0) {
				return itemStack;
			}
		}
		return ItemStack.EMPTY;
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
		var distSqr = formationPos.distToCenterSqr(targetPlayer.getPosition(0));
		if (distSqr <= barrierRange * barrierRange) {
			event.setCanceled(true);
		}
	}

}
