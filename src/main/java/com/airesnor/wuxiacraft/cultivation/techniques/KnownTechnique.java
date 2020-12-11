package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import net.minecraft.potion.PotionEffect;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

public class KnownTechnique {

	private final Technique technique;
	private double proficiency;

	public KnownTechnique(Technique technique, double proficiency) {
		this.technique = technique;
		this.proficiency = proficiency;
	}

	public void progress(double amount) {
		this.proficiency = Math.max(this.proficiency + amount, 0);
	}

	public double getProficiency() {
		return this.proficiency;
	}

	public Technique getTechnique() {
		return technique;
	}

	public String getCurrentCheckpoint() {
		String current = "No success";
		double highestComparedCheckpointProficiency = 0; //in case checkpoints aren't in order
		for (Triple<Double, Float, String> checkpoint : technique.getCheckpoints()) {
			if (checkpoint.getLeft() > highestComparedCheckpointProficiency) {
				if (this.proficiency > checkpoint.getLeft()) {
					highestComparedCheckpointProficiency = checkpoint.getLeft();
					current = checkpoint.getRight();
				}
			}
		}
		return current;
	}

	public List<PotionEffect> getTechniqueEffects() {
		List<PotionEffect> effects = new ArrayList<>();
		for (Pair<Double, PotionEffect> effect : this.getTechnique().getEffects()) {
			if (this.proficiency >= effect.getKey()) {
				effects.add(effect.getValue());
			}
		}
		return effects;
	}

	public TechniqueModifiers getModifiers() {
		return this.getTechnique().getBaseModifiers().multiply(getReleaseFactor());
	}

	public List<Skill> getKnownSkills() {
		List<Skill> skills = new ArrayList<>();
		for (Pair<Double, Skill> skill : this.getTechnique().getSkills()) {
			if (this.proficiency >= skill.getKey()) {
				skills.add(skill.getValue());
			}
		}
		if (this.technique.getSystem() == Cultivation.System.DIVINE) {
			if (this.proficiency > 2000 * 8) {
				skills.add(Skills.SPIRIT_PRESSURE);
			}
			if (this.proficiency >= 10000 * 8) {
				skills.add(Skills.SPIRIT_ARROW);
			}
		}
		if (this.technique.getSystem() == Cultivation.System.ESSENCE) {
			if (this.proficiency >= 4000 * 8) {
				skills.add(Skills.WEAK_SWORD_FLIGHT);
			}
		}
		return skills;
	}

	public List<Technique> getCompatibles() {
		return this.technique.getCompatibles();
	}

	public double getCultivationSpeed(double modifier) {
		double speed = technique.getCultivationSpeed() + (0.5 + 2.5 * this.proficiency / this.technique.getMaxProficiency());
		if (modifier > technique.getEfficientTillModifier()) {
			return speed * (technique.getEfficientTillModifier() / (modifier * 2)); //Eventually will amount to almost nothing
		}
		return speed;
	}

	public double getReleaseFactor() {
		double unlocked = 0; //from 0 to 1
		double highestComparedCheckpointProficiency = 0; //in case checkpoints aren't in order
		for (Triple<Double, Float, String> checkpoint : technique.getCheckpoints()) {
			if (checkpoint.getLeft() > highestComparedCheckpointProficiency) {
				if (this.proficiency > checkpoint.getLeft()) {
					highestComparedCheckpointProficiency = checkpoint.getLeft();
					unlocked = checkpoint.getMiddle();
				}
			}
		}
		return unlocked;
	}
}