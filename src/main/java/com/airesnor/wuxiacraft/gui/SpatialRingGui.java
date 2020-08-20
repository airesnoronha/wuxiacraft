package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SpatialRingGui extends GuiContainer {

	public static final ResourceLocation SPATIAL_RING_GUI = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/spatial_ring_gui.png");


	private final int spatialRingRows;
	private final int spatialRingColumns;

	public SpatialRingGui(EntityPlayer player) {
		super(new SpatialRingContainer(player));
		SpatialRingContainer container = new SpatialRingContainer(player);
		this.spatialRingColumns = container.spatialRingColumns;
		this.spatialRingRows = container.spatialRingRows;
		this.xSize = 20 + Math.max(9, container.spatialRingColumns) * 18; //this 9 is to the players inv
		this.ySize = 100 + container.spatialRingRows * 18;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(SPATIAL_RING_GUI);
		this.drawFramedBox(this.guiLeft, this.guiTop, this.xSize, this.ySize, 7, 0, 133);

		//space
		int totalSpaceX = Math.max(9, this.spatialRingColumns) * 18 + 4;
		int totalSpaceY = this.spatialRingRows * 18 + 4;
		int drawnX = 0;
		int drawnY = 0;
		for (int j = 0; j < totalSpaceY / 133; j++) {
			drawnX = 0;
			for (int i = 0; i < totalSpaceX / 200; i++) {
				drawTexturedModalRect(this.guiLeft + 8 + drawnX, this.guiTop + 8 + drawnY, 0, 0, 200, 133);
				drawnX += 200;
			}
			if (drawnX < totalSpaceX) {
				drawTexturedModalRect(this.guiLeft + 8 + drawnX, this.guiTop + 8 + drawnY, 0, 0, totalSpaceX - drawnX, 133);
			}
			drawnY += 133;
		}
		if (drawnY < totalSpaceY) {
			drawnX = 0;
			for (int i = 0; i < totalSpaceX / 200; i++) {
				drawTexturedModalRect(this.guiLeft + 8 + drawnX, this.guiTop + 8 + drawnY, 0, 0, 200, totalSpaceY - drawnY);
				drawnX += 200;
			}
			if (drawnX < totalSpaceX) {
				drawTexturedModalRect(this.guiLeft + 8 + drawnX, this.guiTop + 8 + drawnY, 0, 0, totalSpaceX - drawnX, totalSpaceY - drawnY);
			}
		}

		GlStateManager.color(1f, 1f, 1f, 0.6f);
		//Space Ring inventory
		for (int y = 0; y < this.spatialRingRows; ++y) {
			for (int x = 0; x < this.spatialRingColumns; ++x) {
				drawTexturedModalRect(this.guiLeft + 10 + x * 18, this.guiTop + 10 + y * 18, 218, 0, 18, 18);
			}
		}

		int xPos = ((20 + Math.max(9, this.spatialRingColumns) * 18) / 2) - (9 * 18) / 2;
		int yPos = 10 + this.spatialRingRows * 18 + 7;
		GlStateManager.color(1f, 1f, 1f, 1f);
		//9-35 Player inventory
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				drawTexturedModalRect(this.guiLeft + xPos + x * 18, this.guiTop + yPos + y * 18, 200, 0, 18, 18);
			}
		}
		//0-8 Player inventory
		for (int x = 0; x < 9; ++x) {
			drawTexturedModalRect(this.guiLeft + xPos + x * 18, this.guiTop + yPos + 58, 200, 0, 18, 18);
		}
	}

	public void drawFramedBox(int x, int y, int width, int height, int borderSize, int textureX, int textureY) {
		drawTexturedModalRect(x, y, textureX, textureY, borderSize, borderSize);
		drawTexturedModalRect(x, y + height - borderSize, textureX, textureY + 2 * borderSize, borderSize, borderSize);
		drawTexturedModalRect(x + width - borderSize, y, textureX + 2 * borderSize, textureY, borderSize, borderSize);
		drawTexturedModalRect(x + width - borderSize, y + height - borderSize, textureX + borderSize * 2, textureY + borderSize * 2, borderSize, borderSize);
		//vertical borders
		int i;
		for (i = 0; i < height - 2 * borderSize; i += borderSize) {
			drawTexturedModalRect(x, y + borderSize + i, textureX, textureY + borderSize, borderSize, borderSize);
			drawTexturedModalRect(x + width - borderSize, y + borderSize + i, textureX + 2 * borderSize, textureY + borderSize, borderSize, borderSize);
		}
		i = Math.max(0, i - borderSize);
		int leftOverY = height - 2 * borderSize - i;
		if (leftOverY > 0) {
			drawTexturedModalRect(x, y + borderSize + i, textureX, textureY + borderSize, borderSize, leftOverY);
			drawTexturedModalRect(x + width - borderSize, y + borderSize + i, textureX + 2 * borderSize, textureY + borderSize, borderSize, leftOverY);
		}
		//horizontal borders
		//vertical borders
		for (i = 0; i < width - 2 * borderSize; i += borderSize) {
			drawTexturedModalRect(x + borderSize + i, y, textureX + borderSize, textureY, borderSize, borderSize);
			drawTexturedModalRect(x + borderSize + i, y + height - borderSize, textureX + borderSize, textureY + 2 * borderSize, borderSize, borderSize);
		}
		i = Math.max(0, i - borderSize);
		int leftOverX = width - 2 * borderSize - i;
		if (leftOverX > 0) {
			drawTexturedModalRect(x + borderSize + i, y, textureX + borderSize, textureY, leftOverX, borderSize);
			drawTexturedModalRect(x + borderSize + i, y + height - borderSize, textureX + borderSize, textureY + 2 * borderSize, leftOverX, borderSize);
		}
		//middle
		int j;
		for (j = 0; j < height - 2 * borderSize; j += borderSize) {
			for (i = 0; i < width - 2 * borderSize; i += borderSize) {
				drawTexturedModalRect(x + borderSize + i, y + borderSize + j, textureX + borderSize, textureY + borderSize, borderSize, borderSize);
			}
			i = Math.max(0, i - borderSize);
			drawTexturedModalRect(x + borderSize + i, y + borderSize + j, textureX + borderSize, textureY + borderSize, leftOverX, borderSize);
		}
		j = Math.max(0, j - borderSize);
		for (i = 0; i < height - 2 * borderSize; i += borderSize) {
			drawTexturedModalRect(x + borderSize + i, y + borderSize + j, textureX + borderSize, textureY + borderSize, borderSize, leftOverY);
		}
		i = Math.max(0, i - borderSize);
		drawTexturedModalRect(x + borderSize + i, y + borderSize + j, textureX + borderSize, textureY + borderSize, leftOverX, leftOverY);
	}
}
