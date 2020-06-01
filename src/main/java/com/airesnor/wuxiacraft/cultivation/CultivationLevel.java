package com.airesnor.wuxiacraft.cultivation;

import com.airesnor.wuxiacraft.utils.TranslateUtils;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.*;

public class CultivationLevel extends IForgeRegistryEntry.Impl<CultivationLevel> {
	public static final LinkedList<CultivationLevel> DEFAULTS = new LinkedList<>();
	static {
		DEFAULTS.add(new CultivationLevel("body_refinement", "soul_refinement", "Body Refinement", 5, 10, 1000F, 1.00F, 1.00F, false, false, false, false, false, false, false));
		DEFAULTS.add(new CultivationLevel("soul_refinement","qi_paths_refinement", "Soul Refinement", 5, 80, 1790.85F, 1.20F, 3.00F, false, false, false, false, false, false, false));
		DEFAULTS.add(new CultivationLevel("qi_paths_refinement","dantian_condensing", "Qi Paths Refinement", 5, 696, 3399.56F, 1.46F, 9.6F, false, false, false, false, false, false, false));
		DEFAULTS.add(new CultivationLevel("dantian_condensing","earth_law", "DanTian Condensing", 5, 6542, 6840.59F, 1.82F, 32.64F, false, false, false, false, false, true, false));
		DEFAULTS.add(new CultivationLevel("earth_law","sky_law", "Earth Law", 10,75238, 16393.87F, 2.36F, 130.56F, false, false, false, false, false, true, false));
		DEFAULTS.add(new CultivationLevel("sky_law","true_law", "Sky Law", 10,865232, 39288.87F, 3.07F, 522.24F, true, false, false, false, false, true, false));
		DEFAULTS.add(new CultivationLevel("true_law","martial_law", "True Law", 10,10555835, 99807.54F, 4.05F, 2193.41F, true, false, true, false, false, true, true));
		DEFAULTS.add(new CultivationLevel("martial_law","immortality_law", "Martial Law", 10,136170275, 268759.03F, 5.43F, 9651.00F, true, false, true, false, false, true, true));
		DEFAULTS.add(new CultivationLevel("immortality_law","immortality_refinement", "Immortality Law", 15,2042554127, 861946.62F, 7.60F, 48254.98F, true, false, true, false, false, true, true));
		DEFAULTS.add(new CultivationLevel("immortality_refinement","immortal_foundation", "Immortality Refinement", 15,30638311900L, 2764379.58F, 10.64F, 241274.88F, true, false, true, false, false, true, true));
		DEFAULTS.add(new CultivationLevel("immortal_foundation","true_immortal", "Immortal Foundation", 15,481021496833L, 9397684.19F, 15.10F, 1254629.38F, true, true, true, true, false, true, true));
		DEFAULTS.add(new CultivationLevel("true_immortal","martial_immortal", "True Immortal", 15,7888752548063L, 33864906.62F, 21.75F, 6774998.68F, true, true, true, true, false, true, true));
		DEFAULTS.add(new CultivationLevel("martial_immortal","divine_law", "Martial Immortal", 20,145941922139175L, 145343801.14F, 32.62F, 40649991.78F, true, true, true, true, false, true, true));
		DEFAULTS.add(new CultivationLevel("divine_law","divine_phenomenon", "Divine Law", 20,2699925559574730L, 623796804.43F, 48.93F, 243899950.69F, true, false, true, true, true, true, true));
		DEFAULTS.add(new CultivationLevel("divine_phenomenon","true_god", "Divine Phenomenon", 20,51838570743834800L, 2837890554.39F, 74.38F, 1512179694.31F, true, true, true, true, true, true, true));
		DEFAULTS.add(new CultivationLevel("true_god","true_god", "True God", 1000000,1031587557802310000L, 13685289995.12F, 114.55F, 9677950043.55F, true, true, true, true, true, true, true));
	}

	/**
	 * This one comes from the server, even in Single player
	 */
	public static final LinkedHashMap<String, CultivationLevel> LOADED_LEVELS = new LinkedHashMap<>();

	/**
	 * Loaded BaseLevel
	 */
	public static CultivationLevel BASE_LEVEL = DEFAULTS.get(0);

