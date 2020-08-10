package com.airesnor.wuxiacraft.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.IItemHandler;

public class SpatialRingGui extends GuiContainer {

    public SpatialRingGui(EntityPlayer player) {
        super(new SpatialRingContainer(player));

        this.xSize = 400;
        this.ySize = 500;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
