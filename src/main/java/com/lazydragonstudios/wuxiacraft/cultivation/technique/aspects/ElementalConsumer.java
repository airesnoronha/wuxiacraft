package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;

import java.math.BigDecimal;
import java.util.HashMap;

public abstract class ElementalConsumer extends TechniqueAspect {

	public ResourceLocation element;
	public double cost;

	public ElementalConsumer(ResourceLocation element, double cost) {
		this.element = element;
		this.cost = cost;
	}

	@Override
	public void accept(HashMap<String, Object> metaData, BigDecimal proficiency) {
		super.accept(metaData, proficiency);
		String elementBase = "element-base-" + element.getPath();
		if (metaData.containsKey(elementBase)) {
			double elementalCultivationBase = (double) metaData.get(elementBase);
			if (elementalCultivationBase >= cost) {
				elementalCultivationBase -= cost;
				metaData.put(elementBase, elementalCultivationBase);
				consumed(metaData);
			} else {
				notConsumed(metaData);
			}
		}
	}

	/**
	 * What happens when there was enough elemental cultivation base
	 *
	 * @param metaData the metadata passing through this aspect
	 */
	public abstract void consumed(HashMap<String, Object> metaData);

	/**
	 * Used when the elemental cultivation base is not enough
	 * Mostly used for punishments
	 *
	 * @param metaData the metadata passing through this aspect
	 */
	public abstract void notConsumed(HashMap<String, Object> metaData);

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
