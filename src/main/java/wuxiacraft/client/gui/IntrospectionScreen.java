package wuxiacraft.client.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.container.IntrospectionContainer;
import wuxiacraft.cultivation.*;
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
			this.font.drawString(stack, statsList[i].getLevel().displayName, 0, 0, 0xFFFFFF);
			stack.pop();
			KnownTechnique kt = knownTechniques[i]; //to avoid warnings
			if (kt != null) {
				stack.push();
				stack.translate(techniqueNameLocation[i].x, techniqueNameLocation[i].y, 0);
				stack.scale(fontScale, fontScale, 1);
				this.font.drawString(stack, kt.getTechnique().getName(), 0, 0, 0xFFFFFF);
				stack.pop();
			}
		}
		stack.pop();
	}
}
