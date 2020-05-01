package com.airesnor.wuxiacraft.cultivation;

public interface IFoundation {
	double getAgilityModifier();

	double getConstitutionModifier();

	double getDexterityModifier();

	double getResistanceModifier();

	double getSpiritModifier();

	double getStrengthModifier();

	long getAgility();

	void setAgility(long agility);

	long getConstitution();

	void setConstitution(long constitution);

	long getDexterity();

	void setDexterity(long dexterity);

	long getResistance();

	void setResistance(long resistance);

	long getSpirit();

	void setSpirit(long spirit);

	long getStrength();

	void setStrength(long strength);

	double getAgilityProgress();

	void setAgilityProgress(double agilityProgress);

	double getConstitutionProgress();

	void setConstitutionProgress(double constitutionProgress);

	double getDexterityProgress();

	void setDexterityProgress(double dexterityProgress);

	double getResistanceProgress();

	void setResistanceProgress(double resistanceProgress);

	double getSpiritProgress();

	void setSpiritProgress(double spiritProgress);

	double getStrengthProgress();

	void setStrengthProgress(double strengthProgress);

	boolean addAgilityProgress(double amount);

	boolean addConstitutionProgress(double amount);

	boolean addDexterityProgress(double amount);

	boolean addResistanceProgress(double amount);

	boolean addSpiritProgress(double amount);

	boolean addStrengthProgress(double amount);

	boolean addSelectedProgress(double amount);

	void selectAttribute(int attribute);

	int getSelectedAttribute();

	void copyFrom(IFoundation foundation);

	void applyDeathPunishment(ICultivation cultivation);

	void keepMaxLevel(ICultivation cultivation);
}
