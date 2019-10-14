package com.airesnor.wuxiacraft.cultivation.skills;

import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface ISkillCap {

    List<Skill> getKnownSkills();

    void addSkill(Skill skill);

    void removeSkill(Skill skill);

    void addScheduledBlockBreaks(BlockPos pos);

    BlockPos popScheduledBlockBreaks();

    float getCooldown();

    void stepCooldown(float step);

    void resetCooldown();

    float getCastProgress();

    void stepCastProgress(float step);

    void resetCastProgress();

}
