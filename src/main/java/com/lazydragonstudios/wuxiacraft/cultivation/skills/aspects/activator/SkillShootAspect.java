package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.entity.ThrowSkill;
import com.lazydragonstudios.wuxiacraft.init.WuxiaEntities;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;

public class SkillShootAspect extends SkillActivatorAspect {

	public SkillShootAspect() {
		super();
		this.activate = (player, skillAspects) -> {
			if (player.level.isClientSide()) return false;
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
