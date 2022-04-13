package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.cultivation.CultivationRealm;
import com.lazydragonstudios.wuxiacraft.cultivation.CultivationStage;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import net.minecraftforge.registries.IForgeRegistry;
import com.lazydragonstudios.wuxiacraft.cultivation.Element;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.TechniqueAspect;

import java.util.function.Supplier;

public class WuxiaRegistries {

	public static Supplier<IForgeRegistry<CultivationRealm>> CULTIVATION_REALMS;

	public static Supplier<IForgeRegistry<CultivationStage>> CULTIVATION_STAGES;

	public static Supplier<IForgeRegistry<Element>> ELEMENTS;

	public static Supplier<IForgeRegistry<TechniqueAspect>> TECHNIQUE_ASPECT;

	public static Supplier<IForgeRegistry<SkillAspectType>> SKILL_ASPECT;
}