	/**
	 * This one comes from the files
	 */
	public static final LinkedHashMap<String, CultivationLevel> REGISTERED_LEVELS = new LinkedHashMap<>();

	/**
	 * It is used for when a new cultivation is created, this way we can define a first level for players and stuff
	 */
	public static CultivationLevel REGISTERED_BASE_LEVEL = DEFAULTS.get(0);

	/**
	 * The current level name for displaying.
	 */
	public final String levelName;

	/**
	 * A reference to the next level, used to test if greater and when leveling up
	 */
	public final String nextLevelName;

	/**
	 * Since levels aer gonna be a server thing, server should write the display name;
	 */
	public final String displayName;

	/**
	 * Amount of sub levels inside this major level.
	 */
	public final int subLevels;

	/**
	 * Progress needed to accomplish to get to next level, similar to Experience.
	 */
	public final double baseProgress;

	/**
	 * The amount of speed the player gets on the level.
	 */
	public final double baseSpeedModifier;

	/**
	 * The amount of strength (for damaging other entities) the player will have.
	 */
	public final double baseStrengthModifier;

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
	 * Uses his energy as a food passively
	 */
	public final boolean energyAsFood;

	/**
	 * Doesn't need to eat no more
	 */
	public final boolean needNoFood;

	/**
	 * Maximum points a foundation stat can have in certain level
	 */
	public final long foundationMaxStat;

	/**
	 * When reach this realm calls for tribulation
	 */
	public final boolean callsTribulation;

	/**
	 * Calls tribulation for each sub level
	 */
	public final boolean tribulationEachSubLevel;

	public CultivationLevel(String levelName, String nextLevelName, String displayName, int subLevels, long foundationMaxStat, double baseProgress, double baseSpeedModifier, double baseStrengthModifier, boolean energyAsFood, boolean needNoFood, boolean canFly, boolean freeFlight, boolean teleportation, boolean callsTribulation, boolean tribulationEachSubLevel) {
		this.levelName = levelName;
		this.nextLevelName = nextLevelName;
		this.displayName = displayName;
		this.subLevels = subLevels;
		this.foundationMaxStat = foundationMaxStat;
		this.baseProgress = baseProgress;
		this.baseSpeedModifier = baseSpeedModifier;
		this.baseStrengthModifier = baseStrengthModifier;
		this.energyAsFood = energyAsFood;
		this.needNoFood = needNoFood;
		this.canFly = canFly;
		this.freeFlight = freeFlight;
		this.teleportation = teleportation;
		this.callsTribulation = callsTribulation;
		this.tribulationEachSubLevel = tribulationEachSubLevel;
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
			return this.displayName;
		}
		return String.format("%s %s %d", this.displayName, TranslateUtils.translateKey("wuxiacraft.label.rank"), subLevel + 1);
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
	public double getSpeedModifierBySubLevel(int subLevel) {
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
	public double getStrengthModifierBySubLevel(int subLevel) {
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
	public double getMaxEnergyByLevel(int subLevel) {
		if (!(subLevel >= 0 && subLevel < this.subLevels)) {
			return 0;
		}
		return 100F * ((this.getStrengthModifierBySubLevel(subLevel) * (1 + 0.2F * subLevel)));
	}

	/**
	 * Get the next cultivation level for when leveling up
	 *
	 * @return the next cultivation level
	 */
	public CultivationLevel getNextLevel() {
		return LOADED_LEVELS.get(this.nextLevelName);
	}

	public long getFoundationMaxStat(int subLevel) {
		if (!(subLevel >= 0 && subLevel < this.subLevels)) {
			return 0;
		}
		return (long)Math.floor(this.foundationMaxStat * (1 + 0.7*subLevel));
	}

	public boolean isEqual(CultivationLevel level) {
		boolean found = false;
		CultivationLevel aux = this;
			if (aux.getUName().equals(level.getUName())) {
				found = true;
			}
		return found;
	}

	public boolean isGreaterThan(CultivationLevel level) {
		boolean found = false; //found that level is a greater level
		CultivationLevel aux = this;
		while (aux != aux.getNextLevel()) { //the last level must point towards itself or game breaks
			if (aux.getUName().equals(level.getUName())) {
				found = true;
				break;
			}
			aux = aux.getNextLevel();
		}
		return !found; //if not found the test level ahead of this level then this level is greater than test level
	}

}
