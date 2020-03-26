package com.airesnor.wuxiacraft.entities.mobs;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GiantAnt extends EntityMob {

	public static final ResourceLocation DROP_LIST = new ResourceLocation(WuxiaCraft.MODID, "entities/giant_ant");

	public GiantAnt(World worldIn) {
		super(worldIn);
		setSize(1.7f, 0.7f);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityAIAttackMeleeWithRange(this, 0.6D, false, 1.0f));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.4D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(18.0D);
	}

	protected static class EntityAIAttackMeleeWithRange extends EntityAIAttackMelee {

		float range;

		public EntityAIAttackMeleeWithRange(EntityCreature creature, double speedIn, boolean useLongMemory, float range) {
			super(creature, speedIn, useLongMemory);
			this.range = range;
		}

		@Override
		protected double getAttackReachSqr(EntityLivingBase attackTarget) {
			return range + attackTarget.width + attacker.width;
		}
	}

	@Nullable
	@Override
	protected ResourceLocation getLootTable() {
		return DROP_LIST;
	}
}
