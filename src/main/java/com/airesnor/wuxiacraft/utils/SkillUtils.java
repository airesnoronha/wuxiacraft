package com.airesnor.wuxiacraft.utils;

import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class SkillUtils {

	public static void sendMessageWithinRange(WorldServer worldIn, BlockPos source, double range, IMessage message) {
		for (int i = 0; i < worldIn.playerEntities.size(); ++i) {
			EntityPlayerMP player = (EntityPlayerMP) worldIn.playerEntities.get(i);
			BlockPos destination = player.getPosition();
			double dist = source.getDistance(destination.getX(), destination.getY(), destination.getZ());
			if (dist < range) {
				NetworkWrapper.INSTANCE.sendTo(message, player);
			}
		}
	}

	private static final Predicate<Entity> SKILL_TARGETS = input -> input != null && EntitySelectors.NOT_SPECTATING.test(input) && EntitySelectors.IS_ALIVE.test(input) && input.canBeCollidedWith();

	@Nullable
	public static Entity rayTraceEntities(EntityLivingBase player, float distance, float partialTicks) {
		Entity entity = null;
		Vec3d start = player.getPositionEyes(partialTicks);
		Vec3d eyeRotation = player.getLook(partialTicks);
		Vec3d end = start.addVector(eyeRotation.x * distance, eyeRotation.y * distance, eyeRotation.z * distance);

		List<Entity> list = player.world.getEntitiesInAABBexcluding(player, new AxisAlignedBB(player.getPosition()).grow(distance + 1), SKILL_TARGETS::test);
		double targetDistance = 0.0D;

		for (Entity entity1 : list) {
			if (entity1 != player) {
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

				if (raytraceresult != null) {
					double newerDistance = start.squareDistanceTo(raytraceresult.hitVec);

					if (newerDistance < targetDistance || targetDistance == 0.0D) {
						entity = entity1;
						targetDistance = newerDistance;
					}
				}
			}
		}

		return entity;
	}

	public static BlockPos rayTraceBlock(EntityLivingBase player, float distance, float partialTicks) {
		BlockPos pos = null;
		Vec3d start = player.getPositionEyes(partialTicks);
		Vec3d eyeRotation = player.getLook(partialTicks);
		Vec3d end = start.addVector(eyeRotation.x * distance, eyeRotation.y * distance, eyeRotation.z * distance);

		RayTraceResult result =  player.world.rayTraceBlocks(start, end);
		if(result != null) {
			if(result.typeOfHit == RayTraceResult.Type.BLOCK) {
				pos = result.getBlockPos();
			}
		}
		return pos;
	}
}
