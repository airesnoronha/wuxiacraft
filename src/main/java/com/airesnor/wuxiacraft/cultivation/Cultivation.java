package com.airesnor.wuxiacraft.cultivation;

import com.airesnor.wuxiacraft.WuxiaCraft;

public class Cultivation implements ICultivation {
	private float progress;
	private CultivationLevel level;
	private int subLevel;
	private float energy;
	public int handicap;
	private int timer;
	private int pelletCooldown;

	public Cultivation() {
		this.subLevel = 0;
		this.progress = 0;
		this.level = CultivationLevel.BODY_REFINEMENT;
		this.energy = 0;
		this.handicap = 100;
		this.timer = 0;
		this.pelletCooldown = 0;
	}

	@Override
	public boolean addProgress(float amount) {
		boolean leveled = false;
		this.progress += amount;
		while(this.progress >= this.level.getProgressBySubLevel(this.subLevel)) {
			leveled = true;
			this.progress -= this.level.getProgressBySubLevel(this.subLevel);
			this.subLevel++;
			if(this.subLevel >= this.level.subLevels) {
				this.subLevel = 0;
				this.level = this.level.getNextLevel();
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
	public float getCurrentProgress() {
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
	public float getEnergy() {
		return this.energy;
	}

	@Override
	public void setEnergy(float amount) {
		this.energy = Math.min(Math.max(0, amount), this.level.getMaxEnergyByLevel(this.subLevel));
	}

	@Override
	public void addEnergy(float amount) {
		this.energy = Math.min(this.energy + amount, this.level.getMaxEnergyByLevel(this.subLevel));
	}

	@Override
	public void remEnergy(float amount) {
		this.energy = Math.max(this.energy - amount, 0);
	}

	@Override
	public void setProgress(float amount) {
		this.progress = Math.min(Math.max(0,amount), this.level.getProgressBySubLevel(this.subLevel));
	}

	@Override
	public void setSpeedHandicap(int handicap) {
		this.handicap = Math.min(100,Math.max(0,handicap));
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
	public int getPelletCooldown() {
		return this.pelletCooldown;
	}

	@Override
	public void lessenPelletCooldown() {
		this.pelletCooldown = Math.max(this.pelletCooldown -1, 0);
	}

	@Override
	public void setPelletCooldown(int cooldown) {
		this.pelletCooldown = Math.max(0, cooldown);
	}
}
