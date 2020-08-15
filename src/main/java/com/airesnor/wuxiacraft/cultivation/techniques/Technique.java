package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillAction;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.utils.TranslateUtils;
import net.minecraft.potion.PotionEffect;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

public class Technique {

	private final Cultivation.System system;
	private final String uName;
	private final TechniquesModifiers baseModifiers;
	private final List<Element> elements;
	private final double cultivationSpeed;
	private final double maxProficiency;
	private final double efficientTillModifier; // this technique may work well until the system level modifier

	public ISkillAction cultivationEffect;

	private final List<Pair<Double, PotionEffect>> effects;
	private final List<Pair<Double, Skill>> skills;
	private final List<Triple<Double, Float, String>> checkpoints;
	private final List<Technique> compatibles;

	public Cultivation.System getSystem() {
		return system;
	}

	public String getName() {
		return TranslateUtils.translateKey("wuxiacraft.techniques." + this.uName);
	}

	public String getUName() {
		return this.uName;
	}

	public TechniquesModifiers getBaseModifiers() {
		return baseModifiers;
	}

	public Technique(String uName, Cultivation.System system, TechniquesModifiers baseModifiers, double cultSpeed, double maxProficiency, double efficientTillModifier) {
		this.system = system;
		this.uName = uName;
		this.baseModifiers = baseModifiers;
		this.cultivationSpeed = cultSpeed;
		this.maxProficiency = maxProficiency;
		this.efficientTillModifier = efficientTillModifier;
		this.elements = new ArrayList<>();
		this.effects = new ArrayList<>();
		this.skills = new ArrayList<>();
		this.checkpoints = new ArrayList<>();
		this.compatibles = new ArrayList<>();
		this.cultivationEffect = actor -> true;
	}

	Technique addElement(Element element) {
		this.elements.add(element);
		return this;
	}

	Technique addSkill(double proficiency, Skill skill) {
		this.skills.add(Pair.of(proficiency, skill));
		return this;
	}

	Technique addEffect(double proficiency, PotionEffect effect) {
		this.effects.add(Pair.of(proficiency, effect));
		return this;
	}

	Technique addCheckpoint(double proficiency, float releaseRate, String checkpoint) {
		this.checkpoints.add(Triple.of(proficiency, releaseRate, checkpoint));
		return this;
	}

	Technique addCompatible(Technique compatible) {
		this.compatibles.add(compatible);
		return this;
	}

	public Technique setCultivationEffect(ISkillAction effect) {
		this.cultivationEffect = effect;
		return this;
	}

	public List<Technique> getCompatibles() {
		return compatibles;
	}

	public List<Element> getElements() {
		return this.elements;
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

	public List<Pair<Double, PotionEffect>> getEffects() {
		return effects;
	}

	public List<Pair<Double, Skill>> getSkills() {
		return skills;
	}

	public List<Triple<Double, Float, String>> getCheckpoints() {
		return checkpoints;
	}
}
