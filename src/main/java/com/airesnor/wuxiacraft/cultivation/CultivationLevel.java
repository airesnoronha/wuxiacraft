package com.airesnor.wuxiacraft.cultivation;

import com.airesnor.wuxiacraft.utils.TranslateUtils;

public enum CultivationLevel {
	BODY_REFINEMENT("body_refinement", 5, 1000F, 1.00F, 1.00F, CultivationCategory.ENERGY_PERCEPTION),
	SOUL_REFINEMENT("soul_refinement", 5, 1790.85F, 1.20F, 3.00F, CultivationCategory.ENERGY_PERCEPTION),
	QI_PATHS_REFINEMENT("qi_paths_refinement", 5, 3399.56F, 1.46F, 9.6F, CultivationCategory.ENERGY_PERCEPTION),
	DANTIAN_CONDENSING("dantian_condensing", 5, 6840.59F, 1.82F, 32.64F, CultivationCategory.ENERGY_PERCEPTION),
	EARTH_LAW("earth_law", 10, 16393.87F, 2.36F, 130.56F, CultivationCategory.MARTIAL_LAW),
	SKY_LAW("sky_law", 10, 39288.87F, 3.07F, 522.24F, CultivationCategory.MARTIAL_LAW),
	TRUE_LAW("true_law", 10, 99807.54F, 4.05F, 2193.41F, CultivationCategory.MARTIAL_LAW, true, false, false),
	MARTIAL_LAW("martial_law", 10, 268759.03F, 5.43F, 9651.00F, CultivationCategory.MARTIAL_LAW, true, false, false),
	IMMORTALITY_LAW("immortality_law", 15, 861946.62F, 7.60F, 48254.98F, CultivationCategory.IMMORTAL_FOUNDATION, true, false, false),
	IMMORTALITY_REFINEMENT("immortality_refinement", 15, 2764379.58F, 10.64F, 241274.88F, CultivationCategory.IMMORTAL_FOUNDATION, true, false, false),
	IMMORTAL_FOUNDATION("immortal_foundation", 15, 9397684.19F, 15.10F, 1254629.38F, CultivationCategory.IMMORTAL_FOUNDATION, true, true, false),
	TRUE_IMMORTAL("true_immortal", 15, 33864906.62F, 21.75F, 6774998.68F, CultivationCategory.IMMORTAL_FOUNDATION, true, true, false),
	MARTIAL_IMMORTAL("martial_immortal", 20, 145343801.14F, 32.62F, 40649991.78F, CultivationCategory.DIVINE_LAW, true, true, false),
	DIVINE_LAW("divine_law", 20, 623796804.43F, 48.93F, 243899950.69F, CultivationCategory.DIVINE_LAW, true, true, false),
	DIVINE_PHENOMENON("divine_phenomenon", 20, 2837890554.39F, 74.38F, 1512179694.31F, CultivationCategory.DIVINE_LAW, true, true, true),
	TRUE_GOD("true_god", 1000000, 13685289995.12F, 114.55F, 9677950043.55F, CultivationCategory.DIVINE_LAW, true, true, true);

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
	private final double baseProgress;

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
	 * this allows players to cross space
	 */
	public final boolean teleportation;

