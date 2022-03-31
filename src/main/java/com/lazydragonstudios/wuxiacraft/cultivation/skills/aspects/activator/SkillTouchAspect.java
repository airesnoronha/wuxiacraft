package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit.SkillHitAspect;
import com.lazydragonstudios.wuxiacraft.util.SkillUtil;
import net.minecraft.world.phys.HitResult;

import java.math.BigDecimal;

public class SkillTouchAspect extends SkillActivatorAspect {

	/**
	 * Try being more arbitrary than this hahaha
	 */
	public SkillTouchAspect() {
		super();
		this.setActivate((player, chain) -> {
			var cultivation = Cultivation.get(player);
			var essenceData = cultivation.getSystemData(System.ESSENCE);
			var result = SkillUtil.getHitResult(player, 2, e -> e != player);
			if (result.getType() == HitResult.Type.MISS) return false;
			if (!essenceData.consumeEnergy(this.getSkillStat(SkillStat.COST))) return false;
			player.swinging = true;
			for (var link : chain) {
				if (link instanceof SkillHitAspect) {
					((SkillHitAspect) link).activate(player, chain, result);
					break;
				}
			}
			return false;
		});
	}

	@Override
	public SkillAspectType getType() {
		return super.getType();
	}
}
