package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.math.BigDecimal;

public class SkillHealAspect extends SkillHitAspect {

	public SkillHealAspect() {
		super();
		this.activation = (caster, skill, hitResult) -> {
			if (hitResult.getType() == HitResult.Type.BLOCK) return false;
			if (hitResult.getType() == HitResult.Type.MISS) return false;
			if (!(hitResult instanceof EntityHitResult result)) return false;
			var target = result.getEntity();
			var casterCultivation = Cultivation.get(caster);
			var casterSystemData = casterCultivation.getSystemData(System.ESSENCE);
			BigDecimal healedAmount = this.getSkillStat(SkillStat.STRENGTH).multiply(casterSystemData.getStat(PlayerStat.STRENGTH).multiply(new BigDecimal("0.8")));
			if (target instanceof Player targetPlayer) {
				var targetCultivation = Cultivation.get(targetPlayer);
				targetCultivation.addStat(PlayerStat.HEALTH, healedAmount);
				targetCultivation.setStat(PlayerStat.HEALTH, targetCultivation.getStat(PlayerStat.HEALTH).min(targetCultivation.getStat(PlayerStat.MAX_HEALTH)));
			} else if (target instanceof LivingEntity targetLiving) {
				targetLiving.heal(healedAmount.floatValue());
			}
			return true;
		};
	}

	@Override
	public SkillAspectType getType() {
		return WuxiaSkillAspects.HEAL.get();
	}
}