	/**
	 * Constructor for the level.
	 *
	 * @param levelName            The current level name for displaying.
	 * @param subLevels            Amount of sub levels inside this major level.
	 * @param baseProgress         Progress needed to accomplish to get to next level, similar to Experience.
	 * @param baseSpeedModifier    The amount of speed the player gets on the level.
	 * @param baseStrengthModifier The amount of strength (for damaging other entities) the player will have.
	 * @param category             The category of this level.
	 */
	CultivationLevel(String levelName, int subLevels, float baseProgress, float baseSpeedModifier, float baseStrengthModifier, CultivationCategory category) {
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
	 *
	 * @param levelName            The current level name for displaying.
	 * @param subLevels            Amount of sub levels inside this major level.
	 * @param baseProgress         Progress needed to accomplish to get to next level, similar to Experience.
	 * @param baseSpeedModifier    The amount of speed the player gets on the level.
	 * @param baseStrengthModifier The amount of strength (for damaging other entities) the player will have.
	 * @param category             The category of this level.
	 * @param canFly               Players can fly?
	 * @param freeFlight           Players can fly for free either?
	 * @param teleportation        Players can use teleport at will?
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
	 *
	 * @param subLevel The current sub level inside major level.
	 * @return The complete level name.
	 */
	public String getLevelName(int subLevel) {
		if (!(subLevel >= 0 && subLevel < this.subLevels)) {
			return TranslateUtils.translateKey("wuxiacraft.cultivation." + this.levelName);
		}
		return String.format("%s %s %d", TranslateUtils.translateKey("wuxiacraft.cultivation." + this.levelName), TranslateUtils.translateKey("wuxiacraft.label.rank"), subLevel + 1);
	}

	public String getUName() {
		return this.levelName;
	}

	/**
	 * Gets the amount needed for progress this sub level.
	 *
	 * @param subLevel The current sub level.
	 * @return The amount needed to progress this sub level.
	 */
	public double getProgressBySubLevel(int subLevel) {
		if (!(subLevel >= 0 && subLevel < this.subLevels)) {
			return 0;
		}

		return this.baseProgress * Math.pow(1.06F, subLevel);

	}

	/**
	 * Gets the amount of the speed modified by each sub level.
	 *
	 * @param subLevel The current sub level.
	 * @return Teh modified speed.
	 */
	public float getSpeedModifierBySubLevel(int subLevel) {
		if (!(subLevel >= 0 && subLevel < this.subLevels)) {
			return 0;
		}
		return this.baseSpeedModifier * (1F + 0.02F * subLevel);
	}

	/**
	 * Gets the amount of the strength modified by each sub level.
	 *
	 * @param subLevel The current sub level.
	 * @return The modified strength.
	 */
	public float getStrengthModifierBySubLevel(int subLevel) {
		if (!(subLevel >= 0 && subLevel < this.subLevels)) {
			return 0;
		}
		return this.baseStrengthModifier * (1f + 0.2F * subLevel);
	}

	/**
	 * Gets the amount of energy this level is going to have
	 *
	 * @param subLevel the current sub level
	 * @return The final maximum energy
	 */
	public float getMaxEnergyByLevel(int subLevel) {
		if (!(subLevel >= 0 && subLevel < this.subLevels)) {
			return 0;
		}
		return 100F * ((this.baseStrengthModifier * (1 + 0.2F * this.subLevels)));
	}

	/**
	 * Get the next cultivation level for when leveling up
	 *
	 * @return the next cultivation level
	 */
	public CultivationLevel getNextLevel() {
		switch (this) {
			case BODY_REFINEMENT:
				return SOUL_REFINEMENT;
			case SOUL_REFINEMENT:
				return QI_PATHS_REFINEMENT;
			case QI_PATHS_REFINEMENT:
				return DANTIAN_CONDENSING;
			case DANTIAN_CONDENSING:
				return EARTH_LAW;
			case EARTH_LAW:
				return SKY_LAW;
			case SKY_LAW:
				return TRUE_LAW;
			case TRUE_LAW:
				return MARTIAL_LAW;
			case MARTIAL_LAW:
				return IMMORTALITY_LAW;
			case IMMORTALITY_LAW:
				return IMMORTALITY_REFINEMENT;
			case IMMORTALITY_REFINEMENT:
				return IMMORTAL_FOUNDATION;
			case IMMORTAL_FOUNDATION:
				return TRUE_IMMORTAL;
			case TRUE_IMMORTAL:
				return MARTIAL_IMMORTAL;
			case MARTIAL_IMMORTAL:
				return DIVINE_LAW;
			case DIVINE_LAW:
				return DIVINE_PHENOMENON;
			case DIVINE_PHENOMENON:
			case TRUE_GOD:
				return TRUE_GOD;
			default:
				return BODY_REFINEMENT;
		}
	}

	public boolean greaterThan(CultivationLevel level) {
		boolean found = false;
		CultivationLevel aux = this;
		if(aux == TRUE_GOD) found = true;
		while (aux != TRUE_GOD) {
			if (aux == level) {
				found = true;
				break;
			}
			aux = aux.getNextLevel();
		}
		return !found;
	}

}
