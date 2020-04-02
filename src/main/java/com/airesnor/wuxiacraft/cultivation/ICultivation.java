package com.airesnor.wuxiacraft.cultivation;

public interface ICultivation {

	/**
	 * Adds progress and also change sub levels and levels
	 *
	 * @param amount
	 */
	boolean addProgress(double amount);

	void setProgress(double amount);

	/**
	 * Gets The current major level
	 *
	 * @return A Cultivation level
	 */
	CultivationLevel getCurrentLevel();

	/**
	 * Gets the current sub level
	 *
	 * @return A sub level
	 */
	int getCurrentSubLevel();

	/**
	 * Gets the current progress.
	 *
	 * @return The current progress.
	 */
	double getCurrentProgress();

	/**
	 * Changes the current level.
	 *
	 * @param level the target level.
	 */
	void setCurrentLevel(CultivationLevel level);

	/**
	 * Changes the current sub level.
	 *
	 * @param subLevel the target sub level.
	 */
	void setCurrentSubLevel(int subLevel);

	float getEnergy();

	void setEnergy(float amount);

	void addEnergy(float amount);

	void remEnergy(float amount);

	boolean hasEnergy(float amount);

	void setSpeedHandicap(int handicap);

	int getSpeedHandicap();

	int getUpdateTimer();

	void advTimer();

	void resetTimer();

	int getPillCooldown();

	void lessenPillCooldown();

	void setPillCooldown(int cooldown);

	float getStrengthIncrease();

	float getSpeedIncrease();

	float getMaxEnergy();

	void setMaxSpeed(float maxSpeed);

	float getMaxSpeed();

	float getHasteLimit();

	void setHasteLimit(float hasteLimit);

	float getJumpLimit();

	void setJumpLimit(float jumpLimit);

	void setSuppress(boolean suppress);

	boolean getSuppress();
}
