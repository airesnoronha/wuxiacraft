package wuxiacraft.cultivation;

import net.minecraft.nbt.CompoundTag;

public interface ICultivation {


	double getHealth();

	void setHealth(double health);

	SystemContainer getSystemData(Cultivation.System system);

	double getMaxHealth();

	double getStrength();

	double getAgility();

	CompoundTag serialize();

	void deserialize(CompoundTag tag);
}
