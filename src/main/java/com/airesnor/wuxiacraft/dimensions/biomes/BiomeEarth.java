package com.airesnor.wuxiacraft.dimensions.biomes;

import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;

import java.util.Random;

public class BiomeEarth extends Biome {

    protected static final WorldGenAbstractTree TREE = new WorldGenBigTree(true);

    public BiomeEarth() {
        super(new BiomeProperties("Earth").setBaseHeight(1.5f).setHeightVariation(0.8f).setTemperature(1.0f).setRainfall(0.1f).setWaterColor(9140823));

        topBlock = Blocks.GRASS.getDefaultState();
        fillerBlock = Blocks.DIRT.getDefaultState();
        this.decorator.treesPerChunk = 3;

        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();

        this.spawnableCreatureList.add(new SpawnListEntry(WanderingCultivator.class, 8, 1, 5));
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return TREE;
    }
}
