package com.airesnor.wuxiacraft.dimensions.biomes;

import com.airesnor.wuxiacraft.blocks.WuxiaBlocks;
import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public class BiomeWater extends Biome {

    public BiomeWater() {
        super(new BiomeProperties("Water").setBaseHeight(-1.0f).setHeightVariation(1.2f).setRainfall(1.0f).setBaseBiome("ocean"));

        topBlock = Blocks.PACKED_ICE.getDefaultState();
        fillerBlock = Blocks.PACKED_ICE.getDefaultState();

        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();

        this.spawnableCreatureList.add(new SpawnListEntry(WanderingCultivator.class, 8, 1, 5));
    }
}
