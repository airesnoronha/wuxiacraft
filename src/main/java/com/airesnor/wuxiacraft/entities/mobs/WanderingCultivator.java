package com.airesnor.wuxiacraft.entities.mobs;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.entities.ai.EntityAIReleaseSkills;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WanderingCultivator extends EntityCultivator {

	private static final ResourceLocation DROP_TABLE = new ResourceLocation(WuxiaCraft.MODID, "entities/wandering_cultivator");

	public WanderingCultivator(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIReleaseSkills(this));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 0.6D, false));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.6D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		//this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityMob.class, true));
	}

	@Override
	protected void applyCultivation(World world) {;
		if(world.provider.getDimensionType().getId() == 0) {
			int result = world.rand.nextInt(100);
			if(MathUtils.between(result, 0, 30)) {
				this.cultivation.setCurrentLevel(CultivationLevel.SOUL_REFINEMENT);
			}
			else if(MathUtils.between(result, 31, 41)) {
				this.cultivation.setCurrentLevel(CultivationLevel.QI_PATHS_REFINEMENT);
			}
			else if(MathUtils.between(result, 42, 47)) {
				this.cultivation.setCurrentLevel(CultivationLevel.DANTIAN_CONDENSING);
			}
			result = 1+world.rand.nextInt(35);
			this.cultivation.setCurrentSubLevel(5-(int)Math.floor(Math.sqrt(result)));
			result = world.rand.nextInt(100);
			if(result < 50) {
				skillCap.addSkill(Skills.FLAMES);
				if(MathUtils.between(result, 0, 5)) {
					skillCap.addSkill(Skills.FIRE_BAll);
				}
			} else if(result >= 50) {
				skillCap.addSkill(Skills.WATER_NEEDLE);
				if(MathUtils.between(result,50, 55)) {
					skillCap.addSkill(Skills.WATER_BLADE);
				}
			}
		}
	}

	@Nullable
	@Override
	protected ResourceLocation getLootTable() {
		return DROP_TABLE;
	}
}
