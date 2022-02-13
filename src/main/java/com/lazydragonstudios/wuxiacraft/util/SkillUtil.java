package com.lazydragonstudios.wuxiacraft.util;

import net.minecraft.world.entity.Entity;
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
				source.getLookAngle().normalize().multiply(range, range, range));
		HitResult hitresult = level.clip(new ClipContext(startPosition, endPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, source));
		if (hitresult.getType() != HitResult.Type.MISS) {
			endPosition = hitresult.getLocation();
		}

		HitResult entityHitResult = getEntityHitResult(level, source, startPosition, endPosition, source.getBoundingBox().expandTowards(source.getDeltaMovement()).inflate(1.0D), entityPredicate, range);
		if (entityHitResult != null) {
			hitresult = entityHitResult;
		}

		return hitresult;
	}

	@Nullable
	public static EntityHitResult getEntityHitResult(Level level, Entity source, Vec3 startPosition, Vec3 endPosition, AABB rangeAABB, Predicate<Entity> entityPredicate) {
		return getEntityHitResult(level, source, startPosition, endPosition, rangeAABB, entityPredicate, 0.3F);
	}

	@Nullable
	public static EntityHitResult getEntityHitResult(Level level, Entity source, Vec3 startPosition, Vec3 endPosition, AABB rangeAABB, Predicate<Entity> entityPredicate, double expandRange) {
		double d0 = Double.MAX_VALUE;
		Entity entity = null;

		for (Entity entity1 : level.getEntities(source, rangeAABB, entityPredicate)) {
			AABB aabb = entity1.getBoundingBox().inflate(expandRange);
			Optional<Vec3> optional = aabb.clip(startPosition, endPosition);
			if (optional.isPresent()) {
				double d1 = startPosition.distanceToSqr(optional.get());
				if (d1 < d0) {
					entity = entity1;
					d0 = d1;
				}
			}
		}

		return entity == null ? null : new EntityHitResult(entity);
	}

}
