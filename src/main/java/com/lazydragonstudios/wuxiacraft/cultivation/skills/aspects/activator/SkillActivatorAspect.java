package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillDescriptor;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillActivationModifierAspect;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspect;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit.SkillHitAspect;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;

import java.util.LinkedList;
import java.util.function.BiPredicate;

public class SkillActivatorAspect extends SkillAspect {

	public BiPredicate<Player, SkillDescriptor> activate;

	public SkillActivatorAspect() {
		super();
		activate = (p, c) ->  {
			return false;
		};
	}

	@Override
	public SkillAspectType getType() {
		return WuxiaSkillAspects.PUNCH.get();
	}

	public SkillActivatorAspect setActivate(BiPredicate<Player, SkillDescriptor> activate) {
		this.activate = activate;
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
