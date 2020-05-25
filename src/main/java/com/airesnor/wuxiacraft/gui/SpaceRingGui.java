package com.airesnor.wuxiacraft.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.IItemHandler;

public class SpaceRingGui extends GuiContainer {

    private final IItemHandler inv;

    public SpaceRingGui(IItemHandler inv, EntityPlayer player) {
        super(new SpaceRingContainer(inv, player));

        this.xSize = 200;
        this.ySize = 250;
        this.inv = inv;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
