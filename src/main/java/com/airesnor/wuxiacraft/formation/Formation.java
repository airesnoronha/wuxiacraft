package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.utils.TranslateUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public abstract class Formation {

	private String name;

	private double operationCost; //cost in spirit stone energy

	private double activationCost; //cost in cultivator skill energy

	private double range;

	public Formation(String name, double cost, double activationCost, double range) {
		this.name = name;
		this.operationCost = cost;
		this.activationCost = activationCost;
		this.range = range;
		Formations.FORMATIONS.add(this);
	}

	/**
	 * The formation main function: What is it gonna do
	 *
	 * @param worldIn the world the formation is in
	 * @param source  where is the formation core
	 * @return how many times has this formation activates this tick
	 */
	public abstract int doUpdate(World worldIn, BlockPos source);

	/**
	 * Sometimes, interrupting a formation may lead to disaster (I hope)
	 *
	 * @param worldIn      the world this formation is in
	 * @param source       where has the interruption occurred
	 * @param interrupters who might have interrupted this formation (generally anyone near the formation disturbance by 6 blocks)
	 */
	public void onInterrupt(World worldIn, BlockPos source, List<EntityLivingBase> interrupters) {

	}

	public String getName() {
		return TranslateUtils.translateKey("wuxiacraft.formations." + this.name + ".name");
	}

	public String getUName() {
		return this.name;
	}

	public double getOperationCost() {
		return operationCost;
	}

	public double getRange() {
		return range;
	}

	public double getActivationCost() {
		return activationCost;
	}
}
