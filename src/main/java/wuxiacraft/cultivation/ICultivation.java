package wuxiacraft.cultivation;

import javax.annotation.Nonnull;

public interface ICultivation {
	double getEnergy();

	void setEnergy(double energy);

	@Nonnull
	Cultivation.SystemStats getStatsBySystem(CultivationLevel.System system);

	void copyFrom(ICultivation cultivation);

	double getEssenceModifier();

	double getBodyModifier();

	double getDivineModifier();
}
