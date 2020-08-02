package com.airesnor.wuxiacraft.world.dimensions.worldtypes;

import com.airesnor.wuxiacraft.world.dimensions.genlayer.GenLayerExtremeQi;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;

public class WorldTypeExtremeQi extends WorldType {

    public WorldTypeExtremeQi() {
        super("ExtremeQi");
    }

    @Override
    public GenLayer getBiomeLayer(long worldSeed, GenLayer parentLayer, ChunkGeneratorSettings chunkSettings) {
        return new GenLayerExtremeQi(worldSeed);
    }
}
