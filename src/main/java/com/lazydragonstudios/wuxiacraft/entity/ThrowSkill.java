package com.lazydragonstudios.wuxiacraft.entity;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillDescriptor;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspect;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit.SkillHitAspect;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedList;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ThrowSkill extends ThrowableProjectile {

	protected SkillDescriptor skill;

	public ThrowSkill(EntityType<ThrowSkill> type, Level level) {
		this(type, level, new SkillDescriptor());
	}

	public ThrowSkill(EntityType<ThrowSkill> type, Level level, SkillDescriptor skill) {
		super(type, level);
		this.skill = skill;
	}

	@Override
	public void tick() {
		var deltaMovement = this.getDeltaMovement();
		super.tick();
		this.setDeltaMovement(deltaMovement.x, deltaMovement.y, deltaMovement.z);
	}

	@Override
	protected void onHit(HitResult hitResult) {
		super.onHit(hitResult);
		for (var skill : skill.getSkillChain()) {
			if (skill instanceof SkillHitAspect hitSkill) {
				hitSkill.activate((Player) this.getOwner(), this.skill, hitResult);
				break;
			}
		}
		this.kill();
	}

	@Override
	protected float getGravity() {
		return 0f;
	}

	@Override
	protected void defineSynchedData() {
	}

}
