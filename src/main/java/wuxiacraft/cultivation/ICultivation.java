package wuxiacraft.cultivation;

import net.minecraft.entity.LivingEntity;
import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.cultivation.technique.TechniqueModifiers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICultivation {
	double getEnergy();

	void addEnergy(double amount);

	void setEnergy(double energy);

	int getTickerTime();

	void advanceTimer();

	void advanceRank(CultivationLevel.System system);

	void addBaseToSystem(double amount, CultivationLevel.System system);

	double getHP();

	void setHP(double HP);

	@Nonnull
	SystemStats getStatsBySystem(CultivationLevel.System system);

	@Nullable
	KnownTechnique getTechniqueBySystem(CultivationLevel.System system);

	void addTechnique(Technique technique);

	void setKnownTechnique(Technique technique, double proficiency);

	double getResistanceToElement(Element element);

	TechniqueModifiers getFinalModifiers();

	void copyFrom(ICultivation cultivation);

	double getEssenceModifier();

	double getMaxEnergy();

	double getBodyModifier();

	double getDivineModifier();

	void calculateFinalModifiers();
}
