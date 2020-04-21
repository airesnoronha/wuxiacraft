package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SkillCap implements ISkillCap {

	private final List<Skill> knownSkills;
	private final Stack<BlockPos> toBreak;
	private float cooldown;
	private float castProgress;
	private final List<Integer> SelectedSkills;
	private int ActiveSkillIndex;
	private boolean casting;
	private boolean doneCasting;
	private int barrageToRelease;
	private int barrageReleased;
	private float maxCooldown;
	private boolean formationActivated;

	public SkillCap() {
		this.knownSkills = new ArrayList<>();
		this.toBreak = new Stack<>();
		this.cooldown = 0;
		this.maxCooldown = 0;
		this.castProgress = 0;
		this.SelectedSkills = new ArrayList<>();
		this.ActiveSkillIndex = -1;
		this.casting = false;
		this.doneCasting = false;
		this.resetBarrageCounter();
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
	public void addAllScheduledBlockBreaks(Stack<BlockPos> pos) {
		this.toBreak.addAll(pos);
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
	public float getMaxCooldown() {
		return this.maxCooldown;
	}

	@Override
	public void stepCooldown(float step) {
		this.cooldown += step;
		if (this.cooldown <= 0) this.maxCooldown = 1f;
		else if (this.cooldown > 0 && this.maxCooldown < step) this.maxCooldown = step;
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
		this.castProgress += step;
		this.castProgress = Math.max(0, this.castProgress);
	}

	@Override
	public void resetCastProgress() {
		this.castProgress = 0;
	}

	@Override
	public boolean isScheduledEmpty() {
		return this.toBreak.isEmpty();
	}

	@Override
	public List<Integer> getSelectedSkills() {
		return this.SelectedSkills;
	}

	@Override
	public void addSelectedSkill(int skillIndex) {
		if (skillIndex >= 0)
			this.SelectedSkills.add(skillIndex);
	}

	@Override
	public void remSelectedSkill(int skillIndex) {
		this.SelectedSkills.remove(new Integer(skillIndex));
	}

	@Override
	public int getActiveSkill() {
		return this.ActiveSkillIndex;
	}

	@Override
	public void setActiveSkill(int i) {
		this.ActiveSkillIndex = i;
	}

	@Override
	public boolean isCasting() {
		return this.casting;
	}

	@Override
	public void setCasting(boolean casting) {
		this.casting = casting;
		if (!casting) {
			resetBarrageCounter();
		}
	}

	@Override
	public boolean isDoneCasting() {
		return this.doneCasting;
	}

	@Override
	public void setDoneCasting(boolean doneCasting) {
		this.doneCasting = doneCasting;
	}

	@Override
	public void resetBarrageCounter() {
		this.barrageToRelease = 0;
		this.barrageReleased = 0;
	}

	@Override
	public int getBarrageReleased() {
		return this.barrageReleased;
	}

	@Override
	public int getBarrageToRelease() {
		return this.barrageToRelease;
	}

	@Override
	public void increaseBarrageReleased() {
		this.barrageReleased++;
	}

	@Override
	public void increaseBarrageToRelease() {
		this.barrageToRelease++;
	}

	@Override
	public void resetBarrageToRelease() {
		this.barrageToRelease = 0;
	}

	@Override
	public void setCastProgress(float castProgress) {
		this.castProgress = castProgress;
	}

	@Override
	public void setCooldown(float Cooldown) {
		this.cooldown = Cooldown;
	}

	@Override
	public Skill getSelectedSkill(ICultTech techniques) {
		List<Skill> totalSkills = this.getTotalKnowSkill(techniques);
		int selectedSkill = this.getActiveSkill(); //get index from selected list
		selectedSkill = MathUtils.clamp(selectedSkill, 0, this.getSelectedSkills().size() - 1); //clamp to selected list
		if(selectedSkill < 0) selectedSkill = 0;
		if(this.getSelectedSkills().size() == 0) return null;
		selectedSkill = MathUtils.clamp(this.getSelectedSkills().get(selectedSkill), 0, totalSkills.size() - 1); //get clamped from known skills
		if(selectedSkill < 0) selectedSkill = 0;
		return !totalSkills.isEmpty() && !getSelectedSkills().isEmpty()? totalSkills.get(selectedSkill) : null;
	}

	@Override
	public List<Skill> getTotalKnowSkill(ICultTech techniques) {
		List<Skill> total = new ArrayList<>();
		total.addAll(techniques.getTechniqueSkills());
		total.addAll(this.getKnownSkills());
		return total;
	}

	@Override
	public void setFormationActivated(boolean activated) {
		this.formationActivated = activated;
	}

	@Override
	public boolean hasFormationActivated() {
		return this.formationActivated;
	}
}
