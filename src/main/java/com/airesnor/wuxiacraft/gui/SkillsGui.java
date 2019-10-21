package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.cultivation.techniques.TechniquesModifiers;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.RemoveTechniqueMessage;
import com.airesnor.wuxiacraft.networking.SkillCapMessage;
import com.airesnor.wuxiacraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.*;

public class SkillsGui extends GuiScreen {

	private static final ResourceLocation gui_texture = new ResourceLocation(WuxiaCraft.MODID, "textures/gui/skills_gui.png");
	private static final Map<String, ResourceLocation>  skillIcons = new HashMap<>();


	private int xSize = 200;
	private int ySize = 133;
	private int guiTop = 0;
	private int guiLeft = 0;

	private ICultivation cultivation;
	private ICultTech cultTech;
	private ISkillCap skillCap;
	private int offset = 0;

	public static void init() {
		for(Skill skill : Skills.SKILLS) {
			skillIcons.put(skill.getUName(), new ResourceLocation(WuxiaCraft.MODID,
					"textures/skills/icons/" + skill.getUName() + ".png"));
		}
	}

	public SkillsGui(EntityPlayer player) {
		this.cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
		this.cultTech = player.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
		this.skillCap = player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
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
		this.drawSkillsIcons();
		this.drawForegroundLayer();
		this.drawTooltips(mouseX, mouseY);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)
			|| ClientProxy.keyBindings[4].isActiveAndMatches(keyCode))
		{
			this.mc.player.closeScreen();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(mouseButton == 0) {
			int i = 0;
			int maxi = 6;
			int displayTS = Math.max(0,cultTech.getTechniqueSkills().size() - offset);
			for(i = 0; i < maxi && i < displayTS; i++) {
				if(inBounds(mouseX, mouseY, this.guiLeft+20, this.guiTop+17+i*17, 9, 9)) {
					selectSkill(cultTech.getTechniqueSkills().get(offset+i));
				}
			}
			int displayKS = skillCap.getKnownSkills().size() + displayTS - offset;
			for(i = i; i < maxi && i < displayKS; i++) {
				if(inBounds(mouseX, mouseY, this.guiLeft+8, this.guiTop+17+i*17, 9, 9)) {
					removeSkill(skillCap.getKnownSkills().get(offset+i-displayTS));
				}
				if(inBounds(mouseX, mouseY, this.guiLeft+20, this.guiTop+17+i*17, 9, 9)) {
					selectSkill(skillCap.getKnownSkills().get(offset+i-displayTS));
				}
			}
		}
	}

	private void drawBackgroundLayer() {
		this.mc.getTextureManager().bindTexture(gui_texture);
		GlStateManager.color(1f,1f,1f,1f);
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft, this.guiTop, 0);
		drawTexturedModalRect(0, 0, 0, 0, this.xSize, this.ySize);
		int i = 0;
		int maxi = 6;
		int displayTS = Math.max(0,cultTech.getTechniqueSkills().size() - offset);
		for(i = 0; i < maxi && i < displayTS; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(6, 15+17*i, 0);
			drawTexturedModalRect(0,0, 0,133,179,13);
			if(skillCap.getSelectedSkills().contains(cultTech.getTechniqueSkills().get(offset+i))) {
				drawTexturedModalRect(14, 2, 45, 148,9,9);
			} else {
				drawTexturedModalRect(14, 2, 36, 148,9,9);
			}
			GlStateManager.popMatrix();
		}
		int displayKS = skillCap.getKnownSkills().size() + displayTS - offset;
		for(i = i; i < maxi && i < displayKS; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(6, 15+17*i, 0);
			drawTexturedModalRect(0,0, 0,133,179,13);
			drawTexturedModalRect(2, 2, 0, 148, 9, 9);
			drawTexturedModalRect(2, 2, 27, 148, 9,9);
			if(skillCap.getSelectedSkills().contains(skillCap.getKnownSkills().get(offset+i-displayTS))) {
				drawTexturedModalRect(14, 2, 45, 148,9,9);
			} else {
				drawTexturedModalRect(14, 2, 36, 148,9,9);
			}
			GlStateManager.popMatrix();
		}
		if(skillCap.getKnownSkills().size() + cultTech.getTechniqueSkills().size() > maxi) {
			drawTexturedModalRect(185, 15, 0, 148, 9, 9);
			drawTexturedModalRect(185, 15, 9, 148, 9,9);
			drawTexturedModalRect(185, 2, 0, 148, 9, 9);
			drawTexturedModalRect(185, 123, 18, 148, 9,9);
		}

		GlStateManager.popMatrix();
	}

	private void drawSkillsIcons() {
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft, this.guiTop, 0);
		int i = 0;
		int maxi = 6;
		int displayTS = Math.max(0,cultTech.getTechniqueSkills().size() - offset);
		for(i = 0; i < maxi && i < displayTS; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(31, 16 + 17 * i, 0);
			GlStateManager.scale(11f/256f, 11f/256f, 1f);
			String UName = cultTech.getTechniqueSkills().get(offset + i).getUName();
			ResourceLocation tex = skillIcons.get(UName);
			this.mc.getTextureManager().bindTexture(tex);
			drawTexturedModalRect(0,0,0,0,256,256);
			GlStateManager.popMatrix();
		}
		int displayKS = skillCap.getKnownSkills().size() + displayTS - offset;
		for(i = i; i < maxi && i < displayKS; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(31, 16 + 17 * i, 0);
			GlStateManager.scale(11f/256f, 11f/256f, 1f);
			String UName = skillCap.getKnownSkills().get(offset + i-displayTS).getUName();
			ResourceLocation tex = skillIcons.get(UName);
			this.mc.getTextureManager().bindTexture(tex);
			drawTexturedModalRect(0,0,0,0,256,256);
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}

	private void drawForegroundLayer() {
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft, this.guiTop, 0);
		int width = this.fontRenderer.getStringWidth(I18n.format("wuxiacraft.label.skills_gui.title"));
		this.fontRenderer.drawString(I18n.format("wuxiacraft.label.skills_gui.title"), (this.xSize-width)/2, 5, 0x404040);
		int i = 0;
		int maxi = 6;
		int displayTS = Math.max(0,cultTech.getTechniqueSkills().size() - offset);
		for(i = 0; i < maxi && i < displayTS; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(44, 17 + 17 * i, 0);
			this.fontRenderer.drawString(cultTech.getTechniqueSkills().get(offset+i).getName(), 0, 0, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
		int displayKS = skillCap.getKnownSkills().size() + displayTS - offset;
		for(i = i; i < maxi && i < displayKS; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(43, 17 + 17 * i, 0);
			this.fontRenderer.drawString(skillCap.getKnownSkills().get(offset+i-displayTS).getName(), 0, 0, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}

	private void drawTooltips(int mouseX, int mouseY) {

	}

	public static boolean inBounds(int x, int y, int left, int top, int width, int height) {
		return (x >= left && x <= (left+width) && y >= top && y <= (top+height));
	}

	public void drawFramedBox(int x, int y, int width, int height, int borderSize, int textureX, int textureY) {
		this.mc.getTextureManager().bindTexture(gui_texture);
		drawTexturedModalRect(x, y, textureX, textureY, borderSize, borderSize);
		drawTexturedModalRect(x, y+height-borderSize, textureX, textureY+2*borderSize, borderSize, borderSize);
		drawTexturedModalRect(x+width-borderSize, y, textureX+2*borderSize, textureY, borderSize, borderSize);
		drawTexturedModalRect(x+width-borderSize, y+height-borderSize, textureX+borderSize*2, textureY+borderSize*2, borderSize, borderSize);
		//vertical borders
		int i = 0;
		for(i = 0; i < height-2*borderSize; i+=borderSize) {
			drawTexturedModalRect(x, y+borderSize + i, textureX, textureY+borderSize, borderSize, borderSize);
			drawTexturedModalRect(x+width-borderSize, y+borderSize + i, textureX+2*borderSize, textureY+borderSize, borderSize, borderSize);
		}
		i=Math.max(0,i-borderSize);
		int leftOverY = height-2*borderSize - i;
		if(leftOverY > 0) {
			drawTexturedModalRect(x, y+borderSize+i, textureX, textureY+borderSize, borderSize, leftOverY);
			drawTexturedModalRect(x+width-borderSize, y+borderSize+i, textureX+2*borderSize, textureY+borderSize, borderSize, leftOverY);
		}
		//horizontal borders
		//vertical borders
		for(i = 0; i < width-2*borderSize; i+=borderSize) {
			drawTexturedModalRect(x+borderSize+i, y, textureX+borderSize, textureY, borderSize, borderSize);
			drawTexturedModalRect(x+borderSize+i, y+height-borderSize, textureX+borderSize, textureY+2*borderSize, borderSize, borderSize);
		}
		i=Math.max(0,i-borderSize);
		int leftOverX = width-2*borderSize - i;
		if(leftOverX > 0) {
			drawTexturedModalRect(x+borderSize+i, y, textureX+borderSize, textureY, leftOverX, borderSize);
			drawTexturedModalRect(x+borderSize+i, y+height-borderSize, textureX+borderSize, textureY+2*borderSize, leftOverX, borderSize);
		}
		//middle
		int j = 0;
		for(j = 0; j < height-2*borderSize; j += borderSize) {
			for(i = 0; i < width-2*borderSize; i+= borderSize) {
				drawTexturedModalRect(x+borderSize+i, y+borderSize+j, textureX+borderSize, textureY+borderSize, borderSize, borderSize);
			}
			i=Math.max(0, i-borderSize);
			drawTexturedModalRect(x+borderSize+i, y+borderSize+j, textureX+borderSize, textureY+borderSize, leftOverX, borderSize);
		}
		j = Math.max(0, j-borderSize);
		for(i = 0; i < height-2*borderSize; i+= borderSize) {
			drawTexturedModalRect(x+borderSize+i, y+borderSize+j, textureX+borderSize, textureY+borderSize, borderSize, leftOverY);
		}
		i=Math.max(0, i-borderSize);
		drawTexturedModalRect(x+borderSize+i, y+borderSize+j, textureX+borderSize, textureY+borderSize, leftOverX, leftOverY);
	}

	private void selectSkill(Skill skill) {
		if(skillCap.getSelectedSkills().contains(skill)) {
			skillCap.remSelectedSkill(skill);
		} else {
			skillCap.addSelectedSkill(skill);
		}
		if(skillCap.getSelectedSkills().size() == 0) {
			skillCap.setActiveSkill(-1);
		} else if(skillCap.getActiveSkill() == -1) {
			skillCap.setActiveSkill(0);
		}
		NetworkWrapper.INSTANCE.sendToServer(new SkillCapMessage(skillCap));
	}

	private void removeSkill(Skill skill) {
		if(skillCap.getKnownSkills().contains(skill)) {
			skillCap.removeSkill(skill);
		}
		if(skillCap.getSelectedSkills().contains(skill)) {
			skillCap.remSelectedSkill(skill);
		}
		if(skillCap.getSelectedSkills().size() == 0) {
			skillCap.setActiveSkill(-1);
		} else if(skillCap.getActiveSkill() == -1) {
			skillCap.setActiveSkill(0);
		}
		NetworkWrapper.INSTANCE.sendToServer(new SkillCapMessage(skillCap));
	}

}
