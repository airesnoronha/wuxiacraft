package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class AspectElementSystemConverter extends AspectElementalConverter {

	/**
	 * the system to be converted to
	 */
	public System system;

	@SuppressWarnings("rawtypes")
	private final static HashMap<Class, Integer> priority = new HashMap<>();
	static {
		priority.put(AspectElementSystemConverter.class, -2);
		priority.put(AspectSystemGather.class, -1);
	}

	public AspectElementSystemConverter(String name, ResourceLocation textureLocation, double amount, ResourceLocation element, System system) {
		super(name, textureLocation, amount, element);
		this.system = system;
	}

	@Override
	public void convert(double converted, HashMap<String, Object> metaData) {
		String systemRawBase = system.name().toLowerCase() + "-raw-cultivation-base";
		String elementBonus = "element-" + this.element.getPath();
		metaData.put(systemRawBase, (double) metaData.getOrDefault(systemRawBase, 0d) + converted);
		metaData.put(elementBonus, (double) metaData.getOrDefault(elementBonus, 0d) + converted);
	}

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if (aspect instanceof AspectElementSystemConverter con) {
			return con.element.equals(this.element);
		}
		if (aspect instanceof AspectSystemGather) return true;
		return super.canConnect(aspect);
	}

	@Override
	public int connectPrioritySorter(TechniqueAspect aspect1, TechniqueAspect aspect2) {
		int priority1 = priority.getOrDefault(aspect1.getClass(), 0);
		int priority2 = priority.getOrDefault(aspect2.getClass(), 0);
		int finalPriority = priority1-priority2;
		return finalPriority/Math.abs(finalPriority);
	}
}
