package wuxiacraft.util;

import com.sun.javafx.geom.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SkillUtils {


	private static final Predicate<Entity> SKILL_TARGETS = input -> input != null && EntityPredicates.NOT_SPECTATING.test(input) && EntityPredicates.IS_ALIVE.test(input) && input.canBeCollidedWith();

	@Nullable
	public static Entity rayTraceEntities(LivingEntity actor, float distance, float partialTicks) {
		Entity entity = null;
		Vector3d start = actor.getEyePosition(partialTicks);
		Vector3d eyeRotation = actor.getLook(partialTicks);
		Vector3d end = start.add(eyeRotation.x * distance, eyeRotation.y * distance, eyeRotation.z * distance);

		List<Entity> list = actor.world.getEntitiesInAABBexcluding(actor, new AxisAlignedBB(actor.getPosition()).grow(distance + 1), SKILL_TARGETS::test);
		double targetDistance = 0.0D;

		for (Entity entity1 : list) {
			if (entity1 != actor) {
				AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(0.30000001192092896D);
				Optional<Vector3d> intersectPoint = axisalignedbb.rayTrace(start, end);

				if (intersectPoint.isPresent()) {
					EntityRayTraceResult result = new EntityRayTraceResult(entity1, intersectPoint.get());

					double newerDistance = start.squareDistanceTo(result.getHitVec());

					if (newerDistance < targetDistance || targetDistance == 0.0D) {
						entity = entity1;
						targetDistance = newerDistance;
					}
				}
			}
		}

		return entity;
	}
}
