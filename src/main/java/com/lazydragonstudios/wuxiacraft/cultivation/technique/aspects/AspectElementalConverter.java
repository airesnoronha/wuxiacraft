package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public abstract class AspectElementalConverter extends TechniqueAspect {

	/**
	 * the amount of elemental cultivation base that is going to become raw cultivation base
	 */
	public double amount;

	/**
	 * the element to be converted from
	 */
	public ResourceLocation element;

	public AspectElementalConverter(String name, ResourceLocation textureLocation, double amount, ResourceLocation element) {
		super(name, textureLocation);
		this.amount = amount;
		this.element = element;
	}

	@Override
	public void accept(HashMap<String, Object> metaData) {
		String elementBase = "element-base-" + element.getPath();
		if(metaData.containsKey(elementBase)) {
			double elementBaseAmount = (double)metaData.get(elementBase);
			var converted = Math.min(elementBaseAmount, this.amount);
			elementBaseAmount-= converted;
			metaData.put(elementBase, elementBaseAmount);
			convert(converted, metaData);
		}
	}

	public abstract void convert(double converted, HashMap<String, Object> metaData);

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if(aspect instanceof AspectElementalConsumer) return true;
		if(aspect instanceof AspectElementalConverter) return true;
		if(aspect instanceof AspectElementalGenerator) return true;
		return false;
	}
}
