package com.lazydragonstudios.wuxiacraft.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class SkillUtil {

	public static HitResult getHitResult(Entity source, double range, Predicate<Entity> entityPredicate) {
		Level level = source.level;
		Vec3 startPosition = source.getEyePosition();
		// end = start + |lookVec|*range
		Vec3 endPosition = startPosition.add(
				source.getLookAngle().normalize().scale(range));
		HitResult hitresult = level.clip(new ClipContext(startPosition, endPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, source));
		if (hitresult.getType() != HitResult.Type.MISS) {
			endPosition = hitresult.getLocation();
		}

		var sourceViewVector = source.getViewVector(1f);
		AABB aabb = source.getBoundingBox().expandTowards(sourceViewVector.scale(range)).inflate(1.0D, 1.0D, 1.0D);
		HitResult entityHitResult = ProjectileUtil.getEntityHitResult(source, startPosition, endPosition, aabb,
				e -> e.isPickable() && !e.isSpectator(), range*range);
		if (entityHitResult != null) {
			hitresult = entityHitResult;
		}

		return hitresult;
	}

}
