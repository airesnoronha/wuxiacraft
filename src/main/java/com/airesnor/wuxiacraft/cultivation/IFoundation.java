package com.airesnor.wuxiacraft.cultivation;

public interface IFoundation {
	double getAgilityModifier();

	double getConstitutionModifier();

	double getDexterityModifier();

	double getResistanceModifier();

	double getSpiritModifier();

	double getStrengthModifier();

	int getAgility();

	void setAgility(int agility);

	int getConstitution();

	void setConstitution(int constitution);

	int getDexterity();

	void setDexterity(int dexterity);

	int getResistance();

	void setResistance(int resistance);

	int getSpirit();

	void setSpirit(int spirit);

	int getStrength();

	void setStrength(int strength);

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

	void applyDeathPunishment();
}
