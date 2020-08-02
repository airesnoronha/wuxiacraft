package com.airesnor.wuxiacraft.world.dimensions.genlayer;

import com.airesnor.wuxiacraft.world.dimensions.biomes.WuxiaBiomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerMetal extends GenLayer {

    private Biome[] biomes = {WuxiaBiomes.METAL};

    public GenLayerMetal(long worldSeed) {
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