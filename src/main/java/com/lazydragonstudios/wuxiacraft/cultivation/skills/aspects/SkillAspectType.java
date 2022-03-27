package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.lang.reflect.InvocationTargetException;

public class SkillAspectType extends ForgeRegistryEntry<SkillAspectType> {

	public final Creator creator;

	public static SkillAspectType build(Creator creator) {
		return new SkillAspectType(creator);
	}

	public SkillAspectType(Creator creator) {
		this.creator = creator;
	}

	@OnlyIn(Dist.CLIENT)
	public void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
		var registryName = this.getRegistryName();
		if (registryName == null) return;
		var font = Minecraft.getInstance().font;
		var title = new TranslatableComponent(registryName.getNamespace() + ".skill." + registryName.getPath());
		int titleWidth = font.width(title);
		RenderSystem.enableBlend();
		GuiComponent.fill(poseStack, mouseX, mouseY, mouseX + titleWidth + 10, mouseY + 13, 0x6A8080A0);
		font.drawShadow(poseStack, title, mouseX + 5, mouseY + 2, 0xFFFFFF);
		RenderSystem.disableBlend();
	}

	@FunctionalInterface
	public interface Creator {
		SkillAspect create();
	}

}
