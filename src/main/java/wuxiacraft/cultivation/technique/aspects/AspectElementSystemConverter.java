package wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;
import wuxiacraft.cultivation.System;

import java.util.HashMap;

public class AspectElementSystemConverter extends AspectElementalConverter {

	/**
	 * the system to be converted to
	 */
	public System system;

	public AspectElementSystemConverter(String name, double amount, ResourceLocation element, System system) {
		super(name, amount, element);
		this.system = system;
	}

	@Override
	public void convert(double converted, HashMap<String, Object> metaData) {
		String systemRawBase = system.name()+"-raw-cultivation-base";
		String elementBonus = "element-" + this.element.getPath();
		metaData.put(systemRawBase, (double)metaData.getOrDefault(systemRawBase, 0d) + converted);
		metaData.put(elementBonus, (double)metaData.getOrDefault(elementBonus, 0) + converted);
	}
}
