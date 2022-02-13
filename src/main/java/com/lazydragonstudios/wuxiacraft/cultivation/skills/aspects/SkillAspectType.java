package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.lang.reflect.InvocationTargetException;

public class SkillAspectType extends ForgeRegistryEntry<SkillAspectType> {

	public final Creator creator;

	public static SkillAspectType build(Creator creator) {
			return new SkillAspectType(creator);
	}

	public SkillAspectType(Creator creator) {
		this.creator = creator;
	}

	@FunctionalInterface
	public interface Creator {
		SkillAspect create();
	}

}
