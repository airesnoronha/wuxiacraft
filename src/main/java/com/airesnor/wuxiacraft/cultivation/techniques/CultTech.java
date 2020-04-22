package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;

import java.util.ArrayList;
import java.util.List;

public class CultTech implements ICultTech {

	private final List<KnownTechnique> knownTechniques;

	public CultTech() {
		this.knownTechniques = new ArrayList<>();
	}

	@Override
	public boolean addTechnique(Technique technique, double progress) {
		boolean contains = false;
		boolean elementalReverse = false;
		for (KnownTechnique t : getKnownTechniques()) {
			if (t.getTechnique() == technique) {
				contains = true;
			}
			for (Element e : t.getTechnique().getElements()) {
				for (Element n : technique.getElements()) {
					if (e.isCounter(n) || n.isCounter(e))
						elementalReverse = true;
				}
			}
		}
		if (!contains && !elementalReverse) {
			this.knownTechniques.add(new KnownTechnique(technique, progress));
			return true;
		}
		return false;
	}

	@Override
	public void remTechnique(Technique technique) {
		for (KnownTechnique t : this.knownTechniques) {
			if (t.getTechnique().equals(technique)) {
				this.knownTechniques.remove(t);
				break;
			}
		}
	}

	@Override
	public void progress(double amount) {
		for (KnownTechnique t : this.knownTechniques) {
			t.progress(amount);
		}
	}

	@Override
	public List<KnownTechnique> getKnownTechniques() {
		return this.knownTechniques;
	}

	@Override
	public TechniquesModifiers getOverallModifiers() {
		double armor = 0;
		double attackSpeed = 0;
		double maxHealth = 0;
		double speed = 0;
		double strength = 0;
		for (KnownTechnique t : this.getKnownTechniques()) {
			armor+= t.getTechnique().getBaseModifiers().armor;
			attackSpeed+=t.getTechnique().getBaseModifiers().attackSpeed;
			maxHealth+=t.getTechnique().getBaseModifiers().maxHealth;
			speed+=t.getTechnique().getBaseModifiers().movementSpeed;
			strength+=t.getTechnique().getBaseModifiers().strength;
		}
		return new TechniquesModifiers(armor, attackSpeed, maxHealth, speed, strength);
	}

	@Override
	public List<Skill> getTechniqueSkills() {
		List<Skill> knownSkills = new ArrayList<>();
		if (this.knownTechniques.size() > 0) {
			knownSkills.add(Skills.CULTIVATE);
		}
		for (KnownTechnique kt : getKnownTechniques()) {
			Technique t = kt.getTechnique();
			if (kt.getProgress() >= t.getTier().smallProgress) {
				for (Skill skill : t.getSmallCompletionSkills()) {
					if (!knownSkills.contains(skill)) {
						knownSkills.add(skill);
					}
				}
			}
			if (kt.getProgress() >= t.getTier().smallProgress + t.getTier().greatProgress) {
				for (Skill skill : t.getGreatCompletionSkills()) {
					if (!knownSkills.contains(skill)) {
						knownSkills.add(skill);
					}
				}
			}
			if (kt.getProgress() >= t.getTier().smallProgress + t.getTier().greatProgress + t.getTier().perfectionProgress) {
				for (Skill skill : t.getPerfectionCompletionSkills()) {
					if (!knownSkills.contains(skill)) {
						knownSkills.add(skill);
					}
				}
			}
		}
		return knownSkills;
	}

	@Override
	public float getOverallCultivationSpeed() {
		int mortals = 0;
		float fromMortal = 0;
		int martials = 0;
		float fromMartial = 0;
		int immortals = 0;
		float fromImmortal = 0;
		int divines = 0;
		float fromDivine = 0;
		for (KnownTechnique kt : getKnownTechniques()) {
			switch (kt.getTechnique().getTier()) {
				case MORTAL:
					mortals++;
					fromMortal += kt.getTechnique().getCultivationSpeed();
					break;
				case MARTIAL:
					martials++;
					fromMartial += kt.getTechnique().getCultivationSpeed();
					break;
				case IMMORTAL:
					immortals++;
					fromImmortal += kt.getTechnique().getCultivationSpeed();
					break;
				case DIVINE:
					divines++;
					fromDivine += kt.getTechnique().getCultivationSpeed();
					break;
			}
		}
		float divider = 1 + mortals + martials * 5 + immortals * 25 + divines * 125;
		return (1 + fromMortal + fromMartial * 5 + fromImmortal * 25 + fromDivine * 125) / divider;
	}

	@Override
	public void copyFrom(ICultTech cultTech) {
		this.knownTechniques.clear();
		this.knownTechniques.addAll(cultTech.getKnownTechniques());
	}
}
