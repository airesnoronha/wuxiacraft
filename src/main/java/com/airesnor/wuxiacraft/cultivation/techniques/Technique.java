package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class Technique {

	private TechniqueTier tier;
	private String uName;
	private TechniquesModifiers baseModifiers;
	private List<PotionEffect> smallCompletionEffects;
	private List<PotionEffect> greatCompletionEffects;
	private List<PotionEffect> perfectionCompletionEffects;
	private List<Element> elements;
	private float cultivationSpeed;

	private List<Skill> smallCompletionSkills;
	private List<Skill> greatCompletionSkills;

	public List<Skill> getSmallCompletionSkills() {
		return smallCompletionSkills;
	}

	public List<Skill> getGreatCompletionSkills() {
		return greatCompletionSkills;
	}

	public List<Skill> getPerfectionCompletionSkills() {
		return perfectionCompletionSkills;
	}

	private List<Skill> perfectionCompletionSkills;

	public TechniqueTier getTier() {
		return this.tier;
	}

	public String getName() {
		return I18n.format("wuxiacraft.techniques." + this.uName);
	}

	public String getUName() {
		return this.uName;
	}

	public TechniquesModifiers getBaseModifiers() {
		return baseModifiers;
	}

	public Technique(TechniqueTier tier, String uName, TechniquesModifiers baseModifiers) {
		this.tier = tier;
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

	public Technique(TechniqueTier tier, String uName, TechniquesModifiers baseModifiers, float cultSpeed) {
		this.tier = tier;
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

	public List<PotionEffect> getPerfectionCompletionEffects() {
		return perfectionCompletionEffects;
	}

	public List<Element> getElements() {
		return this.elements;
	}

	public TechniquesModifiers updateSmallSuccess(EntityPlayer player, ICultivation cultivation) {
		float strength = cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel());
		float speed = cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel());
		float armor = this.baseModifiers.armor * strength;
		float attackSpeed = this.baseModifiers.attackSpeed * speed;
		float maxHealth = this.baseModifiers.maxHealth * strength;
		float movementSpeed = this.baseModifiers.movementSpeed * strength;
		float strengthMod = this.baseModifiers.strength * strength;
		for (PotionEffect p : smallCompletionEffects) {
			if (p.getIsAmbient()) {
				if (player.getActivePotionEffect(p.getPotion()) != null) {
					continue;
				}
			}
			player.addPotionEffect(new PotionEffect(p.getPotion(), p.getDuration(), p.getAmplifier(), false, p.doesShowParticles()));
		}
		return new TechniquesModifiers(armor, attackSpeed, maxHealth, movementSpeed, strengthMod);
	}

	public TechniquesModifiers updateGreatSuccess(EntityPlayer player, ICultivation cultivation) {
		float strength = cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel());
		float speed = cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel());
		float armor = this.baseModifiers.armor * strength;
		armor = armor < 0 ? armor - (armor * 0.1f) : armor * 1.1f;

		float attackSpeed = this.baseModifiers.attackSpeed * speed;
		attackSpeed = attackSpeed < 0 ? attackSpeed - (attackSpeed * 0.1f) : attackSpeed * 1.1f;

		float maxHealth = this.baseModifiers.maxHealth * strength;
		maxHealth = maxHealth < 0 ? maxHealth - (maxHealth * 0.1f) : maxHealth * 1.1f;

		float movementSpeed = this.baseModifiers.movementSpeed * speed;
		movementSpeed = movementSpeed < 0 ? movementSpeed - (movementSpeed * 0.1f) : movementSpeed * 1.1f;

		float strengthMod = this.baseModifiers.strength * strength;
		strengthMod = strengthMod < 0 ? strengthMod - (strengthMod * 0.1f) : strengthMod * 1.1f;

		for (PotionEffect p : greatCompletionEffects) {
			if (p.getIsAmbient()) {
				if (player.getActivePotionEffect(p.getPotion()) != null) {
					continue;
				}
			}
			player.addPotionEffect(new PotionEffect(p.getPotion(), p.getDuration(), p.getAmplifier(), false, p.doesShowParticles()));
		}
		return new TechniquesModifiers(armor, attackSpeed, maxHealth, movementSpeed, strengthMod);
	}

	TechniquesModifiers updatePerfection(EntityPlayer player, ICultivation cultivation) {
		float strength = cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel());
		float speed = cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel());

		float armor = this.baseModifiers.armor * strength;
		armor = armor < 0 ? armor - (armor * 0.25f) : armor * 1.25f;
		float attackSpeed = this.baseModifiers.attackSpeed * speed;
		attackSpeed = attackSpeed < 0 ? attackSpeed - (attackSpeed * 0.25f) : attackSpeed * 1.25f;
		float maxHealth = this.baseModifiers.maxHealth * strength;
		maxHealth = maxHealth < 0 ? maxHealth - (maxHealth * 0.25f) : maxHealth * 1.25f;
		float movementSpeed = this.baseModifiers.movementSpeed * speed;
		movementSpeed = movementSpeed < 0 ? movementSpeed - (movementSpeed * 0.25f) : movementSpeed * 1.25f;
		float strengthMod = this.baseModifiers.strength * strength;
		strengthMod = strengthMod < 0 ? strengthMod - (strengthMod * 0.25f) : strengthMod * 1.25f;

		for (PotionEffect p : perfectionCompletionEffects) {
			if (p.getIsAmbient()) {
				if (player.getActivePotionEffect(p.getPotion()) != null)
					continue;
			}
			player.addPotionEffect(new PotionEffect(p.getPotion(), p.getDuration(), p.getAmplifier(), false, p.doesShowParticles()));
		}
		return new TechniquesModifiers(armor, attackSpeed, maxHealth, movementSpeed, strengthMod);
	}

	public float getCultivationSpeed() {
		return cultivationSpeed;
	}
}
