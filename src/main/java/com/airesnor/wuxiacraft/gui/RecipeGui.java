package com.airesnor.wuxiacraft.gui;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.items.ItemRecipe;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class RecipeGui extends GuiScreen {

	private static final ResourceLocation RECIPE_GUI_TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/recipe_gui.png");

	private ItemStack itemRecipe = null;

	private final int xSize = 228;
	private final int ySize = 178;
	private int guiTop = 0;
	private int guiLeft = 0;

	public RecipeGui(EntityPlayer player) {
		if(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemRecipe) {
			this.itemRecipe = player.getHeldItem(EnumHand.MAIN_HAND);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackgroundLayer();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawForegroundLayer();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			this.mc.player.closeScreen();
		}
	}

	private void drawBackgroundLayer() {
		mc.renderEngine.bindTexture(RECIPE_GUI_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft, this.guiTop, 0);
		drawTexturedModalRect(-19, 0, 0, 0, 256, this.ySize);
		GlStateManager.popMatrix();
	}

	private void drawForegroundLayer() {
		if(this.itemRecipe != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.guiLeft, this.guiTop, 0);
			NBTTagCompound tag =  itemRecipe.getTagCompound();
			if(tag!=null) {
				String name = tag.getString("recipe-name");
				int nameWidth = mc.fontRenderer.getStringWidth(name);
				mc.fontRenderer.drawString(name, (this.xSize - nameWidth) / 2, 10, 0x101010);
				int items = tag.getInteger("recipe-count");
				for(int i = 0; i < items; i ++) {
					String line = "";
					if(tag.hasKey("item-"+i)) {
						ItemStack stack = new ItemStack(tag.getCompoundTag("item-" + i));
						line += stack.getDisplayName();
					}
					line += " -> ";
					if(tag.hasKey("temp-"+i)) {
						ItemRecipe.RecipeTemperature temperature = ItemRecipe.RecipeTemperature.valueOf(tag.getString("temp-" + i));
						line += temperature.getName();
					}
					mc.fontRenderer.drawString(line, 10, (i+2)*10, 0x101010);
				}
			}
			GlStateManager.popMatrix();
		}
	}
}
