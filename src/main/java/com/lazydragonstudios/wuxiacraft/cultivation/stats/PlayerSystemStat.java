package com.lazydragonstudios.wuxiacraft.cultivation.stats;

import com.lazydragonstudios.wuxiacraft.cultivation.SystemContainer;

import java.awt.*;
import java.math.BigDecimal;

public enum PlayerSystemStat {
	//TODO add translation notation to the display formats

	/**
	 * This is the cultivation base of the current stage
	 */
	CULTIVATION_BASE(new BigDecimal("0"), true, new Point(5, 25), "Cultivation Base: %s"),

	/**
	 * This is the cultivation base of the current stage
	 */
	MAX_CULTIVATION_BASE(new BigDecimal("0"), false, new Point(5, 35), "Max Cultivation Base: %s"),

	/**
	 * This is the foundation of the cultivation stage
	 */
	FOUNDATION(new BigDecimal("0"), true, new Point(5, 45), "Foundation: %s"),

	/**
	 * this is how much energy the player has to use for this system
	 */
	ENERGY(new BigDecimal("0"), true, new Point(5, 55), "Energy: %s"),

	/**
	 * this is how much energy will regenerate for this system per tick
	 */
	ENERGY_REGEN(new BigDecimal("0.01"), false, new Point(5, 65), "Energy Regeneration: %s"),

	/**
	 * this is how much energy the player has available for this system
	 */
	MAX_ENERGY(new BigDecimal("10"), false, new Point(5, 75), "Max Energy: %s"),

	/**
	 * This is an additional radius for the grid based on your stage
	 * This comes from the fact that cultivation a system might get comprehension just from cultivating it
	 */
	ADDITIONAL_GRID_RADIUS(new BigDecimal("0"), false, new Point(5, 85), "Additional Grid Radius: %s"),

	/**
	 * This is how fast a player cultivate in this system
	 */
	CULTIVATION_SPEED(new BigDecimal("0"), false, new Point(5, 95), "Cultivation Speed: %s"),

	/**
	 * this is what will influence how this system will make other systems' cultivation speed slower
	 */
	SYSTEM_SUPPRESSION(new BigDecimal("0"), false, new Point(5, 105), "System Suppression: %s");

	public final BigDecimal defaultValue;

	/**
	 * Whether it can modify the value of this stat outside SystemContainer class
	 * using @see {@link SystemContainer#setStat}
	 */
	public final boolean isModifiable;

	/**
	 * This is where the label for displaying this stat will appear in the character stats sheet
	 */
	public final Point locationInStatsSheet;

	PlayerSystemStat(BigDecimal defaultValue, boolean isModifiable, Point locationInStatsSheet, String displayFormat) {
		this.defaultValue = defaultValue;
		this.isModifiable = isModifiable;
		this.locationInStatsSheet = locationInStatsSheet;
	}
}
