package com.airesnor.wuxiacraft.entities.ai;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.entities.mobs.EntityCultivator;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.world.World;

public class EntityAIReleaseSkills extends EntityAIBase {

	World world;
	public EntityCultivator attacker;
	public float maxAttackRange;
	public float minAttackRange;
	Skill selectedSkill;
	private double optimalRange;

	/**
	 * The PathEntity of our entity.
	 */
	Path path;

	public EntityAIReleaseSkills(EntityCultivator cultivator) {
		this.attacker = cultivator;
		this.world = cultivator.world;
		this.minAttackRange = 5f;
		this.maxAttackRange = 25f;

		this.optimalRange = this.minAttackRange + (this.maxAttackRange -this.minAttackRange) * 0.3;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

		if (entitylivingbase == null) {
			return false;
		} else if (!entitylivingbase.isEntityAlive()) {
			return false;
		} else if (!this.attacker.hasSkills()) {
			return false;
		} else {
			return MathUtils.between(this.attacker.getDistance(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ), this.minAttackRange, this.maxAttackRange);
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		if(this.selectedSkill != null) {
			boolean inRange =  this.shouldExecute();
			boolean hasEnergy = attacker.getCultivation().hasEnergy(this.selectedSkill.getCost());
			return inRange && hasEnergy;
		}
		return false;
	}

	@Override
	public boolean isInterruptible() {
		return true;
	}

	@Override
	public void startExecuting() {
		if(attacker.getSkillCap().getKnownSkills().size() > 0) {
			for(Skill skill : attacker.getSkillCap().getKnownSkills()) {
				if(this.selectedSkill == null) {
					this.selectedSkill = skill;
				} else {
					if(this.attacker.getCultivation().hasEnergy(skill.getCost())) {
						if(this.selectedSkill.getCost() < skill.getCost()) {
							this.selectedSkill = skill;
						}
					}
				}
			}
		}
		attacker.getSkillCap().setCasting(true);
	}

	@Override
	public void resetTask() {
		this.selectedSkill = null;
		attacker.getSkillCap().setCasting(false);
		attacker.getSkillCap().resetCastProgress();
	}

	@Override
	public void updateTask() {
		if(this.attacker.getAttackTarget() != null) {
			this.attacker.getLookHelper().setLookPositionWithEntity(this.attacker.getAttackTarget(), 30.0F, 30.0F);
			EntityLivingBase target = attacker.getAttackTarget();
			boolean outOfRange = attacker.getDistance(target) > optimalRange;
			if(attacker.getNavigator().noPath()){
				if(outOfRange) {
					attacker.getNavigator().tryMoveToEntityLiving(target, 0.3f);
				}
			}
			if(!outOfRange) {
				this.attacker.getNavigator().clearPath();
			}
		}
		if (this.selectedSkill != null) {
			ICultivation cultivation = this.attacker.getCultivation();
			ISkillCap skillCap = this.attacker.getSkillCap();
			if (skillCap.isCasting() && cultivation.hasEnergy(this.selectedSkill.getCost())) {
				if (skillCap.getCastProgress() < selectedSkill.getCastTime())
					skillCap.stepCastProgress(cultivation.getSpeedIncrease());
				selectedSkill.castingEffect(this.attacker);
			} else if (skillCap.isDoneCasting()) {
				skillCap.resetCastProgress();
				skillCap.setDoneCasting(false);
			}
			if (skillCap.isCasting() && skillCap.getCastProgress() >= selectedSkill.getCastTime() && skillCap.getCooldown() <= 0) {
				if (cultivation.hasEnergy(selectedSkill.getCost())) {
					if (selectedSkill.activate(attacker)) {
						cultivation.remEnergy(selectedSkill.getCost());
						skillCap.resetCastProgress();
						skillCap.stepCooldown(selectedSkill.getCooldown());
					}
				}
			}
		}
	}
}
