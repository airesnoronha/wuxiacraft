package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit.SkillHitAspect;

import java.util.LinkedList;

public class SkillActivationModifierAspect extends SkillAspect {

	public SkillActivationModifierAspect() {
		super();
	}

	@Override
	public boolean canConnect(SkillAspect aspect) {
		return aspect instanceof SkillActivationModifierAspect || aspect instanceof SkillHitAspect;
	}

	@Override
	public boolean canCompile(LinkedList<SkillAspect> aspectChain) {
		return false;
	}
}
