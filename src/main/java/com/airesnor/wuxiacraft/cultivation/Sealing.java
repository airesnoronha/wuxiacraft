package com.airesnor.wuxiacraft.cultivation;

public class Sealing implements ISealing {

    //Cultivation
    private double progress;
    private CultivationLevel level;
    private int subLevel;
    private double energy;
    private float maxSpeed;
    private float hasteLimit;
    private float jumpLimit;

    //Foundation
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

    //Sealing
    private boolean cultivationSealed;
    private boolean foundationSealed;

    public Sealing() {
        //Cultivation
        this.subLevel = 0;
        this.progress = 0;
        this.level = CultivationLevel.BASE_LEVEL;
        this.energy = 0;
        this.maxSpeed = 5.0f;
        this.hasteLimit = 10.0f;
        this.jumpLimit = 10.0f;

        //Foundation
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

        //Sealing
        this.cultivationSealed = false;
        this.foundationSealed = false;
    }

    //Cultivation

    @Override
    public double getCurrentProgress() {
        return this.progress;
    }

    @Override
    public void setProgress(double amount) {
        this.progress = Math.max(0, amount);
    }

    @Override
    public CultivationLevel getCurrentLevel() {
        return this.level;
    }

    @Override
    public void setCurrentLevel(CultivationLevel level) {
        this.level = level;
    }

    @Override
    public int getCurrentSubLevel() {
        return this.subLevel;
    }

    @Override
    public void setCurrentSubLevel(int subLevel) {
        this.subLevel = subLevel;
    }

    @Override
    public double getEnergy() {
        return this.energy;
    }

    @Override
    public void setEnergy(double amount) {
        this.energy = Math.max(0, amount);
    }

    @Override
    public float getMaxSpeed() {
        return this.maxSpeed;
    }

    @Override
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public float getHasteLimit() {
        return this.hasteLimit;
    }

    @Override
    public void setHasteLimit(float hasteLimit) {
        this.hasteLimit = hasteLimit;
    }

    @Override
    public float getJumpLimit() {
        return this.jumpLimit;
    }

    @Override
    public void setJumpLimit(float jumpLimit) {
        this.jumpLimit = jumpLimit;
    }

    @Override
    public void copyFromCultivation(ICultivation cultivation) {
        this.setCurrentLevel(cultivation.getCurrentLevel());
        this.setCurrentSubLevel(cultivation.getCurrentSubLevel());
        this.setProgress(cultivation.getCurrentProgress());
        this.setEnergy(cultivation.getEnergy());
    }

    //Foundation


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
    public void copyFromFoundation(IFoundation foundation) {
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
    public void copyFromBoth(ICultivation cultivation, IFoundation foundation) {
        //Cultivation
        this.setCurrentLevel(cultivation.getCurrentLevel());
        this.setCurrentSubLevel(cultivation.getCurrentSubLevel());
        this.setProgress(cultivation.getCurrentProgress());
        this.setEnergy(cultivation.getEnergy());
        //Foundation
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
    public void copyFrom(ISealing sealing) {
        this.setCurrentLevel(sealing.getCurrentLevel());
        this.setCurrentSubLevel(sealing.getCurrentSubLevel());
        this.setProgress(sealing.getCurrentProgress());
        this.setEnergy(sealing.getEnergy());
        this.agility = sealing.getAgility();
        this.agilityProgress = sealing.getAgilityProgress();
        this.constitution = sealing.getConstitution();
        this.constitutionProgress = sealing.getConstitutionProgress();
        this.dexterity = sealing.getDexterity();
        this.dexterityProgress = sealing.getDexterityProgress();
        this.resistance = sealing.getResistance();
        this.resistanceProgress = sealing.getResistanceProgress();
        this.spirit = sealing.getSpirit();
        this.spiritProgress = sealing.getSpiritProgress();
        this.strength = sealing.getStrength();
        this.strengthProgress = sealing.getStrengthProgress();
    }

    @Override
    public void setSealed(String type, boolean sealed) {
        if (type.equalsIgnoreCase("cultivation")) {
            this.cultivationSealed = sealed;
        } else if (type.equalsIgnoreCase("foundation")) {
            this.foundationSealed = sealed;
        } else if (type.equalsIgnoreCase("both")) {
            this.cultivationSealed = sealed;
            this.foundationSealed = sealed;
        }
    }

    @Override
    public boolean isBothSealed() {
        return this.cultivationSealed && this.foundationSealed;
    }

    @Override
    public boolean isCultivationSealed() {
        return cultivationSealed;
    }

    @Override
    public boolean isFoundationSealed() {
        return foundationSealed;
    }
}
