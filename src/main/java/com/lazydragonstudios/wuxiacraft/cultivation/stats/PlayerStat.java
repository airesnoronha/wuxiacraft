package com.lazydragonstudios.wuxiacraft.cultivation.stats;

import java.awt.*;
import java.math.BigDecimal;

public enum PlayerStat {
	//TODO add these to the translate json file when we add one
	HEALTH(new BigDecimal("20.0"), true, new Point(5, 5), "Current Health: %s"),
	MAX_HEALTH(new BigDecimal("20.0"), false, new Point(5, 15), "Maximum Health: %s"),
	HEALTH_REGEN(new BigDecimal("0.01"), false, new Point(5, 25), "Health Regeneration: %s"),
	HEALTH_REGEN_COST(new BigDecimal("0.03"), false, new Point(5, 35), "Health Regeneration Cost: %s"),
	BARRIER(new BigDecimal("0.0"), true, new Point(-1, 5), "Barrier : %s"),
	MAX_BARRIER(new BigDecimal("0.0"), false, new Point(-1, 15), "Max Barrier: %s"),
	BARRIER_REGEN(new BigDecimal("0.0"), false, new Point(-1, 25), "Barrier Regeneration: %s"),
	BARRIER_REGEN_COST(new BigDecimal("0.0"), false, new Point(-1, 35), "Barrier Regeneration Cost: %s"),
	STRENGTH(new BigDecimal("0.0"), false, new Point(5, 45), "Strength: %s"),
	AGILITY(new BigDecimal("0.0"), false, new Point(5, 55), "Agility: %s"),
	EXERCISE_COST(new BigDecimal("0.02"), false, new Point(-1,45 ), "Exercise Cost: %s"),
	EXERCISE_CONVERSION(new BigDecimal("0.01"), false, new Point(-1, 55), "Exercise Conversion: %s"),
	DETECTION_RANGE(new BigDecimal("0.00"), false, new Point(5, 65), "Detection Range: %s"),
	DETECTION_STRENGTH(new BigDecimal("0.00"), false, new Point(5, 75), "Detection Strength: %s"),
	DETECTION_RESISTANCE(new BigDecimal("0.00"), false, new Point(5, 85), "Detection Resistance: %s"),
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

	/**
	 * the text format for displaying the stat
	 */
	public final String displayFormat;

	PlayerStat(BigDecimal defaultValue, boolean isModifiable, Point locationInStatsSheet, String displayFormat) {
		this.defaultValue = defaultValue;
		this.isModifiable = isModifiable;
		this.locationInStatsSheet = locationInStatsSheet;
		this.displayFormat = displayFormat;
	}
}
