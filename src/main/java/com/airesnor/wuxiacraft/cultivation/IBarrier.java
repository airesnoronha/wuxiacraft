package com.airesnor.wuxiacraft.cultivation;

public interface IBarrier {

    //Barrier

    void setBarrierAmount(float amount);

    void addBarrierAmount(float amount);

    void removeBarrierAmount(float amount);

    float getBarrierAmount();

    void setBarrierBroken(boolean barrierBroken);

    boolean isBarrierBroken();

    void setBarrierHits(int hits);

    void stepBarrierHits();

    int getBarrierHits();

    void setBarrierMaxAmount(float amount);

    float getBarrierMaxAmount();

    //Barrier Regen

    void setBarrierRegenRate(float regenRate);

    float getBarrierRegenRate();

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
