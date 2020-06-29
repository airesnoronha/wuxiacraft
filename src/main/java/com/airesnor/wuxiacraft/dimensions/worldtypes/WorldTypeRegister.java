package com.airesnor.wuxiacraft.dimensions.worldtypes;

import net.minecraft.world.WorldType;

public class WorldTypeRegister {

    public static WorldType WUXIA;
    public static WorldType MINING;

    public static void registerWorldTypes() {
        WUXIA = new WorldTypeWuxia();
        MINING = new WorldTypeMining();
    }
}
