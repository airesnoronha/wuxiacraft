package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;

import java.math.BigDecimal;
import java.util.HashMap;

public abstract class ElementalConverter extends TechniqueAspect {

	/**
	 * the amount of elemental cultivation base that is going to become raw cultivation base
	 */
	public double amount;

	/**
	 * the element to be converted from
	 */
	public ResourceLocation element;

	public ElementalConverter(double amount, ResourceLocation element) {
		this.amount = amount;
		this.element = element;
	}

	@Override
	public int canConnectToCount() {
		return -1;
	}

	@Override
	public void accept(HashMap<String, Object> metaData, BigDecimal proficiency) {
		super.accept(metaData, proficiency);
		String elementBase = "element-base-" + element.getPath();
		if (!metaData.containsKey(elementBase)) return;
		double elementBaseAmount = (double) metaData.get(elementBase);
		var converted = Math.min(elementBaseAmount, this.amount);
		elementBaseAmount -= converted;
		if(elementBaseAmount == 0) metaData.remove(elementBase);
		else metaData.put(elementBase, elementBaseAmount);
		convert(converted, metaData, proficiency);
	}

	public abstract void convert(double converted, HashMap<String, Object> metaData, BigDecimal proficiency);

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if (aspect instanceof ElementalConsumer con) {
			return con.element.equals(this.element);
		}
		if (aspect instanceof ElementalConverter con) {
			return con.element.equals(this.element);
		}
		return false;
	}
}
