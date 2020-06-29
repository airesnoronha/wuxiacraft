package com.airesnor.wuxiacraft.dimensions.worldtypes;

import com.airesnor.wuxiacraft.dimensions.biomes.WuxiaBiomes;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;

public class WorldTypeMining extends WorldType {

    public WorldTypeMining() {
        super("Mining");
    }

    @Override
    public BiomeProvider getBiomeProvider(World world) {
        return new BiomeProviderSingle(WuxiaBiomes.MINING);
    }
}
