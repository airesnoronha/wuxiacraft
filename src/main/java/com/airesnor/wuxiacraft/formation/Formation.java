package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.utils.TranslateUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Formation {

	private String name;

	private double cost;

	public Formation (String name, double cost) {
		this.name = name;
		this.cost = cost;
		Formations.FORMATIONS.add(this);
	}

	public abstract boolean doUpdate(World worldIn, BlockPos source);

	public String getName() {
		return TranslateUtils.translateKey("wuxiacraft.formations." + this.name + ".name");
	}

	public String getUName() {
		return this.name;
	}

	public double getCost() {
		return cost;
	}
}
