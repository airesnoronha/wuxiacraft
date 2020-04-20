package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.utils.TranslateUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class Formation {

	private final String name;

	private final double operationCost; //cost in spirit stone energy

	private final double activationCost; //cost in cultivator skill energy

	private final double range;

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
	 * @param parent  The tile entity containing the formation
	 * @return how many times has this formation activates this tick
	 */
	public abstract int doUpdate(@Nonnull World worldIn,@Nonnull BlockPos source,@Nonnull FormationTileEntity parent);


	/**
	 * The formation main function: What is it gonna do, except only client
	 *
	 * @param worldIn the world the formation is in
	 * @param source  where is the formation core
	 * @param parent  The tile entity containing the formation
	 */
	@SideOnly(Side.CLIENT)
	public void doClientUpdate(@Nonnull World worldIn,@Nonnull BlockPos source,@Nonnull FormationTileEntity parent) {
	}

	/**
	 * Sometimes, interrupting a formation may lead to disaster (I hope)
	 *
	 * @param worldIn      the world this formation is in
	 * @param source       where has the interruption occurred
	 * @param interrupters who might have interrupted this formation (generally anyone near the formation disturbance by 6 blocks)
	 */
	@SuppressWarnings("EmptyMethod")
	public void onInterrupt(@Nonnull World worldIn, @Nullable BlockPos source, @Nullable List<EntityLivingBase> interrupters) {

	}

	/**
	 * If formation will render something client side
	 * @param x core x+0.5
	 * @param y core y+0.5
	 * @param z core z+0.5
	 */
	@SideOnly(Side.CLIENT)
	public void render(double x, double y, double z) {

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
