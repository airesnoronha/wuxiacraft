package com.lazydragonstudios.wuxiacraft.cultivation;

import net.minecraft.nbt.CompoundTag;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.AspectContainer;

import java.math.BigDecimal;

public interface ICultivation {

	BigDecimal getPlayerStat(PlayerStat stat);

	void setPlayerStat(PlayerStat stat, BigDecimal value);

	SystemContainer getSystemData(System system);

	CompoundTag serialize();

	void deserialize(CompoundTag tag);

	boolean isExercising();

	void setExercising(boolean exercising);

	AspectContainer getAspects();

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
