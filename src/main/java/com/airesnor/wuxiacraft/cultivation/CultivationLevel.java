package com.airesnor.wuxiacraft.cultivation;

import net.minecraft.client.resources.I18n;

public enum CultivationLevel {
	BODY_REFINEMENT			("wuxiacraft.cultivation.body_refinement", 		5, 1000F, 1.00F,1.00F, CultivationCategory.ENERGY_PERCEPTION),
	SOUL_REFINEMENT			("wuxiacraft.cultivation.soul_refinement", 		5, 1276.28F, 1.13F,1.28F, CultivationCategory.ENERGY_PERCEPTION),
	QI_PATHS_REFINEMENT		("wuxiacraft.cultivation.qi_paths_refinement", 	5, 1628.89F, 1.15F,1.63F, CultivationCategory.ENERGY_PERCEPTION),
	DANTIAN_CONDENSING		("wuxiacraft.cultivation.dantian_condensing", 	5, 2078.93F, 1.27F,2.08F, CultivationCategory.ENERGY_PERCEPTION),
	EARTH_LAW				("wuxiacraft.cultivation.earth_law", 			10, 2653.30F, 1.42F,2.66F, CultivationCategory.MARTIAL_LAW),
	SKY_LAW					("wuxiacraft.cultivation.sky_law", 				10, 4321.94F, 1.6F,4.14F, CultivationCategory.MARTIAL_LAW),
	TRUE_LAW				("wuxiacraft.cultivation.true_law", 				10, 7039.99F, 2F,6.43F, CultivationCategory.MARTIAL_LAW, true, false, false),
	MARTIAL_LAW				("wuxiacraft.cultivation.martial_law", 			10, 11467.40F, 2.5F,10.00F, CultivationCategory.MARTIAL_LAW, true, false, false),
	IMMORTALITY_LAW			("wuxiacraft.cultivation.immortality_law", 		15, 18679.19F, 3.13F,15.54F, CultivationCategory.IMMORTAL_FOUNDATION, true, false, false),
	IMMORTALITY_REFINEMENT	("wuxiacraft.cultivation.immortality_refinement", 15, 38832.69F, 3.91F,28.47F, CultivationCategory.IMMORTAL_FOUNDATION, true, false, false),
	IMMORTAL_FOUNDATION		("wuxiacraft.cultivation.immortal_foundation", 	15, 80730.37F, 5.38F,52.14F, CultivationCategory.IMMORTAL_FOUNDATION, true, true, false),
	TRUE_IMMORTAL			("wuxiacraft.cultivation.true_immortal", 		15, 167832.63F, 7.39F,95.51F, CultivationCategory.IMMORTAL_FOUNDATION, true, true, false),
	MARTIAL_IMMORTAL		("wuxiacraft.cultivation.martial_immortal", 		20, 348911.99F, 10.17F,174.96F, CultivationCategory.DIVINE_LAW, true, true, false),
	DIVINE_LAW				("wuxiacraft.cultivation.divine_law", 			20, 925767.37F, 20.97F,368.98F, CultivationCategory.DIVINE_LAW, true, true, false),
	DIVINE_PHENOMENON		("wuxiacraft.cultivation.divine_phenomenon", 	20, 2456336.44F, 31.45F,778.19F, CultivationCategory.DIVINE_LAW, true, true, true),
	TRUE_GOD				("wuxiacraft.cultivation.true_god", 				1000000, 65117391.84F, 47.18F,1641.20F, CultivationCategory.DIVINE_LAW, true, true, true);

	/**
	 * The current level name for displaying.
	 */
	private final String levelName;

	/**
	 * Amount of sub levels inside this major level.
	 */
	public final int subLevels;

	/**
	 * Progress needed to accomplish to get to next level, similar to Experience.
	 */
	private final float baseProgress;

	/**
	 * The amount of speed the player gets on the level.
	 */
	private final float baseSpeedModifier;

	/**
	 * The amount of strength (for damaging other entities) the player will have.
	 */
	private final float baseStrengthModifier;

	/**
	 * The levels category of the level
	 */
	public final CultivationCategory category;

	/**
	 * this allow players to fly
	 */
	public final boolean canFly;

	/**
	 * this allows players to fly without cost
	 */
	public final boolean freeFlight;

	/**
	 * this allows players to fly
	 */
	public final boolean teleportation;

	/**
	 * Constructor for the level.
	 * @param levelName The current level name for displaying.
	 * @param subLevels Amount of sub levels inside this major level.
	 * @param baseProgress Progress needed to accomplish to get to next level, similar to Experience.
	 * @param baseSpeedModifier The amount of speed the player gets on the level.
	 * @param baseStrengthModifier The amount of strength (for damaging other entities) the player will have.
	 * @param category The category of this level.
	 */
	CultivationLevel(String levelName, int subLevels, float baseProgress, float baseSpeedModifier, float baseStrengthModifier, CultivationCategory category ) {
		this.levelName = levelName;
		this.subLevels = subLevels;
		this.baseProgress = baseProgress;
		this.baseSpeedModifier = baseSpeedModifier;
		this.baseStrengthModifier = baseStrengthModifier;
		this.category = category;
		this.canFly = false;
		this.freeFlight = false;
		this.teleportation = false;
	}

