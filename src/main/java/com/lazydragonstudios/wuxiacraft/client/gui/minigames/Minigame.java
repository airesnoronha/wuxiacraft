package com.lazydragonstudios.wuxiacraft.client.gui.minigames;

import com.lazydragonstudios.wuxiacraft.client.gui.MeditateScreen;
import com.mojang.blaze3d.vertex.PoseStack;

public interface Minigame {

	void init(MeditateScreen screen);

	boolean onMouseClick(double x, double y, int button);

	boolean onMouseRelease(double x, double y, int button);

	void onMouseMove(double x, double y);

	void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick);

	void renderTooltips(PoseStack stack, int mouseX, int mouseY);

	void tick();


}
