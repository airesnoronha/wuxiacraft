package com.airesnor.wuxiacraft.cultivation.skills;


import com.airesnor.wuxiacraft.utils.SkillUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class SkillPotionEffectTarget extends Skill {

	public SkillPotionEffectTarget(String name, PotionEffect effect, float distance, float cost, float progress, float castTime, float cooldown, String author) {
		super(name, false, true, cost, progress, castTime, cooldown, author);
		setAction(actor -> {
			boolean activated = false;
			Entity entity = SkillUtils.rayTraceEntities(actor, distance, 1.0f);
			if(entity instanceof EntityLivingBase) {
				activated = true;
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
			}
			return activated;
		});
	}
}
