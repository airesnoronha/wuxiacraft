package wuxiacraft.client.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.container.IntrospectionContainer;
import wuxiacraft.cultivation.*;
import wuxiacraft.network.SetConfigParametersMessage;
import wuxiacraft.network.WuxiaPacketHandler;
import wuxiacraft.util.MathUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Point;
import java.util.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IntrospectionScreen extends ContainerScreen<IntrospectionContainer> {

	private static final ResourceLocation CULT_GUI = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/cult_gui.png");

	private final static Point[] locations = new Point[]{
			new Point(125, 187), //red
			new Point(155, 187), //yellow
			new Point(185, 187), //green
			new Point(215, 187), //blue
			new Point(125, 217), //purple
			new Point(155, 217) //white
	};

	public enum RequireConfirmAction {
		REMOVE_BODY_TECHNIQUE,
		REMOVE_DIVINE_TECHNIQUE,
		REMOVE_ESSENCE_TECHNIQUE
	}

	private RequireConfirmAction requireConfirmAction = RequireConfirmAction.REMOVE_BODY_TECHNIQUE;
	private boolean confirmScreen = false;

	public IntrospectionScreen(IntrospectionContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		//regulator bars
		for (int i = 0; i < 4; i++) {
			boolean shiftModifier = InputMappings.isKeyDown(this.getMinecraft().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)
					|| InputMappings.isKeyDown(this.getMinecraft().getMainWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT);
			if (MathUtils.inBounds(mouseX, mouseY, this.guiLeft - 61 + 13, this.guiTop + i * 15 + 3, 9, 9)) {
				handleBarButtonClick(i, 0, shiftModifier);
			} else if (MathUtils.inBounds(mouseX, mouseY, this.guiLeft - 61 + 51, this.guiTop + i * 15 + 3, 9, 9)) {
				handleBarButtonClick(i, 1, shiftModifier);
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	private void handleBarButtonClick(int prop, int op, boolean shiftModifier) {
		if (this.minecraft == null) return;
		if (this.minecraft.player == null) return;
		ICultivation cultivation = Cultivation.get(this.minecraft.player);
		float step = shiftModifier ? 1.0f : 0.1f;
		switch (prop) {
			case 0:
				if (op == 0) cultivation.setMovementSpeed(Math.max(0, cultivation.getMovementSpeed() - step));
				if (op == 1) cultivation.setMovementSpeed(Math.max(0, cultivation.getMovementSpeed() + step));
				break;
			case 1:
				if (op == 0) cultivation.setBreakSpeed(Math.max(0, cultivation.getBreakSpeed() - step));
				if (op == 1) cultivation.setBreakSpeed(Math.max(0, cultivation.getBreakSpeed() + step));
				break;
			case 2:
				if (op == 0) cultivation.setJumpSpeed(Math.max(0, cultivation.getJumpSpeed() - step));
				if (op == 1) cultivation.setJumpSpeed(Math.max(0, cultivation.getJumpSpeed() + step));
				break;
			case 3:
				if (op == 0) cultivation.setStepHeight(Math.max(0, cultivation.getStepHeight() - step));
				if (op == 1) cultivation.setStepHeight(Math.max(0, cultivation.getStepHeight() + step));
				break;
		}
		WuxiaPacketHandler.INSTANCE.sendToServer(new SetConfigParametersMessage(cultivation.getMovementSpeed(),
				cultivation.getBreakSpeed(), cultivation.getJumpSpeed(), cultivation.getStepHeight()));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y) {
		if (this.minecraft == null) return;
		if (this.minecraft.player == null) return;
		ICultivation cultivation = Cultivation.get(this.minecraft.player);
		SystemStats bodyStats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
		SystemStats divineStats = cultivation.getStatsBySystem(CultivationLevel.System.DIVINE);
		SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		stack.push();
		this.renderBackground(stack);
		stack.translate(this.guiLeft, this.guiTop, 0);
		this.getMinecraft().getTextureManager().bindTexture(CULT_GUI);
		this.blit(stack, 0, 0, 0, 0, 200, 155);
		double foundationOverMaxBase = bodyStats.getFoundation() / bodyStats.getLevel().getBaseBySubLevel(bodyStats.getSubLevel());
		drawFoundationAtPosition(stack, 154, 25, foundationOverMaxBase);
		foundationOverMaxBase = divineStats.getFoundation() / divineStats.getLevel().getBaseBySubLevel(divineStats.getSubLevel());
		drawFoundationAtPosition(stack, 154, 66, foundationOverMaxBase);
		foundationOverMaxBase = essenceStats.getFoundation() / essenceStats.getLevel().getBaseBySubLevel(essenceStats.getSubLevel());
		drawFoundationAtPosition(stack, 154, 107, foundationOverMaxBase);

		double extraHaste = cultivation.getBodyModifier() * 0.05 + cultivation.getEssenceModifier() * 0.05;
		double extraJump = cultivation.getBodyModifier() * 0.09 + cultivation.getEssenceModifier() * 0.04;
		double extraStepHeight = cultivation.getBodyModifier() * 0.1 + cultivation.getEssenceModifier() * 0.05;

		int[] iconPos = new int[]{27, 99, 108, 135};
		int[] fills = new int[]{
				Math.min(27, (int) (27f * cultivation.getMovementSpeed() / Math.min(8.5f, cultivation.getFinalModifiers().movementSpeed))),
				Math.min(27, (int) (27f * cultivation.getBreakSpeed() / extraHaste)),
				Math.min(27, (int) (27f * cultivation.getJumpSpeed() / extraJump)),
				Math.min(27, (int) (27f * cultivation.getStepHeight() / extraStepHeight))
		};
		//Regulator bars
		for (int i = 0; i < 4; i++) {
			blit(stack, -61, +i * 15, 0, 173, 61, 15); //bg
			blit(stack, -61 + 23, +i * 15 + 4, 0, 188, 27, 7); //bar bg
			blit(stack, -61 + 23, +i * 15 + 4, 27, 188, fills[i], 7); //bar fill
			blit(stack, -61 + 13, +i * 15 + 3, 45, 164, 9, 9); //button bg
			blit(stack, -61 + 51, +i * 15 + 3, 45, 164, 9, 9); //button bg
			blit(stack, -61 + 13, +i * 15 + 3, 72, 164, 9, 9); //button icon -
			blit(stack, -61 + 51, +i * 15 + 3, 90, 164, 9, 9); //button icon +
			blit(stack, -61 + 3, +i * 15 + 3, iconPos[i], 164, 9, 9); // icon
		}

		stack.pop();
	}

	private void drawFoundationAtPosition(MatrixStack stack, int x, int y, double foundationOverMaxBase) {
		stack.push();
		stack.translate(x, y, 0);
		Point bg = locations[0]; //red
		Point fg = locations[1]; //yellow
		int fill = (int) (30 * foundationOverMaxBase);
		if (MathUtils.between(foundationOverMaxBase, 1, 3)) {
			fill = (int) (30 * (foundationOverMaxBase - 1) / 2);
			bg = locations[1]; //yellow
			fg = locations[2]; //green
		} else if (MathUtils.between(foundationOverMaxBase, 3, 6)) {
			fill = (int) (30 * (foundationOverMaxBase - 3) / 3);
			bg = locations[2]; //green
			fg = locations[3]; //blue
		} else if (MathUtils.between(foundationOverMaxBase, 6, 10)) {
			fill = (int) (30 * (foundationOverMaxBase - 6) / 4);
			bg = locations[3]; //blue
			fg = locations[4]; //purple
		} else if (MathUtils.between(foundationOverMaxBase, 10, 20)) {
			fill = (int) (30 * (foundationOverMaxBase - 10) / 10);
			bg = locations[4]; //purple
			fg = locations[5]; //white
		} else if (foundationOverMaxBase > 20) {
			fill = 30;
			bg = locations[5]; //white
			fg = locations[5]; //white
		}
		this.blit(stack, 0, 0, bg.x, bg.y, 30, 30);
		this.blit(stack, 0, 30 - fill, fg.x, fg.y + 30 - fill, 30, fill);
		stack.pop();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x, int y) {
		if (this.minecraft == null) return;
		if (this.minecraft.player == null) return;
		ICultivation cultivation = Cultivation.get(this.minecraft.player);
		float fontScale = 0.9f;
		SystemStats[] statsList = new SystemStats[]{
				cultivation.getStatsBySystem(CultivationLevel.System.BODY),
				cultivation.getStatsBySystem(CultivationLevel.System.DIVINE),
				cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE)
		};
		KnownTechnique[] knownTechniques = new KnownTechnique[]{
				cultivation.getTechniqueBySystem(CultivationLevel.System.BODY),
				cultivation.getTechniqueBySystem(CultivationLevel.System.DIVINE),
				cultivation.getTechniqueBySystem(CultivationLevel.System.ESSENCE)
		};
		Point[] nameLocations = new Point[]{
				new Point(13, 25),
				new Point(13, 66),
				new Point(13, 107)
		};
		Point[] techniqueNameLocation = new Point[]{
				new Point(21, 52),
				new Point(21, 93),
				new Point(21, 134)
		};
		stack.push();
		//stack.translate(this.guiLeft, this.guiTop, 0);
		for (int i = 0; i < 3; i++) {
			stack.push();
			stack.translate(nameLocations[i].x, nameLocations[i].y, 0);
			stack.scale(fontScale, fontScale, 1);
			this.font.drawString(stack, statsList[i].getLevel().displayName, 0, 0, 0xFFAA00);
			stack.pop();
			KnownTechnique kt = knownTechniques[i]; //to avoid warnings
			if (kt != null) {
				stack.push();
				stack.translate(techniqueNameLocation[i].x, techniqueNameLocation[i].y, 0);
				stack.scale(fontScale, fontScale, 1);
				this.font.drawString(stack, I18n.format("technique."+kt.getTechnique().getName()), 0, 0, 0xFFAA00);
				stack.pop();
			}
		}

		List<String> barDescriptions = new ArrayList<>();
		barDescriptions.add(String.format("%.1f", cultivation.getMovementSpeed()));
		barDescriptions.add(String.format("%.1f", cultivation.getBreakSpeed()));
		barDescriptions.add(String.format("%.1f", cultivation.getJumpSpeed()));
		barDescriptions.add(String.format("%.1f", cultivation.getStepHeight()));

		for (int i = 0; i < 4; i++) {
			this.font.drawString(stack, barDescriptions.get(i), - 61 + 27, + 4 + i * 15, 0xEFEF00);
		}
		stack.pop();
	}
}
