package com.airesnor.wuxiacraft.cultivation;

public class Cultivation implements ICultivation {
	private double progress;
	private CultivationLevel level;
	private int subLevel;
	private double energy;
	public int handicap;
	private int timer;
	private int pillCooldown;
	private float maxSpeed;
	private float hasteLimit;
	private float jumpLimit;
	private boolean suppress;

	public Cultivation() {
		this.subLevel = 0;
		this.progress = 0;
		this.level = CultivationLevel.BASE_LEVEL;
		this.energy = 0;
		this.handicap = 100;
		this.timer = 0;
		this.pillCooldown = 0;
		this.maxSpeed = 5.0f;
		this.hasteLimit = 10.0f;
		this.jumpLimit = 10.0f;
		this.suppress = false;
	}

	@Override
	public boolean addProgress(double amount, boolean allowBreakThrough) {
		boolean leveled = false;
		this.progress += amount;
		if(allowBreakThrough) {
			if (this.progress >= this.level.getProgressBySubLevel(this.subLevel)) { //no more repeated breakthrough, just one at a time
				leveled = true;
				this.progress -= this.level.getProgressBySubLevel(this.subLevel);
				this.subLevel++;
				if (this.subLevel >= this.level.subLevels) {
					this.subLevel = 0;
					this.level = this.level.getNextLevel();
				}
			}
		}
		return leveled;
	}

	@Override
	public CultivationLevel getCurrentLevel() {
		return this.level;
	}

	@Override
	public int getCurrentSubLevel() {
		return this.subLevel;
	}

	@Override
	public double getCurrentProgress() {
		return this.progress;
	}

	@Override
	public void setCurrentLevel(CultivationLevel level) {
		this.level = level;
	}

	@Override
	public void setCurrentSubLevel(int subLevel) {
		this.subLevel = subLevel;
	}

	@Override
	public double getEnergy() {
		return this.energy;
	}

	@Override
	public void setEnergy(double amount) {
		this.energy = Math.min(Math.max(0, amount), this.level.getMaxEnergyByLevel(this.subLevel));
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
	public void setProgress(double amount) {
		this.progress = Math.min(Math.max(0, amount), this.level.getProgressBySubLevel(this.subLevel));
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
		int speed = (int)Math.round(this.getSpeedIncrease());
		this.pillCooldown = Math.max(this.pillCooldown - speed, 0);
	}

	@Override
	public void setPillCooldown(int cooldown) {
		this.pillCooldown = Math.max(0, cooldown);
	}

	@Override
	public double getStrengthIncrease() {
		return this.level.getStrengthModifierBySubLevel(this.subLevel);
	}

	@Override
	public double getSpeedIncrease() {
		return this.level.getSpeedModifierBySubLevel(this.subLevel);
	}

	@Override
	public double getMaxEnergy(IFoundation foundation) {
		return 100 + 10*foundation.getSpiritModifier();
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
	public void copyFrom(ICultivation cultivation) {
		this.setCurrentLevel(cultivation.getCurrentLevel());
		this.setCurrentSubLevel(cultivation.getCurrentSubLevel());
		this.setProgress(cultivation.getCurrentProgress());
		this.setEnergy(cultivation.getEnergy());
		this.setPillCooldown(cultivation.getPillCooldown());
		this.setSuppress(cultivation.getSuppress());
	}
}
