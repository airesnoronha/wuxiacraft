package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.SkillCapMessage;
import com.airesnor.wuxiacraft.proxy.ClientProxy;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillsGui extends GuiScreen {

	private static final ResourceLocation gui_texture = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/skills_gui.png");
	public static final Map<String, ResourceLocation> skillIcons = new HashMap<>();


	private int xSize = 200;
	private int ySize = 133;
	private int guiTop = 0;
	private int guiLeft = 0;

	private ICultTech cultTech;
	private ISkillCap skillCap;
	private int offset = 0;

	public static void init() {
		for (Skill skill : Skills.SKILLS) {
			skillIcons.put(skill.getUName(), new ResourceLocation(WuxiaCraft.MOD_ID,
					"textures/skills/icons/" + skill.getUName() + ".png"));
		}
	}

	public SkillsGui(EntityPlayer player) {
		this.cultTech = CultivationUtils.getCultTechFromEntity(player);
		this.skillCap = CultivationUtils.getSkillCapFromEntity(player);
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
	protected void keyTyped(char typedChar, int keyCode) {
		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)
				|| ClientProxy.keyBindings[4].isActiveAndMatches(keyCode)) {
			this.mc.player.closeScreen();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton == 0) {
			List<Skill> totalSkills = skillCap.getTotalKnowSkill(cultTech);
			int i;
			int maxi = 6;
			int displayTS = Math.max(0, totalSkills.size() - offset);
			for (i = 0; i < maxi && i < displayTS; i++) {
				int index = Math.min(i + offset, totalSkills.size()-1);
				if (skillCap.getKnownSkills().contains(totalSkills.get(index))) {
					if (inBounds(mouseX, mouseY, this.guiLeft + 8, this.guiTop + 17 + i * 17, 9, 9)) {
						removeSkill(totalSkills.get(index));
					}
				}
				if (inBounds(mouseX, mouseY, this.guiLeft + 20, this.guiTop + 17 + i * 17, 9, 9)) {
					selectSkill(index);
				}
			}
			if (totalSkills.size() > maxi) {
				if (inBounds(mouseX, mouseY, this.guiLeft + 186, this.guiTop + 15, 9, 9)) {
					this.offset = Math.max(0, this.offset - 1);
				}
			}
			if (totalSkills.size() > maxi) {
				if (inBounds(mouseX, mouseY, this.guiLeft + 186, this.guiTop + 104, 9, 9)) {
					this.offset = Math.min(totalSkills.size() - maxi, this.offset + 1);
				}
			}
			if (inBounds(mouseX, mouseY, this.guiLeft + 20, this.guiTop + 4, 9, 9)) {
				this.toggleDeselectAllSkills();
			}
		}
	}

	private void drawBackgroundLayer() {
		this.mc.getTextureManager().bindTexture(gui_texture);
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft, this.guiTop, 0);
		drawTexturedModalRect(0, 0, 0, 0, this.xSize, this.ySize);
		if(skillCap.getSelectedSkills().isEmpty()) {
			drawTexturedModalRect(20, 4, 36, 148, 9, 9);
		} else {
			drawTexturedModalRect(20, 4, 45, 148, 9, 9);
		}
		List<Skill> totalSkills = skillCap.getTotalKnowSkill(cultTech);
		int i;
		int maxi = 6;
		int displayTS = Math.max(0, totalSkills.size() - offset);
		for (i = 0; i < maxi && i < displayTS; i++) {
			int index = Math.min(i + offset, totalSkills.size()-1);
			GlStateManager.pushMatrix();
			GlStateManager.translate(6, 15 + 17 * i, 0);
			drawTexturedModalRect(0, 0, 0, 133, 179, 13);
			if (skillCap.getKnownSkills().contains(totalSkills.get(index))) {
				drawTexturedModalRect(2, 2, 0, 148, 9, 9);
				drawTexturedModalRect(2, 2, 27, 148, 9, 9);
			}
			if (skillCap.getSelectedSkills().contains(index)) {
				drawTexturedModalRect(14, 2, 45, 148, 9, 9);
			} else {
				drawTexturedModalRect(14, 2, 36, 148, 9, 9);
			}
			GlStateManager.popMatrix();
		}
		if (totalSkills.size() > maxi) {
			drawTexturedModalRect(186, 15, 0, 148, 9, 9);
			drawTexturedModalRect(186, 15, 9, 148, 9, 9);
			drawTexturedModalRect(186, 104, 0, 148, 9, 9);
			drawTexturedModalRect(186, 104, 18, 148, 9, 9);
		}

		GlStateManager.popMatrix();
	}

	private void drawSkillsIcons() {
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft, this.guiTop, 0);
		List<Skill> totalSkills = skillCap.getTotalKnowSkill(cultTech);
		int i;
		int maxi = 6;
		int displayTS = Math.max(0, totalSkills.size() - offset);
		for (i = 0; i < maxi && i < displayTS; i++) {
			int index = Math.min(i + offset, totalSkills.size()-1);
			GlStateManager.pushMatrix();
			GlStateManager.translate(31, 16 + 17 * i, 0);
			GlStateManager.scale(11f / 256f, 11f / 256f, 1f);
			String UName = totalSkills.get(index).getUName();
			ResourceLocation tex = skillIcons.get(UName);
			this.mc.getTextureManager().bindTexture(tex);
			drawTexturedModalRect(0, 0, 0, 0, 256, 256);
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}

	private void drawForegroundLayer() {
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft, this.guiTop, 0);
		int width = this.fontRenderer.getStringWidth(I18n.format("wuxiacraft.label.skills_gui.title"));
		this.fontRenderer.drawString(I18n.format("wuxiacraft.label.skills_gui.title"), (this.xSize - width) / 2, 5, 0x404040);
		List<Skill> totalSkills = skillCap.getTotalKnowSkill(cultTech);
		int i = 0;
		int maxi = 6;
		int displayTS = Math.max(0, totalSkills.size() - offset);
		for (i = 0; i < maxi && i < displayTS; i++) {
			int index = Math.min(i + offset, totalSkills.size()-1);
			GlStateManager.pushMatrix();
			GlStateManager.translate(44, 17 + 17 * i, 0);
			int color = skillCap.getSelectedSkills().contains(index) ? 0x2C6CFF : 0xFFFFFF;
			this.fontRenderer.drawString(totalSkills.get(index).getName(), 0, 0, color);
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}

	private void drawTooltips(int mouseX, int mouseY) {

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
		int i = 0;
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
		int j = 0;
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

	private void toggleDeselectAllSkills() {
		if(skillCap.getSelectedSkills().isEmpty()) {
			int size = skillCap.getTotalKnowSkill(cultTech).size();
			for(int i = 0; i < size; i++) {
				skillCap.getSelectedSkills().add(i);
				skillCap.setActiveSkill(0);
			}
		} else {
			skillCap.getSelectedSkills().clear();
			skillCap.setActiveSkill(-1);
		}
		NetworkWrapper.INSTANCE.sendToServer(new SkillCapMessage(skillCap, false));
	}

	private void selectSkill(int skillIndex) {
		if (skillCap.getSelectedSkills().contains(skillIndex)) {
			skillCap.remSelectedSkill(skillIndex);
		} else {
			skillCap.addSelectedSkill(skillIndex);
		}
		if (skillCap.getSelectedSkills().size() == 0) {
			skillCap.setActiveSkill(-1);
		} else if (skillCap.getActiveSkill() == -1) {
			skillCap.setActiveSkill(0);
		}
		skillCap.setActiveSkill(MathUtils.clamp(skillCap.getActiveSkill(), -1, skillCap.getSelectedSkills().size() - 1));
		NetworkWrapper.INSTANCE.sendToServer(new SkillCapMessage(skillCap, false));
	}

	private void removeSkill(Skill skill) {
		if (skillCap.getSelectedSkills().contains(skillCap.getKnownSkills().indexOf(skill))) {
			skillCap.remSelectedSkill(skillCap.getKnownSkills().indexOf(skill));
		}
		if (skillCap.getKnownSkills().contains(skill)) {
			skillCap.removeSkill(skill);
		}
		if (skillCap.getSelectedSkills().size() == 0) {
			skillCap.setActiveSkill(-1);
		} else if (skillCap.getActiveSkill() == -1) {
			skillCap.setActiveSkill(0);
		}
		skillCap.setActiveSkill(Math.max(-1, Math.min(skillCap.getSelectedSkills().size() - 1, skillCap.getActiveSkill())));
		NetworkWrapper.INSTANCE.sendToServer(new SkillCapMessage(skillCap, false));
		this.offset = MathUtils.clamp(this.offset, 0, this.skillCap.getTotalKnowSkill(cultTech).size());
	}

}
