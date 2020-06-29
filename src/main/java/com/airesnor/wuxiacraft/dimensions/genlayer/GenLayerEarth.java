package com.airesnor.wuxiacraft.dimensions.genlayer;

import com.airesnor.wuxiacraft.dimensions.biomes.WuxiaBiomes;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerEarth extends GenLayer {

    private Biome[] biomes = {WuxiaBiomes.EARTH, Biomes.PLAINS};

    public GenLayerEarth(long worldSeed) {
        super(worldSeed);
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] int1 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int j = 0; j < areaHeight; j++) {
            for (int i = 0; i < areaWidth; i++) {
                this.initChunkSeed(i + areaX, j + areaY);
                int1[i + j * areaWidth] = Biome.getIdForBiome(this.biomes[nextInt(this.biomes.length)]);
            }
        }
        return int1;
    }
}