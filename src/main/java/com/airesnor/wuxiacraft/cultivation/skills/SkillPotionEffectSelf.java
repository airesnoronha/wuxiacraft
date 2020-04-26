package com.airesnor.wuxiacraft.cultivation.skills;

import net.minecraft.potion.PotionEffect;

public class SkillPotionEffectSelf extends Skill {
	public SkillPotionEffectSelf(String name, PotionEffect effect, float cost, float progress, float castTime, float cooldown, String author) {
		super(name, false, false, cost, progress, castTime, cooldown, author);
		setAction(actor -> {
			actor.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
			return true;
		});
	}
}
