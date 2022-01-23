package wuxiacraft.cultivation.technique.aspects;

import wuxiacraft.cultivation.System;

import java.util.HashMap;

public class AspectSystemGather extends TechniqueAspect {

	public System system;

	public AspectSystemGather(String name, System system) {
		super(name);
		this.system = system;
	}

	@Override
	public void accept(HashMap<String, Object> metaData) {
		String systemRawBase = system.name()+"-raw-cultivation-base";
		if (metaData.containsValue(systemRawBase)) {
			double rawBase = (double) metaData.remove(systemRawBase);
			metaData.put("cultivation_speed",
					(double) metaData.getOrDefault("cultivation_speed", 0d) + rawBase / 10d);
		}
	}

	@Override
	public void reject(HashMap<String, Object> metaData) {
		metaData.put("cultivation_speed",
				(double) metaData.getOrDefault("cultivation_speed", 0d) - 1d);
	}

	@Override
	public void disconnect(HashMap<String, Object> metaData) {
		super.disconnect(metaData);
	}
}
