package com.airesnor.wuxiacraft.cultivation;

public interface IBarrier {

    //Barrier

    void setBarrierAmount(double amount);

    void addBarrierAmount(double amount);

    void removeBarrierAmount(double amount);

    double getBarrierAmount();

    void setBarrierBroken(boolean barrierBroken);

    boolean isBarrierBroken();

    void setBarrierHits(int hits);

    void stepBarrierHits();

    int getBarrierHits();

    //void setBarrierMaxAmount(float amount);

    double getBarrierMaxAmount(ICultivation cultivation);

    //Barrier Regen

    //void setBarrierRegenRate(float regenRate);

    double getBarrierRegenRate(ICultivation cultivation);

    void setBarrierRegenActive(boolean regenActive);

    boolean isBarrierRegenActive();

    //Barrier Cooldown

    void setBarrierCooldown(int cooldown);

    void addBarrierCooldown(int cooldown);

    void removeBarrierCooldown(int cooldown);

    int getBarrierCooldown();

    void setBarrierMaxCooldown(int maxCooldown);

    int getBarrierMaxCooldown();

    //Barrier Toggling

    void setBarrierActive(boolean active);

    boolean isBarrierActive();

    void copyFrom(IBarrier barrier);
}
