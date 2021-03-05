package wuxiacraft.client.gui.minigame;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.gui.MeditateScreen;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.SystemStats;
import wuxiacraft.network.AddCultivationToPlayerMessage;
import wuxiacraft.network.WuxiaPacketHandler;
import wuxiacraft.util.MathUtils;

import java.util.LinkedList;
import java.util.List;

public class EssenceGatheringMinigame implements IMinigame {

	private static final ResourceLocation ESSENCE_GATHERING = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/minigame/essence_gathering_minigame.png");

	private static final int SPINE_LENGTH = 48;

	private static final Vector2f[] targetPoints = new Vector2f[]
			{new Vector2f(99, 80), //starts with dan tian
					new Vector2f(99, 87), //then go to spine
					new Vector2f(99, 39), //then go to mind
					new Vector2f(95, 42), //then go to eyes
					new Vector2f(99, 44), //then go to ears
					new Vector2f(94, 45), //then go to nose
					new Vector2f(95, 46), //then go to mouth
					new Vector2f(97, 50), //then go to throat
					new Vector2f(99, 65)}; //then go to heart

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
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
		screen.getMinecraft().getTextureManager().bindTexture(ESSENCE_GATHERING);
		screen.blit(stack, 78, 35, 0, 0, 43, 100); //body figure
		SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		if (!MathUtils.between(essenceStats.getSubLevel(), 0, 9))
			return; //something is wrong
		Vector2f point = targetPoints[essenceStats.getSubLevel()];
		if (essenceStats.getSubLevel() == 1) { //the spine moves upwards
			int upwards = (int) (essenceStats.getBase() * SPINE_LENGTH / essenceStats.getLevel().getBaseBySubLevel(essenceStats.getSubLevel()));
			point = new Vector2f(point.x, point.y - upwards);
		}
		screen.blitColored(stack, (int) point.x - 4, (int) point.y - 4, 48, 5, 8, 8, 0.8f, 0.2f, 0.2f); //draw the target point
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
		SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		double energy = essenceStats.getEnergy();
		double strand_cost = 14f;
		int total_Strands = (int) (energy / strand_cost);
		while (this.strands.size() < total_Strands) {
			this.strands.add(new Strand());
		}
		while (this.strands.size() > total_Strands) {
			this.strands.remove(this.strands.size() - 1);
		}
		Vector2f point = targetPoints[essenceStats.getSubLevel()];
		if (essenceStats.getSubLevel() == 1) { //the spine moves upwards
			int upwards = (int) (essenceStats.getBase() * SPINE_LENGTH / essenceStats.getLevel().getBaseBySubLevel(essenceStats.getSubLevel()));
			point = new Vector2f(point.x, point.y - upwards);
		}
		List<Strand> markedForRemoval = new LinkedList<>();
		for (Strand strand : this.strands) {
			strand.tick(); //handles movement
			if (MathUtils.between(strand.x, point.x - 3.5, point.x + 3.5) &&
					MathUtils.between(strand.y, point.y - 3.5, point.y + 3.5)) {
				//lets add cultivation here
				if (essenceStats.getEnergy() >= strand_cost) {
					markedForRemoval.add(strand);
					essenceStats.addBase(strand_cost);
					essenceStats.addEnergy(-strand_cost);
					WuxiaPacketHandler.INSTANCE.sendToServer(new AddCultivationToPlayerMessage(CultivationLevel.System.ESSENCE, strand_cost, false));
				}
			}
		}
		for (Strand marked : markedForRemoval) {
			this.strands.remove(marked);
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
