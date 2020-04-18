package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Stack;

public interface ISkillCap {

	List<Skill> getKnownSkills();

	void addSkill(Skill skill);

	void removeSkill(Skill skill);

	void addScheduledBlockBreaks(BlockPos pos);

	void addAllScheduledBlockBreaks(Stack<BlockPos> pos);

	BlockPos popScheduledBlockBreaks();

	boolean isScheduledEmpty();

	float getCooldown();

	float getMaxCooldown();

	void stepCooldown(float step);

	void resetCooldown();

	float getCastProgress();

	void setCastProgress(float castProgress);

	void setCooldown(float cooldown);

	void stepCastProgress(float step);

	void resetCastProgress();

	List<Integer> getSelectedSkills();

	void addSelectedSkill(int skillIndex);

	void remSelectedSkill(int skillIndex);

	int getActiveSkill();

	void setActiveSkill(int i);

	void setCasting(boolean casting);

	boolean isCasting();

	void setDoneCasting(boolean doneCasting);

	boolean isDoneCasting();

	void resetBarrageCounter();

	int getBarrageReleased();

	int getBarrageToRelease();

	void increaseBarrageReleased();

	void increaseBarrageToRelease();

	void resetBarrageToRelease();

	List<Skill> getTotalKnowSkill(ICultTech techniques);

	Skill getSelectedSkill(ICultTech techniques);

	void setFormationActivated(boolean activated);

	boolean hasFormationActivated();

}
