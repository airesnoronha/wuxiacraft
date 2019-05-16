package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.proxy.ClientProxy;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class CultivationGui extends GuiScreen {

	public static final ResourceLocation gui_texture = new ResourceLocation(WuxiaCraft.MODID, "textures/gui/cult_gui.png");

	private int xSize = 200;
	private int ySize = 133;
	private int guiTop = 0;
	private int guiLeft = 0;

	private ICultivation cultivation;

	public CultivationGui(EntityPlayer player) {
		this.cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
	}

	@Override
	public void initGui() {
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawBackgroundLayer();
		drawForegroundLayer();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)
			|| ClientProxy.keyBindings[2].isActiveAndMatches(keyCode))
		{
			this.mc.player.closeScreen();
		}
	}

	private void drawBackgroundLayer() {
		this.mc.getTextureManager().bindTexture(gui_texture);
		GlStateManager.color(1f,1f,1f,1f);

		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		int progress_fill = (int)(cultivation.getCurrentProgress() * 100 / cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()));

		drawTexturedModalRect(this.guiLeft +  6,this.guiTop + 26, 0, 133, 139*progress_fill/100, 3);

		int energy_fill = (int)(cultivation.getEnergy() * 100 / cultivation.getCurrentLevel().getMaxEnergyByLevel(cultivation.getCurrentSubLevel()));
		int energyPix = 34*energy_fill/100;

		drawTexturedModalRect(this.guiLeft + 168, this.guiTop + 9 + 33 - energyPix, 200, 33-energyPix, 10, energyPix+1);

	}

	private void drawForegroundLayer() {
		this.fontRenderer.drawString(cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()), this.guiLeft + 6,this.guiTop + 7,4210752);
		String display = String.format("Speed: %.3f (%d%%)", cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel()), WuxiaCraftConfig.speedHandicap);
		this.fontRenderer.drawString(display, this.guiLeft + 6,this.guiTop + 35,4210752);
		display = String.format("Strength: %.2f", cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel()));
		this.fontRenderer.drawString(display, this.guiLeft + 6,this.guiTop + 45,4210752);

	}


}
