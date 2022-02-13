package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspect;
import com.lazydragonstudios.wuxiacraft.util.SkillUtil;

import java.util.LinkedList;

public class SkillTouchAspect extends SkillActivatorAspect {

	public SkillTouchAspect() {
		super();
		this.setActivate((player, chain) -> {
			SkillUtil.getHitResult(player, 2, e -> e!=player);
			return false;
		});
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
