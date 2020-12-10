package com.airesnor.wuxiacraft.cultivation;

import com.airesnor.wuxiacraft.utils.MathUtils;

import java.util.Random;

public class Cultivation implements ICultivation {

	//body
	private double bodyProgress;
	private BaseSystemLevel bodyLevel;
	private int bodySubLevel;
	private double bodyFoundation;

	private double divineProgress;
	private BaseSystemLevel divineLevel;
	private int divineSubLevel;
	private double divineFoundation;

	private double essenceProgress;
	private BaseSystemLevel essenceLevel;
	private int essenceSubLevel;
	private double essenceFoundation;

	private double energy;
	public int handicap;
	private int timer;
	private int pillCooldown;
	private float maxSpeed;
	private float hasteLimit;
	private float jumpLimit;
	private float stepAssistLimit;
	private boolean suppress;

	public enum System {
		BODY, ESSENCE, DIVINE;

		public System next() {
			return values()[(this.ordinal() + 1) % values().length];
		}

		public System previous() {
			return values()[this.ordinal() == 0 ? values().length - 1 : (this.ordinal() - 1) % values().length];
		}
	}

	private System selectedSystem;

	public static class RequiresTribulation extends Exception {
		public final double tribulationStrength;
		public final System system;
		public final BaseSystemLevel level;
		public final int subLevel;

		RequiresTribulation(double tribulationStrength, System system, BaseSystemLevel level, int subLevel) {
			super("This breakthrough requires a tribulation ...");
			this.tribulationStrength = tribulationStrength;
			this.system = system;
			this.level = level;
			this.subLevel = subLevel;
		}
	}


	public Cultivation() {
		this.bodyProgress = 0;
		this.divineProgress = 0;
		this.essenceProgress = 0;
		this.bodyLevel = BaseSystemLevel.DEFAULT_BODY_LEVEL;
		this.divineLevel = BaseSystemLevel.DEFAULT_DIVINE_LEVEL;
		this.essenceLevel = BaseSystemLevel.DEFAULT_ESSENCE_LEVEL;
		this.bodySubLevel = 0;
		this.divineSubLevel = 0;
		this.divineSubLevel = 0;
		this.bodyFoundation = 0;
		this.divineFoundation = 0;
		this.essenceFoundation = 0;
		this.energy = 0;
		this.handicap = 100;
		this.timer = 0;
		this.pillCooldown = 0;
		this.maxSpeed = 5.0f;
		this.hasteLimit = 10.0f;
		this.jumpLimit = 10.0f;
		this.stepAssistLimit = 3.0f;
		this.suppress = false;
		this.selectedSystem = System.ESSENCE;
	}

	/**
	 * this will set the amount in the selected system, and if there is more that there should be, it'll be added into foundation
	 *
	 * @param amount the amount to be added
	 * @param system the selected system
	 */
	@Override
	public void setSystemProgress(double amount, System system) {
		switch (system) {
			case BODY:
				this.bodyProgress = MathUtils.clamp(amount, 0, this.bodyLevel.getProgressBySubLevel(this.bodySubLevel) + 200.0);
				if (amount > this.bodyProgress)
					this.addSystemFoundation((amount - this.bodyProgress) / 20, system);
				break;
			case DIVINE:
				this.divineProgress = MathUtils.clamp(amount, 0, this.bodyLevel.getProgressBySubLevel(this.bodySubLevel) + 200.0);
				if (amount > this.divineProgress)
					this.addSystemFoundation((amount - this.divineProgress) / 20, system);
				break;
			case ESSENCE:
				this.essenceProgress = MathUtils.clamp(amount, 0, this.bodyLevel.getProgressBySubLevel(this.bodySubLevel) + 200.0);
				if (amount > this.essenceProgress)
					this.addSystemFoundation((amount - this.essenceProgress) / 20, system);
				break;
		}
	}

	/**
	 * this will add an amount of cultivation base into the selected system
	 *
	 * @param amount the amount to be added
	 * @param system the selected system
	 */
	@Override
	public void addSystemProgress(double amount, System system) {
		this.setSystemProgress(this.getSystemProgress(system) + amount, system);
	}

	/**
	 * This will set a set a level in the selected system
	 *
	 * @param subLevel the sub level or rank to be set
	 * @param system   the selected system
	 */
	@Override
	public void setSystemSubLevel(int subLevel, System system) {
		switch (system) {
			case BODY:
				this.bodySubLevel = Math.max(0, subLevel);
				break;
			case DIVINE:
				this.divineSubLevel = Math.max(0, subLevel);
				break;
			case ESSENCE:
				this.essenceSubLevel = Math.max(0, subLevel);
				break;
		}
	}

