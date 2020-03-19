package com.airesnor.wuxiacraft.entities.mobs;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class WanderingCultivator extends EntityCreature {

	public WanderingCultivator(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(1, new EntityAISwimming(this));
		//this.tasks.addTask(4, new GiantAnt.EntityAIAttackMeleeWithRange(this, 0.6D, false, 1.0f));
		//this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.4D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		//this.tasks.addTask(6, new EntityAILookIdle(this));
		//this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		//this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		//this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(18.0D);
	}

}
