package com.airesnor.wuxiacraft.cultivation;

import com.airesnor.wuxiacraft.utils.MathUtils;

public class Foundation implements IFoundation {

	private int agility; // move speed
	private int constitution; // max health
	private int dexterity; // attack speed
	private int resistance; // armor
	private int spirit; // max energy
	private int strength; //attack damage

	private double agilityProgress;
	private double constitutionProgress;
	private double dexterityProgress;
	private double resistanceProgress;
	private double spiritProgress;
	private double strengthProgress;

	private int selectedAttribute; //for automatic cultivate attribute

	public Foundation() {
		this.agility = 0;
		this.constitution = 0;
		this.dexterity = 0;
		this.resistance = 0;
		this.spirit = 0;
		this.strength = 0;
		this.agilityProgress = 0;
		this.constitutionProgress = 0;
		this.dexterityProgress = 0;
		this.resistanceProgress = 0;
		this.spiritProgress = 0;
		this.strengthProgress = 0;
		this.selectedAttribute = -1;
	}

	public static double getLevelMaxProgress(int level) {
		if(level >= 0) {
			return 100 * (Math.pow(1.12, Math.floor(level / 10.0) + level)); //At each 10 levels in increases another level
		}
		return 0;
	}

	@Override
	public double getAgilityModifier() {
		return this.agility + (this.agilityProgress/getLevelMaxProgress(this.agility));
	}

	@Override
	public double getConstitutionModifier() {
		return this.constitution + (this.constitutionProgress/getLevelMaxProgress(this.constitution));
	}

	@Override
	public double getDexterityModifier() {
		return this.dexterity + (this.dexterityProgress/getLevelMaxProgress(this.dexterity));
	}

	@Override
	public double getResistanceModifier() {
		return this.resistance + (this.resistanceProgress/getLevelMaxProgress(this.agility));
	}

	@Override
	public double getSpiritModifier() {
		return this.spirit + (this.spiritProgress/getLevelMaxProgress(this.spirit));
	}

	@Override
	public double getStrengthModifier() {
		return this.strength + (this.strengthProgress/getLevelMaxProgress(this.strength));
	}

	@Override
	public int getAgility() {
		return agility;
	}

	@Override
	public void setAgility(int agility) {
		this.agility = agility;
	}

	@Override
	public int getConstitution() {
		return constitution;
	}

	@Override
	public void setConstitution(int constitution) {
		this.constitution = constitution;
	}

	@Override
	public int getDexterity() {
		return dexterity;
	}

