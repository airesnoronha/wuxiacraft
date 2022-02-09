package com.lazydragonstudios.wuxiacraft.cultivation.stats;

import java.awt.*;
import java.math.BigDecimal;

public enum PlayerStat {
	HEALTH(new BigDecimal("20.0"), true, new Point(5, 5)),
	MAX_HEALTH(new BigDecimal("20.0"), false, new Point(5, 15)),
	HEALTH_REGEN(new BigDecimal("0.01"), false, new Point(5, 25)),
	HEALTH_REGEN_COST(new BigDecimal("0.03"), false, new Point(5, 35)),
	BARRIER(new BigDecimal("0.0"), true, new Point(-1, 5)),
	MAX_BARRIER(new BigDecimal("0.0"), false, new Point(-1, 15)),
	BARRIER_REGEN(new BigDecimal("0.0"), false, new Point(-1, 25)),
	BARRIER_REGEN_COST(new BigDecimal("0.0"), false, new Point(-1, 35)),
	STRENGTH(new BigDecimal("0.0"), false, new Point(5, 45)),
	AGILITY(new BigDecimal("0.0"), false, new Point(5, 55)),
	EXERCISE_COST(new BigDecimal("0.02"), false, new Point(-1, 45)),
	EXERCISE_CONVERSION(new BigDecimal("0.01"), false, new Point(-1, 55)),
	DETECTION_RANGE(new BigDecimal("0.00"), false, new Point(5, 65)),
	DETECTION_STRENGTH(new BigDecimal("0.00"), false, new Point(5, 75)),
	DETECTION_RESISTANCE(new BigDecimal("0.00"), false, new Point(5, 85)),
	LIVES(new BigDecimal("3"), true, new Point(-1, 65)),
	;
	/**
	 * the default value of the stat
	 */
	public final BigDecimal defaultValue;

	/**
	 * this means that the value of this stat can be modified from outside Cultivation class
	 * Also means this value is stored, but not calculated in every alteration
	 */
	public final boolean isModifiable;

	/**
	 * the location in which it'll appear in the character stats screen
	 */
	public final Point locationInStatsSheet;

	PlayerStat(BigDecimal defaultValue, boolean isModifiable, Point locationInStatsSheet) {
		this.defaultValue = defaultValue;
		this.isModifiable = isModifiable;
		this.locationInStatsSheet = locationInStatsSheet;
	}
}
