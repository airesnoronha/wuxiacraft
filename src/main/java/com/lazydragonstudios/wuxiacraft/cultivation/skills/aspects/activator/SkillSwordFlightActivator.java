package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator;

import com.lazydragonstudios.wuxiacraft.capabilities.ClientAnimationState;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import com.lazydragonstudios.wuxiacraft.networking.BroadcastAnimationChangeRequestMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.SwordItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SkillSwordFlightActivator extends SkillActivatorAspect {

	public SkillSwordFlightActivator() {
		this.activate = (player, skill) -> {
			var cultivation = Cultivation.get(player);
			var systemData = cultivation.getSystemData(System.ESSENCE);
			if (!(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SwordItem)) return false;
			var cost = skill.getStatValue(SkillStat.COST);
			var max_speed = cultivation.getStat(PlayerStat.STRENGTH, true).multiply(skill.getStatValue(SkillStat.STRENGTH)).multiply(new BigDecimal("0.3"));
			var speed = max_speed.multiply(BigDecimal.valueOf(cultivation.getStrengthRegulator()));
			//consumed energy = cost*(strengthRegulator)
			if (!systemData.consumeEnergy(cost.multiply(BigDecimal.valueOf(cultivation.getStrengthRegulator())))) {
				skill.setStat(SkillStat.CURRENT_COOLDOWN, new BigDecimal("300"));
				return false;
			}
			var direction = player.getLookAngle();
			direction.scale(speed.floatValue());
			player.setDeltaMovement(direction.x, direction.y, direction.z);
			player.fallDistance = 0f;
			if (player.level.isClientSide) {
				var animationState = ClientAnimationState.get(player);
				if (!animationState.isSwordFlight()) {
					animationState.setSwordFlight(true);
					WuxiaPacketHandler.INSTANCE.sendToServer(new BroadcastAnimationChangeRequestMessage(animationState, cultivation.isCombat()));
				}
			}
			return true;
		};
	}

	@Override
	public SkillAspectType getType() {
		return WuxiaSkillAspects.SWORD_FLIGHT.get();
	}
}
