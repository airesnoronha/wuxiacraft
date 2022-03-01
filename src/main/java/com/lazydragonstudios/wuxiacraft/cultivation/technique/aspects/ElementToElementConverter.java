package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class ElementToElementConverter extends ElementalConverter {

	public final double conversionRate;

	public final ResourceLocation destinationElement;

	public ElementToElementConverter(double amount, double conversionRate, ResourceLocation elementSource, ResourceLocation elementDestination) {
		super(amount, elementSource);
		this.conversionRate = conversionRate;
		this.destinationElement = elementDestination;
	}

	@Override
	public void convert(double converted, HashMap<String, Object> metaData) {
		String elementBase = "element-base-" + this.element.getPath();
		double finalValue = converted * this.conversionRate;
		double initialValue = (double) metaData.getOrDefault(elementBase, 0d);
		finalValue += initialValue;
		metaData.put(elementBase, finalValue);
	}
}
