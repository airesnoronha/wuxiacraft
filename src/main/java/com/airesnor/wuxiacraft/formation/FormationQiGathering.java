package com.airesnor.wuxiacraft.formation;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FormationQiGathering extends Formation {

	private double generation;

	public FormationQiGathering(String name, double generation, double range) {
		super(name, 0, 0, range);
		this.generation = generation;
	}

	@Override
	public int doUpdate(World worldIn, BlockPos source) {
		for(TileEntity te : worldIn.loadedTileEntityList) {
			if(te instanceof FormationTileEntity && te.getPos().getDistance(source.getX(),source.getY(), source.getZ()) < this.getRange()) {
				((FormationTileEntity) te).addEnergy(this.generation);
			}
		}
		return 1;
	}
}
