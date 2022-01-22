package wuxiacraft.cultivation;

import net.minecraft.nbt.CompoundTag;

public interface ICultivation {


	double getHealth();

	void setHealth(double health);

	SystemContainer getSystemData(Cultivation.System system);

	double getMaxHealth();

	double getStrength();

	double getAgility();

	double getHealthRegen();

	double getHealthRegenCost();

	CompoundTag serialize();

	void deserialize(CompoundTag tag);

	/**
	 * Utility to increment to the tick timer
	 */
	void advanceTimer();

	/**
	 * Utility to reset timer.
	 * Should only be used when a sync message is sent
	 */
	void resetTimer();

	/**
	 * @return the time ticker. It's just for not exposing the ticker.
	 */
	int getTimer();
}
