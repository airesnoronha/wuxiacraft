package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SkillsStorage implements Capability.IStorage<ISkillCap> {
	@Nullable
	@Override
	public NBTBase writeNBT(Capability<ISkillCap> capability, ISkillCap instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("length", instance.getKnownSkills().size());
		for (int i = 0; i < instance.getKnownSkills().size(); i++) {
			int pos = Skills.SKILLS.indexOf(instance.getKnownSkills().get(i));
			tag.setInteger("skill-" + i, pos);
		}
		tag.setFloat("cooldown", instance.getCooldown());
		tag.setFloat("castProgress", instance.getCastProgress());
		tag.setInteger("ks-length", instance.getSelectedSkills().size());
		for (int i = 0; i < instance.getSelectedSkills().size(); i++) {
			tag.setInteger("ks-" + i, Skills.SKILLS.indexOf(instance.getSelectedSkills().get(i)));
		}
		tag.setInteger("active", instance.getActiveSkill());
		return tag;
	}

	@Override
	public void readNBT(Capability<ISkillCap> capability, ISkillCap instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		int length = tag.getInteger("length");
		instance.getKnownSkills().clear();
		for (int i = 0; i < length; i++) {
			int skillId = tag.getInteger("skill-" + i);
			Skill skill = Skills.SKILLS.get(skillId);
			instance.addSkill(skill);
		}
		instance.stepCastProgress(tag.getInteger("castProgress"));
		instance.stepCooldown(tag.getInteger("cooldown"));
		length = tag.getInteger("ks-length");
		instance.getSelectedSkills().clear();
		for (int i = 0; i < length; i++) {
			Skill skill = Skills.SKILLS.get(tag.getInteger("ks-" + i));
			instance.addSelectedSkill(skill);
		}
		instance.setActiveSkill(tag.getInteger("active"));
	}
}
