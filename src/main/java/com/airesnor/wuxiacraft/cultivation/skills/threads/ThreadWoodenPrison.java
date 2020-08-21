package com.airesnor.wuxiacraft.cultivation.skills.threads;

import com.airesnor.wuxiacraft.utils.OreUtils;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class ThreadWoodenPrison extends Thread {

	private final BlockPos targetPos;
	private final WorldServer world;

	public ThreadWoodenPrison(WorldServer world, BlockPos targetPos) {
		this.world = world;
		this.targetPos = targetPos;
	}

	@Override
	public void run() {
		if (world != null) {
			if (OreUtils.earthTypes.contains(world.getBlockState(targetPos).getBlock())) {
				IBlockState destBlock = Blocks.LOG.getDefaultState();
				if(world.getBiome(targetPos) == Biomes.DESERT || world.getBiome(targetPos) == Biomes.DESERT_HILLS ) {
					destBlock = Blocks.CACTUS.getDefaultState();
				} else if(world.getBiome(targetPos) == Biomes.TAIGA || world.getBiome(targetPos) == Biomes.TAIGA_HILLS ||
						world.getBiome(targetPos) == Biomes.COLD_TAIGA || world.getBiome(targetPos) == Biomes.COLD_TAIGA_HILLS ) {
					destBlock = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
				} else if(world.getBiome(targetPos) == Biomes.JUNGLE) {
					destBlock = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
				} else if(world.getBiome(targetPos) == Biomes.BIRCH_FOREST) {
					destBlock = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
				} else if(world.getBiome(targetPos) == Biomes.SAVANNA || world.getBiome(targetPos) == Biomes.SAVANNA_PLATEAU ) {
					destBlock = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);
				} else if(world.getBiome(targetPos) == Biomes.ROOFED_FOREST ) {
					destBlock = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);
				}
				for (int i = -2; i < 5; i++) {
					final IBlockState targetBlock = destBlock;
					if (i != 4) {
						final BlockPos targetLevel = this.targetPos.up(i);
						world.addScheduledTask(() -> {
							if (world.isAirBlock(targetLevel.north().north())) {
								world.setBlockState(targetLevel.north().north(), targetBlock);
							}
							if (world.isAirBlock(targetLevel.north().west())) {
								world.setBlockState(targetLevel.north().west(), targetBlock);
							}
							if (world.isAirBlock(targetLevel.north().east())) {
								world.setBlockState(targetLevel.north().east(), targetBlock);
							}
							if (world.isAirBlock(targetLevel.west().west())) {
								world.setBlockState(targetLevel.west().west(), targetBlock);
							}
							if (world.isAirBlock(targetLevel.east().east())) {
								world.setBlockState(targetLevel.east().east(), targetBlock);
							}
							if (world.isAirBlock(targetLevel.south().west())) {
								world.setBlockState(targetLevel.south().west(), targetBlock);
							}
							if (world.isAirBlock(targetLevel.south().east())) {
								world.setBlockState(targetLevel.south().east(), targetBlock);
							}
							if (world.isAirBlock(targetLevel.south().south())) {
								world.setBlockState(targetLevel.south().south(), targetBlock);
							}
						});
					} else {
						world.addScheduledTask(() ->{
							if (world.isAirBlock(targetPos.up(4))) {
								world.setBlockState(targetPos.up(4), targetBlock);
							}
							if (world.isAirBlock(targetPos.up(4).north())) {
								world.setBlockState(targetPos.up(4).north(), targetBlock);
							}
							if (world.isAirBlock(targetPos.up(4).west())) {
								world.setBlockState(targetPos.up(4).west(), targetBlock);
							}
							if (world.isAirBlock(targetPos.up(4).east())) {
								world.setBlockState(targetPos.up(4).east(), targetBlock);
							}
							if (world.isAirBlock(targetPos.up(4).south())) {
								world.setBlockState(targetPos.up(4).south(), targetBlock);
							}
						});
					}
					try {
						sleep(110);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
