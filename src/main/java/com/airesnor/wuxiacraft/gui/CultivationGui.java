package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.BaseSystemLevel;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.input.Keyboard;
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

	public enum RequireConfirmAction {
		REMOVE_BODY_TECHNIQUE,
		REMOVE_DIVINE_TECHNIQUE,
		REMOVE_ESSENCE_TECHNIQUE,
		REMOVE_SKILL
	}

	public int confirmActionMeta = 0;
	private RequireConfirmAction requireConfirmAction = RequireConfirmAction.REMOVE_BODY_TECHNIQUE;
	private boolean confirmScreen = false;

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
		if (confirmScreen) {
			drawConfirmScreenBackground();
			drawConfirmScreenForeground();
		}
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
		if (!confirmScreen) {

			//offset --
			if (inBounds(mouseX, mouseY, this.guiLeft + 172, this.guiTop + 36, 9, 9)) {
				this.offset = Math.max(0, this.offset - 1);
			}

			//offset ++
			else if (inBounds(mouseX, mouseY, this.guiLeft + 172, this.guiTop + 118, 9, 9)) {
				this.offset = Math.min(this.displayItems - this.tab.maxDisplayItems, this.offset + 1);
			}

			//suppress
			else if (inBounds(mouseX, mouseY, this.guiLeft + 180, this.guiTop + 5, 12, 14)) {
				cultivation.setSuppress(!cultivation.getSuppress());
				NetworkWrapper.INSTANCE.sendToServer(new SuppressCultivationMessage(cultivation.getSuppress(), player.getUniqueID()));
			}

			//select tabs
			else if (inBounds(mouseX, mouseY, this.guiLeft + 5, this.guiTop + 5, 68, 14)) {
				this.changeToTab(Tabs.FOUNDATION);
			}
			//if (inBounds(mouseX, mouseY, this.guiLeft + 61, this.guiTop + 19, 52, 15)) {
			//	this.changeToTab(Tabs.TECHNIQUES);
			//}
			else if (inBounds(mouseX, mouseY, this.guiLeft + 76, this.guiTop + 5, 68, 14)) {
				this.changeToTab(Tabs.SKILLS);
			}

			//tabs
			switch (this.tab) {
				case FOUNDATION:
					handleMouseCultivation(mouseX, mouseY);
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
			for (int i = 0; i < 5; i++) {
				boolean shiftModifier = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
				if (inBounds(mouseX, mouseY, this.guiLeft - 61 + 13, this.guiTop + i * 15 + 3, 9, 9)) {
					handleBarButtonClick(i, 0, shiftModifier);
				} else if (inBounds(mouseX, mouseY, this.guiLeft - 61 + 51, this.guiTop + i * 15 + 3, 9, 9)) {
					handleBarButtonClick(i, 1, shiftModifier);
				}
			}
		} else {
			if (inBounds(mouseX, mouseY, this.guiLeft + 63, this.guiTop + 80, 30, 12)) {
				handleConfirmScreenYes();
			} else if (inBounds(mouseX, mouseY, this.guiLeft + 108, this.guiTop + 80, 30, 12)) {
				this.confirmScreen = false;
				this.confirmActionMeta = 0;
			}
		}
	}

	private void handleConfirmScreenYes() {
		switch (this.requireConfirmAction) {
			case REMOVE_BODY_TECHNIQUE:
				NetworkWrapper.INSTANCE.sendToServer(new RemoveTechniqueMessage(this.cultTech.getBodyTechnique().getTechnique(), this.player.getUniqueID()));
				this.cultTech.setBodyTechnique(null);
				break;
			case REMOVE_DIVINE_TECHNIQUE:
				NetworkWrapper.INSTANCE.sendToServer(new RemoveTechniqueMessage(this.cultTech.getDivineTechnique().getTechnique(), this.player.getUniqueID()));
				this.cultTech.setDivineTechnique(null);
				break;
			case REMOVE_ESSENCE_TECHNIQUE:
				NetworkWrapper.INSTANCE.sendToServer(new RemoveTechniqueMessage(this.cultTech.getEssenceTechnique().getTechnique(), this.player.getUniqueID()));
				this.cultTech.setEssenceTechnique(null);
				break;
			case REMOVE_SKILL:
				break;
		}
		this.confirmScreen = false;
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

	private void handleMouseCultivation(int mouseX, int mouseY) {
		if (inBounds(mouseX, mouseY, this.guiLeft + 7, this.guiTop + 51, 9, 9)) {
			if (this.cultTech.getBodyTechnique() != null) {
				this.confirmScreen = true;
				requireConfirmAction = RequireConfirmAction.REMOVE_BODY_TECHNIQUE;
				confirmActionMeta = 0;
			}
		}
		if (inBounds(mouseX, mouseY, this.guiLeft + 7, this.guiTop + 92, 9, 9)) {
			if (this.cultTech.getDivineTechnique() != null) {
				this.confirmScreen = true;
				requireConfirmAction = RequireConfirmAction.REMOVE_DIVINE_TECHNIQUE;
				confirmActionMeta = 0;
			}
		}
		if (inBounds(mouseX, mouseY, this.guiLeft + 7, this.guiTop + 133, 9, 9)) {
			if (this.cultTech.getEssenceTechnique() != null) {
				this.confirmScreen = true;
				requireConfirmAction = RequireConfirmAction.REMOVE_ESSENCE_TECHNIQUE;
				confirmActionMeta = 0;
			}
		}
	}

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
						if (inBounds(mouseX, mouseY, this.guiLeft + 8, this.guiTop + 25 + pos * 12, 9, 9)) {
							removeSkill(totalSkills.get(index));
						}
					}
					if (inBounds(mouseX, mouseY, this.guiLeft + 20, this.guiTop + 25 + pos * 12, 9, 9)) {
						selectSkill(index);
					}
				} else if (index >= (this.offset + Tabs.SKILLS.maxDisplayItems)) {
					break;
				}
			}
			if (inBounds(mouseX, mouseY, this.guiLeft + 20, this.guiTop + 155, 9, 9)) {
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
		GlStateManager.translate(this.guiLeft + 180, this.guiTop + 5, 0);
		int tex_y = cultivation.getSuppress() ? 0 : 44;
		GlStateManager.scale(14f / 44f, 14f / 44f, 1);
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
		double agilityModifier = CultivationUtils.getAgilityFromEntity(player);
		double dexterityModifier = CultivationUtils.getDexterityFromEntity(player);
		double strengthModifier = CultivationUtils.getStrengthFromEntity(player);
		double maxSpeed = cultivation.getAgilityModifier() * (1 + cultTech.getOverallModifiers().movementSpeed);

		GL11.glColor4f(1f, 1f, 1f, 1f);
		int[] iconPos = new int[]{144, 27, 99, 108, 135};
		int[] fills = new int[]{
				Math.min(27, (int) ((27f * cultivation.getHandicap()) / 100f)),
				Math.min(27, (int) (27f * cultivation.getMaxSpeed() / maxSpeed)),
				Math.min(27, (int) (27f * (cultivation.getHasteLimit() / (0.1f * (strengthModifier * 0.7 + dexterityModifier * 0.3))))),
				Math.min(27, (int) (27f * (cultivation.getJumpLimit()) / (0.08f * (agilityModifier + strengthModifier)))),
				Math.min(27, (int) (27f * (cultivation.getStepAssistLimit()) / (0.6f * (1 + 0.15f * (float) (agilityModifier + dexterityModifier + strengthModifier)))))
		};

		//Regulator bars
		for (int i = 0; i < 5; i++) {
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

	private void drawCultivationBackground() {
		if (cultivation.getEssenceLevel() == BaseSystemLevel.DEFAULT_ESSENCE_LEVEL) {
			double foundationOverMaxBase = cultivation.getEssenceFoundation() / cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel());
			drawFoundationAtPosition(this.guiLeft + 134, this.guiTop + 107, foundationOverMaxBase);
		} else {
			//body
			int bodyProgress = (int) Math.min(124, (124.0 * cultivation.getBodyProgress() / cultivation.getBodyLevel().getProgressBySubLevel(cultivation.getBodySubLevel())));
			drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 34, 0, 195, bodyProgress, 16); //dragon
			if (cultTech.getBodyTechnique() != null) {
				drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 51, 45, 164, 9, 9); //rem button bg
				drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 51, 72, 164, 9, 9); //rem button
				int bodyTech = (int) (138.0 * cultTech.getBodyTechnique().getProficiency()
						/ cultTech.getBodyTechnique().getTechnique().getMaxProficiency());
				drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 61, 0, 161, bodyTech, 3);
			}
			double foundationOverMaxBase = cultivation.getBodyFoundation() / cultivation.getBodyLevel().getProgressBySubLevel(cultivation.getBodySubLevel());
			drawFoundationAtPosition(this.guiLeft + 134, this.guiTop + 25, foundationOverMaxBase);
			//divine
			int divineProgress = (int) Math.min(124, (124.0 * cultivation.getDivineProgress() / cultivation.getDivineLevel().getProgressBySubLevel(cultivation.getDivineSubLevel())));
			drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 75, 0, 227, divineProgress, 16); //dragon
			if (cultTech.getDivineTechnique() != null) {
				drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 92, 45, 164, 9, 9); //rem button bg
				drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 92, 72, 164, 9, 9); //rem button
				int divineTech = (int) (138.0 * cultTech.getDivineTechnique().getProficiency()
						/ cultTech.getDivineTechnique().getTechnique().getMaxProficiency());
				drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 102, 0, 161, divineTech, 3);
			}
			foundationOverMaxBase = cultivation.getDivineFoundation() / cultivation.getDivineLevel().getProgressBySubLevel(cultivation.getDivineSubLevel());
			drawFoundationAtPosition(this.guiLeft + 134, this.guiTop + 66, foundationOverMaxBase);
			//essence
			int essenceProgress = (int) Math.min(124, (124.0 * cultivation.getEssenceProgress() / cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel())));
			drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 116, 0, 211, essenceProgress, 16); //dragon
			if (cultTech.getEssenceTechnique() != null) {
				drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 133, 45, 164, 9, 9); //rem button bg
				drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 133, 72, 164, 9, 9); //rem button
				int essenceTech = (int) (138.0 * cultTech.getEssenceTechnique().getProficiency()
						/ cultTech.getEssenceTechnique().getTechnique().getMaxProficiency());
				drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 143, 0, 161, essenceTech, 3);
			}
			foundationOverMaxBase = cultivation.getEssenceFoundation() / cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel());
			drawFoundationAtPosition(this.guiLeft + 134, this.guiTop + 107, foundationOverMaxBase);
		}
	}

	private void drawFoundationAtPosition(int x, int y, double foundationOverMaxBase) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		if (foundationOverMaxBase < 1) {
			int fillFactor = (int) (30 * foundationOverMaxBase);
			drawTexturedModalRect(0, 30 - fillFactor, 125, 187 + 30 - fillFactor, 30, fillFactor); //red
		} else if (MathUtils.between(foundationOverMaxBase, 1, 3)) {
			int fillFactor = (int) (30 * (foundationOverMaxBase - 1) / 2);
			drawTexturedModalRect(0, 0, 125, 187, 30, 30); //red
			drawTexturedModalRect(0, 30 - fillFactor, 155, 187 + 30 - fillFactor, 30, fillFactor); //yellow
		} else if (MathUtils.between(foundationOverMaxBase, 3, 6)) {
			int fillFactor = (int) (30 * (foundationOverMaxBase - 3) / 3);
			drawTexturedModalRect(0, 0, 155, 187, 30, 30); //yellow
			drawTexturedModalRect(0, 30 - fillFactor, 185, 187 + 30 - fillFactor, 30, fillFactor); // green
		} else if (MathUtils.between(foundationOverMaxBase, 6, 10)) {
			int fillFactor = (int) (30 * (foundationOverMaxBase - 6) / 4);
			drawTexturedModalRect(0, 0, 185, 187, 30, 30); //green
			drawTexturedModalRect(0, 30 - fillFactor, 215, 187 + 30 - fillFactor, 30, fillFactor); // blue
		} else if (MathUtils.between(foundationOverMaxBase, 10, 20)) {
			int fillFactor = (int) (30 * (foundationOverMaxBase - 10) / 10);
			drawTexturedModalRect(0, 0, 215, 187, 30, 30); //blue
			drawTexturedModalRect(0, 30 - fillFactor, 125, 217 + 30 - fillFactor, 30, fillFactor); // purple
		} else if (foundationOverMaxBase > 20) {
			int fillFactor = (int) (30 * Math.min(1, (foundationOverMaxBase - 20) / 80));
			drawTexturedModalRect(0, 0, 125, 217, 30, 30); // purple
			drawTexturedModalRect(0, 30 - fillFactor, 155, 217 + 30 - fillFactor, 30, fillFactor); // white
		}
		GlStateManager.popMatrix();

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
					drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 25 + pos * 12, 45, 164, 9, 9);
					drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 25 + pos * 12, 72, 164, 9, 9);
				}
				if (skillCap.getSelectedSkills().contains(index)) {
					drawTexturedModalRect(this.guiLeft + 20, this.guiTop + 25 + pos * 12, 126, 164, 9, 9);
				} else {
					drawTexturedModalRect(this.guiLeft + 20, this.guiTop + 25 + pos * 12, 117, 164, 9, 9);
				}
			} else if (index >= (this.offset + Tabs.SKILLS.maxDisplayItems)) {
				break;
			}
		}
		if (skillCap.getSelectedSkills().isEmpty()) {
			drawTexturedModalRect(this.guiLeft + 20, this.guiTop + 155, 117, 164, 9, 9);
		} else {
			drawTexturedModalRect(this.guiLeft + 20, this.guiTop + 155, 126, 164, 9, 9);
		}
	}

	private void drawConfirmScreenBackground() {
		GlStateManager.color(0.8f, 0.8f, 0.7f, 1.0f);
		drawFramedBox(this.guiLeft + 50, this.guiTop + 50, 100, 55, 3, 81, 164); //whole box bg
		drawFramedBox(this.guiLeft + 63, this.guiTop + 80, 30, 12, 3, 45, 164); //yes button bg
		drawFramedBox(this.guiLeft + 108, this.guiTop + 80, 30, 12, 3, 45, 164); //no button bg
		GlStateManager.color(1f, 1f, 1f, 1f);

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
		drawCenteredString(this.fontRenderer, "Skills", 0, 0, 0xFFFF00);
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
		barDescriptions.add(cultivation.getHandicap() + "%");
		barDescriptions.add(String.format("%.1f", cultivation.getMaxSpeed()));
		barDescriptions.add(String.format("%.1f", cultivation.getHasteLimit()));
		barDescriptions.add(String.format("%.1f", cultivation.getJumpLimit()));
		barDescriptions.add(String.format("%.1f", cultivation.getStepAssistLimit()));

		for (int i = 0; i < 5; i++) {
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
		float fontScale = 0.9f;
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft + 13, this.guiTop + 25, 0);
		GlStateManager.scale(fontScale, fontScale, 1);
		this.fontRenderer.drawString(cultivation.getBodyLevel().getLevelName(cultivation.getBodySubLevel()), 0, 0, 0xFFFFFF);
		//this.fontRenderer.drawStringWithShadow(String.format("%.2f %.2f", cultivation.getBodyProgress(), cultivation.getBodyFoundation()), 20, 18, 0x00FF20);
		GlStateManager.popMatrix();
		if (cultTech.getBodyTechnique() != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.guiLeft + 21, this.guiTop + 52, 0);
			GlStateManager.scale(fontScale, fontScale, 1);
			this.fontRenderer.drawString(cultTech.getBodyTechnique().getTechnique().getName(), 0, 0, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft + 13, this.guiTop + 66, 0);
		GlStateManager.scale(fontScale, fontScale, 1);
		this.fontRenderer.drawString(cultivation.getDivineLevel().getLevelName(cultivation.getDivineSubLevel()), 0, 0, 0xFFFFFF);
		//this.fontRenderer.drawStringWithShadow(String.format("%.2f %.2f", cultivation.getDivineProgress(), cultivation.getDivineFoundation()), 20, 18, 0x00FF20);
		GlStateManager.popMatrix();
		if (cultTech.getDivineTechnique() != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.guiLeft + 21, this.guiTop + 93, 0);
			GlStateManager.scale(fontScale, fontScale, 1);
			this.fontRenderer.drawString(cultTech.getDivineTechnique().getTechnique().getName(), 0, 0, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft + 13, this.guiTop + 107, 0);
		GlStateManager.scale(fontScale, fontScale, 1);
		this.fontRenderer.drawString(cultivation.getEssenceLevel().getLevelName(cultivation.getEssenceSubLevel()), 0, 0, 0xFFFFFF);
		//this.fontRenderer.drawStringWithShadow(String.format("%.2f %.2f", cultivation.getEssenceProgress(), cultivation.getEssenceFoundation()), 20, 18, 0x00FF20);
		GlStateManager.popMatrix();
		if (cultTech.getEssenceTechnique() != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.guiLeft + 21, this.guiTop + 134, 0);
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
				this.fontRenderer.drawString(skill.getName(), this.guiLeft + 31, this.guiTop + 25 + pos * 12, 0xFFFFFF);
			} else if (index >= (this.offset + Tabs.SKILLS.maxDisplayItems)) {
				break;
			}
		}
	}

	private void drawConfirmScreenForeground() {
		this.drawCenteredString(this.fontRenderer, "Are you sure?", this.guiLeft + 100, this.guiTop + 60, 0xFFFF00);

		this.drawCenteredString(this.fontRenderer, "Yes", this.guiLeft + 78, this.guiTop + 82, 0xFFFFFF);

		this.drawCenteredString(this.fontRenderer, "No", this.guiLeft + 123, this.guiTop + 82, 0xFFFFFF);

	}

	private void drawTooltips(int mouseX, int mouseY) {
		GlStateManager.color(1f, 1f, 1f, 1f);
		// suppress cultivation
		if (!confirmScreen) {
			if (inBounds(mouseX, mouseY, this.guiLeft + 180, this.guiTop + 5, 12, 14)) {
				String line = "Suppress cultivation";
				drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 164);
				this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
			}
			//tabs
			switch (this.tab) {
				case FOUNDATION:
					drawCultivationTooltips(mouseX, mouseY);
					break;
				case TECHNIQUES:
					drawTechniquesTooltips(mouseX, mouseY);
					break;
				case SKILLS:
					drawSkillsTooltips(mouseX, mouseY);
					break;
			}
		}
		//suppress
		//if(inBounds(mouseX, mouseY, this.guiLeft+148, this.guiTop+23, 9,9)) {
		//	String line = "Suppress Cultivation";
		//	drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 142);
		//	this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
		//}
	}

	private void drawCultivationTooltips(int mouseX, int mouseY) {
		boolean drawing = false;
		String line = "";
		//% first
		if (inBounds(mouseX, mouseY, this.guiLeft + 7, this.guiTop + 34, 124, 16)) {
			drawing = true;
			line = String.format("%.2f%%", this.cultivation.getBodyProgress() * 100.0 / this.cultivation.getBodyLevel().getProgressBySubLevel(this.cultivation.getBodySubLevel()));
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 7, this.guiTop + 75, 124, 16)) {
			drawing = true;
			line = String.format("%.2f%%", this.cultivation.getDivineProgress() * 100.0 / this.cultivation.getDivineLevel().getProgressBySubLevel(this.cultivation.getDivineSubLevel()));
		} else if (inBounds(mouseX, mouseY, this.guiLeft + 7, this.guiTop + 116, 124, 16)) {
			drawing = true;
			line = String.format("%.2f%%", this.cultivation.getEssenceProgress() * 100.0 / this.cultivation.getEssenceLevel().getProgressBySubLevel(this.cultivation.getEssenceSubLevel()));
		}
		//techniques 2nd
		if (inBounds(mouseX, mouseY, this.guiLeft + 7, this.guiTop + 52, 138, 12)) {
			if (this.cultTech.getBodyTechnique() != null) {
				drawing = true;
				line = this.cultTech.getBodyTechnique().getCurrentCheckpoint();
			}
		}
		if (inBounds(mouseX, mouseY, this.guiLeft + 7, this.guiTop + 93, 138, 12)) {
			if (this.cultTech.getDivineTechnique() != null) {
				drawing = true;
				line = this.cultTech.getDivineTechnique().getCurrentCheckpoint();
			}
		}
		if (inBounds(mouseX, mouseY, this.guiLeft + 7, this.guiTop + 134, 138, 12)) {
			if (this.cultTech.getEssenceTechnique() != null) {
				drawing = true;
				line = this.cultTech.getEssenceTechnique().getCurrentCheckpoint();
			}
		}
		// foundation third
		if (inBounds(mouseX, mouseY, this.guiLeft + 134, this.guiTop + 25, 30, 30)) {
			double foundationOverMaxBase = cultivation.getBodyFoundation() / cultivation.getBodyLevel().getProgressBySubLevel(cultivation.getBodySubLevel());
			drawing = true;
			if (foundationOverMaxBase < 1) {
				line = "Unstable Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 1, 3)) {
				line = "Weak Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 3, 6)) {
				line = "Average Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 6, 10)) {
				line = "Strong Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 10, 20)) {
				line = "Unbreakable Foundation";
			} else if (foundationOverMaxBase > 20) {
				line = "Peerless Foundation";
			}
		}
		if (inBounds(mouseX, mouseY, this.guiLeft + 134, this.guiTop + 66, 30, 30)) {
			double foundationOverMaxBase = cultivation.getDivineFoundation() / cultivation.getDivineLevel().getProgressBySubLevel(cultivation.getDivineSubLevel());
			drawing = true;
			if (foundationOverMaxBase < 1) {
				line = "Unstable Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 1, 3)) {
				line = "Weak Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 3, 6)) {
				line = "Average Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 6, 10)) {
				line = "Strong Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 10, 20)) {
				line = "Unbreakable Foundation";
			} else if (foundationOverMaxBase > 20) {
				line = "Peerless Foundation";
			}
		}
		if (inBounds(mouseX, mouseY, this.guiLeft + 134, this.guiTop + 107, 30, 30)) {
			double foundationOverMaxBase = cultivation.getEssenceFoundation() / cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel());
			drawing = true;
			if (foundationOverMaxBase < 1) {
				line = "Unstable Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 1, 3)) {
				line = "Weak Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 3, 6)) {
				line = "Average Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 6, 10)) {
				line = "Strong Foundation";
			} else if (MathUtils.between(foundationOverMaxBase, 10, 20)) {
				line = "Unbreakable Foundation";
			} else if (foundationOverMaxBase > 20) {
				line = "Peerless Foundation";
			}
		}
		if (drawing) {
			drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 164);
			this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
		}
	}

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
					double highestFoundBeneath = 0; // in case checkpoints aren't in order
					for (Triple<Double, Float, String> checkpoints : t.getTechnique().getCheckpoints()) {
						if (checkpoints.getLeft() > highestFoundBeneath) {
							if (t.getProficiency() > checkpoints.getLeft()) {
								highestFoundBeneath = checkpoints.getLeft();
								line = checkpoints.getRight();
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

	private void drawSkillsTooltips(int mouseX, int mouseY) {
		boolean drawing = false;
		String line = "";
		if (inBounds(mouseX, mouseY, this.guiLeft + 20, this.guiTop + 155, 9, 9)) {
			drawing = true;
			line = skillCap.getSelectedSkills().size() > 0 ? "Deselect Skills" : "Select All Skills";
		}
		if (drawing) {
			drawFramedBox(mouseX + 6, mouseY - 1, 8 + fontRenderer.getStringWidth(line), 17, 3, 81, 164);
			this.fontRenderer.drawString(line, mouseX + 10, mouseY + 3, 0xFFFFFF);
		}
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

	private void handleBarButtonClick(int prop, int op, boolean shiftModifier) {
		float step = shiftModifier ? 1.0f : 0.1f;
		switch (prop) {
			case 0:
				if (op == 0) WuxiaCraftConfig.speedHandicap = Math.max(0, WuxiaCraftConfig.speedHandicap - 5);
				if (op == 1) WuxiaCraftConfig.speedHandicap = Math.min(100, WuxiaCraftConfig.speedHandicap + 5);
				break;
			case 1:
				if (op == 0) WuxiaCraftConfig.maxSpeed -= step;
				if (op == 1) WuxiaCraftConfig.maxSpeed += step;
				break;
			case 2:
				if (op == 0) WuxiaCraftConfig.blockBreakLimit -= step;
				if (op == 1) WuxiaCraftConfig.blockBreakLimit += step;
				break;
			case 3:
				if (op == 0) WuxiaCraftConfig.jumpLimit -= step;
				if (op == 1) WuxiaCraftConfig.jumpLimit += step;
				break;
			case 4:
				if (op == 0) WuxiaCraftConfig.stepAssistLimit -= step;
				if (op == 1) WuxiaCraftConfig.stepAssistLimit += step;
				break;
		}
		WuxiaCraftConfig.syncFromFields();
		WuxiaCraftConfig.syncCultivationFromConfigToClient();
		NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap, WuxiaCraftConfig.maxSpeed, WuxiaCraftConfig.blockBreakLimit, WuxiaCraftConfig.jumpLimit, WuxiaCraftConfig.stepAssistLimit, player.getUniqueID()));
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
		SKILLS(10);

		public final int maxDisplayItems;

		Tabs(int maxDisplayItems) {
			this.maxDisplayItems = maxDisplayItems;
		}
	}


}
