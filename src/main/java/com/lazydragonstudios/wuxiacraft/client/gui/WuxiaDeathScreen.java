package com.lazydragonstudios.wuxiacraft.client.gui;

import com.google.common.collect.Lists;
import com.lazydragonstudios.wuxiacraft.networking.AskToDieMessage;
import com.lazydragonstudios.wuxiacraft.networking.RequestRTPOnDeathMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WuxiaDeathScreen extends Screen {
	private int delayTicker;
	private final Component causeOfDeath;
	private final boolean hardcore;
	private Component deathScore;
	private final List<Button> exitButtons = Lists.newArrayList();

	public WuxiaDeathScreen(@Nullable Component causeOfDeath, boolean hardcore) {
		super(new TranslatableComponent(hardcore ? "deathScreen.title.hardcore" : "deathScreen.title"));
		this.causeOfDeath = causeOfDeath;
		this.hardcore = hardcore;
	}

	@SuppressWarnings("ConstantConditions")
	protected void init() {
		this.delayTicker = 0;
		this.exitButtons.clear();
		//this is the first btn
		this.exitButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 72, 200, 20, this.hardcore ? new TranslatableComponent("deathScreen.spectate") : new TranslatableComponent("deathScreen.respawn"), (p_95930_) -> {
			this.minecraft.player.respawn();
			this.minecraft.setScreen(null);
		})));
		int btnHeight = 96;
		//this is the random tp btn
		if (!this.hardcore) {
			this.exitButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + btnHeight, 200, 20, new TranslatableComponent("wuxiacraft.gui.death.random_teleport.name"), (p_95925_) -> {
				WuxiaPacketHandler.INSTANCE.sendToServer(new RequestRTPOnDeathMessage(true));
				this.minecraft.player.respawn();
				this.minecraft.setScreen(null);
			})));
			btnHeight += 24;
		}
		//this is the exit btn (exit confirm)
		this.exitButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + btnHeight, 200, 20, new TranslatableComponent("deathScreen.titleScreen"), (p_95925_) -> {
			if (this.hardcore) {
				confirmResult(true);
				this.exitToTitleScreen();
			} else {
				ConfirmScreen confirmscreen = new ConfirmScreen(this::confirmResult, new TranslatableComponent("deathScreen.quit.confirm"), TextComponent.EMPTY, new TranslatableComponent("deathScreen.titleScreen"), new TranslatableComponent("deathScreen.respawn"));
				this.minecraft.setScreen(confirmscreen);
				confirmscreen.setDelay(20);
			}
		})));

		for (Button button : this.exitButtons) {
			button.active = false;
		}

		this.deathScore = (new TranslatableComponent("deathScreen.score")).append(": ").append((new TextComponent(Integer.toString(this.minecraft.player.getScore()))).withStyle(ChatFormatting.YELLOW));
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@SuppressWarnings("ConstantConditions")
	private void confirmResult(boolean p_95932_) {
		if (p_95932_) {
			this.exitToTitleScreen();
		} else {
			this.minecraft.player.respawn();
			this.minecraft.setScreen(null);
		}

	}

	@SuppressWarnings("ConstantConditions")
	private void exitToTitleScreen() {
		if (this.minecraft.level != null) {
			this.minecraft.level.disconnect();
		}

		this.minecraft.clearLevel(new GenericDirtMessageScreen(new TranslatableComponent("menu.savingLevel")));
		this.minecraft.setScreen(new TitleScreen());
	}

	@Override
	public void render(PoseStack p_95920_, int p_95921_, int p_95922_, float p_95923_) {
		this.fillGradient(p_95920_, 0, 0, this.width, this.height, 1615855616, -1602211792);
		p_95920_.pushPose();
		p_95920_.scale(2.0F, 2.0F, 2.0F);
		drawCenteredString(p_95920_, this.font, this.title, this.width / 2 / 2, 30, 16777215);
		p_95920_.popPose();
		if (this.causeOfDeath != null) {
			drawCenteredString(p_95920_, this.font, this.causeOfDeath, this.width / 2, 85, 16777215);
		}

		drawCenteredString(p_95920_, this.font, this.deathScore, this.width / 2, 100, 16777215);
		if (this.causeOfDeath != null && p_95922_ > 85 && p_95922_ < 85 + 9) {
			Style style = this.getClickedComponentStyleAt(p_95921_);
			this.renderComponentHoverEffect(p_95920_, style, p_95921_, p_95922_);
		}

		super.render(p_95920_, p_95921_, p_95922_, p_95923_);
	}

	@SuppressWarnings("ConstantConditions")
	@Nullable
	private Style getClickedComponentStyleAt(int p_95918_) {
		if (this.causeOfDeath == null) {
			return null;
		} else {
			int i = this.minecraft.font.width(this.causeOfDeath);
			int j = this.width / 2 - i / 2;
			int k = this.width / 2 + i / 2;
			return p_95918_ >= j && p_95918_ <= k ? this.minecraft.font.getSplitter().componentStyleAtWidth(this.causeOfDeath, p_95918_ - j) : null;
		}
	}

	@Override
	public boolean mouseClicked(double p_95914_, double p_95915_, int p_95916_) {
		if (this.causeOfDeath != null && p_95915_ > 85.0D && p_95915_ < (double) (85 + 9)) {
			Style style = this.getClickedComponentStyleAt((int) p_95914_);
			if (style != null && style.getClickEvent() != null && style.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
				this.handleComponentClicked(style);
				return false;
			}
		}

		return super.mouseClicked(p_95914_, p_95915_, p_95916_);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		++this.delayTicker;
		if (this.delayTicker == 20) {
			for (Button button : this.exitButtons) {
				button.active = true;
			}
		}

	}
}
