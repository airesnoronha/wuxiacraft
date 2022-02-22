package com.lazydragonstudios.wuxiacraft.client.gui.tab;

import com.lazydragonstudios.wuxiacraft.client.gui.widgets.*;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillDescriptor;
import com.lazydragonstudios.wuxiacraft.networking.RequestSkillSaveMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.lazydragonstudios.wuxiacraft.client.gui.IntrospectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.function.BiConsumer;

public class SkillsTab extends IntrospectionTab {

	private WuxiaFlowPanel skillAspectsPanel;
	private WuxiaScrollPanel skillChainPanel;
	private WuxiaScrollPanel skillStatsPanel;

	private WuxiaSkillComposer skillComposer;

	private WuxiaButton saveBtn;

	private ResourceLocation draggingAspect = null;

	public SkillsTab(String name) {
		super(name, new Point(64, 68));
	}

	@Override
	public void init(IntrospectionScreen screen) {
		skillAspectsPanel = new WuxiaFlowPanel(36, 72, 116, 200, new TextComponent(""));
		skillChainPanel = new WuxiaScrollPanel(36 + 116, 72, 100, 200, new TextComponent(""));
		skillStatsPanel = new WuxiaScrollPanel(36 + 116 + 100, 72, 150, 200, new TextComponent(""));
		screen.addRenderableWidget(skillAspectsPanel);
		screen.addRenderableWidget(skillChainPanel);
		screen.addRenderableWidget(skillStatsPanel);
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		var skillSlot0 = cultivation.getSkills().getSkillAt(0);
		this.skillComposer = new WuxiaSkillComposer(0, 0, new TextComponent(""), skillSlot0);
		this.skillComposer.onClick = onComposerClick;
		this.skillComposer.onRelease = onComposerRelease;
		skillChainPanel.addChild(this.skillComposer);
		for (var skill : cultivation.getSkills().knownSkills) {
			var aspectWidget = new WuxiaAspectWidget(0, 0, skill);
			aspectWidget.setOnClicked(onAspectClick(skill));
			aspectWidget.setOnRelease(onAspectRelease());
			skillAspectsPanel.addChild(aspectWidget);
		}
		this.saveBtn = new WuxiaButton(32 + 116 + 100, 72, 150, 20, new TranslatableComponent("Save"), () -> {
			var skillTag = this.skillComposer.getSkill().serialize();
			//TODO change to the active slot
			WuxiaPacketHandler.INSTANCE.sendToServer(new RequestSkillSaveMessage(0, skillTag));
		});
		screen.addRenderableWidget(saveBtn);
	}


	private BiConsumer<Double, Double> onAspectClick(ResourceLocation aspectLocation) {
		return (mx, my) -> {
			this.draggingAspect = aspectLocation;
		};
	}

	private BiConsumer<Double, Double> onAspectRelease() {
		return (mx, my) -> {
			this.draggingAspect = null;
		};
	}

	private final MouseInputPredicate onComposerClick = (mx, my, mb) -> {
		if (mb == 1) {
			var pos = this.skillComposer.getLinkAtPosition(mx, my);
			if (pos == -1) return false;
			this.skillComposer.removeSkillAtPosition(pos);
			return true;
		}
		return false;
	};

	private final MouseInputPredicate onComposerRelease = (mx, my, mb) -> {
		var pos = this.skillComposer.getLinkAtPosition(mx, my);
		if (pos == -1) return false;
		this.skillComposer.addSkillToPosition(this.draggingAspect, pos);
		this.draggingAspect = null;
		return true;
	};


	@Override
	public void close(IntrospectionScreen screen) {
		super.close(screen);
	}

	@Override
	public void renderBG(PoseStack poseStack, int mouseX, int mouseY) {
		var mc = Minecraft.getInstance();
		int totalXSpace = mc.getWindow().getGuiScaledWidth();
		int totalYSpace = mc.getWindow().getGuiScaledHeight();
		int freeYSpace = totalYSpace - 72;
		int freeXSpace = totalXSpace - 36;
		int stretchedSpace = freeXSpace - 150 - 116;
		skillAspectsPanel.setHeight(freeYSpace);

		skillChainPanel.setHeight(freeYSpace);
		skillChainPanel.setWidth(stretchedSpace);

		skillStatsPanel.setHeight(freeYSpace - 26);
		skillStatsPanel.x = 36 + 116 + stretchedSpace;

		this.saveBtn.y = totalYSpace - 26;
		this.saveBtn.x = totalXSpace - 150;


		if (this.draggingAspect != null) {
			var textureLocation = new ResourceLocation(this.draggingAspect.getNamespace(), "textures/skills/" + this.draggingAspect.getPath() + ".png");
			RenderSystem.setShaderTexture(0, textureLocation);
			poseStack.pushPose();
			poseStack.translate(mouseX, mouseY, 0);
			GuiComponent.blit(poseStack, -16, -16, 32, 32, 0, 0, 32, 32, 32, 32);
			poseStack.popPose();
		}
	}

	@Override
	public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

	}
}
