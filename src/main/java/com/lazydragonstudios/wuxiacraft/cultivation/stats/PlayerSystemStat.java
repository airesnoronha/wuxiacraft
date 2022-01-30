package com.lazydragonstudios.wuxiacraft.cultivation.stats;

import com.lazydragonstudios.wuxiacraft.cultivation.SystemContainer;

import java.awt.*;
import java.math.BigDecimal;

public enum PlayerSystemStat {
	//TODO add translation notation to the display formats
	CULTIVATION_BASE(new BigDecimal("0"), false, new Point(10, 10), "Cultivation Base: %s"),
	FOUNDATION(new BigDecimal("0"), false, new Point(10, 20), "Foundation: %s"),
	ENERGY(new BigDecimal("0"), true, new Point(10, 30), "Energy: %s"),
	ENERGY_REGEN(new BigDecimal("1"), false, new Point(10, 40), "Energy Regeneration: %s"),
	MAX_ENERGY(new BigDecimal("10"), false, new Point(10, 50), "Max Energy: %s"),
	CULTIVATION_SPEED(new BigDecimal("0"), false, new Point(10, 60), "Cultivation Speed: %s");

	public final BigDecimal defaultValue;

	/**
	 * Whether it can modify the value of this stat outside SystemContainer class
	 * using @see {@link SystemContainer#setStat}
	 */
	public final boolean isModifiable;

	public final Point locationInStatsSheet;

	public final String displayFormat;

	PlayerSystemStat(BigDecimal defaultValue, boolean isModifiable, Point locationInStatsSheet, String displayFormat) {
		this.defaultValue = defaultValue;
		this.isModifiable = isModifiable;
		this.locationInStatsSheet = locationInStatsSheet;
		this.displayFormat = displayFormat;
	}
}
