package com.airesnor.wuxiacraft.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class SpaceRingGui extends GuiContainer {

    private final IInventory playerInv;

    public SpaceRingGui(IInventory playerInv, EntityPlayer player) {
        super(new SpaceRingContainer(playerInv, player));

        this.playerInv = playerInv;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
