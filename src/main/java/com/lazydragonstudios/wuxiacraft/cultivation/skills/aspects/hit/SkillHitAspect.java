package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspect;
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

	public boolean activate(Player player, LinkedList<SkillAspect> aspectsChain, HitResult result) {
		return this.activation.hit(player, aspectsChain, result);
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
