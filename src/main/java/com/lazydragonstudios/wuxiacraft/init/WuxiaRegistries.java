package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.cultivation.CultivationRealm;
import com.lazydragonstudios.wuxiacraft.cultivation.CultivationStage;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import net.minecraftforge.registries.IForgeRegistry;
import com.lazydragonstudios.wuxiacraft.cultivation.Element;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.TechniqueAspect;

public class WuxiaRegistries {

	public static IForgeRegistry<CultivationRealm> CULTIVATION_REALMS;

	public static IForgeRegistry<CultivationStage> CULTIVATION_STAGES;

	public static IForgeRegistry<Element> ELEMENTS;

	public static IForgeRegistry<TechniqueAspect> TECHNIQUE_ASPECT;

	public static IForgeRegistry<SkillAspectType> SKILL_ASPECT;
}
