package com.lazydragonstudios.wuxiacraft.cultivation.stats;

import com.lazydragonstudios.wuxiacraft.cultivation.SystemContainer;

import java.awt.*;
import java.math.BigDecimal;

public enum PlayerSystemStat {
	//TODO add translation notation to the display formats

	/**
	 * This is the cultivation base of the current stage
	 */
	CULTIVATION_BASE(new BigDecimal("0"), true),

	/**
	 * This is the cultivation base of the current stage
	 */
	MAX_CULTIVATION_BASE(new BigDecimal("0"), false),

	/**
	 * this is how much energy the player has to use for this system
	 */
	ENERGY(new BigDecimal("0"), true),

	/**
	 * this is how much energy will regenerate for this system per tick
	 */
	ENERGY_REGEN(new BigDecimal("0.01"), false),

	/**
	 * this is how much energy the player has available for this system
	 */
	MAX_ENERGY(new BigDecimal("10"), false),

	/**
	 * This is an additional radius for the grid based on your stage
	 * This comes from the fact that cultivation a system might get comprehension just from cultivating it
	 */
	ADDITIONAL_GRID_RADIUS(new BigDecimal("0"), false),

	/**
	 * This is how fast a player cultivate in this system
	 */
	CULTIVATION_SPEED(new BigDecimal("0"), false),

	/**
	 * this is what will influence how this system will make other systems' cultivation speed slower
	 */
	SYSTEM_SUPPRESSION(new BigDecimal("0"), false),

	/**
	 * this is how fast the cast speed for the skill cast is
	 */
	CAST_SPEED(new BigDecimal("1"), false),

	/**
	 * this is how fast a skill can recover to be used again
	 */
	COOLDOWN_SPEED(new BigDecimal("1"), false),

	;

	public final BigDecimal defaultValue;

	/**
	 * Whether it can modify the value of this stat outside SystemContainer class
	 * using @see {@link SystemContainer#setStat}
	 */
	public final boolean isModifiable;

	PlayerSystemStat(BigDecimal defaultValue, boolean isModifiable) {
		this.defaultValue = defaultValue;
		this.isModifiable = isModifiable;
	}
}
