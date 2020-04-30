package com.airesnor.wuxiacraft.world;

import com.airesnor.wuxiacraft.blocks.WuxiaBlocks;
import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGen implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
			case 0:
				generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
				break;
			case 2:
				generateMiningWorld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider, BlockMatcher.forBlock(Blocks.STONE));
				break;
			case 4:
				generateFireWorld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider, BlockMatcher.forBlock(WuxiaBlocks.FIERY_STONE));
				break;
			case 5:
				generateEarthWorld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider, BlockMatcher.forBlock(Blocks.DIRT));
				break;
			case 6:
				generateMetalWorld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider, BlockMatcher.forBlock(WuxiaBlocks.METALLIC_STONE));
				break;
			case 7:
				generateWaterWorld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider, BlockMatcher.forBlock(WuxiaBlocks.ICY_STONE));
				break;
			case 8:
				generateWoodWorld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider, BlockMatcher.forBlock(Blocks.STONE));
				break;
		}

	}

	private void generateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		generateOre(WuxiaBlocks.NATURAL_ODDITY_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 30, 4, 4);
		generateOre(WuxiaBlocks.WEAK_LIFE_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 20, 50, 5, 7);
		generateOre(WuxiaBlocks.SOUL_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 40, 4, 6);
		generateOre(WuxiaBlocks.PRIMORDIAL_STONE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 30, 4, 5);
		generateOre(WuxiaBlocks.FIVE_ELEMENT_PURE_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 20, 3, 3);
		generateOre(WuxiaBlocks.PURE_QI_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 20, 3, 3);
		generateOre(WuxiaBlocks.EARTH_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 2, 2);
	}

	private void generateFireWorld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, Predicate<IBlockState> replaceWithOre) {
		generateOre(Blocks.COAL_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 80, 12, 8, replaceWithOre);
		generateOre(Blocks.IRON_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 80, 4, 6, replaceWithOre);
		generateOre(Blocks.GOLD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 30, 50, 4, 5, replaceWithOre);
		generateOre(Blocks.DIAMOND_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 20, 3, 3, replaceWithOre);
		generateOre(Blocks.EMERALD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 15, 3, 3, replaceWithOre);

		generateOre(WuxiaBlocks.NATURAL_ODDITY_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 50, 4, 4, replaceWithOre);
		generateOre(WuxiaBlocks.PURE_QI_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 20, 40, 5, 7, replaceWithOre);
		generateOre(WuxiaBlocks.EARTH_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 30, 4, 6, replaceWithOre);
		generateOre(WuxiaBlocks.SKY_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 25, 4, 5, replaceWithOre);
		generateOre(WuxiaBlocks.HEAVENLY_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 20, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.RAINBOW_LAW_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.SKY_AND_EARTH_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 2, 2, replaceWithOre);
	}

	private void generateWaterWorld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, Predicate<IBlockState> replaceWithOre) {
		generateOre(Blocks.COAL_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 80, 12, 8, replaceWithOre);
		generateOre(Blocks.IRON_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 80, 4, 6, replaceWithOre);
		generateOre(Blocks.GOLD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 30, 50, 4, 5, replaceWithOre);
		generateOre(Blocks.DIAMOND_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 20, 3, 3, replaceWithOre);
		generateOre(Blocks.EMERALD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 15, 3, 3, replaceWithOre);

		generateOre(WuxiaBlocks.NATURAL_ODDITY_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 50, 4, 4, replaceWithOre);
		generateOre(WuxiaBlocks.PURE_QI_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 20, 40, 5, 7, replaceWithOre);
		generateOre(WuxiaBlocks.EARTH_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 30, 4, 6, replaceWithOre);
		generateOre(WuxiaBlocks.SKY_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 25, 4, 5, replaceWithOre);
		generateOre(WuxiaBlocks.HEAVENLY_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 20, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.RAINBOW_LAW_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.SKY_AND_EARTH_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 2, 2, replaceWithOre);
	}

	private void generateEarthWorld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, Predicate<IBlockState> replaceWithOre) {
		generateOre(Blocks.COAL_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 80, 12, 8, replaceWithOre);
		generateOre(Blocks.IRON_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 80, 4, 6, replaceWithOre);
		generateOre(Blocks.GOLD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 30, 50, 4, 5, replaceWithOre);
		generateOre(Blocks.DIAMOND_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 20, 3, 3, replaceWithOre);
		generateOre(Blocks.EMERALD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 15, 3, 3, replaceWithOre);

		generateOre(WuxiaBlocks.NATURAL_ODDITY_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 50, 4, 4, replaceWithOre);
		generateOre(WuxiaBlocks.PURE_QI_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 20, 40, 5, 7, replaceWithOre);
		generateOre(WuxiaBlocks.EARTH_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 30, 4, 6, replaceWithOre);
		generateOre(WuxiaBlocks.SKY_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 25, 4, 5, replaceWithOre);
		generateOre(WuxiaBlocks.HEAVENLY_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 20, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.RAINBOW_LAW_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.SKY_AND_EARTH_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 2, 2, replaceWithOre);
	}

	private void generateWoodWorld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, Predicate<IBlockState> replaceWithOre) {
		generateOre(Blocks.COAL_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 80, 12, 8, replaceWithOre);
		generateOre(Blocks.IRON_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 80, 4, 6, replaceWithOre);
		generateOre(Blocks.GOLD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 30, 50, 4, 5, replaceWithOre);
		generateOre(Blocks.DIAMOND_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 20, 3, 3, replaceWithOre);
		generateOre(Blocks.EMERALD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 15, 3, 3, replaceWithOre);

		generateOre(WuxiaBlocks.NATURAL_ODDITY_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 50, 4, 4, replaceWithOre);
		generateOre(WuxiaBlocks.PURE_QI_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 20, 40, 5, 7, replaceWithOre);
		generateOre(WuxiaBlocks.EARTH_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 30, 4, 6, replaceWithOre);
		generateOre(WuxiaBlocks.SKY_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 25, 4, 5, replaceWithOre);
		generateOre(WuxiaBlocks.HEAVENLY_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 20, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.RAINBOW_LAW_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.SKY_AND_EARTH_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 2, 2, replaceWithOre);
	}

	private void generateMetalWorld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, Predicate<IBlockState> replaceWithOre) {
		generateOre(Blocks.COAL_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 80, 12, 8, replaceWithOre);
		generateOre(Blocks.IRON_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 80, 4, 6, replaceWithOre);
		generateOre(Blocks.GOLD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 30, 50, 4, 5, replaceWithOre);
		generateOre(Blocks.DIAMOND_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 20, 3, 3, replaceWithOre);
		generateOre(Blocks.EMERALD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 0, 15, 3, 3, replaceWithOre);

		generateOre(WuxiaBlocks.NATURAL_ODDITY_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 50, 4, 4, replaceWithOre);
		generateOre(WuxiaBlocks.PURE_QI_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 20, 40, 5, 7, replaceWithOre);
		generateOre(WuxiaBlocks.EARTH_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 30, 4, 6, replaceWithOre);
		generateOre(WuxiaBlocks.SKY_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 25, 4, 5, replaceWithOre);
		generateOre(WuxiaBlocks.HEAVENLY_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 20, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.RAINBOW_LAW_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.SKY_AND_EARTH_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 2, 2, replaceWithOre);
	}

	private void generateMiningWorld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, Predicate<IBlockState> replaceWithOre) {
		generateOre(WuxiaBlocks.NATURAL_ODDITY_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 90, 4, 4, replaceWithOre);
		generateOre(WuxiaBlocks.WEAK_LIFE_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 20, 120, 5, 7, replaceWithOre);
		generateOre(WuxiaBlocks.SOUL_STONE_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 120, 4, 6, replaceWithOre);
		generateOre(Blocks.IRON_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 10, 120, 4, 6, replaceWithOre);
		generateOre(WuxiaBlocks.PRIMORDIAL_STONE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 90, 4, 5, replaceWithOre);
		generateOre(Blocks.GOLD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 90, 4, 5, replaceWithOre);
		generateOre(WuxiaBlocks.FIVE_ELEMENT_PURE_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 60, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.PURE_QI_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 60, 3, 3, replaceWithOre);
		generateOre(Blocks.DIAMOND_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 60, 3, 3, replaceWithOre);
		generateOre(Blocks.EMERALD_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 60, 3, 3, replaceWithOre);
		generateOre(WuxiaBlocks.EARTH_LAW_CRYSTAL_VEIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 45, 2, 2, replaceWithOre);
	}

	private void generateOre(IBlockState ore, World world, Random random, int x, int z, int minY, int maxY, int size, int chances) {
		int deltaY = maxY - minY;

		for (int i = 0; i < chances; i++) {
			BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));

			WorldGenMinable generator = new WorldGenMinable(ore, 1 + random.nextInt(size));
			generator.generate(world, random, pos);
		}
	}

	private void generateOre(IBlockState ore, World world, Random random, int x, int z, int minY, int maxY, int size, int chances, Predicate<IBlockState> replaceWithOre) {
		int deltaY = maxY - minY;

		for (int i = 0; i < chances; i++) {
			BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));

			WorldGenMinable generator = new WorldGenMinable(ore, 1 + random.nextInt(size), replaceWithOre);
			generator.generate(world, random, pos);
		}
	}
}
