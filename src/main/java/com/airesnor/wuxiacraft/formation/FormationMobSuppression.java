package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FormationMobSuppression extends Formation {

	private float strength;

	public FormationMobSuppression(String name, double cost, double activationCost, double range, float strength) {
		super(name, cost, activationCost, range);
		this.strength = strength;
	}

	@Override
	public int doUpdate(@Nonnull World worldIn, @Nonnull BlockPos source, @Nonnull FormationTileEntity parent) {
		int activated = 0;
		double preferredRange = this.getRange();
		if (parent.getTimeActivated() % 20 == 0) {
			List<TileEntity> tileEntities = worldIn.loadedTileEntityList;
			for (TileEntity te : tileEntities) {
				if (te instanceof TileEntitySign && te.getPos().getDistance(source.getX(), source.getY(), source.getZ()) < 16) {
					ITextComponent[] lines = ((TileEntitySign) te).signText;
					for (ITextComponent line : lines) {
						String l = line.getUnformattedText();
						if(l.startsWith("range=")) {
							try {
								preferredRange = Math.min(this.getRange(), Double.parseDouble(l.substring("range=".length())));
								parent.getFormationInfo().setDouble("preferredRange", preferredRange);
							} catch (NumberFormatException e) {
								WuxiaCraft.logger.info("Range wasn't readable at " + te.getPos().toString());
							}
						}
					}
				}
			}
		}
		if(parent.getFormationInfo().hasKey("preferredRange")) {
			preferredRange = parent.getFormationInfo().getDouble("preferredRange");
		}
		List<EntityLivingBase> mobs = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(source).grow(preferredRange), input -> input instanceof IMob);
		for(EntityLivingBase mob : mobs) {
			if(mob.getDistance(source.getX(), source.getY(), source.getZ()) < preferredRange) {
				mob.attackEntityFrom(DamageSource.GENERIC, this.strength);
			}
		}
		return activated;
	}
}
