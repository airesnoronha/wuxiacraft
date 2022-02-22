package com.lazydragonstudios.wuxiacraft.cultivation.skills;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;

public class SkillDescriptor {

	private LinkedList<SkillAspect> skillChain;

	private HashMap<SkillStat, BigDecimal> skillStats;

	public SkillDescriptor() {
		this.skillChain = new LinkedList<>();
		this.skillStats = new HashMap<>();
		for (var stat : SkillStat.values()) {
			this.skillStats.put(stat, stat.defaultValue);
		}
	}

	/**
	 * this replaces a link in the chain
	 *
	 * @param position    the position to be replaced
	 * @param skillAspect the aspect to placed at the position
	 */
	public void insertSkillAt(int position, SkillAspect skillAspect) {
		if (position < this.skillChain.size()) {
			this.skillChain.set(position, skillAspect);
		} else if (position >= 0) {
			this.skillChain.add(skillAspect);
		}
	}

	/**
	 * this adds a skill in the end of the chain
	 *
	 * @param skillAspect
	 */
	public void addSkillToChain(SkillAspect skillAspect) {
		this.skillChain.add(skillAspect);
	}

	public LinkedList<SkillAspect> getSkillChain() {
		return skillChain;
	}

	public BigDecimal getStatValue(SkillStat stat) {
		return this.skillStats.getOrDefault(stat, BigDecimal.ZERO);
	}

	public void addStat(SkillStat stat, BigDecimal value) {
		this.skillStats.put(stat, this.skillStats.getOrDefault(stat, BigDecimal.ZERO).add(value).max(BigDecimal.ZERO));
	}

	public void setStat(SkillStat stat, BigDecimal value) {
		this.skillStats.put(stat, value.max(BigDecimal.ZERO));
	}

	public void compile() {
		for (var skill : skillChain) {
		}
	}

	public CompoundTag serialize() {
		var skillLists = new ListTag();
		for (var link : skillChain) {
			skillLists.add(link.serialize());
		}
		var statsList = new ListTag();
		for (var stat : SkillStat.values()) {
			if (stat.isModifiable) {
				var statTag = new CompoundTag();
				statTag.putString("stat-name", stat.toString());
				statTag.putString("stat-value", this.skillStats.getOrDefault(stat, BigDecimal.ZERO).toPlainString());
				statsList.add(statTag);
			}
		}
		var resultTag = new CompoundTag();
		resultTag.put("skills-list", skillLists);
		resultTag.put("stats-list", statsList);
		return resultTag;
	}

	public void deserialize(CompoundTag tag) {
		if (tag.contains("skills-list")) {
			var skillsTag = tag.get("skills-list");
			this.skillChain.clear();
			if (skillsTag instanceof ListTag skillsList) {
				for (var rawSkill : skillsList) {
					if (!(rawSkill instanceof CompoundTag skillTag)) continue;
					var skillAspect = SkillAspect.readAspect(skillTag);
					if (skillAspect == null) continue;
					this.skillChain.add(skillAspect);
				}
			}
		}
		if (tag.contains("stats-list")) {
			var statsTag = tag.get("stats-list");
			if (statsTag instanceof ListTag statsList) {
				for (var rawStats : statsList) {
					if (!(rawStats instanceof CompoundTag statTag)) continue;
					SkillStat stat = SkillStat.valueOf(statTag.getString("stat-name"));
					BigDecimal value = new BigDecimal(statTag.getString("stat-value"));
					this.skillStats.put(stat, value);
				}
			}
		}
		this.compile();
	}

}
