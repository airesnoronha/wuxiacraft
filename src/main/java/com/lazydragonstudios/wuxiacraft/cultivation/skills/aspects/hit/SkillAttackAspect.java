package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.combat.WuxiaDamageSource;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.init.WuxiaElements;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

import java.math.BigDecimal;

public class SkillAttackAspect extends SkillHitAspect {

	public SkillAttackAspect() {
		super();
		setSkillStat(SkillStat.COST, BigDecimal.ONE);
		setSkillStat(SkillStat.COOLDOWN, BigDecimal.ONE);
		setSkillStat(SkillStat.CAST_TIME, BigDecimal.ONE);
		this.activation = (caster, chain, result) -> {
			if (result instanceof EntityHitResult entityResult) {
				if (entityResult.getEntity() instanceof LivingEntity target) {
					var damageSource = new WuxiaDamageSource("wuxiacraft.skill.attack", WuxiaElements.PHYSICAL.get(), caster, new BigDecimal("10"));
					target.hurt(damageSource, 1f);
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
