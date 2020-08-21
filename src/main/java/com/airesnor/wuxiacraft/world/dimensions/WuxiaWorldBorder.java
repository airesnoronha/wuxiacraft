package com.airesnor.wuxiacraft.world.dimensions;

import net.minecraft.world.border.WorldBorder;

public class WuxiaWorldBorder extends WorldBorder {

    public WuxiaWorldBorder(double centerX, double centerY, double damage, double damageBuffer, int warningTime, int warningDistance) {
        super();
        this.setCenter(centerX, centerY);
        this.setDamageAmount(damage);
        this.setDamageBuffer(damageBuffer);
        this.setWarningTime(warningTime);
        this.setWarningDistance(warningDistance);
    }
}
