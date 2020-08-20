package com.airesnor.wuxiacraft.cultivation;

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
		BODY, ESSENCE, DIVINE
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

	@Override
	public boolean addBodyProgress(double amount, boolean allowBreakthrough) throws RequiresTribulation {
		boolean leveled = false;
		this.bodyProgress += amount;
		if (this.bodyProgress > this.bodyLevel.getProgressBySubLevel(this.bodySubLevel) + 200.0) { //players are abusing of their cultivation base in earlier levels
			this.addBodyFoundation(this.bodyProgress - (this.bodyLevel.getProgressBySubLevel(this.bodySubLevel) + 200.0));
			this.bodyProgress = this.bodyLevel.getProgressBySubLevel(this.bodySubLevel) + 200.0;
		}
		if (allowBreakthrough) {
			if (this.bodyProgress >= this.bodyLevel.getProgressBySubLevel(this.bodySubLevel)) {
				Random rnd = new Random();
				// if this then all above
				boolean divineCondition = (this.divineProgress >= this.divineLevel.getProgressBySubLevel(this.divineLevel.subLevels - 1)
						&& this.divineSubLevel >= this.divineLevel.subLevels - 1) || this.divineLevel != BaseSystemLevel.DEFAULT_DIVINE_LEVEL;
				boolean essenceCondition = (this.essenceProgress >= this.essenceLevel.getProgressBySubLevel(this.essenceLevel.subLevels - 1)
						&& this.essenceSubLevel >= this.essenceLevel.subLevels - 1) || this.essenceLevel != BaseSystemLevel.DEFAULT_ESSENCE_LEVEL; // or not in this level
				if (essenceCondition && divineCondition) { // can breakthrough
					if (this.bodyLevel.tribulationEachSubLevel && this.bodySubLevel < this.bodyLevel.subLevels - 1) {
						double strength = this.bodyLevel.getModifierBySubLevel(this.bodySubLevel + 1);
						throw new RequiresTribulation(strength, System.BODY, this.bodyLevel, this.bodySubLevel + 1);
					}
					double oldModifier = this.getBodyModifier();
					if (this.bodySubLevel + 1 == this.bodyLevel.subLevels) {
						if (this.bodyLevel.nextLevel(BaseSystemLevel.BODY_LEVELS).callsTribulation) {
							double strength = this.bodyLevel.nextLevel(BaseSystemLevel.BODY_LEVELS).getModifierBySubLevel(0);
							throw new RequiresTribulation(strength, System.BODY, this.bodyLevel.nextLevel(BaseSystemLevel.BODY_LEVELS), 0);
						}
						if (rnd.nextFloat() > (this.bodyProgress + this.bodyFoundation) / (4 * this.bodyLevel.getProgressBySubLevel(this.bodySubLevel))
								&& this.bodyLevel != BaseSystemLevel.DEFAULT_BODY_LEVEL) {
							applySystemPenalty(System.BODY);
						} else {
							this.bodyProgress -= this.bodyLevel.getProgressBySubLevel(this.bodySubLevel);
							this.bodySubLevel = 0;
							this.bodyLevel = this.bodyLevel.nextLevel(BaseSystemLevel.BODY_LEVELS);
							leveled = true;
						}
					} else {
						if (rnd.nextFloat() > (this.bodyProgress + this.bodyFoundation) / (4 * this.bodyLevel.getProgressBySubLevel(this.bodySubLevel))
								&& this.bodyLevel != BaseSystemLevel.DEFAULT_BODY_LEVEL) {
							applySystemPenalty(System.BODY);
						} else {
							this.bodyProgress -= this.bodyLevel.getProgressBySubLevel(this.bodySubLevel);
							this.bodySubLevel++;
							leveled = true;
						}
					}
					if (leveled) {
						double modifierDifference = oldModifier * 1.1 - this.getBodyModifier();
						if (modifierDifference > 0) {
							this.bodyFoundation += this.bodyLevel.getProgressBySubLevel(this.bodySubLevel) * modifierDifference / (0.4 * this.bodyLevel.getModifierBySubLevel(this.bodySubLevel));
						}
					}
				}
			}
		}
		return leveled;
	}

	@Override
	public boolean addDivineProgress(double amount, boolean allowBreakthrough) throws RequiresTribulation {
		boolean leveled = false;
		this.divineProgress += amount;
		if (this.divineProgress > this.divineLevel.getProgressBySubLevel(this.divineSubLevel) + 200.0) { //players are abusing of their cultivation base in earlier levels
			this.addDivineFoundation(this.divineProgress - (this.divineLevel.getProgressBySubLevel(this.divineSubLevel) + 200.0));
			this.divineProgress = this.divineLevel.getProgressBySubLevel(this.divineSubLevel) + 200.0;
		}
		if (allowBreakthrough) {
			if (this.divineProgress >= this.divineLevel.getProgressBySubLevel(this.divineSubLevel)) {
				Random rnd = new Random();
				// if this then all above
				boolean bodyCondition = (this.bodyProgress >= this.bodyLevel.getProgressBySubLevel(this.bodyLevel.subLevels - 1)
						&& this.bodySubLevel >= this.bodyLevel.subLevels - 1) || this.bodyLevel != BaseSystemLevel.DEFAULT_BODY_LEVEL; // or not in this level
				boolean essenceCondition = (this.essenceProgress >= this.essenceLevel.getProgressBySubLevel(this.essenceLevel.subLevels - 1)
						&& this.essenceSubLevel >= this.essenceLevel.subLevels - 1) || this.essenceLevel != BaseSystemLevel.DEFAULT_ESSENCE_LEVEL;
				if (bodyCondition && essenceCondition) { // can breakthrough
					if (this.divineLevel.tribulationEachSubLevel && this.divineSubLevel < this.divineLevel.subLevels - 1) {
						double strength = this.divineLevel.getModifierBySubLevel(this.divineSubLevel + 1);
						throw new RequiresTribulation(strength, System.DIVINE, this.divineLevel, this.divineSubLevel + 1);
					}
					double oldModifier = this.getDivineModifier();
					if (this.divineSubLevel + 1 == this.divineLevel.subLevels) {
						if (this.divineLevel.nextLevel(BaseSystemLevel.DIVINE_LEVELS).callsTribulation) {
							double strength = this.divineLevel.nextLevel(BaseSystemLevel.DIVINE_LEVELS).getModifierBySubLevel(0);
							throw new RequiresTribulation(strength, System.DIVINE, this.divineLevel.nextLevel(BaseSystemLevel.DIVINE_LEVELS), 0);
						}
						if (rnd.nextFloat() > (this.divineProgress + this.divineFoundation) / (4 * this.divineLevel.getProgressBySubLevel(this.divineSubLevel))
								&& this.divineLevel != BaseSystemLevel.DEFAULT_DIVINE_LEVEL) {
							applySystemPenalty(System.DIVINE);
						} else {
							this.divineProgress -= this.divineLevel.getProgressBySubLevel(this.divineSubLevel);
							this.divineSubLevel = 0;
							this.divineLevel = this.divineLevel.nextLevel(BaseSystemLevel.DIVINE_LEVELS);
							leveled = true;
						}
					} else {
						if (rnd.nextFloat() > (this.divineProgress + this.divineFoundation) / (4 * this.divineLevel.getProgressBySubLevel(this.divineSubLevel))
								&& this.divineLevel != BaseSystemLevel.DEFAULT_DIVINE_LEVEL) {
							applySystemPenalty(System.DIVINE);
						} else {
							this.divineProgress -= this.divineLevel.getProgressBySubLevel(this.divineSubLevel);
							this.divineSubLevel++;
							leveled = true;
						}
					}
					if (leveled) {
						double modifierDifference = oldModifier * 1.1 - this.getDivineModifier();
						if (modifierDifference > 0) {
							this.divineFoundation += this.divineLevel.getProgressBySubLevel(this.divineSubLevel) * modifierDifference / (0.4 * this.divineLevel.getModifierBySubLevel(this.divineSubLevel));
						}
					}
				}
			}
		}
		return leveled;
	}

	@Override
	public boolean addEssenceProgress(double amount, boolean allowBreakthrough) throws RequiresTribulation {
		boolean leveled = false;
		this.essenceProgress += amount;
		if (this.essenceProgress > this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel) + 200.0) { //players are abusing of their cultivation base in earlier levels
			this.addEssenceFoundation(this.essenceProgress - (this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel) + 200.0));
			this.essenceProgress = this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel) + 200.0;
		}
		if (allowBreakthrough) {
			if (this.essenceProgress >= this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel)) {
				Random rnd = new Random();
				boolean bodyCondition = (this.bodyProgress >= this.bodyLevel.getProgressBySubLevel(this.bodyLevel.subLevels - 1)
						&& this.bodySubLevel >= this.bodyLevel.subLevels - 1) || this.bodyLevel != BaseSystemLevel.DEFAULT_BODY_LEVEL; // or not in this level
				boolean divineCondition = (this.divineProgress >= this.divineLevel.getProgressBySubLevel(this.divineLevel.subLevels - 1)
						&& this.divineSubLevel >= this.divineLevel.subLevels - 1) || this.divineLevel != BaseSystemLevel.DEFAULT_DIVINE_LEVEL;
				if (bodyCondition && divineCondition) { // can breakthrough
					if (this.essenceLevel.tribulationEachSubLevel && this.essenceSubLevel < this.essenceLevel.subLevels - 1) {
						double strength = this.essenceLevel.getModifierBySubLevel(this.essenceSubLevel + 1);
						throw new RequiresTribulation(strength, System.ESSENCE, this.essenceLevel, this.essenceSubLevel + 1);
					}
					double oldModifier = this.getEssenceModifier();
					if (this.essenceSubLevel + 1 == this.essenceLevel.subLevels) {
						if (this.essenceLevel.nextLevel(BaseSystemLevel.ESSENCE_LEVELS).callsTribulation) {
							double strength = this.essenceLevel.nextLevel(BaseSystemLevel.ESSENCE_LEVELS).getModifierBySubLevel(0);
							throw new RequiresTribulation(strength, System.ESSENCE, this.essenceLevel.nextLevel(BaseSystemLevel.ESSENCE_LEVELS), 0);
						}
						if (rnd.nextFloat() > (this.essenceProgress + this.essenceFoundation) / (4 * this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel))
								&& this.essenceLevel != BaseSystemLevel.DEFAULT_ESSENCE_LEVEL) {
							applySystemPenalty(System.ESSENCE);
						} else {
							this.essenceProgress -= this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel);
							this.essenceSubLevel = 0;
							this.essenceLevel = this.essenceLevel.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
							leveled = true;
						}
					} else {
						if (rnd.nextFloat() > (this.essenceProgress + this.essenceFoundation) / (4 * this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel))
								&& this.essenceLevel != BaseSystemLevel.DEFAULT_ESSENCE_LEVEL) {
							applySystemPenalty(System.ESSENCE);
						} else {
							this.essenceProgress -= this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel);
							this.essenceSubLevel++;
							leveled = true;
						}
					}
					if (leveled) {
						double modifierDifference = oldModifier * 1.1 - this.getEssenceModifier();
						if (modifierDifference > 0) {
							this.essenceFoundation += this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel) * modifierDifference / (0.4 * this.essenceLevel.getModifierBySubLevel(this.essenceSubLevel));
						}
					}
				}
			}
		}
		return leveled;
	}

	@Override
	public boolean addSystemProgress(double amount, System system, boolean allowBreakThrough) throws RequiresTribulation {
		boolean leveled = false;
		switch (system) {
			case BODY:
				leveled = addBodyProgress(amount, allowBreakThrough);
				break;
			case DIVINE:
				leveled = addDivineProgress(amount, allowBreakThrough);
				break;
			case ESSENCE:
				leveled = addEssenceProgress(amount, allowBreakThrough);
				break;
		}
		return leveled;
	}

	@Override
	public void addBodyFoundation(double amount) {
		this.bodyFoundation = Math.max(this.bodyFoundation + amount * 2 / 3, 0);
	}

	@Override
	public void addDivineFoundation(double amount) {
		this.divineFoundation = Math.max(this.divineFoundation + amount * 2 / 3, 0);
	}

	@Override
	public void addEssenceFoundation(double amount) {
		this.essenceFoundation = Math.max(this.essenceFoundation + amount * 2 / 3, 0);
	}

	@Override
	public void addSystemFoundation(double amount, System system) {
		switch (system) {
			case BODY:
				addBodyFoundation(amount);
				break;
			case DIVINE:
				addDivineFoundation(amount);
				break;
			case ESSENCE:
				addEssenceFoundation(amount);
				break;
		}
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
		switch (system) {
			case BODY:
				return this.getBodyModifier();
			case DIVINE:
				return this.getDivineModifier();
			case ESSENCE:
				return this.getEssenceModifier();
		}
		return 0;
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
		this.bodyProgress = Math.max(amount, 0);
	}

	@Override
	public void setDivineProgress(double amount) {
		this.divineProgress = Math.max(amount, 0);
	}

	@Override
	public void setEssenceProgress(double amount) {
		this.essenceProgress = Math.max(amount, 0);
	}

	@Override
	public void setSpeedHandicap(int handicap) {
		this.handicap = Math.min(100, Math.max(0, handicap));
	}

	@Override
	public int getSpeedHandicap() {
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
		return Math.max(1, this.bodyLevel.getModifierBySubLevel(this.bodySubLevel) *
				(0.6 + Math.min(21, ((this.bodyProgress + this.bodyFoundation) / this.bodyLevel.getProgressBySubLevel(this.bodySubLevel))) * 0.4));
	}

	@Override
	public double getDivineModifier() {
		return Math.max(1, this.divineLevel.getModifierBySubLevel(this.divineSubLevel) *
				(0.6 + Math.min(21, ((this.divineProgress + this.divineFoundation) / this.divineLevel.getProgressBySubLevel(this.divineSubLevel))) * 0.4));
	}

	@Override
	public double getEssenceModifier() {
		return Math.max(1, this.essenceLevel.getModifierBySubLevel(this.essenceSubLevel) *
				(0.6 + Math.min(21, ((this.essenceProgress + this.essenceFoundation) / this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel))) * 0.4));
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
		return ((this.getBodyModifier() - 1) * 0.7 + (this.getEssenceModifier() - 1) * 0.7 + (this.getDivineModifier() - 1)*0.1)* 0.062;
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
