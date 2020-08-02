package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.items.ItemSpatialRing;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.IItemHandler;

public class SpatialRingGui extends GuiContainer {

    private final IItemHandler inv;

    public SpatialRingGui(IItemHandler inv, EntityPlayer player, ItemSpatialRing spatialRing) {
        super(new SpatialRingContainer(inv, player, spatialRing));

        this.xSize = 200;
        this.ySize = 250;
        this.inv = inv;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
