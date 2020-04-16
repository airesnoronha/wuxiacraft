package com.airesnor.wuxiacraft.entities.mobs;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.entities.ai.EntityAIReleaseSkills;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WanderingCultivator extends EntityCultivator implements IMob {

	private static final ResourceLocation DROP_TABLE_1 = new ResourceLocation(WuxiaCraft.MOD_ID, "entities/wandering_cultivator_l1");
	private static final ResourceLocation DROP_TABLE_2 = new ResourceLocation(WuxiaCraft.MOD_ID, "entities/wandering_cultivator_l2");
	private static final ResourceLocation DROP_TABLE_3 = new ResourceLocation(WuxiaCraft.MOD_ID, "entities/wandering_cultivator_l3");
	private static final ResourceLocation DROP_TABLE_4 = new ResourceLocation(WuxiaCraft.MOD_ID, "entities/wandering_cultivator_l4");

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
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityMob.class, true));
		this.experienceValue=5;
	}

	@Override
	protected void applyCultivation(World world) {
		if(world.provider.getDimensionType().getId() == 0) {
			int result = world.rand.nextInt(100);
			if(MathUtils.between(result, 0, 30)) {
				this.cultivation.setCurrentLevel(CultivationLevel.SOUL_REFINEMENT);
				this.experienceValue = 7;
			}
			else if(MathUtils.between(result, 31, 41)) {
				this.cultivation.setCurrentLevel(CultivationLevel.QI_PATHS_REFINEMENT);
				this.experienceValue = 10;
			}
			else if(MathUtils.between(result, 42, 47)) {
				this.cultivation.setCurrentLevel(CultivationLevel.DANTIAN_CONDENSING);
				this.experienceValue = 15;
			}
			result = 1+world.rand.nextInt(35);
			this.cultivation.setCurrentSubLevel(5-(int)Math.floor(Math.sqrt(result)));
			result = world.rand.nextInt(100);
			if(result < 50) {
				skillCap.addSkill(Skills.FLAMES);
				if(MathUtils.between(result, 0, 5)) {
					skillCap.addSkill(Skills.FIRE_BAll);
				}
			} else {
				skillCap.addSkill(Skills.WATER_NEEDLE);
				if(MathUtils.between(result,50, 55)) {
					skillCap.addSkill(Skills.WATER_BLADE);
				}
			}
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere();
	}

	@Nullable
	@Override
	protected ResourceLocation getLootTable() {
		ResourceLocation table = DROP_TABLE_1;
		switch(this.cultivation.getCurrentLevel()) {
			case BODY_REFINEMENT:
				table = DROP_TABLE_1;
				break;
			case SOUL_REFINEMENT:
				table = DROP_TABLE_2;
				break;
			case QI_PATHS_REFINEMENT:
				table = DROP_TABLE_3;
				break;
			case DANTIAN_CONDENSING:
				table = DROP_TABLE_4;
				break;
		}
		return table;
	}

}
