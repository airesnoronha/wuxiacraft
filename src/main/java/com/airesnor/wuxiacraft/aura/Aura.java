package com.airesnor.wuxiacraft.aura;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Aura {

	protected String name;

	public Aura(String name) {
		this.name = name;
		registerSelf();
	}

	protected void registerSelf() {
		Auras.AURAS.put(new ResourceLocation(WuxiaCraft.MOD_ID, this.name), this);
	}

	@SideOnly(Side.CLIENT)
	public abstract void renderPre(double x, double y, double z);

	@SideOnly(Side.CLIENT)
	public abstract void renderPost(double x, double y, double z);

	@SideOnly(Side.CLIENT)
	public void update() {
	}
}
