package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects;

import java.util.LinkedList;

public class SkillElementalHitModifier extends SkillAspect {

	public SkillElementalHitModifier(String name) {
		super();
	}

	@Override
	public SkillAspectType getType() {
		return null;
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
