package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit.SkillHitAspect;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import net.minecraft.world.phys.EntityHitResult;

import java.math.BigDecimal;

public class SkillSelfAspect extends SkillActivatorAspect {

	public SkillSelfAspect() {
		super();
		this.setActivate((player, skill) -> {
			var cultivation = Cultivation.get(player);
			var essenceData = cultivation.getSystemData(System.ESSENCE);
			BigDecimal cost = this.getSkillStat(SkillStat.COST).multiply(BigDecimal.valueOf(cultivation.getStrengthRegulator()));
			if (!essenceData.consumeEnergy(cost)) return false;
			var result = new EntityHitResult(player, player.getEyePosition());
			player.swinging = true;
			for (var link : skill.getSkillChain()) {
				if (link instanceof SkillHitAspect) {
					((SkillHitAspect) link).activate(player, skill, result);
					break;
				}
			}
			return true;
		});
	}

	@Override
	public SkillAspectType getType() {
		return WuxiaSkillAspects.SELF.get();
	}
}
