package com.lazydragonstudios.wuxiacraft.cultivation.skills;

import com.lazydragonstudios.wuxiacraft.util.MathUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashSet;

public class SkillContainer {


	private final ArrayList<SkillDescriptor> skills = new ArrayList<>(10);

	public final HashSet<ResourceLocation> knownSkills = new HashSet<>();

	public SkillContainer() {
		for(int i = 0; i < 10; i ++) {
			this.skills.add(i, new SkillDescriptor());
		}
	}

	public boolean casting = false;
	public int selectedSkill = 0;

	public void setSkillAt(int pos, SkillDescriptor skill) {
		if (!MathUtil.between(pos, 0, 10)) return;
		try {
			this.skills.set(pos, skill);
		} catch (IndexOutOfBoundsException e) {
			this.skills.add(skill);
		}
	}

	public SkillDescriptor getSkillAt(int pos) {
		try {
			return this.skills.get(pos);
		} catch (IndexOutOfBoundsException e) {
			return new SkillDescriptor();
		}
	}

	public CompoundTag serialize() {
		var tag = new CompoundTag();
		var listTag = new ListTag();
		for (var skillDescriptor : this.skills) {
			var skillTag = skillDescriptor.serialize();
			skillTag.putInt("position", this.skills.indexOf(skillDescriptor));
			listTag.add(skillTag);
		}
		tag.put("skills-list", listTag);
		return tag;
	}

	public void deserialize(CompoundTag tag) {
		if (tag.contains("skills-list")) {
			var list = (ListTag) tag.get("skills-list");
			if (list != null) {
				for (var rawTag : list) {
					if (!(rawTag instanceof CompoundTag skillTag)) continue;
					var skillDescriptor = new SkillDescriptor();
					skillDescriptor.deserialize(skillTag);
					int pos = skillTag.getInt("position");
					try {
						this.skills.set(pos, skillDescriptor);
					} catch (IndexOutOfBoundsException e) {
						this.skills.add(skillDescriptor);
					}
				}
			}
		}
	}
}
