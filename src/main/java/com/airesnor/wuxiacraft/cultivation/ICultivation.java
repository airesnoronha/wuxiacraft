package com.airesnor.wuxiacraft.cultivation;

public interface ICultivation {

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
	 * this will set the amount in the selected system, and if there is more that there should be, it'll be added into foundation
	 *
	 * @param amount the amount to be added
	 * @param system the selected system
	 */
	void setSystemProgress(double amount, Cultivation.System system);

	/**
	 * this will add an amount of cultivation base into the selected system
	 *
	 * @param amount the amount to be added
	 * @param system the selected system
	 */
	void addSystemProgress(double amount, Cultivation.System system);

	/**
	 * This will set a set a level in the selected system
	 *
	 * @param subLevel the sub level or rank to be set
	 * @param system   the selected system
	 */
	void setSystemSubLevel(int subLevel, Cultivation.System system);

	/**
	 * This will set a level in the selected system
	 *
	 * @param level  the level to be set
	 * @param system the system of the level
	 */
	void setSystemLevel(BaseSystemLevel level, Cultivation.System system);

	/**
	 * Will advance a sub level in the selected system and will add a level if needed
	 * This is merely numbers, real action will happen outside here
	 *
	 * @param system the system to raise a level
	 */
	void riseSubLevel(Cultivation.System system);

	/**
	 * Sets the foundations and keeps it >= 0 in the selected system
	 * @param amount the amount to be set
	 * @param system the selected system
	 */
	void setSystemFoundation(double amount, Cultivation.System system);

	/**
	 * Adds a certain amount of foundation in the selected system
	 * @param amount the amount to be added
	 * @param system the selected system
	 */
	void addSystemFoundation(double amount, Cultivation.System system);

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
	 * @param bodyFoundation sets the amount of foundation of the body
	 */
	void setBodyFoundation(double bodyFoundation);

	/**
	 * @param divineFoundation sets the amount of foundation of the divinity
	 */
	void setDivineFoundation(double divineFoundation);

	/**
	 * @param essenceFoundation sets the amount of foundation of the essence
	 */
	void setEssenceFoundation(double essenceFoundation);

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
	 * @return body system accumulated foundation
	 */
	double getBodyFoundation();

	/**
	 * @return divine system accumulated foundation
	 */
	double getDivineFoundation();

	/**
	 * @return essence system accumulated foundation
	 */
	double getEssenceFoundation();

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
	double getSystemProgress(Cultivation.System system);


	/**
	 * @param system the system in question
	 * @return specified system modifier
	 */
	double getSystemModifier(Cultivation.System system);

	/**
	 * @param system the required system
	 * @return specified system accumulated foundation
	 */
	double getSystemFoundation(Cultivation.System system);

	/**
	 * @return The amount of energy available to cast spells
	 */
	double getEnergy();

	/**
	 * Sets the amount of energy available to cast spells
	 *
	 * @param amount to be set
	 */
	void setEnergy(double amount);

	/**
	 * Adds energy to cast spells
	 *
	 * @param amount Energy to be added
	 */
	void addEnergy(double amount);

	/**
	 * Removes the energy and doesn't allow to get negative
	 *
	 * @param amount Energy to be removed
	 */
	void remEnergy(double amount);

	/**
	 * Check if there is enough energy for some action
	 *
	 * @param amount energy to be checked
	 * @return if it has the required amount
	 */
	boolean hasEnergy(double amount);

	/**
	 * Sets relative max speed based on player total available speed
	 *
	 * @param handicap The percentage of the speed
	 */
	void setHandicap(int handicap);

	/**
	 * @return The relative amount of speed
	 */
	int getHandicap();

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
	 *
	 * @param cooldown the time in tick that has to be waited
	 */
	void setPillCooldown(int cooldown);

	/**
	 * @return The maximum energy available to the player
	 */
	double getMaxEnergy();

	/**
	 * Sets the max speed a player want to use
	 *
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
	 *
	 * @param hasteLimit the max break block speed
	 */
	void setHasteLimit(float hasteLimit);

	/**
	 * @return How high a player want to jump
	 */
	float getJumpLimit();

	/**
	 * Sets the jump limit a player want to jump
	 *
	 * @param jumpLimit The multiplier of vanilla jump
	 */
	void setJumpLimit(float jumpLimit);

	/**
	 * Sets if the player want to suppress it's own cultivation advancement
	 *
	 * @param suppress whether will stop receiving progress
	 */
	void setSuppress(boolean suppress);

	/**
	 * @return If player won't receive progress
	 */
	boolean getSuppress();

	/**
	 * Sets which system will evolve from cultivate;
	 *
	 * @param selectedSystem The new selected cultivation system
	 */
	void setSelectedSystem(Cultivation.System selectedSystem);

	/**
	 * @return The selected system to cultivate
	 */
	Cultivation.System getSelectedSystem();

	float getStepAssistLimit();

	void setStepAssistLimit(float stepAssistLimit);

	/**
	 * @return an increase to the movement speed
	 */
	double getAgilityModifier();

	/**
	 * @return an increase to max hp
	 */
	double getConstitutionModifier();

	/**
	 * @return an increase to the attack speed
	 */
	double getDexterityModifier();

	/**
	 * @return an increase to armor
	 */
	double getResistanceModifier();

	/**
	 * @return an increase to the attack speed
	 */
	double getStrengthModifier();

	/**
	 * Sets it's own cultivation using another one as parameter
	 *
	 * @param cultivation The cultivation to be mirrored
	 */
	void copyFrom(ICultivation cultivation);
}
