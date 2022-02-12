package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects;

import java.util.LinkedList;
import java.util.function.Predicate;

public class SkillHitModifierAspect extends SkillAspect {


	public SkillHitModifierAspect(String name) {
		super(name);
	}

	@Override
	public boolean canConnect(SkillAspect aspect) {
		return false;
	}

	@Override
	public boolean canCompile(LinkedList<SkillAspect> aspectChain) {
		return false;
	}
}
