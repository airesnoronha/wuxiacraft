package wuxiacraft.client.gui.minigame;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.gui.MeditateScreen;
import wuxiacraft.cultivation.*;
import wuxiacraft.network.AddCultivationToPlayerMessage;
import wuxiacraft.network.WuxiaPacketHandler;
import wuxiacraft.util.MathUtils;

import java.awt.*;
import java.util.ArrayList;
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

	private static final Point[] ballsUV = new Point[]{
			new Point(56, 0), //red
			new Point(96, 0), //yellow
			new Point(136, 0), //green
			new Point(176, 0), //blue
			new Point(216, 0), //purple
			new Point(56, 40), //white
	};

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
		//render cultivation
		double maxBase = essenceStats.getLevel().getBaseBySubLevel(essenceStats.getSubLevel()) / 3.0;
		double cultBaseOverMax = essenceStats.getBase() / maxBase;
		Point bg = ballsUV[0]; //red
		Point fg = ballsUV[1]; //yellow
		float scale = (float) cultBaseOverMax;
		if (MathUtils.between(cultBaseOverMax, 1, 3)) {
			bg = ballsUV[1]; //yellow
			fg = ballsUV[2]; //green
			scale = (float) (cultBaseOverMax - 1) / 2f;
		} else if (MathUtils.between(cultBaseOverMax, 3, 6)) {
			bg = ballsUV[2]; //green
			fg = ballsUV[3]; //blue
			scale = (float) (cultBaseOverMax - 3) / 3f;
		} else if (MathUtils.between(cultBaseOverMax, 6, 10)) {
			bg = ballsUV[3]; //blue
			fg = ballsUV[4]; //purple
			scale = (float) (cultBaseOverMax - 6) / 4f;
		} else if (MathUtils.between(cultBaseOverMax, 10, 20)) {
			bg = ballsUV[4]; //purple
			fg = ballsUV[5]; //white
			scale = (float) (cultBaseOverMax - 10) / 10f;
		} else if (cultBaseOverMax > 20) {
			bg = ballsUV[5]; //white
			fg = ballsUV[5]; //white
			scale = 1f;
		}
		stack.push();
		stack.translate(27, 56, 0);
		screen.blit(stack, -20, -20, bg.x, bg.y, 40, 40); //red
		stack.scale(scale, scale, 1);
		screen.blit(stack, -20, -20, fg.x, fg.y, 40, 40); //yellow
		stack.pop();
		//render minigame
		Vector2f point = targetPoints[essenceStats.getSubLevel()];
		if (essenceStats.getSubLevel() == 1) { //the spine moves upwards
			int upwards = (int) (essenceStats.getBase() * SPINE_LENGTH / essenceStats.getLevel().getBaseBySubLevel(essenceStats.getSubLevel()));
			point = new Vector2f(point.x, point.y - upwards);
		}
		screen.blitColored(stack, (int) point.x - 4, (int) point.y - 4, 48, 0, 8, 8, 0.8f, 0.2f, 0.2f); //draw the target point
		for (Strand strand : this.strands) {
			strand.draw(stack, screen);
		}
	}

	@Override
	public void renderForeground(MatrixStack stack, MeditateScreen screen) {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
		screen.getFont().drawString(stack, "Essence Gathering", 10, 23, 0xFFAA00);
		KnownTechnique kt = cultivation.getTechniqueBySystem(CultivationLevel.System.ESSENCE);
		if (kt != null) {
			screen.getFont().drawString(stack, I18n.format("technique." + kt.getTechnique().getName()), 26, 140, 0xFFAA00);
		}
	}

	@Override
	public void handleTooltips(MatrixStack stack, int mouseX, int mouseY) {

	}

	@Override
	public void tick() {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
		SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		SystemStats divineStats = cultivation.getStatsBySystem(CultivationLevel.System.DIVINE);
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
					KnownTechnique essenceTechnique = cultivation.getTechniqueBySystem(CultivationLevel.System.ESSENCE);
					double energy_conversion = essenceTechnique != null ? 1 + essenceTechnique.getCultivationSpeed(essenceStats.getModifier()) : 1f;
					essenceStats.addBase(strand_cost * energy_conversion);
					essenceStats.addEnergy(-strand_cost);
					divineStats.addEnergy(-strand_cost * 0.2);
					WuxiaPacketHandler.INSTANCE.sendToServer(new AddCultivationToPlayerMessage(CultivationLevel.System.ESSENCE, strand_cost, false));
				}
			}
		}
		for (Strand marked : markedForRemoval) {
			this.strands.remove(marked);
		}
	}

}
