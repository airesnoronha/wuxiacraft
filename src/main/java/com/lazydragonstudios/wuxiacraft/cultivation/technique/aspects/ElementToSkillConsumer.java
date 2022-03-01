package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;

public class ElementToSkillConsumer extends ElementalConsumer {

	public final ResourceLocation skillAspectTypeLocation;

	public ElementToSkillConsumer(ResourceLocation element, double cost, ResourceLocation skillAspectTypeLocation) {
		super(element, cost);
		this.skillAspectTypeLocation = skillAspectTypeLocation;
	}

	@Override
	public void consumed(HashMap<String, Object> metaData) {
		if (!metaData.containsKey("skills")) metaData.put("skills", new HashSet<ResourceLocation>());
		//noinspection unchecked
		var skillSet = (HashSet<ResourceLocation>) metaData.get("skills");
		skillSet.add(this.skillAspectTypeLocation);
	}

	@Override
	public void notConsumed(HashMap<String, Object> metaData) {

	}
}
