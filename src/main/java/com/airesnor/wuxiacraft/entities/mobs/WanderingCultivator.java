package com.airesnor.wuxiacraft.entities.mobs;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.BaseSystemLevel;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.entities.ai.EntityAIReleaseSkills;
import com.airesnor.wuxiacraft.utils.MathUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WanderingCultivator extends EntityCultivator implements IMob {

	private static final ResourceLocation DROP_TABLE_1 = new ResourceLocation(WuxiaCraft.MOD_ID, "entities/wandering_cultivator_l1");
	private static final ResourceLocation DROP_TABLE_2 = new ResourceLocation(WuxiaCraft.MOD_ID, "entities/wandering_cultivator_l2");
	private static final ResourceLocation DROP_TABLE_3 = new ResourceLocation(WuxiaCraft.MOD_ID, "entities/wandering_cultivator_l3");
	private static final ResourceLocation DROP_TABLE_4 = new ResourceLocation(WuxiaCraft.MOD_ID, "entities/wandering_cultivator_l4");
	private static final ResourceLocation DROP_TABLE_5 = new ResourceLocation(WuxiaCraft.MOD_ID, "entities/wandering_cultivator_l5");
	private static final ResourceLocation DROP_TABLE_6 = new ResourceLocation(WuxiaCraft.MOD_ID, "entities/wandering_cultivator_l6");

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
		//I'll put these manually here because i couldn't extract creeper from entity mob
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityZombie.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityZombieVillager.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntitySpider.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityCaveSpider.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityEnderman.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntitySkeleton.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntitySkeletonHorse.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityBlaze.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPigZombie.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityWitherSkeleton.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityGhast.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityHusk.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntitySilverfish.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityWitch.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, GiantBee.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, GiantAnt.class, true));
		this.experienceValue=5;
	}

	@Override
	protected void applyCultivation(World world) {
		if(world.provider.getDimensionType().getId() == 0) {
			int result = world.rand.nextInt(100);
			BaseSystemLevel aux = BaseSystemLevel.DEFAULT_ESSENCE_LEVEL.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
			if(MathUtils.between(result, 0, 30)) {
				this.cultivation.setEssenceLevel(aux);
				this.experienceValue = 7;
			}
			aux = aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
			if(MathUtils.between(result, 31, 41)) {
				this.cultivation.setEssenceLevel(aux);
				this.experienceValue = 10;
			}
			aux = aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
			if(MathUtils.between(result, 42, 47)) {
				this.cultivation.setEssenceLevel(aux);
				this.experienceValue = 15;
			}
			result = 1+world.rand.nextInt(35);
			this.cultivation.setEssenceSubLevel(5-(int)Math.floor(Math.sqrt(result)));
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
		if(world.provider.getDimensionType().getId() != 0) {
			int result = world.rand.nextInt(100);
			BaseSystemLevel aux = BaseSystemLevel.DEFAULT_ESSENCE_LEVEL.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
			aux = aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);//third level minimum
			if(MathUtils.between(result, 48, 100)) { // base level for other worlds
				this.cultivation.setEssenceLevel(aux);
				this.experienceValue = 10;
			}
			aux = aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
			if(MathUtils.between(result, 0, 30)) {
				this.cultivation.setEssenceLevel(aux);
				this.experienceValue = 15;
			}
			aux = aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
			if(MathUtils.between(result, 31, 41)) {
				this.cultivation.setEssenceLevel(aux);
				this.experienceValue = 25;
			}
			aux = aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS); // guess this should be immortal 3
			if(MathUtils.between(result, 42, 47)) {
				this.cultivation.setEssenceLevel(aux);
				this.experienceValue = 40;
			}
			result = 1+world.rand.nextInt(35);
			this.cultivation.setEssenceSubLevel(5-(int)Math.floor(Math.sqrt(result)));
			result = world.rand.nextInt(100);
			if(result < 50) {
				skillCap.addSkill(Skills.FLAMES);
				skillCap.addSkill(Skills.FIRE_BAll);
			} else {
				skillCap.addSkill(Skills.WATER_NEEDLE);
				skillCap.addSkill(Skills.WATER_BLADE);
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
		BaseSystemLevel aux = BaseSystemLevel.DEFAULT_ESSENCE_LEVEL.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
		if(this.getCultivationLevel() == aux) {
			table = DROP_TABLE_2;
		}
		aux = aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
		if(this.getCultivationLevel() == aux) {
			table = DROP_TABLE_3;
		}
		aux = aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
		if(this.getCultivationLevel() == aux) {
			table = DROP_TABLE_4;
		}
		aux = aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
		if(this.getCultivationLevel() == aux) {
			table = DROP_TABLE_5;
		}
		aux = aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
		if(this.getCultivationLevel() == aux) {
			table = DROP_TABLE_6;
		}
		return table;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 2;
	}
}
