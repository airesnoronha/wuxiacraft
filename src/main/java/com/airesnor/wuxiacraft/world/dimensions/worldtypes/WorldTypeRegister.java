package com.airesnor.wuxiacraft.world.dimensions.worldtypes;

import net.minecraft.world.WorldType;

public class WorldTypeRegister {

    public static WorldType WUXIA;
    public static WorldType MINING;
    public static WorldType EXTREMEQI;

    public static void registerWorldTypes() {
        WUXIA = new WorldTypeWuxia();
        MINING = new WorldTypeMining();
        EXTREMEQI = new WorldTypeExtremeQi();
    }
}