	@Override
	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}

	@Override
	public int getResistance() {
		return resistance;
	}

	@Override
	public void setResistance(int resistance) {
		this.resistance = resistance;
	}

	@Override
	public int getSpirit() {
		return spirit;
	}

	@Override
	public void setSpirit(int spirit) {
		this.spirit = spirit;
	}

	@Override
	public int getStrength() {
		return strength;
	}

	@Override
	public void setStrength(int strength) {
		this.strength = strength;
	}

	@Override
	public double getAgilityProgress() {
		return agilityProgress;
	}

	@Override
	public void setAgilityProgress(double agilityProgress) {
		this.agilityProgress = agilityProgress;
	}

	@Override
	public double getConstitutionProgress() {
		return constitutionProgress;
	}

	@Override
	public void setConstitutionProgress(double constitutionProgress) {
		this.constitutionProgress = constitutionProgress;
	}

	@Override
	public double getDexterityProgress() {
		return dexterityProgress;
	}

	@Override
	public void setDexterityProgress(double dexterityProgress) {
		this.dexterityProgress = dexterityProgress;
	}

	@Override
	public double getResistanceProgress() {
		return resistanceProgress;
	}

	@Override
	public void setResistanceProgress(double resistanceProgress) {
		this.resistanceProgress = resistanceProgress;
	}

	@Override
	public double getSpiritProgress() {
		return spiritProgress;
	}

	@Override
	public void setSpiritProgress(double spiritProgress) {
		this.spiritProgress = spiritProgress;
	}

	@Override
	public double getStrengthProgress() {
		return strengthProgress;
	}

	@Override
	public void setStrengthProgress(double strengthProgress) {
		this.strengthProgress = strengthProgress;
	}

	@Override
	public boolean addAgilityProgress(double amount) {
		this.agilityProgress += amount;
		if(this.agilityProgress >= getLevelMaxProgress(this.agility)) {
			this.agilityProgress -= getLevelMaxProgress(this.dexterity);
			this.agility++;
			return true;
		}
		return false;
	}

	@Override
	public boolean addConstitutionProgress(double amount) {
		this.constitutionProgress += amount;
		if(this.constitutionProgress >= getLevelMaxProgress(this.constitution)) {
			this.constitutionProgress -= getLevelMaxProgress(this.constitution);
			this.constitution++;
			return true;
		}
		return false;
	}

	@Override
	public boolean addDexterityProgress(double amount) {
		this.dexterityProgress += amount;
		if(this.dexterityProgress >= getLevelMaxProgress(this.dexterity)) {
			this.dexterityProgress -= getLevelMaxProgress(this.dexterity);
			this.dexterity++;
			return true;
		}
		return false;
	}

	@Override
	public boolean addResistanceProgress(double amount) {
		this.resistanceProgress += amount;
		if(this.resistanceProgress >= getLevelMaxProgress(this.resistance)) {
			this.resistanceProgress -= getLevelMaxProgress(this.resistance);
			this.resistance++;
			return true;
		}
		return false;
	}

	@Override
	public boolean addSpiritProgress(double amount) {
		this.spiritProgress += amount;
		if(this.spiritProgress >= getLevelMaxProgress(this.spirit)) {
			this.spiritProgress -= getLevelMaxProgress(this.spirit);
			this.spirit++;
			return true;
		}
		return false;
	}

	@Override
	public boolean addStrengthProgress(double amount) {
		this.strengthProgress += amount;
		if(this.strengthProgress >= getLevelMaxProgress(this.strength)) {
			this.strengthProgress -= getLevelMaxProgress(this.strength);
			this.strength++;
			return true;
		}
		return false;
	}

	@Override
	public boolean addSelectedProgress(double amount) {
		switch (this.selectedAttribute) {
			case 0:
				return this.addAgilityProgress(amount);
			case 1:
				return this.addConstitutionProgress(amount);
			case 2:
				return this.addDexterityProgress(amount);
			case 3:
				return this.addResistanceProgress(amount);
			case 4:
				return this.addSpiritProgress(amount);
			case 5:
				return this.addStrengthProgress(amount);
		}
		return false;
	}

	@Override
	public void selectAttribute(int attribute){
		this.selectedAttribute = MathUtils.clamp(attribute, -1, 5);
	}

	@Override
	public int getSelectedAttribute() {
		return selectedAttribute;
	}

	@Override
	public void copyFrom(IFoundation foundation) {
		this.selectedAttribute = foundation.getSelectedAttribute();
		this.agility = foundation.getAgility();
		this.agilityProgress = foundation.getAgilityProgress();
		this.constitution = foundation.getConstitution();
		this.constitutionProgress = foundation.getConstitutionProgress();
		this.dexterity = foundation.getDexterity();
		this.dexterityProgress = foundation.getDexterityProgress();
		this.resistance = foundation.getResistance();
		this.resistanceProgress = foundation.getAgilityProgress();
		this.spirit = foundation.getSpirit();
		this.spiritProgress = foundation.getSpiritProgress();
		this.strength = foundation.getStrength();
		this.strengthProgress = foundation.getStrengthProgress();
	}

	@Override
	public void applyDeathPunishment() {
		this.agility = Math.max(this.agility-1, 0);
		this.constitution = Math.max(this.constitution-1, 0);
		this.dexterity = Math.max(this.dexterity-1, 0);
		this.resistance = Math.max(this.resistance-1, 0);
		this.spirit = Math.max(this.spirit-1, 0);
		this.strength = Math.max(this.strength-1, 0);
		this.agilityProgress = 0;
		this.constitutionProgress = 0;
		this.dexterityProgress = 0;
		this.resistanceProgress = 0;
		this.spiritProgress = 0;
		this.strengthProgress = 0;
	}
}
