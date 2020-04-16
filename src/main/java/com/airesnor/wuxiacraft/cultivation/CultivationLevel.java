package com.airesnor.wuxiacraft.cultivation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.utils.TranslateUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CultivationLevel extends IForgeRegistryEntry.Impl<CultivationLevel> {
	public static final ArrayList<CultivationLevel> DEFAULTS = new ArrayList<>();
	static {
		DEFAULTS.add(new CultivationLevel("body_refinement", "soul_refinement", 5, 1000F, 1.00F, 1.00F, false, false, false, false, false));
		DEFAULTS.add(new CultivationLevel("soul_refinement","qi_paths_refinement", 5, 1790.85F, 1.20F, 3.00F, false, false, false, false, false));
		DEFAULTS.add(new CultivationLevel("qi_paths_refinement","dantian_condensing", 5, 3399.56F, 1.46F, 9.6F, false, false, false, false, false));
		DEFAULTS.add(new CultivationLevel("dantian_condensing","earth_law", 5, 6840.59F, 1.82F, 32.64F, false, false, false, false, false));
		DEFAULTS.add(new CultivationLevel("earth_law","sky_law", 10, 16393.87F, 2.36F, 130.56F, false, false, false, false, false));
		DEFAULTS.add(new CultivationLevel("sky_law","true_law", 10, 39288.87F, 3.07F, 522.24F, true, false, false, false, false));
		DEFAULTS.add(new CultivationLevel("true_law","martial_law", 10, 99807.54F, 4.05F, 2193.41F, true, false, true, false, false));
		DEFAULTS.add(new CultivationLevel("martial_law","immortality_law", 10, 268759.03F, 5.43F, 9651.00F, true, false, true, false, false));
		DEFAULTS.add(new CultivationLevel("immortality_law","immortality_refinement", 15, 861946.62F, 7.60F, 48254.98F, true, false, true, false, false));
		DEFAULTS.add(new CultivationLevel("immortality_refinement","immortal_foundation", 15, 2764379.58F, 10.64F, 241274.88F, true, false, true, false, false));
		DEFAULTS.add(new CultivationLevel("immortal_foundation","true_immortal", 15, 9397684.19F, 15.10F, 1254629.38F, true, true, true, true, false));
		DEFAULTS.add(new CultivationLevel("true_immortal","martial_immortal", 15, 33864906.62F, 21.75F, 6774998.68F, true, true, true, true, false));
		DEFAULTS.add(new CultivationLevel("martial_immortal","divine_law", 20, 145343801.14F, 32.62F, 40649991.78F, true, true, true, true, false));
		DEFAULTS.add(new CultivationLevel("divine_law","divine_phenomenon", 20, 623796804.43F, 48.93F, 243899950.69F, true, false, true, true, true));
		DEFAULTS.add(new CultivationLevel("divine_phenomenon","true_god", 20, 2837890554.39F, 74.38F, 1512179694.31F, true, true, true, true, true));
		DEFAULTS.add(new CultivationLevel("true_god","true_god", 1000000, 13685289995.12F, 114.55F, 9677950043.55F, true, true, true, true, true));
	}

	public static final Map<String, CultivationLevel> REGISTERED_LEVELS = new HashMap<>();

	/**
	 * It is used for when a new cultivation is created, this way we can define a first level for players and stuff
	 */
	public static CultivationLevel BASE_LEVEL = DEFAULTS.get(0);

	/**
	 * The current level name for displaying.
	 */
	public final String levelName;

	/**
	 * A reference to the next level, used to test if greater and when leveling up
	 */
	public final String nextLevelName;

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

	public CultivationLevel(String levelName, String nextLevelName, int subLevels, double baseProgress, double baseSpeedModifier, double baseStrengthModifier, boolean energyAsFood, boolean needNoFood, boolean canFly, boolean freeFlight, boolean teleportation) {
		this.levelName = levelName;
		this.nextLevelName = nextLevelName;
		this.subLevels = subLevels;
		this.baseProgress = baseProgress;
		this.baseSpeedModifier = baseSpeedModifier;
		this.baseStrengthModifier = baseStrengthModifier;
		this.energyAsFood = energyAsFood;
		this.needNoFood = needNoFood;
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
		return REGISTERED_LEVELS.get(this.nextLevelName);
	}

	public boolean greaterThan(CultivationLevel level) {
		boolean found = false; //found that level is a greater level
		CultivationLevel aux = this;
		while (aux != aux.getNextLevel()) { //the last level must point towards itself or game breaks
			if (aux == level) {
				found = true;
				break;
			}
			aux = aux.getNextLevel();
		}
		return !found; //if not found the test level ahead of this level then this level is greater than test level
	}

}
