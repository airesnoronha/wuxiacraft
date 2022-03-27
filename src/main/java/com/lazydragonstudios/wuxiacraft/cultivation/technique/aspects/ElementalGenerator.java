package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabel;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;

public class ElementalGenerator extends TechniqueAspect {

	public double generated;

	public ResourceLocation element;

	@SuppressWarnings("rawtypes")
	private final static HashMap<Class, Integer> priority = new HashMap<>();

	static {
		priority.put(ElementalGenerator.class, -3);
		priority.put(ElementalConverter.class, -2);
		priority.put(ElementalConsumer.class, -1);
	}

	public ElementalGenerator(double generated, ResourceLocation element) {
		super();
		this.generated = generated;
		this.element = element;
	}

	@Override
	public void accept(HashMap<String, Object> metaData, BigDecimal proficiency) {
		super.accept(metaData, proficiency);
		String elementBase = "element-base-" + element.getPath();
		var modifier = this.getCurrentCheckpoint(proficiency).modifier();
		var generated = this.generated * (1 + modifier.doubleValue());
		metaData.put(elementBase, (double) metaData.getOrDefault(elementBase, 0d) + generated);
	}

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if (aspect instanceof ElementalGenerator gen) {
			return gen.element.equals(this.element);
		}
		if (aspect instanceof ElementalConsumer con) {
			return con.element.equals(this.element);
		}
		if (aspect instanceof ElementalConverter con) {
			return con.element.equals(this.element);
		}
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
		var nameLocation = this.getRegistryName();
		if (nameLocation == null) return;
		var name = new TranslatableComponent("wuxiacraft.aspect." + nameLocation.getPath() + ".name");
		var generated = this.generated;
		var player = Minecraft.getInstance().player;
		if (player != null) {
			var cultivation = Cultivation.get(player);
			var proficiency = cultivation.getAspects().getAspectProficiency(nameLocation);
			generated *= 1d + proficiency.doubleValue();
		}
		var amount = String.format("%.1f", generated);

		var font = Minecraft.getInstance().font;
		var nameWidth = font.width(name);
		var amountWidth = font.width(amount);
		var tooTipWidth = Math.max(nameWidth + 10, 28 + amountWidth);
		RenderSystem.enableBlend();
		GuiComponent.fill(poseStack, mouseX, mouseY, mouseX + tooTipWidth, mouseY + 31, 0x6A8080A0);
		RenderSystem.setShaderTexture(0, new ResourceLocation(this.element.getNamespace(), "textures/elements/" + this.element.getPath() + ".png"));
		RenderSystem.enableBlend();
		GuiComponent.blit(poseStack, mouseX + 5, mouseY + 13, 16, 16, 0, 0, 16, 16, 16, 16);
		font.drawShadow(poseStack, name, mouseX + 5, mouseY + 2, 0xFFFFFF);
		font.drawShadow(poseStack, amount, mouseX + 23, mouseY + 18, 0xFFFFFF);
		RenderSystem.disableBlend();
	}

	@Nonnull
	@Override
	public LinkedList<AbstractWidget> getStatsSheetDescriptor() {
		var widgets = super.getStatsSheetDescriptor();
		widgets.get(2).y += 11;//must be the description
		widgets.add(new WuxiaLabel(5, 25, new TranslatableComponent("wuxiacraft.gui.generates", this.generated), 0xFFAA00));
		return widgets;
	}

	@Override
	public int connectPrioritySorter(TechniqueAspect aspect1, TechniqueAspect aspect2) {
		int priority1 = priority.getOrDefault(aspect1.getClass(), 0);
		int priority2 = priority.getOrDefault(aspect2.getClass(), 0);
		int finalPriority = priority1 - priority2;
		return finalPriority != 0 ? finalPriority / Math.abs(finalPriority) : 0;
	}
}
