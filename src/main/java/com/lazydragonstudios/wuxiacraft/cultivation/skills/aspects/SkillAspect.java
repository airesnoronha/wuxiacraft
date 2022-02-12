package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.LinkedList;


public abstract class SkillAspect extends ForgeRegistryEntry<SkillAspect> {

	public final String name;

	public SkillAspect(String name) {
		this.name = name;
	}

	/**
	 * Will determine whether it can connect to next node
	 *
	 * @param aspect the next skill aspect trying to connect to this
	 * @return whether it can connect or not
	 */
	public abstract boolean canConnect(SkillAspect aspect);

	public abstract boolean canCompile(LinkedList<SkillAspect> aspectChain);

}
