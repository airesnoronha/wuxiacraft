package wuxiacraft.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.gui.minigame.*;
import wuxiacraft.client.handler.RenderHudHandler;
import wuxiacraft.container.MeditationContainer;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.SystemStats;
import wuxiacraft.network.AttemptBreakthroughMessage;
import wuxiacraft.network.WuxiaPacketHandler;
import wuxiacraft.util.MathUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

@ParametersAreNonnullByDefault
public class MeditateScreen extends ContainerScreen<MeditationContainer> {

	private static final ResourceLocation MEDITATE_GUI = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/meditation_gui.png");

	private IMinigame currentMinigame;

	public static double mousePosX;
	public static double mousePosY;

	private CultivationLevel.System selected = CultivationLevel.System.ESSENCE;

	private Point btButtonPos = new Point(0, 155);
	private Vector2f btMouseOffset = new Vector2f(0, 0);
	private boolean btButtonGrabbed = false;

	private final Point[] tabLocations = new Point[]{new Point(4, 4), new Point(128, 4)};

	public MeditateScreen(MeditationContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.xSize = 200;
		this.ySize = 155;
	}

	private void setAppropriateMinigame() {
		if (this.minecraft == null) return;
		if (this.minecraft.player == null) return;
		ICultivation cultivation = Cultivation.get(this.minecraft.player);
		if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() == CultivationLevel.DEFAULT_ESSENCE_LEVEL) {
			this.currentMinigame = new FoundationRealmMinigame();
		} else if (this.selected == CultivationLevel.System.BODY) {
			if (cultivation.getStatsBySystem(CultivationLevel.System.BODY).getLevel() == CultivationLevel.BODY_LEVELS.get(1)) {
				this.currentMinigame = new BodyCleansingMinigame();
			} else if (cultivation.getStatsBySystem(CultivationLevel.System.BODY).getLevel() == CultivationLevel.BODY_LEVELS.get(2)) {
				this.currentMinigame = new BodyForgingMinigame();
			} else if (cultivation.getStatsBySystem(CultivationLevel.System.BODY).getLevel() == CultivationLevel.BODY_LEVELS.get(3)) {
				this.currentMinigame = new BodyCleansingMinigame();
			}
		} else if (this.selected == CultivationLevel.System.DIVINE) {
			if (cultivation.getStatsBySystem(CultivationLevel.System.DIVINE).getLevel() == CultivationLevel.DIVINE_LEVELS.get(1)) {
				this.currentMinigame = new SoulCondensingMinigame();
			} else if (cultivation.getStatsBySystem(CultivationLevel.System.DIVINE).getLevel() == CultivationLevel.DIVINE_LEVELS.get(2)) {
				this.currentMinigame = new SoulCondensingMinigame();
			}

		} else if (this.selected == CultivationLevel.System.ESSENCE) {
			if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() == CultivationLevel.ESSENCE_LEVELS.get(1)) {
				this.currentMinigame = new EssenceGatheringMinigame();
			} else if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() == CultivationLevel.ESSENCE_LEVELS.get(2)) {
				this.currentMinigame = new EssenceConsolidationMinigame();
			} else if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() == CultivationLevel.ESSENCE_LEVELS.get(3)) {
				this.currentMinigame = new RevolvingCoreMinigame();
			} else if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() == CultivationLevel.ESSENCE_LEVELS.get(4)) {
				this.currentMinigame = new EssenceConsolidationMinigame();
			} else if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() == CultivationLevel.ESSENCE_LEVELS.get(5)) {
				this.currentMinigame = new EssenceConsolidationMinigame();
			} else if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() == CultivationLevel.ESSENCE_LEVELS.get(6)) {
				this.currentMinigame = new EssenceConsolidationMinigame();
			} else if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() == CultivationLevel.ESSENCE_LEVELS.get(7)) {
				this.currentMinigame = new EssenceConsolidationMinigame();
			}
		}
	}

	@Override
	protected void init() {
		super.init();
		setAppropriateMinigame();
	}

	private void drawBackgroundLayer(MatrixStack stack) {
		if (this.minecraft == null) return;
		if (this.minecraft.player == null) return;
		stack.push();
		this.renderBackground(stack);
		stack.translate(this.guiLeft, this.guiTop, 0);
		this.getMinecraft().getTextureManager().bindTexture(MEDITATE_GUI);
		this.blit(stack, 0, 0, 0, 0, this.xSize, this.ySize);
		if (!(this.currentMinigame instanceof FoundationRealmMinigame)) {
			this.blit(stack, tabLocations[0].x, tabLocations[0].y, 0, 201, 68, 14);
			this.blit(stack, tabLocations[1].x, tabLocations[1].y, 0, 201, 68, 14);
		}

		this.blit(stack, btButtonPos.x, btButtonPos.y, 0, 201, 68, 14);
		ICultivation cultivation = Cultivation.get(this.minecraft.player);
		SystemStats bodyStats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
		SystemStats divineStats = cultivation.getStatsBySystem(CultivationLevel.System.DIVINE);
		SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		RenderSystem.enableBlend();
		this.blit(stack, 7, 100, 13, 155, 17, 46);
		int fill = (int) (40 * bodyStats.getEnergy() / cultivation.getMaxBodyEnergy());
		this.blit(stack, 9, 143 - fill, 0, 155, 13, fill);

		this.blit(stack, 150, 35, 156, 155, 44, 35);
		fill = (int) (33 * divineStats.getEnergy() / cultivation.getMaxDivineEnergy());
		this.blit(stack, 151, 36 + 33 - fill, 116, 155 + 33 - fill, 40, fill);

		this.blit(stack, 150, 100, 70, 155, 46, 46);
		float essenceFill = (float) (essenceStats.getEnergy() / cultivation.getMaxEssenceEnergy());
		stack.push();
		stack.translate(153 + 20, 103 + 20, 0);
		stack.scale(essenceFill, essenceFill, 1);
		stack.rotate(Vector3f.ZP.rotation(-RenderHudHandler.rotationAngle));
		this.blit(stack, -20, -20, 30, 155, 40, 40);
		stack.pop();

		this.currentMinigame.renderBackground(stack, this);
		RenderSystem.disableBlend();
		stack.pop();
	}

	private void drawForegroundLayer(MatrixStack stack) {
		stack.push();
		float fontScale = 0.9f;
		if (!(this.currentMinigame instanceof FoundationRealmMinigame)) {
			String[] texts = new String[]{this.selected == CultivationLevel.System.BODY ? "Essence" : "Body",
					this.selected == CultivationLevel.System.DIVINE ? "Essence" : "Divine"};
			for (int i = 0; i < 2; i++) {
				stack.push();
				stack.translate(tabLocations[i].x + 3, tabLocations[i].y + 3, 0);
				stack.scale(fontScale, fontScale, 1);
				int width = this.font.getStringWidth(texts[i]);
				this.font.drawString(stack, texts[i], (72-width)/2f, 0, 0xFFAA11);
				stack.pop();
			}
		}
		stack.push();
		stack.translate(btButtonPos.x + 3, btButtonPos.y + 3, 0);
		stack.scale(fontScale, fontScale, 1);
		this.font.drawString(stack, "Breakthrough", 0, 0, 0xFFAA11);
		stack.pop();
		this.currentMinigame.renderForeground(stack, this);
		stack.pop();
	}

	@Override
	public void tick() {
		super.tick();
		if (btButtonGrabbed) {
			btButtonPos = new Point(MathUtils.clamp((int) (mousePosX - btMouseOffset.x), 0, 132), btButtonPos.y);
			if (btButtonPos.x > 128) { //attempt Breakthrough
				WuxiaPacketHandler.INSTANCE.sendToServer(new AttemptBreakthroughMessage(CultivationLevel.System.ESSENCE));
				btButtonGrabbed = false;
				btButtonPos = new Point(0, 155);
				this.closeScreen();
			}
		}
		this.currentMinigame.tick();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			if (MathUtils.between(mouseX - this.guiLeft, btButtonPos.x, btButtonPos.x + 68)
					&& MathUtils.between(mouseY - this.guiTop, btButtonPos.y, btButtonPos.y + 14)) {
				btButtonGrabbed = true;
				btMouseOffset = new Vector2f((float) mouseX - this.guiLeft - btButtonPos.x, (float) mouseY - this.guiTop - btButtonPos.y);
			}
			if (!(this.currentMinigame instanceof FoundationRealmMinigame)) {
				if (MathUtils.inBounds(mouseX - this.guiLeft, mouseY - this.guiTop, tabLocations[0].x, tabLocations[0].y, 68, 14)) {
					this.selected = this.selected == CultivationLevel.System.BODY ? CultivationLevel.System.ESSENCE : CultivationLevel.System.BODY;
					this.setAppropriateMinigame();
				}
				if (MathUtils.inBounds(mouseX - this.guiLeft, mouseY - this.guiTop, tabLocations[1].x, tabLocations[1].y, 68, 14)) {
					this.selected = this.selected == CultivationLevel.System.DIVINE ? CultivationLevel.System.ESSENCE : CultivationLevel.System.DIVINE;
					this.setAppropriateMinigame();
				}
			}
			this.currentMinigame.handleMouseDown(mouseX, mouseY, this);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0) {
			this.currentMinigame.handleMouseUp(mouseX, mouseY, this);
		}
		btButtonGrabbed = false;
		btButtonPos = new Point(0, 155);
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void mouseMoved(double xPos, double mouseY) {
		mousePosX = xPos - this.guiLeft;
		mousePosY = mouseY - this.guiTop;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		this.drawBackgroundLayer(matrixStack);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		this.drawForegroundLayer(matrixStack);
	}

	public void blitColored(MatrixStack stack, int x, int y, int uPos, int vPos, int width, int height, float red, float green, float blue) {
		int x1 = x;
		int x2 = x + width;
		int y1 = y;
		int y2 = y + height;
		float minU = (uPos) / 256f;
		float maxU = (uPos + width) / 256f;
		float minV = (vPos) / 256f;
		float maxV = (vPos + height) / 256f;
		int blitOffset = 0;
		float alpha = 1f;
		Matrix4f matrix = stack.getLast().getMatrix();
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
		bufferbuilder.pos(matrix, (float) x1, (float) y2, (float) blitOffset).color(red, green, blue, alpha).tex(minU, maxV).endVertex();
		bufferbuilder.pos(matrix, (float) x2, (float) y2, (float) blitOffset).color(red, green, blue, alpha).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(matrix, (float) x2, (float) y1, (float) blitOffset).color(red, green, blue, alpha).tex(maxU, minV).endVertex();
		bufferbuilder.pos(matrix, (float) x1, (float) y1, (float) blitOffset).color(red, green, blue, alpha).tex(minU, minV).endVertex();
		bufferbuilder.finishDrawing();
		WorldVertexBufferUploader.draw(bufferbuilder);
	}
}
