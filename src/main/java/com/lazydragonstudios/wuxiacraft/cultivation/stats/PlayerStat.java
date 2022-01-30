package com.lazydragonstudios.wuxiacraft.cultivation.stats;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;

import java.awt.*;
import java.math.BigDecimal;

public enum PlayerStat {
	//TODO add these to the translate json file when we add one
	STRENGTH(new BigDecimal("0.0"), false, new Point(10, 50), "Strength: %s"),
	AGILITY(new BigDecimal("0.0"), false, new Point(10, 60), "Agility: %s"),
	HEALTH(new BigDecimal("20.0"), true, new Point(10, 10), "Current Health: %s"),
	MAX_HEALTH(new BigDecimal("20.0"), false, new Point(10, 20), "Maximum Health: %s"),
	HEALTH_REGEN(new BigDecimal("1.0"), false, new Point(10, 30), "Health Regeneration: %s"),
	HEALTH_REGEN_COST(new BigDecimal("1.0"), false, new Point(10, 40), "Health Regeneration Cost: %s");

	/**
	 * the default value of the stat
	 */
	public final BigDecimal defaultValue;

	/**
	 * this means that the value of this stat can be modified from outside Cultivation class
	 * using {@link Cultivation#setPlayerStat}
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
