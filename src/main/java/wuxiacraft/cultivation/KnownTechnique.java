package wuxiacraft.cultivation;

import net.minecraft.potion.Effect;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.cultivation.technique.TechniqueModifiers;

import java.util.LinkedList;
import java.util.List;

public class KnownTechnique {

	private final Technique technique;
	private double proficiency;

	/**
	 * Known technique is a way to store a technique coupled with a proficiency in such technique
	 * and also get it's modifiers and effects from it
	 *
	 * @param technique   The technique that is known
	 * @param proficiency How much effort was put into this technique
	 */
	public KnownTechnique(Technique technique, double proficiency) {
		this.technique = technique;
		this.proficiency = proficiency;
	}

	/**
	 * This will add proficiency to this known technique
	 *
	 * @param amount the amount to be added
	 */
	public void proficiency(double amount) {
		this.proficiency = Math.max(this.proficiency + amount, 0);
	}

	/**
	 * @return Proficiency works as experience in such technique
	 */
	public double getProficiency() {
		return this.proficiency;
	}

	/**
	 * @return The technique
	 */
	public Technique getTechnique() {
		return technique;
	}

	/**
	 * @return The name of the current stage of the cultivation technique
	 */
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

	/**
	 * @return A list of active potion effects that will be passive to the player based on it's proficiency
	 */
	public List<Effect> getTechniqueEffects() {
		List<Effect> effects = new LinkedList<>();
		for (Pair<Double, Effect> effect : this.getTechnique().getEffects()) {
			if (this.proficiency >= effect.getKey()) {
				effects.add(effect.getValue());
			}
		}
		return effects;
	}

	/**
	 * @return This is where release factor from below is made into modifiers.
	 */
	public TechniqueModifiers getModifiers() {
		return this.getTechnique().getBaseModifiers().multiply(getReleaseFactor());
	}

	/**
	 * @return A list of skills that this technique can provide.
	 */
	public List<Skill> getKnownSkills() {
		List<Skill> skills = new LinkedList<>();
		for (Pair<Double, Skill> skill : this.getTechnique().getSkills()) {
			if (this.proficiency >= skill.getKey()) {
				skills.add(skill.getValue());
			}
		}
		return skills;
	}

	/**
	 * @return All techniques that can be switched without punishment.
	 */
	public List<Technique> getCompatibles() {
		return this.technique.getCompatibles();
	}

	/**
	 * This is how much the cultivation speed is gonna be influenced by this technique.
	 * This is not a boost but rather a break, so that techniques can have a limited strength.
	 *
	 * @param modifier The strength of the cultivator in such system.
	 * @return The speed of cultivation.
	 */
	public double getCultivationSpeed(double modifier) {
		double speed = technique.getCultivationSpeed() + (0.5 + 2.5 * this.proficiency / this.technique.getMaxProficiency());
		if (modifier > technique.getEfficientTillModifier()) {
			return speed * (technique.getEfficientTillModifier() / (modifier * 2)); //Eventually will amount to almost nothing
		}
		return speed;
	}

	/**
	 * This is how much the modifiers from technique are going to be added to final stats
	 *
	 * @return The relative amount from the maximum of the known techniques
	 */
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

	/**
	 * Gets the amount of relative energy regen modifier
	 *
	 * @return the amount will be used like final_regen =  base_regen * (1+technique_regen)
	 */
	public double getEnergyRegen() {
		return this.getTechnique().getEnergyRegen() * this.getReleaseFactor();
	}

	/**
	 * Gets the amount of relative max energy modifier
	 *
	 * @return the amount will be used like final_max =  base_max * (1+technique_max)
	 */
	public double getMaxEnergy() {
		return this.getTechnique().getMaxEnergy() * this.getReleaseFactor();
	}

	/**
	 * this will affect how the technique will increase healing to a player
	 * most techniques won't, or maybe will increase a little
	 *
	 * @return the amount will be used like final_cost = base_cost * (1 + technique_cost)
	 */
	public double getHealingCostModifier() {
		return this.getTechnique().getHealingCostModifier() * this.getReleaseFactor();
	}

	/**
	 * this will affect how the technique will increase healing to a player
	 * most techniques won't, or maybe will increase a little
	 *
	 * @return the amount will be used like final_amount = base_amount * (1 + technique_amount)
	 */
	public double getHealingAmountModifier() {
		return this.getTechnique().getHealingAmountModifier() * this.getReleaseFactor();
	}

}
