package com.airesnor.wuxiacraft.world.dimensions;

import net.minecraft.world.border.WorldBorder;

public class WuxiaWorldBorder extends WorldBorder {

    private int worldSize;
    //private double diameter = 6.0E7D;
    private double diameter;
    private double centerX;
    private double centerZ;
    private int warningDistance;
    private int warningTime;
    private double damageBuffer;
    private double damageAmount;

    public WuxiaWorldBorder(int worldSize) {
        super();
        this.worldSize = worldSize;
        this.diameter = (double)this.worldSize * 2.0D;
        this.centerX = 0.0D;
        this.centerZ = 0.0D;
        this.warningDistance = 5;
        this.warningTime = 10;
        this.damageBuffer = 5.0D;
        this.damageAmount = 100.0D;
    }

    @Override
    public double getCenterX() {
        return centerX;
    }

    @Override
    public double getCenterZ() {
        return centerZ;
    }

    @Override
    public int getSize() {
        return worldSize;
    }

    @Override
    public double getDiameter() {
        return this.diameter;
    }

    @Override
    public int getWarningDistance() {
        return warningDistance;
    }

    @Override
    public int getWarningTime() {
        return warningTime;
    }

    @Override
    public double getDamageBuffer() {
        return damageBuffer;
    }

    @Override
    public double getDamageAmount() {
        return damageAmount;
    }
}
