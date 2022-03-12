package com.lazydragonstudios.wuxiacraft.client.gui;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.networking.AskToDieMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WuxiaSemiDeadScreen extends Screen {

	public WuxiaSemiDeadScreen() {
		super(new TextComponent(""));
		this.passEvents = true;
	}

	@Override
	protected void init() {
		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 72, 200, 20, new TranslatableComponent("wuxiacraft.ask_to_die"),
				(btn) -> {
					WuxiaPacketHandler.INSTANCE.sendToServer(new AskToDieMessage());
				}));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		this.fillGradient(poseStack, 0, 0, this.width, this.height, 1615855616, -1602211792);
		poseStack.pushPose();
		poseStack.scale(2.0F, 2.0F, 2.0F);
		drawCenteredString(poseStack, this.font, new TranslatableComponent("wuxiacraft.semi_dead.title"), this.width / 2 / 2, 30, 16777215);
		poseStack.popPose();
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		var timer = cultivation.getSemiDeadTimer() / 20;
		drawCenteredString(poseStack, this.font, new TranslatableComponent("wuxiacraft.semi_dead.time", timer), this.width / 2, 85, 16777215);
		super.render(poseStack, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifier) {
		return false;
	}
}
