package com.airesnor.wuxiacraft.world.dimensions.biomes;

import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class BiomeMining extends Biome {

    protected static final WorldGenAbstractTree TREE = new WorldGenBigTree(true);

    public BiomeMining() {
        super(new BiomeProperties("Mining").setBaseHeight(1.0f).setHeightVariation(0.9f).setTemperature(0.2f).setRainfall(0.3f).setWaterColor(9140823));

        topBlock = Blocks.DIRT.getDefaultState();
        fillerBlock = Blocks.STONE.getDefaultState();
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
