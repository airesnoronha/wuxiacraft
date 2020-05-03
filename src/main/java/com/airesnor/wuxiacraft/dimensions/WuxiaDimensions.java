package com.airesnor.wuxiacraft.dimensions;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

import java.awt.*;

public class WuxiaDimensions {

    public static final DimensionType MINING = DimensionType.register("Mining", "_mining", 2, DimensionMining.class, false);
    public static final DimensionType FIRE = DimensionType.register("Fire", "_fire", 4, DimensionFire.class, false);
    public static final DimensionType EARTH = DimensionType.register("Earth", "_earth", 5, DimensionEarth.class, false);
    public static final DimensionType METAL = DimensionType.register("Metal", "_metal", 6, DimensionMetal.class, false);
    public static final DimensionType WATER = DimensionType.register("Water", "_water", 7, DimensionWater.class, false);
    public static final DimensionType WOOD = DimensionType.register("Wood", "_wood", 8, DimensionWood.class, false);

    public static void registerDimensions() {
        DimensionManager.registerDimension(WuxiaDimensions.MINING.getId(), MINING);
        DimensionManager.registerDimension(WuxiaDimensions.FIRE.getId(), FIRE);
        DimensionManager.registerDimension(WuxiaDimensions.EARTH.getId(), EARTH);
        DimensionManager.registerDimension(WuxiaDimensions.METAL.getId(), METAL);
        DimensionManager.registerDimension(WuxiaDimensions.WATER.getId(), WATER);
        DimensionManager.registerDimension(WuxiaDimensions.WOOD.getId(), WOOD);
    }
}