	/**
	 * Constructor for the level. this one sets flying and tp
	 * @param levelName The current level name for displaying.
	 * @param subLevels Amount of sub levels inside this major level.
	 * @param baseProgress Progress needed to accomplish to get to next level, similar to Experience.
	 * @param baseSpeedModifier The amount of speed the player gets on the level.
	 * @param baseStrengthModifier The amount of strength (for damaging other entities) the player will have.
	 * @param category The category of this level.
	 * @param canFly Players can fly?
	 * @param freeFlight Players can fly for free either?
	 * @param teleportation Players can use teleport at will?
	 */
	CultivationLevel(String levelName, int subLevels, float baseProgress, float baseSpeedModifier, float baseStrengthModifier, CultivationCategory category, boolean canFly, boolean freeFlight, boolean teleportation) {
		this.levelName = levelName;
		this.subLevels = subLevels;
		this.baseProgress = baseProgress;
		this.baseSpeedModifier = baseSpeedModifier;
		this.baseStrengthModifier = baseStrengthModifier;
		this.category = category;
		this.canFly = canFly;
		this.freeFlight = freeFlight;
		this.teleportation = teleportation;
	}

	/**
	 * Gets the complete level name.
	 * If sub level is outside the major level sub levels range, then returns just the major level name.
	 * @param subLevel The current sub level inside major level.
	 * @return The complete level name.
	 */
	public String getLevelName(int subLevel) {
		if(!(subLevel >= 0 && subLevel < this.subLevels)) {	return this.levelName; }
		return String.format("%s %s %d", I18n.format(this.levelName), I18n.format("wuxiacraft.label.rank"), subLevel + 1);
	}

	/**
	 * Gets the amount needed for progress this sub level.
	 * @param subLevel The current sub level.
	 * @return The amount needed to progress this sub level.
	 */
	public float getProgressBySubLevel(int subLevel) {
		if(!(subLevel >= 0 && subLevel < this.subLevels)) {	return 0; }

		return this.baseProgress * (float)Math.pow(1.05F , subLevel);

 	}

	/**
	 * Gets the amount of the speed modified by each sub level.
	 * @param subLevel The current sub level.
	 * @return Teh modified speed.
	 */
 	public float getSpeedModifierBySubLevel(int subLevel) {
		if(!(subLevel >= 0 && subLevel < this.subLevels)) {	return 0; }
		return this.baseSpeedModifier * (1F + 0.025F*subLevel);
	}

	/**
	 * Gets the amount of the strength modified by each sub level.
	 * @param subLevel The current sub level.
	 * @return The modified strength.
	 */
	public float getStrengthModifierBySubLevel(int subLevel) {
		if(!(subLevel >= 0 && subLevel < this.subLevels)) {	return 0; }
 		return this.baseStrengthModifier * (1f + 0.0554F*subLevel);
	}

	public float getMaxEnergyByLevel(int subLevel) {
		if(!(subLevel >= 0 && subLevel < this.subLevels)) {	return 0; }
		return 100F *((this.baseStrengthModifier * (1 + 0.0554F*this.subLevels)));
	}

	public CultivationLevel getNextLevel() {
		switch(this) {
			case BODY_REFINEMENT: return SOUL_REFINEMENT;
			case SOUL_REFINEMENT: return QI_PATHS_REFINEMENT;
			case QI_PATHS_REFINEMENT: return DANTIAN_CONDENSING;
			case DANTIAN_CONDENSING: return EARTH_LAW;
			case EARTH_LAW: return SKY_LAW;
			case SKY_LAW: return TRUE_LAW;
			case TRUE_LAW: return MARTIAL_LAW;
			case MARTIAL_LAW: return IMMORTALITY_LAW;
			case IMMORTALITY_LAW: return IMMORTALITY_REFINEMENT;
			case IMMORTALITY_REFINEMENT: return IMMORTAL_FOUNDATION;
			case IMMORTAL_FOUNDATION: return TRUE_IMMORTAL;
			case TRUE_IMMORTAL: return MARTIAL_IMMORTAL;
			case MARTIAL_IMMORTAL: return DIVINE_LAW;
			case DIVINE_LAW: return DIVINE_PHENOMENON;
			case DIVINE_PHENOMENON:
			case TRUE_GOD: return TRUE_GOD;
			default: return BODY_REFINEMENT;
		}
	}

}
