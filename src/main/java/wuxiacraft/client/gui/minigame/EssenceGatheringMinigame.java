package wuxiacraft.client.gui.minigame;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.gui.MeditateScreen;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.util.MathUtils;

import java.util.LinkedList;
import java.util.List;

public class EssenceGatheringMinigame implements IMinigame {

	private static final ResourceLocation ESSENCE_GATHERING = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/minigame/essence_gathering_minigame.png");

	private static final Vector2f[] targetPoints = new Vector2f[]
			{new Vector2f(0, 0), //starts with dan tian
					new Vector2f(0, 0), //then go to spine
					new Vector2f(0, 0), //then go to mind
					new Vector2f(0, 0), //then go to eyes
					new Vector2f(0, 0), //then go to ears
					new Vector2f(0, 0), //then go to nose
					new Vector2f(0, 0), //then go to mouth
					new Vector2f(0, 0), //then go to throat
					new Vector2f(0, 0)}; //then go to heart

	private static Vector2f currentTargetPoint = targetPoints[0];

	private final List<Strand> strands;

	public EssenceGatheringMinigame() {
		this.strands = new LinkedList<>();
	}

	@Override
	public void handleClick(int mouseX, int mouseY, MeditateScreen screen) {
	}

	@Override
	public void handleMouseDown(double mouseX, double mouseY, MeditateScreen screen) {
		for (Strand strand : this.strands) {
			if (MathUtils.between(mouseX, screen.getGuiLeft() + strand.x - 2, screen.getGuiLeft() + strand.x + 2) &&
					MathUtils.between(mouseY, screen.getGuiTop() + strand.y - 2, screen.getGuiTop() + strand.y + 2)) {
				strand.isGrabbed = true;
			}
		}
	}

	@Override
	public void handleMouseUp(double mouseX, double mouseY, MeditateScreen screen) {
		for (Strand strand : this.strands) {
			strand.isGrabbed = false;
		}
	}

	@Override
	public void renderBackground(MatrixStack stack, MeditateScreen screen) {
		screen.getMinecraft().getTextureManager().bindTexture(ESSENCE_GATHERING);
		screen.blit(stack, 78, 35, 0, 0, 43, 100);
		for (Strand strand : this.strands) {
			strand.draw(stack, screen);
		}
	}

	@Override
	public void renderForeground(MatrixStack stack, MeditateScreen screen) {

	}

	@Override
	public void handleTooltips(MatrixStack stack, int mouseX, int mouseY) {

	}

	@Override
	public void tick() {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
		double energy = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getEnergy();
		double strand_cost = 2.4f;
		int total_Strands = (int) (energy / strand_cost);
		while (this.strands.size() < total_Strands) {
			this.strands.add(new Strand());
		}
		for (Strand strand : this.strands) {
			strand.tick();
		}
	}

	private static class Strand {

		public float x;
		public float y;

		public float movX;
		public float movY;

		public int ticker;

		public boolean isGrabbed;

		public float red;
		public float green;
		public float blue;

		public Strand() {
			this.x = 78 + (float) Math.random() * 43f;
			this.y = 35 + (float) Math.random() * 100f;
			this.movX = -0.3f + 0.6f * (float) Math.random();
			this.movY = -0.3f + 0.6f * (float) Math.random();
			this.ticker = 0;
			this.isGrabbed = false;
			this.red = 0.90f;
			this.green = 0.95f;
			this.blue = 0.20f;
		}

		public Strand setColor(float red, float green, float blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
			return this;
		}

		public void tick() {
			if (this.isGrabbed) {
				this.x = (float) MeditateScreen.mousePosX;
				this.y = (float) MeditateScreen.mousePosY;
			} else {
				this.x += movX;
				if (!MathUtils.between(this.x, 68, 78 + 53)) {
					this.movX *= -1;
				}
				this.y += movY;
				if (!MathUtils.between(this.y, 25, 35 + 110)) {
					this.movY *= -1;
				}
			}
			ticker++;
			if (this.ticker >= 30) {
				this.ticker = 0;
				this.movX = -0.3f + 0.6f * (float) Math.random();
				this.movY = -0.3f + 0.6f * (float) Math.random();
			}
			this.x = MathUtils.clamp(this.x, 68, 78 + 53);
			this.y = MathUtils.clamp(this.y, 25, 35 + 110);
		}

		public void draw(MatrixStack stack, MeditateScreen screen) {
			screen.blitColored(stack, (int) this.x - 2, (int) this.y - 2, 43, 0, 5, 5, this.red, this.green, this.blue);

		}

	}
}
