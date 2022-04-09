package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.combat.WuxiaDamageSource;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaElements;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

import java.math.BigDecimal;

public class SkillAttackAspect extends SkillHitAspect {

	public SkillAttackAspect() {
		super();
		this.activation = (caster, skill, result) -> {
			if (result instanceof EntityHitResult entityResult) {
				if (entityResult.getEntity() instanceof LivingEntity target) {
					var cultivation = Cultivation.get(caster);
					var systemData = cultivation.getSystemData(System.ESSENCE);
					var systemStrength = systemData.getStat(PlayerStat.STRENGTH);
					var skillStrength = systemStrength.multiply(BigDecimal.valueOf(cultivation.getStrengthRegulator()));
					var damage = skill.getStatValue(SkillStat.STRENGTH).multiply(skillStrength.multiply(new BigDecimal("1.5")));
					var damageSource = new WuxiaDamageSource("wuxiacraft.skill.attack", WuxiaElements.PHYSICAL.get(), caster, damage);
					target.hurt(damageSource, damageSource.getDamage().floatValue());
				}
			}
			return false;
		};
	}

	@Override
	public SkillAspectType getType() {
		return WuxiaSkillAspects.ATTACK.get();
	}
}
