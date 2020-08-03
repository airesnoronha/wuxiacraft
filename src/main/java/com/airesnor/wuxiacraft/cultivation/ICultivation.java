package com.airesnor.wuxiacraft.cultivation;

public interface ICultivation {

	/**
	 * Adds progress and also change sub levels and levels
	 *
	 * @param amount the amount of progress gained
	 */
	boolean addBodyProgress(double amount, boolean allowBreakThrough);

	/**
	 * Adds progress and also change sub levels and levels
	 *
	 * @param amount the amount of progress gained
	 */
	boolean addDivineProgress(double amount, boolean allowBreakThrough);

	/**
	 * Adds progress and also change sub levels and levels
	 *
	 * @param amount the amount of progress gained
	 */
	boolean addEssenceProgress(double amount, boolean allowBreakThrough);

	/**
	 * Sets the amount of progress of the body cultivation
	 *
	 * @param amount the progress to be set
	 */
	void setBodyProgress(double amount);

	/**
	 * Sets the amount of progress of the divine cultivation
	 *
	 * @param amount the progress to be set
	 */
	void setDivineProgress(double amount);

	/**
	 * Sets the amount of progress of the essence cultivation
	 *
	 * @param amount the progress to be set
	 */
	void setEssenceProgress(double amount);

	/**
	 * Gets The current major level
	 *
	 * @return A Cultivation level
	 */
	BaseSystemLevel getBodyLevel();

	/**
	 * Gets The current major level
	 *
	 * @return A Cultivation level
	 */
	BaseSystemLevel getDivineLevel();

	/**
	 * Gets The current major level
	 *
	 * @return A Cultivation level
	 */
	BaseSystemLevel getEssenceLevel();

	/**
	 * Gets the current sub level
	 *
	 * @return A sub level
	 */
	int getBodySubLevel();

	/**
	 * Gets the current sub level
	 *
	 * @return A sub level
	 */
	int getDivineSubLevel();

	/**
	 * Gets the current sub level
	 *
	 * @return A sub level
	 */
	int getEssenceSubLevel();

	/**
	 * Gets the current progress.
	 *
	 * @return The current progress.
	 */
	double getBodyProgress();

	/**
	 * Gets the current progress.
	 *
	 * @return The current progress.
	 */
	double getDivineProgress();

	/**
	 * Gets the current progress.
	 *
	 * @return The current progress.
	 */
	double getEssenceProgress();

	/**
	 * Changes the current level.
	 *
	 * @param level the target level.
	 */
	void setBodyLevel(BaseSystemLevel level);

	/**
	 * Changes the current level.
	 *
	 * @param level the target level.
	 */
	void setDivineLevel(BaseSystemLevel level);

	/**
	 * Changes the current level.
	 *
	 * @param level the target level.
	 */
	void setEssenceLevel(BaseSystemLevel level);

	/**
	 * Changes the current sub level.
	 *
	 * @param subLevel the target sub level.
	 */
	void setBodySubLevel(int subLevel);

	/**
	 * Changes the current sub level.
	 *
	 * @param subLevel the target sub level.
	 */
	void setDivineSubLevel(int subLevel);

	/**
	 * Changes the current sub level.
	 *
	 * @param subLevel the target sub level.
	 */
	void setEssenceSubLevel(int subLevel);

	/**
	 * @return the modifier for the body cultivation
	 */
	double getBodyModifier();

	/**
	 * @return the modifier for the divine cultivation
	 */
	double getDivineModifier();

	/**
	 * @return the modifier for the essence cultivation
	 */
	double getEssenceModifier();


	/**
	 * @param system the system in question
	 * @return specified system level
	 */
	BaseSystemLevel getSystemLevel(Cultivation.System system);

	/**
	 * @param system the system in question
	 * @return specified system sub level
	 */
	int getSystemSubLevel(Cultivation.System system);


	/**
	 * @param system the system in question
	 * @return specified system progress
	 */
	double  getSystemProgress(Cultivation.System system);


	/**
	 * @param system the system in question
	 * @return specified system modifier
	 */
	double getSystemModifier(Cultivation.System system);

	/**
	 * @return The amount of energy available to cast spells
	 */
	double getEnergy();

	/**
	 * Sets the amount of energy available to cast spells
	 * @param amount to be set
	 */
	void setEnergy(double amount);

	/**
	 * Adds energy to cast spells
	 * @param amount Energy to be added
	 */
	void addEnergy(double amount);

	/**
	 * Removes the energy and doesn't allow to get negative
	 * @param amount Energy to be removed
	 */
	void remEnergy(double amount);

	/**
	 * Check if there is enough energy for some action
	 * @param amount energy to be checked
	 * @return if it has the required amount
	 */
	boolean hasEnergy(double amount);

	/**
	 * Sets relative max speed based on player total available speed
	 * @param handicap The percentage of the speed
	 */
	void setSpeedHandicap(int handicap);

	/**
	 * @return The relative amount of speed
	 */
	int getSpeedHandicap();

	/**
	 * @return The timer that goes from 0 to 100 and sends data to client
	 */
	int getUpdateTimer();

	/**
	 * Increases +1 to the timer
	 */
	void advTimer();

	/**
	 * Sets timer to 0
	 */
	void resetTimer();

	/**
	 * @return The cooldown that has to be waited until take another pill
	 */
	int getPillCooldown();

	/**
	 * Decreases 1 to the cooldown
	 */
	void lessenPillCooldown();

	/**
	 * Sets total cooldown that has to be waited
	 * @param cooldown the time in tick that has to be waited
	 */
	void setPillCooldown(int cooldown);

	/**
	 * @return The maximum energy available to the player
	 */
	double getMaxEnergy();

	/**
	 * Sets the max speed a player want to use
	 * @param maxSpeed the value in speed
	 */
	void setMaxSpeed(float maxSpeed);

	/**
	 * @return the max speed the player want to use;
	 */
	float getMaxSpeed();

	/**
	 * @return the max break block speed a player want to use
	 */
	float getHasteLimit();

	/**
	 * Sets the max break block speed a player want to use
	 * @param hasteLimit  the max break block speed
	 */
	void setHasteLimit(float hasteLimit);

	/**
	 * @return How high a player want to jump
	 */
	float getJumpLimit();

	/**
	 * Sets the jump limit a player want to jump
	 * @param jumpLimit The multiplier of vanilla jump
	 */
	void setJumpLimit(float jumpLimit);

	/**
	 * Sets if the player want to suppress it's own cultivation advancement
	 * @param suppress whether will stop receiving progress
	 */
	void setSuppress(boolean suppress);

	/**
	 * @return If player won't receive progress
	 */
	boolean getSuppress();

	/**
	 * Sets which system will evolve from cultivate;
	 * @param selectedSystem The new selected cultivation system
	 */
	void setSelectedSystem(Cultivation.System selectedSystem);

	/**
	 * @return The selected system to cultivate
	 */
	Cultivation.System getSelectedSystem();

	/**
	 * Sets it's own cultivation using another one as parameter
	 * @param cultivation The cultivation to be mirrored
	 */
	void copyFrom(ICultivation cultivation);
}