	/**
	 * This will set a level in the selected system
	 *
	 * @param level  the level to be set
	 * @param system the system of the level
	 */
	@Override
	public void setSystemLevel(BaseSystemLevel level, System system) {
		switch (system) {
			case BODY:
				if (BaseSystemLevel.BODY_LEVELS.contains(level)) this.bodyLevel = level;
				break;
			case DIVINE:
				if (BaseSystemLevel.DIVINE_LEVELS.contains(level)) this.divineLevel = level;
				break;
			case ESSENCE:
				if (BaseSystemLevel.ESSENCE_LEVELS.contains(level)) this.essenceLevel = level;
				break;
		}
	}

	/**
	 * Will advance a sub level in the selected system and will add a level if needed
	 * This is merely numbers, real action will happen outside here
	 *
	 * @param system the system to raise a level
	 */
	@Override
	public void riseSubLevel(System system) {
		if (this.essenceLevel == BaseSystemLevel.DEFAULT_ESSENCE_LEVEL) { // got to first level in everything
			double beforeFoundationOverBase = this.essenceFoundation / this.getEssenceLevel().getProgressBySubLevel(this.essenceSubLevel);
			this.bodyLevel = this.bodyLevel.nextLevel(BaseSystemLevel.BODY_LEVELS);
			this.divineLevel = this.divineLevel.nextLevel(BaseSystemLevel.DIVINE_LEVELS);
			this.essenceLevel = this.essenceLevel.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
			this.essenceFoundation = this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel) * beforeFoundationOverBase;
			this.bodyFoundation = this.essenceFoundation;
			this.divineFoundation = this.essenceFoundation;
		} else { // just add a level
			double oldModifier = this.getSystemModifier(system);
			this.setSystemProgress(0, system);
			this.setSystemSubLevel(this.getSystemSubLevel(system) + 1, system);
			if (this.getSystemSubLevel(system) >= this.getSystemLevel(system).subLevels) {
				this.setSystemLevel(this.getSystemLevel(system).nextLevel(BaseSystemLevel.getListBySystem(system)), system);
				this.setSystemSubLevel(0, system);
			}
			//level's cultivation modifiers rise at a 1.2 rate, so there will be a little loss on foundation
			double modifierDifference = oldModifier * 1.19 - this.getSystemModifier(system);
			if (modifierDifference > 0) {
				this.setSystemFoundation(this.getSystemFoundation(system) + this.getSystemLevel(system).getProgressBySubLevel(this.getSystemSubLevel(system)) * modifierDifference /
						(0.6 * this.getSystemLevel(system).getModifierBySubLevel(this.getSystemSubLevel(system))), system);
			}
		}
	}

	/**
	 * Sets the foundations and keeps it >= 0 in the selected system
	 *
	 * @param amount the amount to be set
	 * @param system the selected system
	 */
	@Override
	public void setSystemFoundation(double amount, System system) {
		switch (system) {
			case BODY:
				this.bodyFoundation = Math.max(amount, 0);
				break;
			case DIVINE:
				this.divineFoundation = Math.max(amount, 0);
				break;
			case ESSENCE:
				this.essenceFoundation = Math.max(amount, 0);
				break;
		}
	}

	/**
	 * Adds a certain amount of foundation in the selected system
	 *
	 * @param amount the amount to be added
	 * @param system the selected system
	 */
	@Override
	public void addSystemFoundation(double amount, System system) {
		this.setSystemFoundation(this.getSystemFoundation(system) + amount, system);
	}

	@Override
	public BaseSystemLevel getBodyLevel() {
		return this.bodyLevel;
	}

	@Override
	public BaseSystemLevel getDivineLevel() {
		return this.divineLevel;
	}

	@Override
	public BaseSystemLevel getEssenceLevel() {
		return this.essenceLevel;
	}

	@Override
	public int getBodySubLevel() {
		return this.bodySubLevel;
	}

	@Override
	public int getDivineSubLevel() {
		return this.divineSubLevel;
	}

	@Override
	public int getEssenceSubLevel() {
		return this.essenceSubLevel;
	}

	@Override
	public double getBodyProgress() {
		return this.bodyProgress;
	}

	@Override
	public double getDivineProgress() {
		return this.divineProgress;
	}

	@Override
	public double getEssenceProgress() {
		return this.essenceProgress;
	}

	@Override
	public void setBodyLevel(BaseSystemLevel level) {
		this.bodyLevel = level;
	}

	@Override
	public void setDivineLevel(BaseSystemLevel level) {
		this.divineLevel = level;
	}

	@Override
	public void setEssenceLevel(BaseSystemLevel level) {
		this.essenceLevel = level;
	}

	@Override
	public void setBodySubLevel(int subLevel) {
		this.bodySubLevel = subLevel;
	}

	@Override
	public void setDivineSubLevel(int subLevel) {
		this.divineSubLevel = subLevel;
	}

	@Override
	public void setEssenceSubLevel(int subLevel) {
		this.essenceSubLevel = subLevel;
	}

	@Override
	public void setBodyFoundation(double bodyFoundation) {
		this.bodyFoundation = bodyFoundation;
	}

	@Override
	public void setDivineFoundation(double divineFoundation) {
		this.divineFoundation = divineFoundation;
	}

	@Override
	public void setEssenceFoundation(double essenceFoundation) {
		this.essenceFoundation = essenceFoundation;
	}

	@Override
	public double getBodyFoundation() {
		return bodyFoundation;
	}

	@Override
	public double getDivineFoundation() {
		return divineFoundation;
	}

	@Override
	public double getEssenceFoundation() {
		return essenceFoundation;
	}

	@Override
	public BaseSystemLevel getSystemLevel(System system) {
		switch (system) {
			case BODY:
				return this.getBodyLevel();
			case DIVINE:
				return this.getDivineLevel();
			case ESSENCE:
				return this.getEssenceLevel();
		}
		return null;
	}

	@Override
	public int getSystemSubLevel(System system) {
		switch (system) {
			case BODY:
				return this.getBodySubLevel();
			case DIVINE:
				return this.getDivineSubLevel();
			case ESSENCE:
				return this.getEssenceSubLevel();
		}
		return 0;
	}

	@Override
	public double getSystemProgress(System system) {
		switch (system) {
			case BODY:
				return this.getBodyProgress();
			case DIVINE:
				return this.getDivineProgress();
			case ESSENCE:
				return this.getEssenceProgress();
		}
		return 0;
	}

	@Override
	public double getSystemModifier(System system) {
		return Math.max(0, this.getSystemLevel(system).getModifierBySubLevel(this.getSystemSubLevel(system)) *
				(0.4 + Math.min(21, (this.getSystemFoundation(system) / this.getSystemLevel(system).getProgressBySubLevel(this.getSystemSubLevel(system)))) * 0.6));
	}

	@Override
	public double getSystemFoundation(System system) {
		switch (system) {
			case BODY:
				return this.getBodyFoundation();
			case DIVINE:
				return this.getDivineFoundation();
			case ESSENCE:
				return this.getEssenceFoundation();
		}
		return 0;
	}

	@Override
	public double getEnergy() {
		return this.energy;
	}

	@Override
	public void setEnergy(double amount) {
		this.energy = Math.max(0, amount);
	}

	@Override
	public void addEnergy(double amount) {
		this.energy += amount;
	}

	@Override
	public void remEnergy(double amount) {
		this.energy = Math.max(this.energy - amount, 0);
	}

	@Override
	public boolean hasEnergy(double amount) {
		return this.energy >= amount;
	}

	@Override
	public void setBodyProgress(double amount) {
		this.setSystemProgress(amount, System.BODY);
	}

	@Override
	public void setDivineProgress(double amount) {
		this.setSystemProgress(amount, System.DIVINE);
	}

	@Override
	public void setEssenceProgress(double amount) {
		this.setSystemProgress(amount, System.ESSENCE);
	}

	@Override
	public void setHandicap(int handicap) {
		this.handicap = Math.min(100, Math.max(0, handicap));
	}

	@Override
	public int getHandicap() {
		return this.handicap;
	}

	@Override
	public int getUpdateTimer() {
		return this.timer;
	}

	@Override
	public void advTimer() {
		this.timer++;
	}

	@Override
	public void resetTimer() {
		this.timer = 0;
	}

	@Override
	public int getPillCooldown() {
		return this.pillCooldown;
	}

	@Override
	public void lessenPillCooldown() {
		this.pillCooldown = Math.max(this.pillCooldown - 1, 0);
	}

	@Override
	public void setPillCooldown(int cooldown) {
		this.pillCooldown = Math.max(0, cooldown);
	}

	@Override
	public double getMaxEnergy() {
		return 18 * this.getEssenceModifier() + 8 * this.getBodyModifier() + 12 * this.getDivineModifier();
	}

	@Override
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Override
	public float getMaxSpeed() {
		return this.maxSpeed;
	}

	@Override
	public float getHasteLimit() {
		return this.hasteLimit;
	}

	@Override
	public void setHasteLimit(float hasteLimit) {
		this.hasteLimit = hasteLimit;
	}

	@Override
	public float getJumpLimit() {
		return this.jumpLimit;
	}

	@Override
	public void setJumpLimit(float jumpLimit) {
		this.jumpLimit = jumpLimit;
	}

	@Override
	public void setSuppress(boolean suppress) {
		this.suppress = suppress;
	}

	@Override
	public boolean getSuppress() {
		return this.suppress;
	}

	@Override
	public double getBodyModifier() {
		return getSystemModifier(System.BODY);
	}

	@Override
	public double getDivineModifier() {
		return getSystemModifier(System.DIVINE);
	}

	@Override
	public double getEssenceModifier() {
		return getSystemModifier(System.ESSENCE);
	}

	@Override
	public System getSelectedSystem() {
		return this.selectedSystem;
	}

	@Override
	public void setSelectedSystem(System selectedSystem) {
		this.selectedSystem = selectedSystem;
	}

	@Override
	public float getStepAssistLimit() {
		return stepAssistLimit;
	}

	@Override
	public void setStepAssistLimit(float stepAssistLimit) {
		this.stepAssistLimit = stepAssistLimit;
	}

	@Override
	public double getAgilityModifier() {
		return ((this.getBodyModifier() - 1) * 0.2 + (this.getEssenceModifier() - 1) * 0.4 + (this.getDivineModifier() - 1) * 0.1) * 0.03;
	}

	@Override
	public double getConstitutionModifier() {
		return ((this.getBodyModifier() - 1) + (this.getEssenceModifier() - 1) * 0.4 + (this.getDivineModifier() - 1) * 0.1) * 0.8;
	}

	@Override
	public double getDexterityModifier() {
		return ((this.getBodyModifier() - 1) * 0.4 + (this.getEssenceModifier() - 1) * 0.8 + (this.getDivineModifier() - 1) * 0.1) * 0.01;
	}

	@Override
	public double getResistanceModifier() {
		return ((this.getBodyModifier() - 1) * 0.7 + (this.getEssenceModifier() - 1) * 0.7 + (this.getDivineModifier() - 1) * 0.1) * 0.062;
	}

	@Override
	public double getStrengthModifier() {
		return ((this.getBodyModifier() - 1) * 0.8 + (this.getEssenceModifier() - 1) * 0.6 + (this.getDivineModifier() - 1) * 0.14) * 0.2;
	}

	@Override
	public void copyFrom(ICultivation cultivation) {
		this.bodyLevel = cultivation.getBodyLevel();
		this.divineLevel = cultivation.getDivineLevel();
		this.essenceLevel = cultivation.getEssenceLevel();
		this.bodySubLevel = cultivation.getBodySubLevel();
		this.divineSubLevel = cultivation.getDivineSubLevel();
		this.essenceSubLevel = cultivation.getEssenceSubLevel();
		this.bodyProgress = cultivation.getBodyProgress();
		this.divineProgress = cultivation.getDivineProgress();
		this.essenceProgress = cultivation.getEssenceProgress();
		this.bodyFoundation = cultivation.getBodyFoundation();
		this.divineFoundation = cultivation.getDivineFoundation();
		this.essenceFoundation = cultivation.getEssenceFoundation();
		this.setEnergy(cultivation.getEnergy());
		this.setPillCooldown(cultivation.getPillCooldown());
		this.setSuppress(cultivation.getSuppress());
	}

	public void applySystemPenalty(System system) {
		switch (system) {
			case BODY:
				this.setBodyFoundation(Math.max(this.bodyFoundation * 0.9, this.bodyFoundation - this.bodyLevel.getProgressBySubLevel(this.bodySubLevel)));
				this.setBodyProgress(this.bodyProgress * 0.3);
				break;
			case DIVINE:
				this.setDivineFoundation(Math.max(this.divineFoundation * 0.9, this.divineFoundation - this.divineLevel.getProgressBySubLevel(this.divineSubLevel)));
				this.setDivineProgress(this.divineProgress * 0.3);
				break;
			case ESSENCE:
				this.setEssenceFoundation(Math.max(this.essenceFoundation * 0.9, this.essenceFoundation - this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel)));
				this.setEssenceProgress(this.essenceProgress * 0.3);
				break;
		}

	}
}
