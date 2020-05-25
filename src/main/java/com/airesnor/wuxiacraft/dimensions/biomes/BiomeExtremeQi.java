package com.airesnor.wuxiacraft.dimensions.biomes;

import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;

import java.util.Random;

public class BiomeExtremeQi extends Biome {

    protected static final WorldGenAbstractTree TREE = new WorldGenBigTree(true);

    public BiomeExtremeQi() {
        super(new BiomeProperties("ExtremeQi").setBaseHeight(2.0f).setHeightVariation(1.0f).setTemperature(1.0f).setRainfall(0.5f).setWaterColor(16777215));

        topBlock = Blocks.GRASS.getDefaultState();
        fillerBlock = Blocks.DIRT.getDefaultState();
        this.decorator.treesPerChunk = 2;

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
