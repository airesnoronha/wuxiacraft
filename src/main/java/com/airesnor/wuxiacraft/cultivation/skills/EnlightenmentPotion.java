package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnlightenmentPotion extends Potion {

	private static final ResourceLocation TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/effects/enlightenment.png");

	public EnlightenmentPotion(String name) {
		super(false, 0xDFDFDF);
		this.setRegistryName(new ResourceLocation(WuxiaCraft.MOD_ID, name));
		this.setPotionName("effect." + name);
		this.setBeneficial();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasStatusIcon() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		GlStateManager.pushMatrix();
		mc.renderEngine.bindTexture(TEXTURE);
		GlStateManager.translate(x+3, y+3, 0);
		GlStateManager.scale(1/16f, 1/16f, 1);
		mc.ingameGUI.drawTexturedModalRect(0,0,0,0,256,256);
		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
		GlStateManager.pushMatrix();
		mc.renderEngine.bindTexture(TEXTURE);
		GlStateManager.translate(x+4, y+4, 0);
		GlStateManager.scale(1/16f, 1/16f, 1);
		GlStateManager.color(1f,1f,1f,alpha);
		mc.ingameGUI.drawTexturedModalRect(1+3,1+3,0,0,255,255);
		GlStateManager.popMatrix();
	}
}
