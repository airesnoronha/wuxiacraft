package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.cultivation.techniques.TechniquesModifiers;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.RemoveTechniqueMessage;
import com.airesnor.wuxiacraft.networking.SpeedHandicapMessage;
import com.airesnor.wuxiacraft.networking.SuppressCultivationMessage;
import com.airesnor.wuxiacraft.proxy.ClientProxy;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CultivationGui extends GuiScreen {

	private static final ResourceLocation gui_texture = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/cult_gui.png");

	private int xSize = 200;
	private int ySize = 133;
	private int guiTop = 0;
	private int guiLeft = 0;

	private ICultivation cultivation;
	private ICultTech cultTech;
	private int offset = 0;

	public CultivationGui(EntityPlayer player) {
		this.cultivation = CultivationUtils.getCultivationFromEntity(player);
		this.cultTech = CultivationUtils.getCultTechFromEntity(player);
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
		this.drawBackgroundLayer();
		this.drawForegroundLayer();
		this.drawTooltips(mouseX, mouseY);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)
				|| ClientProxy.keyBindings[2].isActiveAndMatches(keyCode)) {
			this.mc.player.closeScreen();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (inBounds(mouseX, mouseY, this.guiLeft + 156, this.guiTop + 36, 9, 9)) {
			this.offset = Math.max(0, this.offset - 1);
		}
		if (inBounds(mouseX, mouseY, this.guiLeft + 148, this.guiTop + 23, 9, 9)) {
			cultivation.setSuppress(!cultivation.getSuppress());
			NetworkWrapper.INSTANCE.sendToServer(new SuppressCultivationMessage(cultivation.getSuppress()));
		}
		if (inBounds(mouseX, mouseY, this.guiLeft + 156, this.guiTop + 106, 9, 9)) {
			this.offset = Math.min(this.cultTech.getKnownTechniques().size() - 5, this.offset + 1);
		}
		for (int i = 0; i < Math.min(5, this.cultTech.getKnownTechniques().size()); i++) {
			if (inBounds(mouseX, mouseY, this.guiLeft + 8, this.guiTop + 35 + 4 + i * 16, 9, 9)) {
				int index = this.cultTech.getKnownTechniques().size() > 5 ? offset + i : i;
				NetworkWrapper.INSTANCE.sendToServer(new RemoveTechniqueMessage(cultTech.getKnownTechniques().get(index).getTechnique()));
				cultTech.getKnownTechniques().remove(index);
			}
		}
		for(int i = 0; i < 4; i ++) {
			if(inBounds(mouseX, mouseY, this.guiLeft-61+13, this.guiTop+i*15+3, 9,9)) {
				handleBarButtonClick(i, 0);
			}
			else if(inBounds(mouseX, mouseY, this.guiLeft-61+51, this.guiTop+i*15+3, 9,9)) {
				handleBarButtonClick(i, 1);
			}
		}
	}

	private void drawBackgroundLayer() {
		this.mc.getTextureManager().bindTexture(gui_texture);
		GlStateManager.color(1f, 1f, 1f, 1f);

		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		int progress_fill = (int) (cultivation.getCurrentProgress() * 100 / cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()));

		drawTexturedModalRect(this.guiLeft + 6, this.guiTop + 26, 0, 133, 139 * progress_fill / 100, 3);

		int energy_fill = (int) (cultivation.getEnergy() * 100 / cultivation.getCurrentLevel().getMaxEnergyByLevel(cultivation.getCurrentSubLevel()));
		int energyPix = 34 * energy_fill / 100;

		drawTexturedModalRect(this.guiLeft + 185, this.guiTop + 5 + 33 - energyPix, 200, 33 - energyPix, 10, energyPix + 1);

		int knowTechniquesSize = this.cultTech.getKnownTechniques().size();

		List<KnownTechnique> drawing = new ArrayList<>();
		if (knowTechniquesSize > 5) {
			drawTexturedModalRect(this.guiLeft + 172, this.guiTop + 36, 45, 142, 9, 9);
			drawTexturedModalRect(this.guiLeft + 172, this.guiTop + 36, 54, 142, 9, 9);
			drawTexturedModalRect(this.guiLeft + 172, this.guiTop + 106, 45, 142, 9, 9);
			drawTexturedModalRect(this.guiLeft + 172, this.guiTop + 106, 63, 142, 9, 9);
			int counter = 0;
			for (KnownTechnique t : this.cultTech.getKnownTechniques()) {
				if (counter >= offset && counter < offset + 5) {
					drawing.add(t);
				}
				counter++;
			}
		} else {
			drawing.addAll(this.cultTech.getKnownTechniques());
		}
		int pos = 0;
		for (KnownTechnique t : drawing) {
			int progressFill = 0;
			if (t.getProgress() > t.getTechnique().getTier().smallProgress) {
				progressFill += 46;
			} else {
				progressFill += (int) ((t.getProgress() * 46f) / t.getTechnique().getTier().smallProgress);
			}
			if (t.getProgress() > t.getTechnique().getTier().greatProgress + t.getTechnique().getTier().smallProgress) {
				progressFill += 47;
			} else {
				progressFill += (int) ((t.getProgress() - t.getTechnique().getTier().smallProgress) * 47 / t.getTechnique().getTier().greatProgress);
			}
			if (t.getProgress() > t.getTechnique().getTier().perfectionProgress + t.getTechnique().getTier().greatProgress + t.getTechnique().getTier().smallProgress) {
				progressFill += 46;
			} else {
				progressFill += (int) ((t.getProgress() - t.getTechnique().getTier().greatProgress - t.getTechnique().getTier().smallProgress) * 45 / t.getTechnique().getTier().perfectionProgress);
			}
			drawTexturedModalRect(this.guiLeft + 19, this.guiTop + 35 + pos * 16 + 11, 0, 136, 139, 3);
			drawTexturedModalRect(this.guiLeft + 19, this.guiTop + 35 + pos * 16 + 11, 0, 139, progressFill, 3);
			drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 35 + pos * 16 + 4, 45, 142, 9, 9);
			drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 35 + pos * 16 + 4, 72, 142, 9, 9);
			pos++;
		}

		TechniquesModifiers overall = this.cultTech.getOverallModifiers();
		int armorCount = (int) Math.abs(overall.armor / 0.1f);
		int atkSpdCount = (int) Math.abs(overall.attackSpeed / 0.1f);
		int maxHPCount = (int) Math.abs(overall.maxHealth / 0.1f);
		int spdCount = (int) Math.abs(overall.movementSpeed / 0.1f);
		int strCount = (int) Math.abs(overall.strength / 0.1f);

		pos = 0;
		if (overall.armor < 0) GL11.glColor4f(1.0f, 0.15f, 0.15f, 1.0f);
		else GL11.glColor4f(1f, 1f, 1f, 1f);
		for (int i = 0; i < armorCount; i++) {
			drawTexturedModalRect(this.guiLeft + 6 + 10 * pos, this.guiTop + 118, 0, 142, 9, 9);
			pos++;
		}
		if (overall.attackSpeed < 0) GL11.glColor4f(1.0f, 0.15f, 0.15f, 1.0f);
		else GL11.glColor4f(1f, 1f, 1f, 1f);
		for (int i = 0; i < atkSpdCount; i++) {
			drawTexturedModalRect(this.guiLeft + 6 + 10 * pos, this.guiTop + 118, 9, 142, 9, 9);
			pos++;
		}
		if (overall.maxHealth < 0) GL11.glColor4f(0.15f, 0.15f, 0.15f, 1.0f);
		else GL11.glColor4f(1f, 1f, 1f, 1f);
		for (int i = 0; i < maxHPCount; i++) {
			drawTexturedModalRect(this.guiLeft + 6 + 10 * pos, this.guiTop + 118, 18, 142, 9, 9);
			pos++;
		}
		if (overall.movementSpeed < 0) GL11.glColor4f(1.0f, 0.15f, 0.15f, 1.0f);
		else GL11.glColor4f(1f, 1f, 1f, 1f);
		for (int i = 0; i < spdCount; i++) {
			drawTexturedModalRect(this.guiLeft + 6 + 10 * pos, this.guiTop + 118, 27, 142, 9, 9);
			pos++;
		}
		if (overall.strength < 0) GL11.glColor4f(1.0f, 0.15f, 0.15f, 1.0f);
		else GL11.glColor4f(1f, 1f, 1f, 1f);
		for (int i = 0; i < strCount; i++) {
			drawTexturedModalRect(this.guiLeft + 6 + 10 * pos, this.guiTop + 118, 36, 142, 9, 9);
			pos++;
		}

		GL11.glColor4f(1f, 1f, 1f, 1f);
		int[] iconPos = new int[]{27, 27, 99, 108};
		int[] fills = new int[]{
				Math.min(27,(int)((27f * cultivation.getSpeedHandicap()) / 100f)),
				Math.min(27,(int)(27f*cultivation.getMaxSpeed()/cultivation.getSpeedIncrease())),
				Math.min(27,(int)(27f*(cultivation.getHasteLimit()/(0.1f * (cultivation.getStrengthIncrease() - 1))))),
				Math.min(27,(int)(27f*(cultivation.getJumpLimit()*0.42f)/(0.88f * (cultivation.getSpeedIncrease() - 1f))))
		};

		//cultivation suppressing
		if(this.cultivation.getSuppress()) {
			drawTexturedModalRect(this.guiLeft + 148, this.guiTop+23, 117, 142, 9, 9);
		}

		//Regulator bars
		for (int i = 0; i < 4; i++) {
			drawTexturedModalRect(this.guiLeft - 61, this.guiTop + i * 15, 0, 151, 61, 15); //bg
			drawTexturedModalRect(this.guiLeft - 61 + 23, this.guiTop + i * 15 + 4, 0, 166, 27, 7); //bar bg
			drawTexturedModalRect(this.guiLeft - 61 + 23, this.guiTop + i * 15 + 4, 27, 166, fills[i], 7); //bar fill
			drawTexturedModalRect(this.guiLeft - 61 + 13, this.guiTop + i * 15 + 3, 45, 142, 9, 9); //button bg
			drawTexturedModalRect(this.guiLeft - 61 + 51, this.guiTop + i * 15 + 3, 45, 142, 9, 9); //button bg
			drawTexturedModalRect(this.guiLeft - 61 + 13, this.guiTop + i * 15 + 3, 72, 142, 9, 9); //button icon -
			drawTexturedModalRect(this.guiLeft - 61 + 51, this.guiTop + i * 15 + 3, 90, 142, 9, 9); //button icon +
			drawTexturedModalRect(this.guiLeft - 61 + 3, this.guiTop + i * 15 + 3, iconPos[i], 142, 9, 9); // icon
		}
	}

	private void drawForegroundLayer() {
		this.fontRenderer.drawString(cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()), this.guiLeft + 6, this.guiTop + 7, 0x404040);

		//String display = String.format("Speed: %.3f (%d%%)", cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel()), WuxiaCraftConfig.speedHandicap);
		//this.fontRenderer.drawString(display, this.guiLeft + 6,this.guiTop + 35,4210752);
		//display = String.format("Strength: %.2f", cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel()));
		//this.fontRenderer.drawString(display, this.guiLeft + 6,this.guiTop + 45,4210752);

		int knowTechniquesSize = this.cultTech.getKnownTechniques().size();
		List<KnownTechnique> drawing = new ArrayList<>();
		if (knowTechniquesSize > 5) {
			int counter = 0;
			for (KnownTechnique t : this.cultTech.getKnownTechniques()) {
				if (counter >= offset && counter < offset + 5) {
					drawing.add(t);
				}
				counter++;
			}
		} else {
			drawing.addAll(this.cultTech.getKnownTechniques());
		}
		int pos = 0;
		for (KnownTechnique t : drawing) {
			String display = t.getTechnique().getName();// + " " + (int)t.getProgress();
			this.fontRenderer.drawString(display, this.guiLeft + 19, this.guiTop + 35 + pos * 16 + 2, 0xFFFFFF);
			pos++;
		}

		List<String> barDescriptions = new ArrayList<>();
		barDescriptions.add(cultivation.getSpeedHandicap() + "%");
		barDescriptions.add(String.format("%.1f", cultivation.getMaxSpeed()));
		barDescriptions.add(String.format("%.1f", cultivation.getHasteLimit()));
		barDescriptions.add(String.format("%.1f", cultivation.getJumpLimit()));

		for(int i = 0; i < 4; i++) {
			this.fontRenderer.drawString(barDescriptions.get(i), this.guiLeft-61+27, this.guiTop+4+i*15, 0xEFEF00);
		}
	}

	private void drawTooltips(int mouseX, int mouseY) {
		GlStateManager.color(1f,1f,1f,1f);
		for (int i = 0; i < Math.min(this.cultTech.getKnownTechniques().size(), 5); i++) {
			int index = this.cultTech.getKnownTechniques().size() > 5 ? offset + i : i;
			if (inBounds(mouseX, mouseY, this.guiLeft + 19, this.guiTop + 35 + i * 16 + 11, 139, 3)) {
				String line = "No success";
				if (this.cultTech.getKnownTechniques().get(index).getProgress() > this.cultTech.getKnownTechniques().get(i).getTechnique().getTier().smallProgress +
						this.cultTech.getKnownTechniques().get(i).getTechnique().getTier().greatProgress +
						this.cultTech.getKnownTechniques().get(i).getTechnique().getTier().perfectionProgress)
					line = "Perfection success";
				else if (this.cultTech.getKnownTechniques().get(index).getProgress() > this.cultTech.getKnownTechniques().get(i).getTechnique().getTier().smallProgress +
						this.cultTech.getKnownTechniques().get(i).getTechnique().getTier().greatProgress)
					line = "Great success";
				else if (this.cultTech.getKnownTechniques().get(index).getProgress() > this.cultTech.getKnownTechniques().get(i).getTechnique().getTier().smallProgress)
					line = "Small success";
				drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 142);
				this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
			}
		}
		if (inBounds(mouseX, mouseY, this.guiLeft + 6, this.guiTop + 26, 133, 3)) {
			int progress_fill = (int) (cultivation.getCurrentProgress() * 100 / cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()));
			String line = String.format("%d%%", progress_fill);
			drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 142);
			this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
		}
		if (inBounds(mouseX, mouseY, this.guiLeft + 185, this.guiTop + 5, 10, 33)) {
			int energy_fill = (int) (cultivation.getEnergy() * 100 / cultivation.getCurrentLevel().getMaxEnergyByLevel(cultivation.getCurrentSubLevel()));
			String line = String.format("%d%%", energy_fill);
			drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 142);
			this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
		}
		if(inBounds(mouseX, mouseY, this.guiLeft+148, this.guiTop+23, 9,9)) {
			String line = "Suppress Cultivation";
			drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 142);
			this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
		}
	}

	public static boolean inBounds(int x, int y, int left, int top, int width, int height) {
		return (x >= left && x <= (left + width) && y >= top && y <= (top + height));
	}

	public void drawFramedBox(int x, int y, int width, int height, int borderSize, int textureX, int textureY) {
		this.mc.getTextureManager().bindTexture(gui_texture);
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

	private void handleBarButtonClick(int prop, int op) {
		switch (prop) {
			case 0:
				if(op==0)WuxiaCraftConfig.speedHandicap = Math.max(0,WuxiaCraftConfig.speedHandicap - 5);
				if(op==1)WuxiaCraftConfig.speedHandicap = Math.min(100,WuxiaCraftConfig.speedHandicap + 5);
				break;
			case 1:
				if(op==0) WuxiaCraftConfig.maxSpeed -= 1f;
				if(op==1) WuxiaCraftConfig.maxSpeed += 1f;
				break;
			case 2:
				if(op==0) WuxiaCraftConfig.blockBreakLimit -= 1f;
				if(op==1) WuxiaCraftConfig.blockBreakLimit += 1f;
				break;
			case 3:
				if(op==0) WuxiaCraftConfig.jumpLimit -= 1f;
				if(op==1) WuxiaCraftConfig.jumpLimit += 1f;
				break;
		}
		WuxiaCraftConfig.syncFromFields();
		WuxiaCraftConfig.syncCultivationFromConfigToClient();
		NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap, WuxiaCraftConfig.maxSpeed, WuxiaCraftConfig.blockBreakLimit, WuxiaCraftConfig.jumpLimit));
	}


}
