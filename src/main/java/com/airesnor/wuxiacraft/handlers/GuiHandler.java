package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.entities.tileentity.GrinderTileEntity;
import com.airesnor.wuxiacraft.gui.*;
import com.airesnor.wuxiacraft.items.ItemSpatialRing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

	public static final int CULTIVATION_GUI_ID = 0;
	public static final int SKILLS_GUI_ID = 1;
	public static final int RECIPE_GUI_ID = 2;
	public static final int GRINDER_GUI_ID = 3;
	public static final int SPATIAL_RING_GUI_ID = 4;

	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		switch (ID) {
			case GRINDER_GUI_ID:
				return new GrinderContainer(player.inventory, (GrinderTileEntity) world.getTileEntity(pos));
			case CULTIVATION_GUI_ID:
				break;
			case SKILLS_GUI_ID:
				break;
			case RECIPE_GUI_ID:
				return null;
			case SPATIAL_RING_GUI_ID:
				if (player.getHeldItemMainhand().getItem() instanceof ItemSpatialRing) {
					return new SpatialRingContainer(player);
				} else {
					return null;
				}
		}
		return null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		switch (ID) {
			case CULTIVATION_GUI_ID:
				return new CultivationGui(player);
			case SKILLS_GUI_ID:
				return new CultivationGui(player).withTab(CultivationGui.Tabs.SKILLS);
			case RECIPE_GUI_ID:
				return new RecipeGui(player);
			case GRINDER_GUI_ID:
				return new GrinderGui(player.inventory, (GrinderTileEntity) world.getTileEntity(pos));
			case SPATIAL_RING_GUI_ID:
				if (player.getHeldItemMainhand().getItem() instanceof ItemSpatialRing) {
					return new SpatialRingGui(player);
				} else {
					return null;
				}
		}
		return null;
	}
}
