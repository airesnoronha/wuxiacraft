package com.airesnor.wuxiacraft.cultivation;

public interface ISealing {

    //Cultivation

    double getCurrentProgress();

    void setProgress(double amount);

    CultivationLevel getCurrentLevel();

    void setCurrentLevel(CultivationLevel level);

    int getCurrentSubLevel();

    void setCurrentSubLevel(int subLevel);

    double getEnergy();

    void setEnergy(double amount);

    float getMaxSpeed();

    void setMaxSpeed(float maxSpeed);

    float getHasteLimit();

    void setHasteLimit(float hasteLimit);

    float getJumpLimit();

    void setJumpLimit(float jumpLimit);

    void copyFromCultivation(ICultivation cultivation);

    //Foundation

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

    void copyFromFoundation(IFoundation foundation);

    //Both

    void copyFromBoth(ICultivation cultivation, IFoundation foundation);

    void copyFrom(ISealing sealing);

    //Sealing

    void setSealed(String type, boolean sealed);

    boolean isBothSealed();

    boolean isCultivationSealed();

    boolean isFoundationSealed();
}
