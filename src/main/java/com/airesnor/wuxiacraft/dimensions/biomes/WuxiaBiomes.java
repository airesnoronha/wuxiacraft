package com.airesnor.wuxiacraft.dimensions.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class WuxiaBiomes {

    public static final Biome MINING = new BiomeMining();
    public static final Biome METAL = new BiomeMetal();
    public static final Biome FIRE = new BiomeFire();
    public static final Biome WOOD = new BiomeWood();
    public static final Biome WATER = new BiomeWater();
    public static final Biome EARTH = new BiomeEarth();
    public static final Biome EXTREMEQI = new BiomeExtremeQi();

    private static Biome registerBiome(Biome biome, String biomeName, BiomeType biomeType, boolean spawnOverworld, Type... types) {
        biome.setRegistryName(biomeName);
        ForgeRegistries.BIOMES.register(biome);
        System.out.println("The biomes have been registered");
        BiomeDictionary.addTypes(biome, types);

        //This makes it so that the overworld gets the biome added then it spawns it
        if(spawnOverworld) {
            BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(biome, 5));
        }
        BiomeManager.addSpawnBiome(biome);
        System.out.println("The biomes have been added");
        return biome;
    }

    public static void registerBiomes() {
        registerBiome(MINING, "Mining", BiomeType.COOL, false, Type.MAGICAL, Type.DRY, Type.MOUNTAIN);
        registerBiome(METAL, "Metal", BiomeType.ICY, false, Type.MAGICAL, Type.DRY, Type.MOUNTAIN, Type.COLD);
        registerBiome(WOOD, "Wood", BiomeType.WARM, false, Type.MAGICAL, Type.DENSE, Type.FOREST, Type.PLAINS, Type.WATER);
        registerBiome(WATER, "Water", BiomeType.COOL, false, Type.MAGICAL, Type.OCEAN, Type.BEACH);
        registerBiome(FIRE, "Fire", BiomeType.DESERT, false, Type.MAGICAL, Type.HOT);
        registerBiome(EARTH, "Earth", BiomeType.WARM, false, Type.MAGICAL, Type.MOUNTAIN, Type.HILLS, Type.LUSH, Type.PLAINS, Type.DENSE);
        registerBiome(EXTREMEQI, "ExtremeQi", BiomeType.WARM, true, Type.MAGICAL, Type.MOUNTAIN, Type.LUSH, Type.DENSE);
    }
}
