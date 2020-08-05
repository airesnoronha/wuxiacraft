package com.airesnor.wuxiacraft.cultivation;

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
	private boolean suppress;

	public enum System {
		BODY, ESSENCE, DIVINE
	}

	private System selectedSystem;


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
		this.suppress = false;
		this.selectedSystem = System.ESSENCE;
	}

	@Override
	public boolean addBodyProgress(double amount, boolean allowBreakthrough) {
		boolean leveled = false;
		this.bodyProgress += amount;
		if (allowBreakthrough) {
			if (this.bodyProgress >= this.bodyLevel.getProgressBySubLevel(this.bodySubLevel)) {
				boolean divineCondition = (this.divineProgress >= this.divineLevel.getProgressBySubLevel(this.divineLevel.subLevels - 1)
						&& this.divineSubLevel >= this.divineLevel.subLevels - 1
						&& this.divineLevel == BaseSystemLevel.DEFAULT_DIVINE_LEVEL)
						|| this.divineLevel != BaseSystemLevel.DEFAULT_DIVINE_LEVEL;
				boolean essenceCondition = (this.essenceProgress >= this.essenceLevel.getProgressBySubLevel(this.essenceLevel.subLevels - 1)
						&& this.essenceSubLevel >= this.essenceLevel.subLevels - 1
						&& this.essenceLevel == BaseSystemLevel.DEFAULT_ESSENCE_LEVEL) // if this tehn all above
						|| this.bodyLevel != BaseSystemLevel.DEFAULT_ESSENCE_LEVEL; // or not in this level
				if (essenceCondition && divineCondition) { // can breaktrough
					this.bodyProgress -= this.bodyLevel.getProgressBySubLevel(this.bodySubLevel);
					leveled = true;
					this.bodySubLevel++;
					if (this.bodySubLevel == this.bodyLevel.subLevels) {
						this.bodySubLevel = 0;
						this.bodyLevel = this.bodyLevel.nextLevel(BaseSystemLevel.BODY_LEVELS);
					}
				}
			}
		}
		return leveled;
	}

	@Override
	public boolean addDivineProgress(double amount, boolean allowBreakthrough) {
		boolean leveled = false;
		this.divineProgress += amount;
		if (allowBreakthrough) {
			if (this.divineProgress >= this.divineLevel.getProgressBySubLevel(this.divineSubLevel)) {
				boolean bodyCondition = (this.bodyProgress >= this.bodyLevel.getProgressBySubLevel(this.bodyLevel.subLevels - 1)
						&& this.bodySubLevel >= this.bodyLevel.subLevels - 1
						&& this.bodyLevel == BaseSystemLevel.DEFAULT_BODY_LEVEL) // if this tehn all above
						|| this.bodyLevel != BaseSystemLevel.DEFAULT_BODY_LEVEL; // or not in this level
				boolean essenceCondition = (this.essenceProgress >= this.essenceLevel.getProgressBySubLevel(this.essenceLevel.subLevels - 1)
						&& this.essenceSubLevel >= this.essenceLevel.subLevels - 1
						&& this.essenceLevel == BaseSystemLevel.DEFAULT_ESSENCE_LEVEL) // if this tehn all above
						|| this.bodyLevel != BaseSystemLevel.DEFAULT_ESSENCE_LEVEL; // or not in this level
				if (bodyCondition && essenceCondition) { // can breaktrough
					this.divineProgress -= this.divineLevel.getProgressBySubLevel(this.divineSubLevel);
					leveled = true;
					this.divineSubLevel++;
					if (this.divineSubLevel == this.divineLevel.subLevels) {
						this.divineSubLevel = 0;
						this.divineLevel = this.divineLevel.nextLevel(BaseSystemLevel.DIVINE_LEVELS);
					}
				}
			}
		}
		return leveled;
	}

	@Override
	public boolean addEssenceProgress(double amount, boolean allowBreakthrough) {
		boolean leveled = false;
		this.essenceProgress += amount;
		if (allowBreakthrough) {
			if (this.essenceProgress >= this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel)) {
				boolean bodyCondition = (this.bodyProgress >= this.bodyLevel.getProgressBySubLevel(this.bodyLevel.subLevels - 1)
						&& this.bodySubLevel >= this.bodyLevel.subLevels - 1
						&& this.bodyLevel == BaseSystemLevel.DEFAULT_BODY_LEVEL) // if this tehn all above
						|| this.bodyLevel != BaseSystemLevel.DEFAULT_BODY_LEVEL; // or not in this level
				boolean divineCondition = (this.divineProgress >= this.divineLevel.getProgressBySubLevel(this.divineLevel.subLevels - 1)
						&& this.divineSubLevel >= this.divineLevel.subLevels - 1
						&& this.divineLevel == BaseSystemLevel.DEFAULT_DIVINE_LEVEL)
						|| this.divineLevel != BaseSystemLevel.DEFAULT_DIVINE_LEVEL;
				if (bodyCondition && divineCondition) { // can breaktrough
					this.essenceProgress -= this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel);
					leveled = true;
					this.essenceSubLevel++;
					if (this.essenceSubLevel == this.essenceLevel.subLevels) {
						this.essenceSubLevel = 0;
						this.essenceLevel = this.essenceLevel.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
					}
				}
			}
		}
		return leveled;
	}

	@Override
	public boolean addSystemProgress(double amount, System system, boolean allowBreakThrough) {
		switch (system) {
			case BODY:
				addBodyProgress(amount, allowBreakThrough);
				break;
			case DIVINE:
				addDivineProgress(amount, allowBreakThrough);
				break;
			case ESSENCE:
				addEssenceProgress(amount, allowBreakThrough);
				break;
		}
		return false;
	}

	@Override
	public void addBodyFoundation(double amount) {
		this.bodyFoundation = Math.max(this.bodyFoundation + amount, 0);
	}

	@Override
	public void addDivineFoundation(double amount) {
		this.divineFoundation = Math.max(this.divineFoundation + amount, 0);
	}

	@Override
	public void addEssenceFoundation(double amount) {
		this.essenceFoundation = Math.max(this.essenceFoundation + amount, 0);
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
		return this.bodyLevel.getModifierBySubLevel(this.bodySubLevel) *
				(0.6 + ((this.bodyProgress + this.bodyFoundation) / this.bodyLevel.getProgressBySubLevel(this.bodySubLevel)) * 0.4);
	}

	@Override
	public double getDivineModifier() {
		return this.divineLevel.getModifierBySubLevel(this.divineSubLevel) *
				(0.6 + ((this.divineProgress + this.divineFoundation) / this.divineLevel.getProgressBySubLevel(this.divineSubLevel)) * 0.4);
	}

	@Override
	public double getEssenceModifier() {
		return this.essenceLevel.getModifierBySubLevel(this.essenceSubLevel) *
				(0.6 + ((this.essenceProgress + this.essenceFoundation) / this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel)) * 0.4);
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
		this.setEnergy(cultivation.getEnergy());
		this.setPillCooldown(cultivation.getPillCooldown());
		this.setSuppress(cultivation.getSuppress());
	}
}
