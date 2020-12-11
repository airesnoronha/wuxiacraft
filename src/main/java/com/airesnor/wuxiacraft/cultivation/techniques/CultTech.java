package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class CultTech implements ICultTech {

	public KnownTechnique bodyTechnique, divineTechnique, essenceTechnique;

	public CultTech() {
		bodyTechnique = null;
		divineTechnique = null;
		essenceTechnique = null;
	}

	@Override
	public void progress(double amount, Cultivation.System system) {
		switch (system) {
			case BODY:
				if (this.bodyTechnique != null)
					this.bodyTechnique.progress(amount);
				break;
			case DIVINE:
				if (this.divineTechnique != null)
					this.divineTechnique.progress(amount);
				break;
			case ESSENCE:
				if (this.essenceTechnique != null)
					this.essenceTechnique.progress(amount);
				break;
		}
	}

	@Override
	public boolean addTechnique(Technique technique) {
		boolean added = false;
		switch(technique.getSystem()) {
			case BODY:
				if(this.bodyTechnique == null) {
					this.bodyTechnique = new KnownTechnique(technique, 0);
					added = true;
				} else if(this.bodyTechnique.getTechnique().getCompatibles().contains(technique)) {
					this.bodyTechnique = new KnownTechnique(technique, this.bodyTechnique.getProficiency());
					added = true;
				}
				break;
			case DIVINE:
				if(this.divineTechnique == null) {
					this.divineTechnique = new KnownTechnique(technique, 0);
					added = true;
				} else if(this.divineTechnique.getTechnique().getCompatibles().contains(technique)) {
					this.divineTechnique = new KnownTechnique(technique, this.divineTechnique.getProficiency());
					added = true;
				}
				break;
			case ESSENCE:
				if(this.essenceTechnique == null) {
					this.essenceTechnique = new KnownTechnique(technique, 0);
					added = true;
				} else if(this.essenceTechnique.getTechnique().getCompatibles().contains(technique)) {
					this.essenceTechnique = new KnownTechnique(technique, this.essenceTechnique.getProficiency());
					added = true;
				}
				break;
		}
		return added;
	}

	@Override
	public KnownTechnique getBodyTechnique() {
		return bodyTechnique;
	}

	@Override
	public KnownTechnique getDivineTechnique() {
		return divineTechnique;
	}

	@Override
	public KnownTechnique getEssenceTechnique() {
		return essenceTechnique;
	}

	@Override
	public void setBodyTechnique(KnownTechnique knownTechnique) {
		if(knownTechnique == null || knownTechnique.getTechnique().getSystem() == Cultivation.System.BODY)
		this.bodyTechnique = knownTechnique;
	}

	@Override
	public void setDivineTechnique(KnownTechnique knownTechnique) {
		if(knownTechnique == null || knownTechnique.getTechnique().getSystem() == Cultivation.System.DIVINE)
		this.divineTechnique = knownTechnique;
	}

	@Override
	public void setEssenceTechnique(KnownTechnique knownTechnique) {
		if(knownTechnique == null || knownTechnique.getTechnique().getSystem() == Cultivation.System.ESSENCE)
		this.essenceTechnique = knownTechnique;
	}

	@Override
	public KnownTechnique getTechniqueBySystem(Cultivation.System system) {
		switch (system) {
			case BODY:
				return this.bodyTechnique;
			case DIVINE:
				return this.divineTechnique;
			case ESSENCE:
				return this.essenceTechnique;
		}
		return null;
	}

	@Override
	public TechniqueModifiers getOverallModifiers() {
		TechniqueModifiers tm = new TechniqueModifiers(0, 0, 0, 0, 0, 0);
		if (this.bodyTechnique != null)
			tm = tm.add(bodyTechnique.getModifiers());
		if (this.essenceTechnique != null)
			tm = tm.add(essenceTechnique.getModifiers());
		if (this.divineTechnique != null)
			tm = tm.add(divineTechnique.getModifiers());
		return tm;
	}

	@Override
	public List<Skill> getTechniqueSkills() {
		List<Skill> knownSkills = new ArrayList<>();
		if (this.bodyTechnique != null) {
			knownSkills.add(Skills.CULTIVATE_BODY);
			knownSkills.addAll(bodyTechnique.getKnownSkills());
		}
		if (this.essenceTechnique != null) {
			knownSkills.add(Skills.CULTIVATE_ESSENCE);
			knownSkills.addAll(essenceTechnique.getKnownSkills());
		}
		if (this.divineTechnique != null) {
			knownSkills.add(Skills.CULTIVATE_DIVINE);
			knownSkills.addAll(divineTechnique.getKnownSkills());
		}
		return knownSkills;
	}

	@Override
	public List<PotionEffect> getTechniquesEffects() {
		List<PotionEffect> effects = new ArrayList<>();
		if(this.getBodyTechnique() != null) {
			effects.addAll(this.getBodyTechnique().getTechniqueEffects());
		}
		if(this.getDivineTechnique() != null) {
			effects.addAll(this.getDivineTechnique().getTechniqueEffects());
		}
		if(this.getEssenceTechnique() != null) {
			effects.addAll(this.getEssenceTechnique().getTechniqueEffects());
		}
		return effects;
	}

	@Override
	public void copyFrom(ICultTech cultTech) {
		this.bodyTechnique = cultTech.getBodyTechnique();
		this.divineTechnique = cultTech.getDivineTechnique();
		this.essenceTechnique = cultTech.getEssenceTechnique();
	}

	@Override
	public boolean hasElement(Element element) {
		boolean contains = false;
		if (this.bodyTechnique != null) {
			if (this.bodyTechnique.getTechnique().getElements().contains(element)) contains = true;
		} else if (this.divineTechnique != null) {
			if (this.divineTechnique.getTechnique().getElements().contains(element)) contains = true;
		} else if (this.essenceTechnique != null) {
			if (this.essenceTechnique.getTechnique().getElements().contains(element)) contains = true;
		}
		return contains;
	}

	@Override
	public double getScanFactor(double divineModifier) {
		if (this.divineTechnique.getTechnique() instanceof DivineTechnique) {
			return divineModifier * ((DivineTechnique) this.divineTechnique.getTechnique()).scanFactor * this.divineTechnique.getReleaseFactor();
		}
		return divineModifier;
	}

	@Override
	public double getResistFactor(double divineModifier) {
		if (this.divineTechnique.getTechnique() instanceof DivineTechnique) {
			return divineModifier * (1 + ((DivineTechnique) this.divineTechnique.getTechnique()).resistFactor * this.divineTechnique.getReleaseFactor());
		}
		return divineModifier;
	}
}
