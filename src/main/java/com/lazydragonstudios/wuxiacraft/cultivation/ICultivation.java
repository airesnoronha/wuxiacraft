package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillContainer;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import net.minecraft.nbt.CompoundTag;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.AspectContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.math.BigDecimal;

public interface ICultivation {

	BigDecimal getStat(PlayerStat stat);

	BigDecimal getStat(ResourceLocation elementLocation, PlayerElementalStat stat);

	void setStat(PlayerStat stat, BigDecimal value);

	void setStat(ResourceLocation element, PlayerElementalStat stat, BigDecimal value);

	void addStat(PlayerStat stat, BigDecimal value);

	void addStat(ResourceLocation element, PlayerElementalStat stat, BigDecimal value);

	SystemContainer getSystemData(System system);

	void calculateStats();

	boolean addCultivationBase(Player player, System system, BigDecimal amount);

	CompoundTag serialize();

	void deserialize(CompoundTag tag);

	boolean isExercising();

	void setExercising(boolean exercising);

	AspectContainer getAspects();

	SkillContainer getSkills();

	boolean isCombat();

	void setCombat(boolean combat);

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
