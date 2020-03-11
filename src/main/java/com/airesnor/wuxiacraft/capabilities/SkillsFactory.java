package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.skills.SkillCap;

import java.util.concurrent.Callable;

public class SkillsFactory implements Callable<SkillCap> {
	@Override
	public SkillCap call() throws Exception {
		return new SkillCap();
	}
}
