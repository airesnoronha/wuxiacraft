package com.lazydragonstudios.wuxiacraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
				e -> e.isPickable() && !e.isSpectator(), range * range);
		if (entityHitResult != null) {
			hitresult = entityHitResult;
		}

		return hitresult;
	}

	public static LinkedHashSet<BlockPos> getLogsToBreak(BlockPos initialPos, Level level, HashSet<BlockPos> visited) {
		var linkedList = new LinkedHashSet<BlockPos>();
		var blockState = level.getBlockState(initialPos);
		var tag = BlockTags.LOGS;
		if (visited.contains(initialPos)) return linkedList;
		visited.add(initialPos);
		if (!blockState.is(tag)) return linkedList;
		linkedList.add(initialPos);
		BlockPos[] directions = new BlockPos[]{
				initialPos.above(),
				initialPos.above().north(),
				initialPos.above().north().east(),
				initialPos.above().north().west(),
				initialPos.above().south(),
				initialPos.above().south().east(),
				initialPos.above().south().west(),
				initialPos.above().east(),
				initialPos.above().west(),
				initialPos.below(),
				initialPos.below().north(),
				initialPos.below().north().east(),
				initialPos.below().north().west(),
				initialPos.below().south(),
				initialPos.below().south().east(),
				initialPos.below().south().west(),
				initialPos.below().east(),
				initialPos.below().west(),
				initialPos.north(),
				initialPos.north().east(),
				initialPos.north().west(),
				initialPos.south(),
				initialPos.south().east(),
				initialPos.south().west(),
				initialPos.east(),
				initialPos.west(),
		};
		for (var dir : directions) {
			linkedList.addAll(getLogsToBreak(dir, level, visited));
		}
		return linkedList;
	}

}
