package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillContainer;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.AspectContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.math.BigDecimal;

public interface ICultivation {

	BigDecimal getStat(PlayerStat stat);

	BigDecimal getStat(PlayerStat stat, boolean fullValue);

	BigDecimal getStat(ResourceLocation elementLocation, PlayerElementalStat stat);

	void setStat(PlayerStat stat, BigDecimal value);

	void setStat(ResourceLocation element, PlayerElementalStat stat, BigDecimal value);

	void addStat(PlayerStat stat, BigDecimal value);

	void addStat(ResourceLocation element, PlayerElementalStat stat, BigDecimal value);

	SystemContainer getSystemData(System system);

	/**
	 * Adds cultivation base to the player, it should only be used in cultivate handlers from each stage
	 *
	 * @param player the player that is cultivating
	 * @param system the system that is being cultivated
	 * @param amount the amount to be added in the cultivation
	 */
	void addCultivationBase(Player player, System system, BigDecimal amount);

	/**
	 * Attempts to break through the next stage in the specified system
	 *
	 * @param system the system of the stage to break through
	 * @return true if successful in the breakthrough
	 */
	boolean attemptBreakthrough(System system);

	void calculateStats();

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

	void setSemiDeadState(boolean state);

	void advanceSemiDead(int cooldown);

	boolean isSemiDead();

	int getSemiDeadTimer();

	boolean isDivineSense();

	void setDivineSense(boolean divineSense);

	void setFormation(BlockPos blockPos);

	@Nullable
	BlockPos getFormation();

	double getAgilityRegulator();

	void setAgilityRegulator(double agilityRegulator);

	double getStrengthRegulator();

	void setStrengthRegulator(double strengthRegulator);
}
