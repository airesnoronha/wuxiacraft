package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.utils.TranslateUtils;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"SameParameterValue", "unused"})
public class Technique {

	private final Cultivation.System system;
	private final String uName;
	private final TechniquesModifiers baseModifiers;
	private final List<PotionEffect> smallCompletionEffects;
	private final List<PotionEffect> greatCompletionEffects;
	private final List<PotionEffect> perfectionCompletionEffects;
	private final List<Element> elements;
	private float cultivationSpeed;

	private final List<Skill> smallCompletionSkills;
	private final List<Skill> greatCompletionSkills;

	public Cultivation.System getSystem() {
		return system;
	}

	public List<Skill> getSmallCompletionSkills() {
		return smallCompletionSkills;
	}

	public List<Skill> getGreatCompletionSkills() {
		return greatCompletionSkills;
	}

	public List<Skill> getPerfectionCompletionSkills() {
		return perfectionCompletionSkills;
	}

	private final List<Skill> perfectionCompletionSkills;

	public String getName() {
		return TranslateUtils.translateKey("wuxiacraft.techniques." + this.uName);
	}

	public String getUName() {
		return this.uName;
	}

	public TechniquesModifiers getBaseModifiers() {
		return baseModifiers;
	}

	public Technique(Cultivation.System system, String uName, TechniquesModifiers baseModifiers) {
		this.system = system;
		this.uName = uName;
		this.baseModifiers = baseModifiers;
		this.smallCompletionEffects = new ArrayList<>();
		this.greatCompletionEffects = new ArrayList<>();
		this.perfectionCompletionEffects = new ArrayList<>();
		this.elements = new ArrayList<>();
		this.smallCompletionSkills = new ArrayList<>();
		this.greatCompletionSkills = new ArrayList<>();
		this.perfectionCompletionSkills = new ArrayList<>();
		this.cultivationSpeed = 1;
	}

	public Technique(Cultivation.System system, String uName, TechniquesModifiers baseModifiers, float cultSpeed) {
		this.system = system;
		this.uName = uName;
		this.baseModifiers = baseModifiers;
		this.smallCompletionEffects = new ArrayList<>();
		this.greatCompletionEffects = new ArrayList<>();
		this.perfectionCompletionEffects = new ArrayList<>();
		this.elements = new ArrayList<>();
		this.smallCompletionSkills = new ArrayList<>();
		this.greatCompletionSkills = new ArrayList<>();
		this.perfectionCompletionSkills = new ArrayList<>();
		this.cultivationSpeed = cultSpeed;
	}

	Technique addSmallEffect(PotionEffect potion) {
		this.smallCompletionEffects.add(potion);
		return this;
	}

	Technique addGreatEffect(PotionEffect potion) {
		this.greatCompletionEffects.add(potion);
		return this;
	}

	Technique addPerfectionEffect(PotionEffect potion) {
		this.perfectionCompletionEffects.add(potion);
		return this;
	}

	Technique addSmallSkill(Skill skill) {
		this.smallCompletionSkills.add(skill);
		return this;
	}

	Technique addGreatSkill(Skill skill) {
		this.greatCompletionSkills.add(skill);
		return this;
	}

	Technique addPerfectSkill(Skill skill) {
		this.perfectionCompletionSkills.add(skill);
		return this;
	}

	Technique addElement(Element element) {
		this.elements.add(element);
		return this;
	}

	public List<PotionEffect> getSmallCompletionEffects() {
		return this.smallCompletionEffects;
	}

	public List<PotionEffect> getGreatCompletionEffects() {
		return this.greatCompletionEffects;
	}

	public List<PotionEffect> getPerfectionCompletionEffects() {
		return this.perfectionCompletionEffects;
	}

	public List<Element> getElements() {
		return this.elements;
	}

	public float getCultivationSpeed() {
		return cultivationSpeed;
	}

	public Technique setCultivationSpeed(float speed) {
		this.cultivationSpeed = speed;
		return this;
	}
}
