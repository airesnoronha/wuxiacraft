package com.airesnor.wuxiacraft.cultivation;

import com.airesnor.wuxiacraft.utils.MathUtils;

public class Cultivation implements ICultivation {
	private double bodyProgress;
	private BaseSystemLevel bodyLevel;
	private int bodySubLevel;
	private double divineProgress;
	private BaseSystemLevel divineLevel;
	private int divineSubLevel;
	private double essenceProgress;
	private BaseSystemLevel essenceLevel;
	private int essenceSubLevel;
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
		if(allowBreakthrough) {
			if(this.bodyProgress >= this.bodyLevel.getProgressBySubLevel(this.bodySubLevel)) {
				this.bodyProgress -= this.bodyLevel.getProgressBySubLevel(this.bodySubLevel);
				leveled = true;
				this.bodySubLevel ++;
				if(this.bodySubLevel == this.bodyLevel.subLevels) {
					this.bodySubLevel = 0;
					this.bodyLevel = this.bodyLevel.nextLevel(BaseSystemLevel.BODY_LEVELS);
				}
			}
		}
		return leveled;
	}

	@Override
	public boolean addDivineProgress(double amount, boolean allowBreakthrough) {
		boolean leveled = false;
		this.divineProgress += amount;
		if(allowBreakthrough) {
			if(this.divineProgress >= this.divineLevel.getProgressBySubLevel(this.divineSubLevel)) {
				this.divineProgress -= this.divineLevel.getProgressBySubLevel(this.divineSubLevel);
				leveled = true;
				this.divineSubLevel ++;
				if(this.divineSubLevel == this.divineLevel.subLevels) {
					this.divineSubLevel = 0;
					this.divineLevel = this.divineLevel.nextLevel(BaseSystemLevel.DIVINE_LEVELS);
				}
			}
		}
		return leveled;
	}

	@Override
	public boolean addEssenceProgress(double amount, boolean allowBreakthrough) {
		boolean leveled = false;
		this.essenceProgress += amount;
		if(allowBreakthrough) {
			if(this.essenceProgress >= this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel)) {
				this.essenceProgress -= this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel);
				leveled = true;
				this.essenceSubLevel ++;
				if(this.essenceSubLevel == this.essenceLevel.subLevels) {
					this.essenceSubLevel = 0;
					this.essenceLevel = this.essenceLevel.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
				}
			}
		}
		return leveled;
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
	public BaseSystemLevel getSystemLevel(System system) {
		switch(system) {
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
		switch(system) {
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
		switch(system) {
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
		switch(system) {
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
		this.bodyProgress = MathUtils.clamp(amount, 0, this.bodyLevel.getProgressBySubLevel(this.bodySubLevel));
	}

	@Override
	public void setDivineProgress(double amount) {
		this.divineProgress = MathUtils.clamp(amount, 0, this.divineLevel.getProgressBySubLevel(this.divineSubLevel));
	}

	@Override
	public void setEssenceProgress(double amount) {
		this.essenceProgress = MathUtils.clamp(amount, 0, this.essenceLevel.getProgressBySubLevel(this.essenceSubLevel));
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
		return 18*this.getEssenceModifier() + 8*this.getBodyModifier() + 12*this.getDivineModifier();
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
		return this.bodyLevel.getModifierBySubLevel(this.bodySubLevel);
	}

	@Override
	public double getDivineModifier() {
		return this.divineLevel.getModifierBySubLevel(this.divineSubLevel);
	}

	@Override
	public double getEssenceModifier() {
		return this.essenceLevel.getModifierBySubLevel(this.essenceSubLevel);
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
