package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillDescriptor;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspect;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillHitModifierAspect;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;

import java.util.LinkedList;
import java.util.function.BiPredicate;

public class SkillHitAspect extends SkillAspect {

	ISkillHitAction activation;

	public SkillHitAspect() {
		super();
		this.activation = (p,l,r) -> false;
	}

	public SkillAspect setActivation(ISkillHitAction activation) {
		this.activation = activation;
		return this;
	}

	@Override
	public SkillAspectType getType() {
		return WuxiaSkillAspects.PUNCH.get();
	}

	public boolean activate(Player player, SkillDescriptor skill, HitResult result) {
		return this.activation.hit(player, skill, result);
	}

	@Override
	public boolean canConnect(SkillAspect aspect) {
		return aspect instanceof SkillHitModifierAspect;
	}

	@Override
	public boolean canCompile(LinkedList<SkillAspect> aspectChain) {
		return false;
	}
}
