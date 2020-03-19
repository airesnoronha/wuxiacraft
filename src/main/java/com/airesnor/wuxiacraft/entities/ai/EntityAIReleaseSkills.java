package com.airesnor.wuxiacraft.entities.ai;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.world.World;

public class EntityAIReleaseSkills extends EntityAIBase {

	World world;
	public WanderingCultivator attacker;
	public float maxAttackRange;
	public float minAttackRange;
	Skill selectedSkill;

	/**
	 * The PathEntity of our entity.
	 */
	Path path;

	public EntityAIReleaseSkills(WanderingCultivator cultivator) {
		this.attacker = cultivator;
		this.world = cultivator.world;
		this.minAttackRange = 5f;
		this.maxAttackRange = 35f;
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
			return MathUtils.between(this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ), this.minAttackRange, this.maxAttackRange);
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute();
	}

	@Override
	public boolean isInterruptible() {
		return true;
	}

	@Override
	public void startExecuting() {
		this.selectedSkill = this.attacker.getSkillList().get(0);
		attacker.getSkillCap().setCasting(true);
	}

	@Override
	public void resetTask() {
		attacker.getSkillCap().setCasting(false);
		attacker.getSkillCap().resetCastProgress();
	}

	@Override
	public void updateTask() {
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
