package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects;

import javax.swing.*;
import java.util.LinkedList;

public class SkillActivationModifierAspect extends SkillAspect {

	public SkillActivationModifierAspect(String name) {
		super(name);
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
