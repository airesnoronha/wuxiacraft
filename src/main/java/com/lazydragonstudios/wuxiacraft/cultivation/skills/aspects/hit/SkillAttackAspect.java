package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.combat.WuxiaDamageSource;
import com.lazydragonstudios.wuxiacraft.init.WuxiaElements;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

import java.math.BigDecimal;

public class SkillAttackAspect extends SkillHitAspect {

	public SkillAttackAspect() {
		super();
		this.activation = (caster, chain, result) -> {
			if(result instanceof EntityHitResult entityResult) {
				if(entityResult.getEntity() instanceof LivingEntity target) {
					var damageSource = new WuxiaDamageSource("wuxiacraft.skill.attack", WuxiaElements.PHYSICAL.get(), caster, new BigDecimal("10"));
					target.hurt(damageSource, 1f);
				}
			}
			return false;
		};
		this.castModifier = BigDecimal.ONE;
		this.cooldownModifier= BigDecimal.ONE;
		this.costModifier = BigDecimal.ONE;
	}


}
