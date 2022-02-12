package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects;

import net.minecraft.world.entity.player.Player;

import javax.swing.*;
import java.util.LinkedList;
import java.util.function.Predicate;

public class SkillActivatorAspect extends SkillAspect {

	private Predicate<Player> onHit;

	public SkillActivatorAspect(String name) {
		super(name);
		onHit = p -> false;
	}

	public SkillActivatorAspect setOnHit(Predicate<Player> onHit) {
		this.onHit = onHit;
		return this;
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
