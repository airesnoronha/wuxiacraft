package com.airesnor.wuxiacraft.cultivation;

import com.airesnor.wuxiacraft.utils.MathUtils;

public class Foundation implements IFoundation {

	private long agility; // move speed
	private long constitution; // max health
	private long dexterity; // attack speed
	private long resistance; // armor
	private long spirit; // max energy
	private long strength; //attack damage

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

	public static double getLevelMaxProgress(long level) {
		if(level >= 0) {
			return 40 * (Math.pow(1.02, Math.floor(level / 10.0) + level)); //At each 10 levels in increases another level
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
	public long getAgility() {
		return agility;
	}

	@Override
	public void setAgility(long agility) {
		this.agility = agility;
	}

	@Override
	public long getConstitution() {
		return constitution;
	}

	@Override
	public void setConstitution(long constitution) {
		this.constitution = constitution;
	}

	@Override
	public long getDexterity() {
		return dexterity;
	}

	@Override
	public void setDexterity(long dexterity) {
		this.dexterity = dexterity;
	}

	@Override
	public long getResistance() {
		return resistance;
	}

	@Override
	public void setResistance(long resistance) {
		this.resistance = resistance;
	}

	@Override
	public long getSpirit() {
		return spirit;
	}

	@Override
	public void setSpirit(long spirit) {
		this.spirit = spirit;
	}

	@Override
	public long getStrength() {
		return strength;
	}

	@Override
	public void setStrength(long strength) {
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
		boolean levelelUp = false;
		while(this.agilityProgress >= getLevelMaxProgress(this.agility)) {
			this.agilityProgress -= getLevelMaxProgress(this.agility);
			this.agility++;
			levelelUp = true;
		}
		return levelelUp;
	}

	@Override
	public boolean addConstitutionProgress(double amount) {
		this.constitutionProgress += amount;
		boolean levelelUp = false;
		while(this.constitutionProgress >= getLevelMaxProgress(this.constitution)) {
			this.constitutionProgress -= getLevelMaxProgress(this.constitution);
			this.constitution++;
			levelelUp = true;
		}
		return levelelUp;
	}

	@Override
	public boolean addDexterityProgress(double amount) {
		this.dexterityProgress += amount;
		boolean levelelUp = false;
		while(this.dexterityProgress >= getLevelMaxProgress(this.dexterity)) {
			this.dexterityProgress -= getLevelMaxProgress(this.dexterity);
			this.dexterity++;
			levelelUp = true;
		}
		return levelelUp;
	}

	@Override
	public boolean addResistanceProgress(double amount) {
		this.resistanceProgress += amount;
		boolean levelelUp = false;
		while(this.resistanceProgress >= getLevelMaxProgress(this.resistance)) {
			this.resistanceProgress -= getLevelMaxProgress(this.resistance);
			this.resistance++;
			levelelUp = true;
		}
		return levelelUp;
	}

	@Override
	public boolean addSpiritProgress(double amount) {
		this.spiritProgress += amount;
		boolean levelelUp = false;
		while(this.spiritProgress >= getLevelMaxProgress(this.spirit)) {
			this.spiritProgress -= getLevelMaxProgress(this.spirit);
			this.spirit++;
			levelelUp = true;
		}
		return levelelUp;
	}

	@Override
	public boolean addStrengthProgress(double amount) {
		this.strengthProgress += amount;
		boolean levelelUp = false;
		while(this.strengthProgress >= getLevelMaxProgress(this.strength)) {
			this.strengthProgress -= getLevelMaxProgress(this.strength);
			this.strength++;
			levelelUp = true;
		}
		return levelelUp;
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
		this.resistanceProgress = foundation.getResistanceProgress();
		this.spirit = foundation.getSpirit();
		this.spiritProgress = foundation.getSpiritProgress();
		this.strength = foundation.getStrength();
		this.strengthProgress = foundation.getStrengthProgress();
	}

	@Override
	public void applyDeathPunishment(ICultivation cultivation) {
		long toLose = (long) Math.max(4, (cultivation.getStrengthIncrease()* 0.3)); //lose around 15 % of what it would need to level up
		this.agility = Math.max(this.agility-toLose, 0);
		this.constitution = Math.max(this.constitution-toLose, 0);
		this.dexterity = Math.max(this.dexterity-toLose, 0);
		this.resistance = Math.max(this.resistance-toLose, 0);
		this.spirit = Math.max(this.spirit-toLose, 0);
		this.strength = Math.max(this.strength-toLose, 0);
		this.agilityProgress = 0;
		this.constitutionProgress = 0;
		this.dexterityProgress = 0;
		this.resistanceProgress = 0;
		this.spiritProgress = 0;
		this.strengthProgress = 0;
	}

	@Override
	public void keepMaxLevel(ICultivation cultivation) {
		long maxStat = cultivation.getCurrentLevel().getFoundationMaxStat(cultivation.getCurrentSubLevel());
		if(this.agility >= maxStat) {
			this.agility = maxStat;
			cultivation.addProgress(this.agilityProgress, false);
			this.agilityProgress = 0;
		}
		if(this.constitution >= maxStat) {
			this.constitution = maxStat;
			cultivation.addProgress(this.constitutionProgress, false);
			this.constitutionProgress = 0;
		}
		if(this.dexterity >= maxStat) {
			this.dexterity = maxStat;
			cultivation.addProgress(this.dexterityProgress, false);
			this.dexterityProgress = 0;
		}
		if(this.resistance >= maxStat) {
			this.resistance = maxStat;
			cultivation.addProgress(this.resistanceProgress, false);
			this.resistanceProgress = 0;
		}
		if(this.spirit >= maxStat) {
			this.spirit = maxStat;
			cultivation.addProgress(this.spiritProgress, false);
			this.spiritProgress = 0;
		}
		if(this.strength >= maxStat) {
			this.strength = maxStat;
			cultivation.addProgress(this.spiritProgress, false);
			this.strengthProgress = 0;
		}
	}
}
