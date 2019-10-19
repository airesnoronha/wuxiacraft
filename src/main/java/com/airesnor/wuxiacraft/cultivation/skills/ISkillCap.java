package com.airesnor.wuxiacraft.cultivation.skills;

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

    void stepCooldown(float step);

    void resetCooldown();

    float getCastProgress();

    void stepCastProgress(float step);

    void resetCastProgress();

    List<Skill> getSelectedSkills();

    void addSelectedSkill(Skill skill);

    void remSelectedSkill(Skill skill);

    int getActiveSkill();

    void setActiveSkill(int i);

}
