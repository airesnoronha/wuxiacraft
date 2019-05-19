package com.airesnor.wuxiacraft.cultivation;

import net.minecraft.client.resources.I18n;

public enum CultivationLevel {
	BODY_REFINEMENT			("wuxiacraft.cultivation.body_refinement", 		5, 1000F, 1.00F,1.00F, CultivationCategory.ENERGY_PERCEPTION),
	SOUL_REFINEMENT			("wuxiacraft.cultivation.soul_refinement", 		5, 1628.89F, 1.20F,1.55F, CultivationCategory.ENERGY_PERCEPTION),
	QI_PATHS_REFINEMENT		("wuxiacraft.cultivation.qi_paths_refinement", 	5, 2785.96F, 1.46F,2.49F, CultivationCategory.ENERGY_PERCEPTION),
	DANTIAN_CONDENSING		("wuxiacraft.cultivation.dantian_condensing", 	5, 5003.19F, 1.82F,4.13F, CultivationCategory.ENERGY_PERCEPTION),
	EARTH_LAW				("wuxiacraft.cultivation.earth_law", 			10, 10401.27F, 2.36F,7.54F, CultivationCategory.MARTIAL_LAW),
	SKY_LAW					("wuxiacraft.cultivation.sky_law", 				10, 21623.49F, 3.07F,13.75F, CultivationCategory.MARTIAL_LAW),
	TRUE_LAW				("wuxiacraft.cultivation.true_law", 				10, 47201.37F, 4.05F,25.86F, CultivationCategory.MARTIAL_LAW, true, false, false),
	MARTIAL_LAW				("wuxiacraft.cultivation.martial_law", 			10, 108186.41F, 5.43F,50.04F, CultivationCategory.MARTIAL_LAW, true, false, false),
	IMMORTALITY_LAW			("wuxiacraft.cultivation.immortality_law", 		15, 287050.75F, 7.60F,105.07F, CultivationCategory.IMMORTAL_FOUNDATION, true, false, false),
	IMMORTALITY_REFINEMENT	("wuxiacraft.cultivation.immortality_refinement", 15, 761631.11F, 10.64F,220.66F, CultivationCategory.IMMORTAL_FOUNDATION, true, false, false),
	IMMORTAL_FOUNDATION		("wuxiacraft.cultivation.immortal_foundation", 	15, 2121875.77F, 15.10F,475.52F, CultivationCategory.IMMORTAL_FOUNDATION, true, true, false),
	TRUE_IMMORTAL			("wuxiacraft.cultivation.true_immortal", 		15, 6207039.85F, 21.75F,1050.89F, CultivationCategory.IMMORTAL_FOUNDATION, true, true, false),
	MARTIAL_IMMORTAL		("wuxiacraft.cultivation.martial_immortal", 		20, 21019240.06F, 32.62F,2495.87F, CultivationCategory.DIVINE_LAW, true, true, false),
	DIVINE_LAW				("wuxiacraft.cultivation.divine_law", 			20, 71178607.43F, 48.93F,5927.68F, CultivationCategory.DIVINE_LAW, true, true, false),
	DIVINE_PHENOMENON		("wuxiacraft.cultivation.divine_phenomenon", 	20, 253087830.40F, 74.38F,14404.26F, CultivationCategory.DIVINE_LAW, true, true, true),
	TRUE_GOD				("wuxiacraft.cultivation.true_god", 				1000000, 944892360.52F, 114.55F,35794.59F, CultivationCategory.DIVINE_LAW, true, true, true);

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
		return this.baseSpeedModifier * (1F + 0.02F*subLevel);
	}

	/**
	 * Gets the amount of the strength modified by each sub level.
	 * @param subLevel The current sub level.
	 * @return The modified strength.
	 */
	public float getStrengthModifierBySubLevel(int subLevel) {
		if(!(subLevel >= 0 && subLevel < this.subLevels)) {	return 0; }
 		return this.baseStrengthModifier * (1f + 0.055F*subLevel);
	}

	public float getMaxEnergyByLevel(int subLevel) {
		if(!(subLevel >= 0 && subLevel < this.subLevels)) {	return 0; }
		return 100F *((this.baseStrengthModifier * (1 + 0.055F*this.subLevels)));
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
