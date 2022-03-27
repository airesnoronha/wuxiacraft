package com.lazydragonstudios.wuxiacraft.formation;

import com.lazydragonstudios.wuxiacraft.blocks.entity.FormationCore;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.event.CultivatingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.math.BigDecimal;

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

}
