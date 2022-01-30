package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class AspectElementSystemConverter extends AspectElementalConverter {

	/**
	 * the system to be converted to
	 */
	public System system;

	public AspectElementSystemConverter(String name, ResourceLocation textureLocation, double amount, ResourceLocation element, System system) {
		super(name, textureLocation, amount, element);
		this.system = system;
	}

	@Override
	public void convert(double converted, HashMap<String, Object> metaData) {
		String systemRawBase = system.name().toLowerCase()+"-raw-cultivation-base";
		String elementBonus = "element-" + this.element.getPath();
		metaData.put(systemRawBase, (double)metaData.getOrDefault(systemRawBase, 0d) + converted);
		metaData.put(elementBonus, (double)metaData.getOrDefault(elementBonus, 0d) + converted);
	}

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if(aspect instanceof AspectSystemGather) return true;
		return super.canConnect(aspect);
	}
}
