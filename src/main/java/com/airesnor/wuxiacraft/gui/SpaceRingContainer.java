package com.airesnor.wuxiacraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SpaceRingContainer extends Container {

    public SpaceRingContainer(IInventory playerInv, EntityPlayer player) {
        //this.addSlotToContainer(new SlotItemHandler(player.getHeldItemMainhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)), index, xPos, yPos);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}
