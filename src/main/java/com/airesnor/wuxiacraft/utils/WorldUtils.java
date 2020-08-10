package com.airesnor.wuxiacraft.utils;

import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.SpawnEntityOnClientMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class WorldUtils {

	public static void spawnEntity(World world, Entity entity) {
		if(!world.isRemote) {
			world.spawnEntity(entity);
			AxisAlignedBB aabb = new AxisAlignedBB(new BlockPos(entity.posX, entity.posY, entity.posZ)).grow(512);
			List<EntityPlayerMP> players =  world.getEntitiesWithinAABB(EntityPlayerMP.class, aabb);
			for(EntityPlayerMP player : players) {
				NetworkWrapper.INSTANCE.sendTo(new SpawnEntityOnClientMessage(entity), player);
			}
		}
	}

}
