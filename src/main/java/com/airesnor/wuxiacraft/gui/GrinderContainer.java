package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.entities.tileentity.GrinderTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GrinderContainer extends Container {

	GrinderTileEntity te;

	public GrinderContainer(IInventory playerInv, GrinderTileEntity te) {
		this.te = te;

		//te slots
		this.addSlotToContainer(new Slot(te, 0, 108, 14)); //input slot
		this.addSlotToContainer(new Slot(te, 1, 15, 55)); //stones slot
		for(int i = 0; i < 4; i ++) {
			this.addSlotToContainer(new Slot(te, i+2, 75 + i*21, 55)); //stones slot
		}

		// Player Inventory, Slot 9-35, Slot IDs 9-35
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		// Player Inventory, Slot 0-8, Slot IDs 36-44
		for (int x = 0; x < 9; ++x) {
			this.addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.te.isUsableByPlayer(playerIn);
	}
}
