package com.airesnor.wuxiacraft.dimensions;

import com.airesnor.wuxiacraft.dimensions.ChunkGen.EarthChunkGen;
import com.airesnor.wuxiacraft.dimensions.biomes.BiomeProviderEarth;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class DimensionEarth extends WorldProvider {

    @Override
    protected void init() {
        this.biomeProvider = new BiomeProviderEarth(this.world.getSeed());
    }

    @Override
    public DimensionType getDimensionType() {
        return WuxiaDimensions.EARTH;
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new EarthChunkGen(this.world, this.world.getSeed());
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public boolean doesXZShowFog(int x, int z) {
        return true;
    }

    @Override
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        return new Vec3d(15d/255d, 25d/255d, 31d/255d);
    }

    @Override
    public float getCloudHeight() {
        return 255.0f;
    }

    @Override
    protected void generateLightBrightnessTable() {
        float f = 0.5f;
        for(int i = 0; i <= 15; i++) {
            float f1 = 1.0f - (float)i/15.0f;
            this.lightBrightnessTable[i] = (1.0f - f1) / (f1 * 3.0f + 1.0f) * (1.0f - f) + f;
        }
    }
}
