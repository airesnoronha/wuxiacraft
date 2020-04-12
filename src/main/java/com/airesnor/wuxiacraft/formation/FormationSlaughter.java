package com.airesnor.wuxiacraft.formation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class FormationSlaughter extends Formation {

	public FormationSlaughter(String name, double cost, double activationCost, double range) {
		super(name, cost, activationCost, range);
	}

	@Override
	public int doUpdate(@Nonnull World worldIn, @Nonnull BlockPos source, @Nonnull FormationTileEntity parent) {
		return 0;
	}
}
