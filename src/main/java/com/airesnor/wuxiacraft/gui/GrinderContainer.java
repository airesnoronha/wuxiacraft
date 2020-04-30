package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.entities.tileentity.GrinderTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GrinderContainer extends Container {

	GrinderTileEntity te;

	public GrinderContainer(IInventory playerInv, GrinderTileEntity te) {
		this.te = te;

		//te slots
		this.addSlotToContainer(new Slot(te, 0, 108, 14)); //input slot
		this.addSlotToContainer(new Slot(te, 1, 16, 56)); //stones slot
		for(int i = 0; i < 4; i ++) {
			this.addSlotToContainer(new Slot(te, i+2, 76 + i*21, 56)); //stones slot
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

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemStack1 = slot.getStack();
			itemstack = itemStack1.copy();

			if (index == 2)
			{
				if (!this.mergeItemStack(itemStack1, 3, 39, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemStack1, itemstack);
			}
			else if (index != 1 && index != 0)
			{
				if (!FurnaceRecipes.instance().getSmeltingResult(itemStack1).isEmpty())
				{
					if (!this.mergeItemStack(itemStack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (TileEntityFurnace.isItemFuel(itemStack1))
				{
					if (!this.mergeItemStack(itemStack1, 1, 2, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 3 && index < 30)
				{
					if (!this.mergeItemStack(itemStack1, 30, 39, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 30 && index < 39 && !this.mergeItemStack(itemStack1, 3, 30, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemStack1, 3, 39, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemStack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemStack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemStack1);
		}

		return itemstack;
	}
}
