package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects;

import net.minecraft.world.entity.player.Player;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.function.BiPredicate;

public class SkillHitAspect extends SkillAspect {

	BiPredicate<Player, LinkedList<SkillAspect>> activation;

	public BigDecimal costModifier = BigDecimal.ZERO;

	public BigDecimal castModifier = BigDecimal.ZERO;

	public BigDecimal cooldownModifier = BigDecimal.ZERO;

	public SkillHitAspect(String name) {
		super(name);
		this.activation = (p,l) -> false;
	}

	public SkillAspect setCostModifier(BigDecimal costModifier) {
		this.costModifier = costModifier;
		return this;
	}

	public SkillAspect setCastModifier(BigDecimal castModifier) {
		this.castModifier = castModifier;
		return this;
	}

	public SkillAspect setCooldownModifier(BigDecimal cooldownModifier) {
		this.cooldownModifier = cooldownModifier;
		return this;
	}

	public SkillAspect setActivation(BiPredicate<Player, LinkedList<SkillAspect>> activation) {
		this.activation = activation;
		return this;
	}

	public boolean activate(Player player, LinkedList<SkillAspect> aspectsChain) {
		return this.activation.test(player, aspectsChain);
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
