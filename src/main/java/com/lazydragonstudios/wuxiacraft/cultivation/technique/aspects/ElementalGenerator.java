package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;

import java.math.BigDecimal;
import java.util.HashMap;

public class ElementalGenerator extends TechniqueAspect {

	public double generated;

	public ResourceLocation element;

	@SuppressWarnings("rawtypes")
	private final static HashMap<Class, Integer> priority = new HashMap<>();

	static {
		priority.put(ElementalGenerator.class, -3);
		priority.put(ElementalConverter.class, -2);
		priority.put(ElementalConsumer.class, -1);
	}

	public ElementalGenerator(double generated, ResourceLocation element) {
		super();
		this.generated = generated;
		this.element = element;
	}

	@Override
	public void accept(HashMap<String, Object> metaData, BigDecimal proficiency) {
		super.accept(metaData, proficiency);
		String elementBase = "element-base-" + element.getPath();
		metaData.put(elementBase, (double) metaData.getOrDefault(elementBase, 0d) + this.generated);
	}

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if (aspect instanceof ElementalGenerator gen) {
			return gen.element.equals(this.element);
		}
		if (aspect instanceof ElementalConsumer con) {
			return con.element.equals(this.element);
		}
		if (aspect instanceof ElementalConverter con) {
			return con.element.equals(this.element);
		}
		return false;
	}

	@Override
	public int connectPrioritySorter(TechniqueAspect aspect1, TechniqueAspect aspect2) {
		int priority1 = priority.getOrDefault(aspect1.getClass(), 0);
		int priority2 = priority.getOrDefault(aspect2.getClass(), 0);
		int finalPriority = priority1 - priority2;
		return finalPriority != 0 ? finalPriority / Math.abs(finalPriority) : 0;
	}
}
