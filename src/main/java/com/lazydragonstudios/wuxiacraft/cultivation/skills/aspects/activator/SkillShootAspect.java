package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.entity.ThrowSkill;
import com.lazydragonstudios.wuxiacraft.init.WuxiaEntities;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;

import java.math.BigDecimal;

public class SkillShootAspect extends SkillActivatorAspect {

	public SkillShootAspect() {
		super();
		this.activate = (player, skillAspects) -> {
			if (player.level.isClientSide()) return false;
			var cultivation = Cultivation.get(player);
			var essenceData = cultivation.getSystemData(System.ESSENCE);
			BigDecimal cost = this.getSkillStat(SkillStat.COST).multiply(BigDecimal.valueOf(cultivation.getStrengthRegulator()));
			if (!essenceData.consumeEnergy(cost)) return false;
			var level = player.level;
			var lookAngle = player.getLookAngle();
			var entity = new ThrowSkill(WuxiaEntities.THROW_SKILL_TYPE.get(), level, skillAspects);
			entity.setPos(player.getEyePosition());
			entity.setOwner(player);
			entity.shoot(lookAngle.x, lookAngle.y, lookAngle.z, 0.86f, 0.05f);
			level.addFreshEntity(entity);
			return true;
		};
	}

	@Override
	public SkillAspectType getType() {
		return WuxiaSkillAspects.SHOOT.get();
	}
}
