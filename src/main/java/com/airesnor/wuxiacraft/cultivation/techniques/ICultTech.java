package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import net.minecraft.potion.PotionEffect;

import java.util.List;

public interface ICultTech {

	void progress(double amount, Cultivation.System system);

	boolean addTechnique(Technique technique);

	KnownTechnique getBodyTechnique();

	KnownTechnique getDivineTechnique();

	KnownTechnique getEssenceTechnique();

	void setBodyTechnique(KnownTechnique knownTechnique);

	void setDivineTechnique(KnownTechnique knownTechnique);

	void setEssenceTechnique(KnownTechnique knownTechnique);

	KnownTechnique getTechniqueBySystem(Cultivation.System system);

	List<Skill> getTechniqueSkills();

	List<PotionEffect> getTechniquesEffects();

	TechniqueModifiers getOverallModifiers();

	void copyFrom(ICultTech cultTech);

	boolean hasElement(Element element);

	double getScanFactor(double divineModifier);

	double getResistFactor(double divineModifier);

}
