package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.items.ItemSpatialRing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SpatialRingContainer extends Container {

    private final int inventorySize;
    private final int spatialRingRows;
    private final int spatialRingColumns;

    public SpatialRingContainer(IItemHandler inv, EntityPlayer player, ItemSpatialRing spatialRing) {
        this.inventorySize = inv.getSlots();
        this.spatialRingRows = spatialRing.spatialRingRows;
        this.spatialRingColumns = spatialRing.spatialRingColumns;

        int xPos = 8;
        int yPos = 18;

        //0-53 Space Ring inventory
        for (int y = 0; y < spatialRingRows; ++y) {
            for (int x = 0; x < spatialRingColumns; ++x) {
                addSlotToContainer(new SlotItemHandler(inv, x + y * 9, xPos + x * 18, yPos + y * 18));
            }
        }

        yPos = 140;
        //9-35 Player inventory
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
            }
        }
        //0-8 Player inventory
        for (int x = 0; x < 9; ++x) {
            addSlotToContainer(new Slot(player.inventory, x, xPos + x * 18, 198));
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
            } else {
                //from player to space ring
                if (!this.mergeItemStack(stack1, 0, inventorySize, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack1.getCount() == stack0.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, stack1);
        }
        return stack0;
    }
}
