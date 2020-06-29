package com.airesnor.wuxiacraft.dimensions.worldtypes;

import com.airesnor.wuxiacraft.dimensions.genlayer.GenLayerWuxia;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;

public class WorldTypeWuxia extends WorldType {

    public WorldTypeWuxia() {
        super("Wuxia");
    }

    @Override
    public GenLayer getBiomeLayer(long worldSeed, GenLayer parentLayer, ChunkGeneratorSettings chunkSettings) {
        return new GenLayerWuxia(worldSeed);
    }
}
