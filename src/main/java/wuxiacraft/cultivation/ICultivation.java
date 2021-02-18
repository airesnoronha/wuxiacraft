package wuxiacraft.cultivation;

import wuxiacraft.cultivation.technique.Technique;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICultivation {
	double getEnergy();

	void setEnergy(double energy);

	void advanceRank(CultivationLevel.System system);

	void addBaseToSystem(double amount, CultivationLevel.System system);

	@Nonnull
	SystemStats getStatsBySystem(CultivationLevel.System system);

	@Nullable
	KnownTechnique getTechniqueBySystem(CultivationLevel.System system);

	void addTechnique(Technique technique);

	void setKnownTechnique(Technique technique, double proficiency);

	void copyFrom(ICultivation cultivation);

	double getEssenceModifier();

	double getMaxEnergy(double energy);

	double getBodyModifier();

	double getDivineModifier();
}
