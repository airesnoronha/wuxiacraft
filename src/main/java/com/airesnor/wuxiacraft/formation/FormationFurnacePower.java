package com.airesnor.wuxiacraft.formation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FormationFurnacePower extends Formation {

	public FormationFurnacePower(String name, double cost, double activationCost, double range) {
		super(name, cost, activationCost, range);
	}

	@Override
	public int doUpdate(World worldIn, BlockPos source) {
		List<TileEntity> tes = worldIn.loadedTileEntityList;
		int activated = 0;
		for(TileEntity te : tes) {
			if(te instanceof TileEntityFurnace) {
				if(te.getPos().getDistance(source.getX(), source.getY(), source.getZ()) < this.getRange()) {
					TileEntityFurnace furnace = (TileEntityFurnace) te;
					if(!furnace.isBurning()) {
						furnace.setField(0, 200);
						activated++;
						IBlockState state = worldIn.getBlockState(furnace.getPos());
						worldIn.notifyBlockUpdate(furnace.getPos(), state, state, 3);
					}
				}
			}
		}
		return activated;
	}
}
