package wuxiacraft.capabilities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.KnownTechnique;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.init.WuxiaSkills;
import wuxiacraft.init.WuxiaTechniques;

import java.util.*;
import java.util.stream.Collectors;

public class CultivationStorage implements Capability.IStorage<ICultivation> {

	@Nullable
	@Override
	public INBT writeNBT(Capability<ICultivation> capability, ICultivation instance, Direction side) {
		CompoundNBT tag = new CompoundNBT();
		//system Stats
		tag.put("bodyStats", instance.getStatsBySystem(CultivationLevel.System.BODY).writeToNBT());
		tag.put("divineStats", instance.getStatsBySystem(CultivationLevel.System.DIVINE).writeToNBT());
		tag.put("essenceStats", instance.getStatsBySystem(CultivationLevel.System.ESSENCE).writeToNBT());

		//body known technique
		KnownTechnique knownTechnique = instance.getTechniqueBySystem(CultivationLevel.System.BODY);
		if (knownTechnique != null) {
			tag.putString("bodyTechName", knownTechnique.getTechnique().getName());
			tag.putDouble("bodyProficiency", knownTechnique.getProficiency());
		}
		//divine known technique
		knownTechnique = instance.getTechniqueBySystem(CultivationLevel.System.DIVINE);
		if (knownTechnique != null) {
			tag.putString("divineTechName", knownTechnique.getTechnique().getName());
			tag.putDouble("divineProficiency", knownTechnique.getProficiency());
		}
		//essence known technique
		knownTechnique = instance.getTechniqueBySystem(CultivationLevel.System.ESSENCE);
		if (knownTechnique != null) {
			tag.putString("essenceTechName", knownTechnique.getTechnique().getName());
			tag.putDouble("essenceProficiency", knownTechnique.getProficiency());
		}

		//skill values
		List<Skill> skillsList = instance.getKnownSkills();
		tag.putInt("skillsCount", skillsList.size());
		for(Skill skill : skillsList) {
			tag.putString("skill-" + skillsList.indexOf(skill), skill.name);
		}
		tag.putIntArray("selectedSkills", instance.getSelectedSkills());
		tag.putDouble("skillCooldown", instance.getSkillCooldown());

		//character's health
		tag.putDouble("health", instance.getHP());

		return tag;
	}

	@Override
	public void readNBT(Capability<ICultivation> capability, ICultivation instance, Direction side, INBT nbt) {
		CompoundNBT tag = (CompoundNBT) nbt;
		//system Stats
		if (tag.contains("bodyStats"))
			instance.getStatsBySystem(CultivationLevel.System.BODY).readFromNBT((CompoundNBT) Objects.requireNonNull(tag.get("bodyStats")), CultivationLevel.System.BODY);
		if (tag.contains("divineStats"))
			instance.getStatsBySystem(CultivationLevel.System.DIVINE).readFromNBT((CompoundNBT) Objects.requireNonNull(tag.get("divineStats")), CultivationLevel.System.DIVINE);
		if (tag.contains("essenceStats"))
			instance.getStatsBySystem(CultivationLevel.System.ESSENCE).readFromNBT((CompoundNBT) Objects.requireNonNull(tag.get("essenceStats")), CultivationLevel.System.ESSENCE);

		//body known technique
		if (tag.contains("bodyTechName")) {
			Technique technique = WuxiaTechniques.getTechniqueByName(tag.getString("bodyTechName"));
			instance.setKnownTechnique(technique, tag.getDouble("bodyProficiency"));
		}
		//divine known technique
		if (tag.contains("divineTechName")) {
			Technique technique = WuxiaTechniques.getTechniqueByName(tag.getString("divineTechName"));
			instance.setKnownTechnique(technique, tag.getDouble("divineProficiency"));
		}
		//essence known technique
		if (tag.contains("essenceTechName")) {
			Technique technique = WuxiaTechniques.getTechniqueByName(tag.getString("essenceTechName"));
			instance.setKnownTechnique(technique, tag.getDouble("essenceProficiency"));
		}

		int skillsCount = tag.getInt("skillsCount");
		for(int i = 0; i < skillsCount; i++) {
			Skill skill = WuxiaSkills.getSkillByName(tag.getString("skill-" + i));
			if(skill != null) {
				instance.addKnownSkill(skill);
			}
		}

		//skill values
		instance.getSelectedSkills().clear();
		instance.getSelectedSkills().addAll(Arrays.stream(tag.getIntArray("selectedSkills")).boxed().collect(Collectors.toList()));
		instance.setSkillCooldown(tag.getDouble("skillCooldown"));

		//health
		instance.setHP(tag.getDouble("health"));

	}
}
