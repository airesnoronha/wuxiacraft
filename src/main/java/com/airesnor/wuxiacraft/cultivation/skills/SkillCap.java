package com.airesnor.wuxiacraft.cultivation.skills;

import net.minecraft.util.math.BlockPos;

import java.util.*;

public class SkillCap implements ISkillCap {

    private List<Skill> knownSkills;
    private Stack<BlockPos> toBreak;
    private float cooldown;
    private float castProgress;

    public SkillCap() {
        this.knownSkills = new ArrayList<>();
        this.toBreak = new Stack<>();
        this.cooldown = 0;
        this.castProgress = 0;
    }

    @Override
    public List<Skill> getKnownSkills() {
        return this.knownSkills;
    }

    @Override
    public void addSkill(Skill skill) {
        this.knownSkills.add(skill);
    }

    @Override
    public void removeSkill(Skill skill) {
        this.knownSkills.remove(skill);
    }

    @Override
    public void addScheduledBlockBreaks(BlockPos pos) {
        this.toBreak.add(pos);
    }

    @Override
    public BlockPos popScheduledBlockBreaks() {
        return this.toBreak.pop();
    }

    @Override
    public float getCooldown() {
        return this.cooldown;
    }

    @Override
    public void stepCooldown(float step) {
        this.cooldown+= step;
    }

    @Override
    public void resetCooldown() {
        this.cooldown = 0;
    }

    @Override
    public float getCastProgress() {
        return this.castProgress;
    }

    @Override
    public void stepCastProgress(float step) {
        this.castProgress+=step;
    }

    @Override
    public void resetCastProgress() {
        this.castProgress = 0;
    }
}
