package wuxiacraft.cultivation.technique;

import net.minecraft.potion.Effect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.skill.Skill;

import java.util.LinkedList;
import java.util.List;

public class Technique {

	private final CultivationLevel.System system;
	private final String name;
	private final TechniqueModifiers baseModifiers;
	private final double cultivationSpeed;
	private final double maxProficiency;
	private final double efficientTillModifier; // this technique may work well until the system level modifier

	private final List<Pair<Double, Skill>> skills;
	private final List<Pair<Double, Effect>> effects;
	private final List<Triple<Double, Float, String>> checkpoints;
	private final List<Technique> compatibles;

	public Technique(CultivationLevel.System system, String name, TechniqueModifiers baseModifiers, double cultivationSpeed, double maxProficiency, double efficientTillModifier) {
		this.system = system;
		this.name = name;
		this.baseModifiers = baseModifiers;
		this.cultivationSpeed = cultivationSpeed;
		this.maxProficiency = maxProficiency;
		this.efficientTillModifier = efficientTillModifier;
		this.skills = new LinkedList<>();
		this.effects = new LinkedList<>();
		this.checkpoints = new LinkedList<>();
		this.compatibles = new LinkedList<>();
	}

	public CultivationLevel.System getSystem() {
		return system;
	}

	public String getName() {
		return name;
	}

	public TechniqueModifiers getBaseModifiers() {
		return baseModifiers;
	}

	public double getCultivationSpeed() {
		return cultivationSpeed;
	}

	public double getMaxProficiency() {
		return maxProficiency;
	}

	public double getEfficientTillModifier() {
		return efficientTillModifier;
	}

	public List<Pair<Double, Skill>> getSkills() {
		return skills;
	}

	public List<Pair<Double, Effect>> getEffects() {
		return effects;
	}

	public List<Triple<Double, Float, String>> getCheckpoints() {
		return checkpoints;
	}

	public List<Technique> getCompatibles() {
		return compatibles;
	}

}
