package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.items.WuxiaItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockBase extends Block {

	private static boolean canSupportPlant;

	public BlockBase(String name, Material materialIn, boolean canSupportPlant) {
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(WuxiaBlocks.BLOCKS_TAB);

		WuxiaBlocks.BLOCKS.add(this);
		WuxiaItems.ITEMS.add(new ItemBlock(this).setRegistryName(name));
		this.canSupportPlant = canSupportPlant;
	}

	public BlockBase(String name, Material materialIn, boolean canSupportPlant, float hardness, float resistance, int harvestLevel) {
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(WuxiaBlocks.BLOCKS_TAB);

		WuxiaBlocks.BLOCKS.add(this);
		WuxiaItems.ITEMS.add(new ItemBlock(this).setRegistryName(name));
		this.canSupportPlant = canSupportPlant;
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setHarvestLevel("pickaxe", harvestLevel);
	}

	@ParametersAreNonnullByDefault
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		return canSupportPlant;
	}
}
