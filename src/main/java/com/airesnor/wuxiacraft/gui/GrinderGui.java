package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.entities.tileentity.GrinderTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GrinderGui extends GuiContainer {

	public static final ResourceLocation GRINDER_GUI = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/grinder_gui.png");
	private final IInventory tileGrinder;

	public GrinderGui(IInventory playerInv, GrinderTileEntity te) {
		super(new GrinderContainer(playerInv, te));
		this.tileGrinder = te;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		GlStateManager.color(1f,1f,1f,1f);
		mc.getTextureManager().bindTexture(GRINDER_GUI);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0,0,this.xSize, this.ySize);
		int grindProgress = (int)(Math.min(21f,21f*tileGrinder.getField(0)/150f));
		int energy = (int)(Math.min(39f, 39f*tileGrinder.getField(1)/10000f));
		drawTexturedModalRect(this.guiLeft + 16, this.guiTop + 12 + 39-energy, 0, 166+39-energy, 15, energy);
		drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 33, 15, 166, 71, grindProgress);
	}
}
