package com.airesnor.wuxiacraft.dimensions;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class WuxiaDimensions {

    public static final DimensionType MINING = DimensionType.register("Mining", "_mining", 2, DimensionMining.class, false);
    public static final DimensionType FIRE = DimensionType.register("Fire", "_fire", 4, DimensionFire.class, false);
    public static final DimensionType EARTH = DimensionType.register("Earth", "_earth", 5, DimensionEarth.class, false);
    public static final DimensionType METAL = DimensionType.register("Metal", "_metal", 6, DimensionMetal.class, false);
    public static final DimensionType WATER = DimensionType.register("Water", "_water", 7, DimensionWater.class, false);
    public static final DimensionType WOOD = DimensionType.register("Wood", "_wood", 8, DimensionWood.class, false);

    public static void registerDimensions(){
        DimensionManager.registerDimension(2, MINING);
        DimensionManager.registerDimension(4, FIRE);
        DimensionManager.registerDimension(5, EARTH);
        DimensionManager.registerDimension(6, METAL);
        DimensionManager.registerDimension(7, WATER);
        DimensionManager.registerDimension(8, WOOD);
    }
}
