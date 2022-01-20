package wuxiacraft.cultivation;

public interface ICultivation {


	double getHealth();

	void setHealth(double health);

	Cultivation.SystemContainer getSystemData(Cultivation.System system);
}
