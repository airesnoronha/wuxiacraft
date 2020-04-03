package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.blocks.BlockBase;
import com.airesnor.wuxiacraft.blocks.Materials;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FormationCoreBlock extends BlockBase implements ITileEntityProvider {

	public FormationCoreBlock(String name) {
		super(name, Materials.RUNE);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new FormationTileEntity();
	}
}
