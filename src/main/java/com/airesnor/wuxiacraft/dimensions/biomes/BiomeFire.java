package com.airesnor.wuxiacraft.dimensions.biomes;

import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public class BiomeFire extends Biome {

    public BiomeFire() {
        super(new BiomeProperties("Fire").setBaseHeight(1.0f).setHeightVariation(1.0f).setTemperature(3.0f).setRainDisabled().setWaterColor(16711680));

        topBlock = Blocks.DIRT.getDefaultState();
        fillerBlock = Blocks.MAGMA.getDefaultState();

        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();

        this.spawnableCreatureList.add(new SpawnListEntry(WanderingCultivator.class, 8, 1, 5));
    }
}
