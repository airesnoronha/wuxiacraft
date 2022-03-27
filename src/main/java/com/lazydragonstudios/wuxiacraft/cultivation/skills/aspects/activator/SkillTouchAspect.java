package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator;

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
		this.skillStats.put(SkillStat.CAST_TIME, new BigDecimal("6"));
		this.skillStats.put(SkillStat.COOLDOWN, new BigDecimal("2"));
		this.skillStats.put(SkillStat.COST, new BigDecimal("0"));
		this.setActivate((player, chain) -> {
			var result = SkillUtil.getHitResult(player, 2, e -> e != player);
			if (result.getType() == HitResult.Type.MISS) return false;
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
