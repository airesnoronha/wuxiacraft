package com.airesnor.wuxiacraft.world.dimensions;

import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class WuxiaDimensions {

    public static final DimensionType MINING = DimensionType.register("Mining", "_mining", WuxiaCraftConfig.DIMENSION_MINING, DimensionMining.class, false);
    public static final DimensionType FIRE = DimensionType.register("Fire", "_fire", WuxiaCraftConfig.DIMENSION_FIRE, DimensionFire.class, false);
    public static final DimensionType EARTH = DimensionType.register("Earth", "_earth", WuxiaCraftConfig.DIMENSION_EARTH, DimensionEarth.class, false);
    public static final DimensionType METAL = DimensionType.register("Metal", "_metal", WuxiaCraftConfig.DIMENSION_METAL, DimensionMetal.class, false);
    public static final DimensionType WATER = DimensionType.register("Water", "_water", WuxiaCraftConfig.DIMENSION_WATER, DimensionWater.class, false);
    public static final DimensionType WOOD = DimensionType.register("Wood", "_wood", WuxiaCraftConfig.DIMENSION_WOOD, DimensionWood.class, false);

    public static void registerDimensions() {
        DimensionManager.registerDimension(WuxiaDimensions.MINING.getId(), MINING);
        DimensionManager.registerDimension(WuxiaDimensions.FIRE.getId(), FIRE);
        DimensionManager.registerDimension(WuxiaDimensions.EARTH.getId(), EARTH);
        DimensionManager.registerDimension(WuxiaDimensions.METAL.getId(), METAL);
        DimensionManager.registerDimension(WuxiaDimensions.WATER.getId(), WATER);
        DimensionManager.registerDimension(WuxiaDimensions.WOOD.getId(), WOOD);
    }
}
