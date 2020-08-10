package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.items.ItemSpatialRing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SpatialRingContainer extends Container {

    private final IItemHandler inv;
    private final int inventorySize;
    private final int spatialRingRows;
    private final int spatialRingColumns;

    public SpatialRingContainer(EntityPlayer player) {
        this.inv = player.getHeldItemMainhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        this.inventorySize = inv.getSlots();
        this.spatialRingRows = ((ItemSpatialRing) player.getHeldItemMainhand().getItem()).getSpatialRingRows();
        this.spatialRingColumns = ((ItemSpatialRing) player.getHeldItemMainhand().getItem()).getSpatialRingColumns();

        int xPos = 119 - (9 * (this.spatialRingColumns - 9));
        int yPos = 200 - (18 * (this.spatialRingRows - 3));

        //Space Ring inventory
        for (int y = 0; y < this.spatialRingRows; ++y) {
            for (int x = 0; x < this.spatialRingColumns; ++x) {
                addSlotToContainer(new SlotItemHandler(inv, x + y * this.spatialRingColumns, xPos + x * 18, yPos + y * 18));
            }
        }

        yPos = yPos + (this.spatialRingRows * 18) + 9;
        xPos = 119;
        //9-35 Player inventory
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
            }
        }
        //0-8 Player inventory
        for (int x = 0; x < 9; ++x) {
            addSlotToContainer(new Slot(player.inventory, x, xPos + x * 18, yPos + 58));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stack0 = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack0 = stack1.copy();

            if (index < inventorySize) {
                //from space ring to player
                if (!this.mergeItemStack(stack1, inventorySize, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack1, 0, inventorySize, false)){
                //from player to space ring
                return ItemStack.EMPTY;
            }

            if (stack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

//            if (stack1.getCount() == stack0.getCount()) {
//                return ItemStack.EMPTY;
//            }
//            slot.onTake(playerIn, stack1);
        }
        return stack0;
    }
}
