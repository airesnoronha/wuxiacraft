package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;


public abstract class SkillAspect {

	protected final HashMap<SkillStat, BigDecimal> skillStats = new HashMap<>();

	public SkillAspect() {
	}

	public SkillAspect setSkillStat(SkillStat stat, BigDecimal value) {
		if (stat.isModifiable) return this;
		this.skillStats.put(stat, value);
		return this;
	}

	public abstract SkillAspectType getType();

	public BigDecimal getSkillStat(SkillStat stat) {
		return skillStats.getOrDefault(stat, BigDecimal.ZERO);
	}

	/**
	 * Will determine whether it can connect to next node
	 *
	 * @param aspect the next skill aspect trying to connect to this
	 * @return whether it can connect or not
	 */
	public abstract boolean canConnect(SkillAspect aspect);

	public abstract boolean canCompile(LinkedList<SkillAspect> aspectChain);

	public CompoundTag serialize() {
		var statsList = new ListTag();
		for (var stat : this.skillStats.keySet()) {
			var statTag = new CompoundTag();
			statTag.putString("stat-name", stat.toString());
			statTag.putString("stat-value", this.skillStats.get(stat).toPlainString());
			statsList.add(statTag);
		}
		var resultTag = new CompoundTag();
		resultTag.put("stats-list", statsList);
		var aspectType = this.getType();
		var typeLocation = aspectType.getRegistryName();
		if(typeLocation != null) {
			resultTag.putString("skill-type", typeLocation.toString());
		}
		return resultTag;
	}

	public void deserialize(CompoundTag tag) {
		if (tag.contains("stats-list")) {
			var statsTag = tag.get("stats-list");
			if (!(statsTag instanceof ListTag statsList)) return;
			for (var rawStat : statsList) {
				if (!(rawStat instanceof CompoundTag statTag)) continue;
				var stat = SkillStat.valueOf(statTag.getString("stat-name"));
				var value = new BigDecimal(statTag.getString("stat-value"));
				this.skillStats.put(stat, value);
			}
		}
	}

	public static SkillAspect readAspect(CompoundTag tag) {
		if (tag.contains("skill-type")) {
			var skillTypeName = tag.getString("skill-type");
			var skillType = WuxiaRegistries.SKILL_ASPECT.getValue(new ResourceLocation(skillTypeName));
			if (skillType == null) return null;
			var skillAspect = skillType.creator.create();
			skillAspect.deserialize(tag);
			return skillAspect;
		}
		return null;
	}

}
