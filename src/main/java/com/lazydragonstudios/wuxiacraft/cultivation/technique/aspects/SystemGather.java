package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;

import java.math.BigDecimal;
import java.util.HashMap;

public class SystemGather extends TechniqueAspect {

	public System system;

	public SystemGather(System system) {
		this.system = system;
	}

	@Override
	public void accept(HashMap<String, Object> metaData, BigDecimal proficiency) {
		super.accept(metaData, proficiency);
		String statName = this.system.name().toLowerCase() + "-stat-" + PlayerSystemStat.CULTIVATION_SPEED.name().toLowerCase();
		String systemRawBase = system.name().toLowerCase() + "-raw-cultivation-base";
		if (metaData.containsKey(systemRawBase)) {
			double rawBase = (double) metaData.remove(systemRawBase);
			metaData.put(statName,
					//(value or 0) + (rawBase / 10)
					((BigDecimal) metaData.getOrDefault(statName, BigDecimal.ZERO)).add(new BigDecimal(rawBase).multiply(new BigDecimal("0.1"))));
		}
	}

	@Override
	public int canConnectToCount() {
		return 0;
	}

	@Override
	public int canConnectFromCount() {
		return -1;
	}

	@Override
	public void reject(HashMap<String, Object> metaData) {
		String statName = this.system.name().toLowerCase() + "-stat-" + PlayerSystemStat.CULTIVATION_SPEED.name().toLowerCase();
		metaData.put(statName,
				(double) metaData.getOrDefault(statName, 0d) - 1d);
	}

	@Override
	public void disconnect(HashMap<String, Object> metaData) {
		super.disconnect(metaData);
	}

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		return false;
	}

	@Override
	public boolean canShowForSystem(System system) {
		return system == this.system;
	}
}
