package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.math.BigDecimal;
import java.util.HashMap;

public class ElementSystemConverter extends ElementalConverter {

	/**
	 * the system to be converted to
	 */
	public System system;

	@SuppressWarnings("rawtypes")
	private final static HashMap<Class, Integer> priority = new HashMap<>();

	static {
		priority.put(ElementSystemConverter.class, -2);
		priority.put(SystemGather.class, -1);
	}

	public ElementSystemConverter(double amount, ResourceLocation element, System system) {
		super(amount, element);
		this.system = system;
	}

	@Override
	public void convert(double converted, HashMap<String, Object> metaData, BigDecimal proficiency) {
		String systemRawBase = system.name().toLowerCase() + "-raw-cultivation-base";
		String elementBonus = "element-" + this.element.getPath();
		var modifier = this.getCurrentCheckpoint(proficiency).modifier().doubleValue();
		converted = converted * (1 + modifier);
		metaData.put(systemRawBase, (double) metaData.getOrDefault(systemRawBase, 0d) + converted);
		metaData.put(elementBonus, (double) metaData.getOrDefault(elementBonus, 0d) + converted);
	}

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if (aspect instanceof ElementSystemConverter con) {
			return con.element.equals(this.element);
		}
		if (aspect instanceof SystemGather) return true;
		return super.canConnect(aspect);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
		var nameLocation = this.getRegistryName();
		if (nameLocation == null) return;
		var name = new TranslatableComponent("wuxiacraft.aspect." + nameLocation.getPath() + ".name");
		var amount = String.format(" ->     (%.1f)", this.amount);

		var font = Minecraft.getInstance().font;
		var nameWidth = font.width(name);
		var amountWidth = font.width(amount);
		var tooTipWidth = Math.max(nameWidth + 10, 28 + amountWidth);
		RenderSystem.enableBlend();
		GuiComponent.fill(poseStack, mouseX, mouseY, mouseX + tooTipWidth, mouseY + 31, 0x8A8080A0);
		RenderSystem.setShaderTexture(0, new ResourceLocation(this.element.getNamespace(), "textures/elements/" + this.element.getPath() + ".png"));
		RenderSystem.enableBlend();
		GuiComponent.blit(poseStack, mouseX + 5, mouseY + 13, 16, 16, 0, 0, 16, 16, 16, 16);
		RenderSystem.setShaderTexture(0, new ResourceLocation(this.element.getNamespace(), "textures/systems/" + this.system.name().toLowerCase() + ".png"));
		GuiComponent.blit(poseStack, mouseX + 38, mouseY + 13, 16, 16, 0, 0, 16, 16, 16, 16);
		font.drawShadow(poseStack, name, mouseX + 5, mouseY + 2, 0xFFFFFF);
		font.drawShadow(poseStack, amount, mouseX + 23, mouseY + 18, 0xFFFFFF);
		RenderSystem.disableBlend();
	}

	@Override
	public int connectPrioritySorter(TechniqueAspect aspect1, TechniqueAspect aspect2) {
		int priority1 = priority.getOrDefault(aspect1.getClass(), 0);
		int priority2 = priority.getOrDefault(aspect2.getClass(), 0);
		int finalPriority = priority1 - priority2;
		return finalPriority != 0 ? finalPriority / Math.abs(finalPriority) : 0;
	}

	@Override
	public boolean canShowForSystem(System system) {
		return system == this.system;
	}
}
