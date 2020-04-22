package com.airesnor.wuxiacraft.formation;

import net.minecraft.entity.monster.IMob;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * General purpose event handler for formations
 */
@Mod.EventBusSubscriber
public class FormationEventHandler {

	/**
	 * Will check around the world for mob suppression formations and if in range not spawn
	 *
	 * @param event description
	 */
	@SubscribeEvent
	public void onMobSpawn(LivingSpawnEvent.CheckSpawn event) {
		World world = event.getWorld();
		if (event.getEntityLiving() instanceof IMob) {
			List<TileEntity> tileEntityList = world.loadedTileEntityList;
			for (TileEntity te : tileEntityList) {
				if (te instanceof FormationTileEntity) {
					if (((FormationTileEntity) te).getFormation() != null) {
						if (((FormationTileEntity) te).getFormation() instanceof FormationMobSuppression) {
							double range = ((FormationTileEntity) te).getFormation().getRange();
							if (((FormationTileEntity) te).getFormationInfo().hasKey("preferredRange")) {
								range = ((FormationTileEntity) te).getFormationInfo().getDouble("preferredRange");
							}
							if (te.getPos().getDistance((int) event.getX(), (int) event.getY(), (int) event.getZ()) < range) {
								event.setResult(Event.Result.DENY);
							}
						}
					}
				}
			}
		}
	}

}
