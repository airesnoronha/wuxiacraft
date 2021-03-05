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
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.gui.minigame.EssenceGatheringMinigame;
import wuxiacraft.client.gui.minigame.FoundationRealmMinigame;
import wuxiacraft.client.gui.minigame.IMinigame;
import wuxiacraft.container.MeditationContainer;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.SystemStats;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MeditateScreen extends ContainerScreen<MeditationContainer> {

	private static final ResourceLocation MEDITATE_GUI = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/meditation_gui.png");

	private IMinigame currentMinigame;

	public static double mousePosX;
	public static double mousePosY;

	public MeditateScreen(MeditationContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.xSize = 200;
		this.ySize = 155;
	}

	@Override
	protected void init() {
		super.init();
		if (this.minecraft == null) return;
		if (this.minecraft.player == null) return;
		ICultivation cultivation = Cultivation.get(this.minecraft.player);
		if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() == CultivationLevel.ESSENCE_LEVELS.get(1)) {
			this.currentMinigame = new EssenceGatheringMinigame();
		} else {
			this.currentMinigame = new FoundationRealmMinigame();
		}
	}

	private void drawBackgroundLayer(MatrixStack stack) {
		if (this.minecraft == null) return;
		if (this.minecraft.player == null) return;
		stack.push();
		this.renderBackground(stack);
		stack.translate(this.guiLeft, this.guiTop, 0);
		this.getMinecraft().getTextureManager().bindTexture(MEDITATE_GUI);
		this.blit(stack, 0, 0, 0, 0, this.xSize, this.ySize);
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
		float rotationSpeed = 0.8f - essenceFill * 0.803f;//in hertz
		if (rotationSpeed == 0) rotationSpeed = 0.000001f;
		int timeStep = (int) (System.currentTimeMillis() % (int) (1000f / rotationSpeed));
		float rotationAngle = (float) Math.PI * (float) timeStep / (1000f / (rotationSpeed * 2));
		stack.rotate(Vector3f.ZP.rotation(-rotationAngle));
		this.blit(stack, -20, -20, 30, 155, 40, 40);
		stack.pop();

		this.currentMinigame.renderBackground(stack, this);
		RenderSystem.disableBlend();
		stack.pop();
	}

	private void drawForegroundLayer(MatrixStack stack) {
		stack.push();
		stack.translate(this.guiLeft, this.guiTop, 0);
		this.currentMinigame.renderForeground(stack, this);
		stack.pop();
	}

	@Override
	public void tick() {
		super.tick();
		this.currentMinigame.tick();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			this.currentMinigame.handleMouseDown(mouseX, mouseY, this);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0) {
			this.currentMinigame.handleMouseUp(mouseX, mouseY, this);
		}
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
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.draw(bufferbuilder);
	}
}
