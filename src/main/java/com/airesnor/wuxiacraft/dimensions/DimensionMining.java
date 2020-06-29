package com.airesnor.wuxiacraft.dimensions;

import com.airesnor.wuxiacraft.dimensions.ChunkGen.MiningChunkGen;
import com.airesnor.wuxiacraft.dimensions.biomes.BiomeProviderMining;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.IChunkGenerator;

public class DimensionMining extends WorldProvider {

    public static int worldBorderSize;

    @Override
    protected void init() {
        this.biomeProvider = new BiomeProviderMining(this.world.getSeed());
        this.worldBorderSize = 3000000;
    }

    @Override
    public DimensionType getDimensionType() {
        return WuxiaDimensions.MINING;
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new MiningChunkGen(this.world, true, this.world.getSeed());
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
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
        return new Vec3d(190d/255d, 204d/255d, 78d/255d);
    }

    @Override
    public float getCloudHeight() {
        return 255.0f;
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double z, double rotation) {
        return true;
    }

    @Override
    public WorldBorder createWorldBorder() {
        return new WuxiaWorldBorder(this.worldBorderSize);
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
