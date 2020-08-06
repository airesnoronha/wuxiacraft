package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.proxy.ClientProxy;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CultivationGui extends GuiScreen {

	private static final ResourceLocation gui_texture = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/cult_gui.png");

	private final int xSize = 200;
	private final int ySize = 155;
	private int guiTop = 0;
	private int guiLeft = 0;

	private final ICultivation cultivation;
	private final ICultTech cultTech;
	private final ISkillCap skillCap;
	private final EntityPlayer player;
	private int offset = 0;
	private int displayItems = 0;

	//private static double amountAddedToFoundationPerClick = 1;

	private Tabs tab;

	//private boolean perClickFocus = false;
	//private int caretPosition = 0;
	//private String perClickDisplay;

	public CultivationGui(EntityPlayer player) {
		this.player = player;
		this.cultivation = CultivationUtils.getCultivationFromEntity(player);
		this.cultTech = CultivationUtils.getCultTechFromEntity(player);
		this.skillCap = CultivationUtils.getSkillCapFromEntity(player);
		//this.foundation = CultivationUtils.getFoundationFromEntity(player);
		this.tab = Tabs.FOUNDATION;
	}

	public CultivationGui withTab(Tabs tab) {
		this.tab = tab;
		return this;
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
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		this.drawBackgroundLayer();
		this.drawForegroundLayer();
		this.drawTooltips(mouseX, mouseY);
		GlStateManager.disableBlend();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		/*if (this.perClickFocus) {
			//handlerPerClickKey(typedChar, keyCode);
		} else {*/
		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)
				|| ClientProxy.keyBindings[2].isActiveAndMatches(keyCode)) {
			this.mc.player.closeScreen();
		}
		//}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		//offset --
		if (inBounds(mouseX, mouseY, this.guiLeft + 172, this.guiTop + 36, 9, 9)) {
			this.offset = Math.max(0, this.offset - 1);
		}

		//offset ++
		if (inBounds(mouseX, mouseY, this.guiLeft + 172, this.guiTop + 118, 9, 9)) {
			this.offset = Math.min(this.displayItems - this.tab.maxDisplayItems, this.offset + 1);
		}

		//suppress
		if (inBounds(mouseX, mouseY, this.guiLeft + 180, this.guiTop + 5, 12, 14)) {
			cultivation.setSuppress(!cultivation.getSuppress());
			NetworkWrapper.INSTANCE.sendToServer(new SuppressCultivationMessage(cultivation.getSuppress(), player.getUniqueID()));
		}

		//select tabs
		if (inBounds(mouseX, mouseY, this.guiLeft + 5, this.guiTop + 5, 68, 14)) {
			this.changeToTab(Tabs.FOUNDATION);
		}
		//if (inBounds(mouseX, mouseY, this.guiLeft + 61, this.guiTop + 19, 52, 15)) {
		//	this.changeToTab(Tabs.TECHNIQUES);
		//}
		if (inBounds(mouseX, mouseY, this.guiLeft + 76, this.guiTop + 5, 68, 14)) {
			this.changeToTab(Tabs.SKILLS);
		}

		//tabs
		switch (this.tab) {
			case FOUNDATION:
				//handleMouseFoundation(mouseX, mouseY);
				break;
			case TECHNIQUES:
				handleMouseTechniques(mouseX, mouseY);
				break;
			case SKILLS:
				handleMouseSkills(mouseX, mouseY, mouseButton);
				break;
		}

		//techniques

		//regulator bars
		for (int i = 0; i < 4; i++) {
			if (inBounds(mouseX, mouseY, this.guiLeft - 61 + 13, this.guiTop + i * 15 + 3, 9, 9)) {
				handleBarButtonClick(i, 0);
			} else if (inBounds(mouseX, mouseY, this.guiLeft - 61 + 51, this.guiTop + i * 15 + 3, 9, 9)) {
				handleBarButtonClick(i, 1);
			}
		}
	}

	private void changeToTab(Tabs tab) {
		this.tab = tab;
		this.offset = 0;
		switch (tab) {
			case FOUNDATION:
				this.displayItems = 0;
			case TECHNIQUES:
				this.displayItems = 0;
				break;
			case SKILLS:
				this.displayItems = this.skillCap.getTotalKnowSkill(this.cultTech).size();
				break;
		}
	}

	/*
	private void handleMouseFoundation(int mouseX, int mouseY) {
		if (inBounds(mouseX, mouseY, this.guiLeft + 8, this.guiTop + 38, 14, 14)) {
			addProgressToFoundation(0);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 164, this.guiTop + 38, 14, 14)) {
			addProgressToFoundation(1);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 6, this.guiTop + 68, 14, 14)) {
			addProgressToFoundation(2);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 166, this.guiTop + 68, 14, 14)) {
			addProgressToFoundation(3);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 8, this.guiTop + 98, 14, 14)) {
			addProgressToFoundation(4);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 164, this.guiTop + 98, 14, 14)) {
			addProgressToFoundation(5);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 22, this.guiTop + 38, 14, 14)) {
			selectFoundationAttribute(0);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 150, this.guiTop + 38, 14, 14)) {
			selectFoundationAttribute(1);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 20, this.guiTop + 68, 14, 14)) {
			selectFoundationAttribute(2);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 152, this.guiTop + 68, 14, 14)) {
			selectFoundationAttribute(3);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 22, this.guiTop + 98, 14, 14)) {
			selectFoundationAttribute(4);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 150, this.guiTop + 98, 14, 14)) {
			selectFoundationAttribute(5);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 64, this.guiTop + 118, 14, 14)) {
			amountAddedToFoundationPerClick = MathUtils.clamp(amountAddedToFoundationPerClick - 1, 0, 100);
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 113, this.guiTop + 118, 14, 14)) {
			amountAddedToFoundationPerClick = MathUtils.clamp(amountAddedToFoundationPerClick + 1, 0, 100);
		}
		if(inBounds(mouseX, mouseY, this.guiLeft + 73, this.guiTop + 118, 40, 9)) {
			if(!this.perClickFocus) {
				perClickOnFocus();
			}
		} else {
			if(this.perClickFocus) {
				perClickLoseFocus();
			}
		}
	}*/

	@SuppressWarnings("unused")
	private void handleMouseTechniques(int mouseX, int mouseY) {
		List<KnownTechnique> toDisplay = new ArrayList<>();
		if (this.cultTech.getBodyTechnique() != null)
			toDisplay.add(this.cultTech.getBodyTechnique());
		if (this.cultTech.getEssenceTechnique() != null)
			toDisplay.add(this.cultTech.getEssenceTechnique());
		if (this.cultTech.getDivineTechnique() != null)
			toDisplay.add(this.cultTech.getDivineTechnique());
		for (KnownTechnique t : toDisplay) {
			int index = toDisplay.indexOf(t);
			if (index >= this.offset && index < (this.offset + Tabs.TECHNIQUES.maxDisplayItems)) {
				int pos = index - this.offset;
				if (inBounds(mouseX, mouseY, this.guiLeft + 8, this.guiTop + 35 + 4 + pos * 19, 9, 9)) {
					NetworkWrapper.INSTANCE.sendToServer(new RemoveTechniqueMessage(t.getTechnique(), player.getUniqueID()));
					if (cultTech.getBodyTechnique().getTechnique().equals(t.getTechnique())) {
						cultTech.setBodyTechnique(null);
					} else if (cultTech.getDivineTechnique().getTechnique().equals(t.getTechnique())) {
						cultTech.setDivineTechnique(null);
					} else if (cultTech.getEssenceTechnique().getTechnique().equals(t.getTechnique())) {
						cultTech.setEssenceTechnique(null);
					}
				}
			} else if (index >= (this.offset + Tabs.TECHNIQUES.maxDisplayItems)) {
				break;
			}
		}
	}

	private void handleMouseSkills(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			List<Skill> totalSkills = skillCap.getTotalKnowSkill(cultTech);
			for (Skill skill : totalSkills) {
				int index = totalSkills.indexOf(skill);
				if (index >= this.offset && index < (this.offset + Tabs.SKILLS.maxDisplayItems)) {
					int pos = index - this.offset;
					if (skillCap.getKnownSkills().contains(totalSkills.get(index))) {
						if (inBounds(mouseX, mouseY, this.guiLeft + 8, this.guiTop + 37 + pos * 12, 9, 9)) {
							removeSkill(totalSkills.get(index));
						}
					}
					if (inBounds(mouseX, mouseY, this.guiLeft + 20, this.guiTop + 37 + pos * 12, 9, 9)) {
						selectSkill(index);
					}
				} else if (index >= (this.offset + Tabs.SKILLS.maxDisplayItems)) {
					break;
				}
			}
			if (inBounds(mouseX, mouseY, this.guiLeft + 20, this.guiTop + 133, 9, 9)) {
				this.toggleDeselectAllSkills();
			}
		}
	}

	private void drawBackgroundLayer() {
		this.mc.getTextureManager().bindTexture(gui_texture);

		GlStateManager.color(1f, 1f, 1f, 1f);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		//suppression
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft+180, this.guiTop + 5, 0);
		int tex_y = cultivation.getSuppress() ? 0 : 44;
		GlStateManager.scale(14f/44f, 14f/44f, 1);
		drawTexturedModalRect(0, 0, 200, tex_y, 36, 44);
		GlStateManager.popMatrix();

		//tabs selectors
		drawTexturedModalRect(this.guiLeft + 5, this.guiTop + 5, 104, 173, 68, 14);
		drawTexturedModalRect(this.guiLeft + 76, this.guiTop + 5, 104, 173, 68, 14);

		//scrollers
		if (this.displayItems > this.tab.maxDisplayItems) {
			drawTexturedModalRect(this.guiLeft + 172, this.guiTop + 36, 45, 142, 9, 9);
			drawTexturedModalRect(this.guiLeft + 172, this.guiTop + 36, 54, 142, 9, 9);
			drawTexturedModalRect(this.guiLeft + 172, this.guiTop + 118, 45, 142, 9, 9);
			drawTexturedModalRect(this.guiLeft + 172, this.guiTop + 118, 63, 142, 9, 9);
		}

		//tabs
		switch (this.tab) {
			case FOUNDATION:
				drawCultivationBackground();
				break;
			case TECHNIQUES:
				drawTechniquesBackground();
				break;
			case SKILLS:
				drawSkillsBackground();
				break;
		}
		double agilityModifier = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
		double dexterityModifier = player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue();
		double strengthModifier = player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();

		GL11.glColor4f(1f, 1f, 1f, 1f);
		int[] iconPos = new int[]{27, 27, 99, 108};
		int[] fills = new int[]{
				Math.min(27, (int) ((27f * cultivation.getSpeedHandicap()) / 100f)),
				Math.min(27, (int) (27f * cultivation.getMaxSpeed() / (agilityModifier * 0.005))),
				Math.min(27, (int) (27f * (cultivation.getHasteLimit() / (0.1f * (dexterityModifier * 0.5 + strengthModifier * 0.5))))),
				Math.min(27, (int) (27f * (cultivation.getJumpLimit()) / (0.05f * (agilityModifier * 0.3 + strengthModifier * 0.7))))
		};

		//Regulator bars
		for (int i = 0; i < 4; i++) {
			drawTexturedModalRect(this.guiLeft - 61, this.guiTop + i * 15, 0, 173, 61, 15); //bg
			drawTexturedModalRect(this.guiLeft - 61 + 23, this.guiTop + i * 15 + 4, 0, 188, 27, 7); //bar bg
			drawTexturedModalRect(this.guiLeft - 61 + 23, this.guiTop + i * 15 + 4, 27, 188, fills[i], 7); //bar fill
			drawTexturedModalRect(this.guiLeft - 61 + 13, this.guiTop + i * 15 + 3, 45, 164, 9, 9); //button bg
			drawTexturedModalRect(this.guiLeft - 61 + 51, this.guiTop + i * 15 + 3, 45, 164, 9, 9); //button bg
			drawTexturedModalRect(this.guiLeft - 61 + 13, this.guiTop + i * 15 + 3, 72, 164, 9, 9); //button icon -
			drawTexturedModalRect(this.guiLeft - 61 + 51, this.guiTop + i * 15 + 3, 90, 164, 9, 9); //button icon +
			drawTexturedModalRect(this.guiLeft - 61 + 3, this.guiTop + i * 15 + 3, iconPos[i], 164, 9, 9); // icon
		}
	}

	private void drawCultivationBackground () {
		int bodyProgress = (int)(124.0* cultivation.getBodyProgress()/cultivation.getBodyLevel().getProgressBySubLevel(cultivation.getBodySubLevel()) );
		drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 34, 0, 195, bodyProgress, 16); //dragon
		if(cultTech.getBodyTechnique()!=null) {
			int bodyTech = (int) (138.0 * cultTech.getBodyTechnique().getProficiency()
					/ cultTech.getBodyTechnique().getTechnique().getMaxProficiency());
			drawTexturedModalRect(this.guiLeft + 7, this.guiTop+61, 0, 161, bodyTech, 3);
		}
		int divineProgress = (int)(124.0* cultivation.getDivineProgress()/cultivation.getDivineLevel().getProgressBySubLevel(cultivation.getDivineSubLevel()) );
		drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 75, 0, 227, divineProgress, 16); //dragon
		if(cultTech.getDivineTechnique()!=null) {
			int divineTech = (int) (138.0 * cultTech.getDivineTechnique().getProficiency()
					/ cultTech.getDivineTechnique().getTechnique().getMaxProficiency());
			drawTexturedModalRect(this.guiLeft + 7, this.guiTop+102, 0, 161, divineTech, 3);
		}
		int essenceProgress = (int)(124.0* cultivation.getEssenceProgress()/cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel()) );
		drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 116, 0, 211, essenceProgress, 16); //dragon
		if(cultTech.getEssenceTechnique()!=null) {
			int essenceTech = (int) (138.0 * cultTech.getEssenceTechnique().getProficiency()
					/ cultTech.getEssenceTechnique().getTechnique().getMaxProficiency());
			drawTexturedModalRect(this.guiLeft + 7, this.guiTop+61, 0, 161, essenceTech, 3);
		}
	}

	private void drawTechniquesBackground() {
		List<KnownTechnique> toDisplay = new ArrayList<>();
		if (this.cultTech.getBodyTechnique() != null)
			toDisplay.add(this.cultTech.getBodyTechnique());
		if (this.cultTech.getEssenceTechnique() != null)
			toDisplay.add(this.cultTech.getEssenceTechnique());
		if (this.cultTech.getDivineTechnique() != null)
			toDisplay.add(this.cultTech.getDivineTechnique());
		for (KnownTechnique t : toDisplay) {
			int index = toDisplay.indexOf(t);
			if (index >= this.offset && index < (this.offset + Tabs.TECHNIQUES.maxDisplayItems)) {
				int progressFill = (int) (t.getProficiency() * 139.0 / t.getTechnique().getMaxProficiency());
				drawTexturedModalRect(this.guiLeft + 19, this.guiTop + 35 + index * 19 + 11, 0, 136, 139, 3);
				drawTexturedModalRect(this.guiLeft + 19, this.guiTop + 35 + index * 19 + 11, 0, 139, progressFill, 3);
				drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 35 + index * 19 + 4, 45, 142, 9, 9);
				drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 35 + index * 19 + 4, 72, 142, 9, 9);
			} else if (index >= (this.offset + Tabs.TECHNIQUES.maxDisplayItems)) {
				break;
			}
		}
	}

	private void drawSkillsBackground() {
		List<Skill> totalSkills = skillCap.getTotalKnowSkill(cultTech);
		for (Skill skill : totalSkills) {
			int index = totalSkills.indexOf(skill);
			if (index >= this.offset && index < (this.offset + Tabs.SKILLS.maxDisplayItems)) {
				int pos = index - this.offset;
				if (skillCap.getKnownSkills().contains(totalSkills.get(index))) {
					drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 37 + pos * 12, 45, 142, 9, 9);
					drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 37 + pos * 12, 72, 142, 9, 9);
				}
				if (skillCap.getSelectedSkills().contains(index)) {
					drawTexturedModalRect(this.guiLeft + 20, this.guiTop + 37 + pos * 12, 126, 142, 9, 9);
				} else {
					drawTexturedModalRect(this.guiLeft + 20, this.guiTop + 37 + pos * 12, 117, 142, 9, 9);
				}
			} else if (index >= (this.offset + Tabs.SKILLS.maxDisplayItems)) {
				break;
			}
		}
		if (skillCap.getSelectedSkills().isEmpty()) {
			drawTexturedModalRect(this.guiLeft + 20, this.guiTop + 133, 117, 142, 9, 9);
		} else {
			drawTexturedModalRect(this.guiLeft + 20, this.guiTop + 133, 126, 142, 9, 9);
		}
	}

	private void drawForegroundLayer() {
		//this.fontRenderer.drawString(cultivation.getEssenceLevel().getLevelName(cultivation.getEssenceSubLevel()), this.guiLeft + 6, this.guiTop + 7, 0xFFFFFF);

		//String display = String.format("Speed: %.3f (%d%%)", cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel()), WuxiaCraftConfig.speedHandicap);
		//this.fontRenderer.drawString(display, this.guiLeft + 6,this.guiTop + 35,4210752);
		//display = String.format("Strength: %.2f", cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel()));
		//this.fontRenderer.drawString(display, this.guiLeft + 6,this.guiTop + 45,4210752);

		float fontScale = 1f;
		//select tab
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft + 39, this.guiTop + 8, 0);
		GlStateManager.scale(fontScale, fontScale, 1);
		drawCenteredString(this.fontRenderer, "Cultivation", 0, 0, 0xFFFF00);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft + 110, this.guiTop + 8, 0);
		GlStateManager.scale(fontScale, fontScale, 1);
		drawCenteredString(this.fontRenderer, "Skills", 0, 0, 0xFFFFFF);
		GlStateManager.popMatrix();

		switch (this.tab) {
			case FOUNDATION:
				drawCultivationForeground();
				break;
			case TECHNIQUES:
				drawTechniquesForeground();
				break;
			case SKILLS:
				drawSkillsForeground();
				break;
		}

		List<String> barDescriptions = new ArrayList<>();
		barDescriptions.add(cultivation.getSpeedHandicap() + "%");
		barDescriptions.add(String.format("%.1f", cultivation.getMaxSpeed()));
		barDescriptions.add(String.format("%.1f", cultivation.getHasteLimit()));
		barDescriptions.add(String.format("%.1f", cultivation.getJumpLimit()));

		for (int i = 0; i < 4; i++) {
			this.fontRenderer.drawString(barDescriptions.get(i), this.guiLeft - 61 + 27, this.guiTop + 4 + i * 15, 0xEFEF00);
		}
	}

	public static String getShortNumberAmount(long amount) {
		String value = "";
		if (amount < 0) {
			value += "-";
		}
		amount = Math.abs(amount);
		if (amount < 1000) {
			value += amount;
		} else if (amount < 10000) {
			float mills = amount / 1000f;
			value += String.format("%.1fk", mills);
		} else if (amount < 100000) {
			float mills = amount / 1000f;
			value += String.format("%.0fk", mills);
		} else if (amount < 1000000) {
			float mills = amount / 1000000f;
			value += String.format("%.2fM", mills);
		} else if (amount < 10000000) {
			float mills = amount / 1000000f;
			value += String.format("%.1fM", mills);
		} else if (amount < 100000000) {
			float mills = amount / 1000000f;
			value += String.format("%.0fM", mills);
		} else if (amount < 1000000000) {
			float mills = amount / 1000000000f;
			value += String.format("%.2fG", mills);
		}
		return value;
	}


	private void drawCultivationForeground() {
		float fontScale = 0.8f;
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft + 13, this.guiTop + 25, 0);
		GlStateManager.scale(fontScale, fontScale, 1);
		this.fontRenderer.drawString(cultivation.getBodyLevel().getLevelName(cultivation.getBodySubLevel()), 0, 0, 0xFFFFFF);
		this.fontRenderer.drawStringWithShadow(String.format("%.2f",cultivation.getBodyProgress()), 20, 18, 0x00FF20);
		GlStateManager.popMatrix();
		if(cultTech.getBodyTechnique() != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.guiLeft + 13, this.guiTop + 52, 0);
			GlStateManager.scale(fontScale, fontScale, 1);
			this.fontRenderer.drawString(cultTech.getBodyTechnique().getTechnique().getName(), 0, 0, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft + 13, this.guiTop + 66, 0);
		GlStateManager.scale(fontScale, fontScale, 1);
		this.fontRenderer.drawString(cultivation.getDivineLevel().getLevelName(cultivation.getDivineSubLevel()), 0, 0, 0xFFFFFF);
		this.fontRenderer.drawStringWithShadow(String.format("%.2f",cultivation.getDivineProgress()), 20, 18, 0x00FF20);
		GlStateManager.popMatrix();
		if(cultTech.getDivineTechnique() != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.guiLeft + 13, this.guiTop + 93, 0);
			GlStateManager.scale(fontScale, fontScale, 1);
			this.fontRenderer.drawString(cultTech.getDivineTechnique().getTechnique().getName(), 0, 0, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft + 13, this.guiTop + 107, 0);
		GlStateManager.scale(fontScale, fontScale, 1);
		this.fontRenderer.drawString(cultivation.getEssenceLevel().getLevelName(cultivation.getEssenceSubLevel()), 0, 0, 0xFFFFFF);
		this.fontRenderer.drawStringWithShadow(String.format("%.2f",cultivation.getEssenceProgress()), 20, 18, 0x00FF20);
		GlStateManager.popMatrix();
		if(cultTech.getEssenceTechnique() != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.guiLeft + 13, this.guiTop + 134, 0);
			GlStateManager.scale(fontScale, fontScale, 1);
			this.fontRenderer.drawString(cultTech.getEssenceTechnique().getTechnique().getName(), 0, 0, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
	}

	private void drawTechniquesForeground() {
		List<KnownTechnique> toDisplay = new ArrayList<>();
		if (this.cultTech.getBodyTechnique() != null)
			toDisplay.add(this.cultTech.getBodyTechnique());
		if (this.cultTech.getEssenceTechnique() != null)
			toDisplay.add(this.cultTech.getEssenceTechnique());
		if (this.cultTech.getDivineTechnique() != null)
			toDisplay.add(this.cultTech.getDivineTechnique());
		for (KnownTechnique t : toDisplay) {
			int index = toDisplay.indexOf(t);
			if (index >= this.offset && index < (this.offset + Tabs.TECHNIQUES.maxDisplayItems)) {
				this.fontRenderer.drawString(t.getTechnique().getName(), this.guiLeft + 19, this.guiTop + 35 + index * 19 + 2, 0xFFFFFF);
			} else if (index >= (this.offset + Tabs.TECHNIQUES.maxDisplayItems)) {
				break;
			}
		}
	}

	private void drawSkillsForeground() {
		List<Skill> totalSkills = skillCap.getTotalKnowSkill(cultTech);
		for (Skill skill : totalSkills) {
			int index = totalSkills.indexOf(skill);
			if (index >= this.offset && index < (this.offset + Tabs.SKILLS.maxDisplayItems)) {
				int pos = index - this.offset;
				this.fontRenderer.drawString(skill.getName(), this.guiLeft + 31, this.guiTop + 37 + pos * 12, 0xFFFFFF);
			} else if (index >= (this.offset + Tabs.SKILLS.maxDisplayItems)) {
				break;
			}
		}
	}

	private void drawTooltips(int mouseX, int mouseY) {
		GlStateManager.color(1f, 1f, 1f, 1f);
		// suppress cultivation
		if (inBounds(mouseX, mouseY, this.guiLeft + 180, this.guiTop + 5, 12, 14)) {
			String line = "Suppress cultivation";
			drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 142);
			this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
		}
		//tabs
		switch (this.tab) {
			case FOUNDATION:
				//drawFoundationTooltips(mouseX, mouseY);
				break;
			case TECHNIQUES:
				drawTechniquesTooltips(mouseX, mouseY);
				break;
			case SKILLS:
				drawSkillsTooltips(mouseX, mouseY);
				break;
		}

		//suppress
		//if(inBounds(mouseX, mouseY, this.guiLeft+148, this.guiTop+23, 9,9)) {
		//	String line = "Suppress Cultivation";
		//	drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 142);
		//	this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
		//}
	}

	/*private void drawFoundationTooltips(int mouseX, int mouseY) {
		boolean drawing = false;
		String line = "";
		if(inBounds(mouseX, mouseY, this.guiLeft+16, this.guiTop+54, 54, 3)) {
			drawing = true;
			line = String.format("%d%%", (int)(this.foundation.getAgilityProgress()*100f/Foundation.getLevelMaxProgress(this.foundation.getAgility())));
		}
		else if(inBounds(mouseX, mouseY, this.guiLeft+114, this.guiTop+54, 54, 3)) {
			drawing = true;
			line = String.format("%d%%", (int)(this.foundation.getConstitutionProgress()*100f/Foundation.getLevelMaxProgress(this.foundation.getConstitution())));
		}
		else if(inBounds(mouseX, mouseY, this.guiLeft+14, this.guiTop+84, 54, 3)) {
			drawing = true;
			line = String.format("%d%%", (int)(this.foundation.getDexterityProgress()*100f/Foundation.getLevelMaxProgress(this.foundation.getDexterity())));
		}
		else if(inBounds(mouseX, mouseY, this.guiLeft+116, this.guiTop+84, 54, 3)) {
			drawing = true;
			line = String.format("%d%%", (int)(this.foundation.getResistanceProgress()*100f/Foundation.getLevelMaxProgress(this.foundation.getResistance())));
		}
		else if(inBounds(mouseX, mouseY, this.guiLeft+16, this.guiTop+114, 54, 3)) {
			drawing = true;
			line = String.format("%d%%", (int)(this.foundation.getSpiritProgress()*100f/Foundation.getLevelMaxProgress(this.foundation.getSpirit())));
		}
		else if(inBounds(mouseX, mouseY, this.guiLeft+114, this.guiTop+114, 54, 3)) {
			drawing = true;
			line = String.format("%d%%", (int)(this.foundation.getStrengthProgress()*100f/Foundation.getLevelMaxProgress(this.foundation.getStrength())));
		}
		else if(inBounds(mouseX, mouseY, this.guiLeft+36, this.guiTop+41, 44, 11)) {
			drawing = true;
			line = String.format("Agility: %d", this.foundation.getAgility());
		}
		else if(inBounds(mouseX, mouseY, this.guiLeft+104, this.guiTop+41, 44, 11)) {
			drawing = true;
			line = String.format("Constitution: %d", this.foundation.getConstitution());
		}
		else if(inBounds(mouseX, mouseY, this.guiLeft+34, this.guiTop+71, 44, 11)) {
			drawing = true;
			line = String.format("Dexterity: %d", this.foundation.getDexterity());
		}
		else if(inBounds(mouseX, mouseY, this.guiLeft+106, this.guiTop+71, 44, 11)) {
			drawing = true;
			line = String.format("Resistance: %d", this.foundation.getResistance());
		}
		else if(inBounds(mouseX, mouseY, this.guiLeft+36, this.guiTop+101, 44, 11)) {
			drawing = true;
			line = String.format("Spirit: %d", this.foundation.getSpirit());
		}
		else if(inBounds(mouseX, mouseY, this.guiLeft+104, this.guiTop+101, 44, 11)) {
			drawing = true;
			line = String.format("Strength: %d", this.foundation.getStrength());
		}
		if(drawing) {
			drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 142);
			this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
		}
	}*/

	private void drawTechniquesTooltips(int mouseX, int mouseY) {
		List<KnownTechnique> toDisplay = new ArrayList<>();
		if (this.cultTech.getBodyTechnique() != null)
			toDisplay.add(this.cultTech.getBodyTechnique());
		if (this.cultTech.getEssenceTechnique() != null)
			toDisplay.add(this.cultTech.getEssenceTechnique());
		if (this.cultTech.getDivineTechnique() != null)
			toDisplay.add(this.cultTech.getDivineTechnique());
		for (KnownTechnique t : toDisplay) {
			int index = toDisplay.indexOf(t);
			if (index >= this.offset && index < (this.offset + Tabs.TECHNIQUES.maxDisplayItems)) {
				int pos = index - this.offset;
				if (inBounds(mouseX, mouseY, this.guiLeft + 19, this.guiTop + 35 + pos * 19 + 11, 139, 3)) {
					String line = "No success";
					//TODO save this while loading the screen to save this loop every frame
					double highestFoundBeneath = 0; // in case checkpoints aren't in order
					for (Pair<Double, String> checkpoints : t.getTechnique().getCheckpoints()) {
						if (checkpoints.getKey() > highestFoundBeneath) {
							if (t.getProficiency() > checkpoints.getKey()) {
								highestFoundBeneath = checkpoints.getKey();
								line = checkpoints.getValue();
							}
						}
					}
					drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 142);
					this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
				}
			} else if (index >= (this.offset + Tabs.TECHNIQUES.maxDisplayItems)) {
				break;
			}
		}
	}

	@SuppressWarnings("unused")
	private void drawSkillsTooltips(int mouseX, int mouseY) {

	}

	private void toggleDeselectAllSkills() {
		if (skillCap.getSelectedSkills().isEmpty()) {
			int size = skillCap.getTotalKnowSkill(cultTech).size();
			for (int i = 0; i < size; i++) {
				skillCap.getSelectedSkills().add(i);
				skillCap.setActiveSkill(0);
			}
		} else {
			skillCap.getSelectedSkills().clear();
			skillCap.setActiveSkill(-1);
		}
		NetworkWrapper.INSTANCE.sendToServer(new SkillCapMessage(skillCap, false, player.getUniqueID()));
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
		NetworkWrapper.INSTANCE.sendToServer(new SkillCapMessage(skillCap, false, player.getUniqueID()));
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
		NetworkWrapper.INSTANCE.sendToServer(new SkillCapMessage(skillCap, false, player.getUniqueID()));
		this.offset = MathUtils.clamp(this.offset, 0, this.skillCap.getTotalKnowSkill(cultTech).size());
	}

	/*
	private void selectFoundationAttribute(int attribute) {
		MathUtils.clamp(attribute, 0, 5);
		if (foundation.getSelectedAttribute() == attribute) attribute = -1;
		this.foundation.selectAttribute(attribute);
		NetworkWrapper.INSTANCE.sendToServer(new SelectFoundationAttributeMessage(attribute, player.getUniqueID()));
	}*/

	/*private void addProgressToFoundation(int attribute) {
		double amount = cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()) * (amountAddedToFoundationPerClick / 100.0);
		if (cultivation.getCurrentProgress() < amountAddedToFoundationPerClick) {
			amount = cultivation.getCurrentProgress();
		}
		amount = Math.max(0, amount);
		cultivation.setProgress(Math.max(0, cultivation.getCurrentProgress()));
		cultivation.addProgress(-amount, false);
		switch (attribute) {
			case 0:
				foundation.addAgilityProgress(amount);
				break;
			case 1:
				foundation.addConstitutionProgress(amount);
				break;
			case 2:
				foundation.addDexterityProgress(amount);
				break;
			case 3:
				foundation.addResistanceProgress(amount);
				break;
			case 4:
				foundation.addSpiritProgress(amount);
				break;
			case 5:
				foundation.addStrengthProgress(amount);
				break;
		}
		foundation.keepMaxLevel(this.cultivation);
		NetworkWrapper.INSTANCE.sendToServer(new AddProgressToFoundationAttributeMessage(amount, attribute, player.getUniqueID()));
	}*/

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
				if (op == 0) WuxiaCraftConfig.speedHandicap = Math.max(0, WuxiaCraftConfig.speedHandicap - 5);
				if (op == 1) WuxiaCraftConfig.speedHandicap = Math.min(100, WuxiaCraftConfig.speedHandicap + 5);
				break;
			case 1:
				if (op == 0) WuxiaCraftConfig.maxSpeed -= 1f;
				if (op == 1) WuxiaCraftConfig.maxSpeed += 1f;
				break;
			case 2:
				if (op == 0) WuxiaCraftConfig.blockBreakLimit -= 1f;
				if (op == 1) WuxiaCraftConfig.blockBreakLimit += 1f;
				break;
			case 3:
				if (op == 0) WuxiaCraftConfig.jumpLimit -= 1f;
				if (op == 1) WuxiaCraftConfig.jumpLimit += 1f;
				break;
		}
		WuxiaCraftConfig.syncFromFields();
		WuxiaCraftConfig.syncCultivationFromConfigToClient();
		NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap, WuxiaCraftConfig.maxSpeed, WuxiaCraftConfig.blockBreakLimit, WuxiaCraftConfig.jumpLimit, player.getUniqueID()));
	}

	/*private void handlerPerClickKey(char typedChar, int keyCode) {
		if (MathUtils.between(keyCode, Keyboard.KEY_1, Keyboard.KEY_0) || keyCode == Keyboard.KEY_PERIOD ||
				MathUtils.between(keyCode, Keyboard.KEY_NUMPAD1, Keyboard.KEY_NUMPAD0) ||
				MathUtils.between(keyCode, Keyboard.KEY_NUMPAD4, Keyboard.KEY_NUMPAD6) ||
				MathUtils.between(keyCode, Keyboard.KEY_NUMPAD7, Keyboard.KEY_NUMPAD9) ||
				keyCode == Keyboard.KEY_DECIMAL) {
			this.perClickDisplay = this.perClickDisplay.substring(0, caretPosition) + typedChar + this.perClickDisplay.substring(caretPosition);
			this.caretPosition++;
		}
		if (keyCode == Keyboard.KEY_LEFT) {
			this.caretPosition = Math.max(0, this.caretPosition - 1);
		}
		if (keyCode == Keyboard.KEY_RIGHT) {
			this.caretPosition = Math.min(perClickDisplay.length() - 1, this.caretPosition + 1);
		}
		if (keyCode == Keyboard.KEY_BACK) {
			this.perClickDisplay = this.perClickDisplay.substring(0, Math.max(caretPosition - 1, 0)) + this.perClickDisplay.substring(caretPosition);
			this.caretPosition = Math.max(0, this.caretPosition-1);
		}
		if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_ESCAPE) {
			perClickLoseFocus();
		}
	}*/

	/*private void perClickOnFocus() {
		this.perClickFocus = true;
		this.perClickDisplay = String.format("%.1f_", amountAddedToFoundationPerClick);
		this.caretPosition = this.perClickDisplay.length() - 1;
	}*/

	/*private void perClickLoseFocus() {
		this.perClickFocus = false;
		this.caretPosition = 0;
		try {
			amountAddedToFoundationPerClick = Double.parseDouble(this.perClickDisplay.replace("|","").replace("_",""));
		} catch (NumberFormatException e) {
			WuxiaCraft.logger.error("Couldn't convert back the number");
		}
	}*/

	public enum Tabs {
		FOUNDATION(0),
		TECHNIQUES(5),
		SKILLS(7);

		public final int maxDisplayItems;

		Tabs(int maxDisplayItems) {
			this.maxDisplayItems = maxDisplayItems;
		}
	}


}
