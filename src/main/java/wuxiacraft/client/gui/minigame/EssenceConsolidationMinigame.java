package wuxiacraft.client.gui.minigame;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.gui.MeditateScreen;
import wuxiacraft.cultivation.*;
import wuxiacraft.network.AddCultivationToPlayerMessage;
import wuxiacraft.network.WuxiaPacketHandler;
import wuxiacraft.util.MathUtils;

import java.awt.*;

public class EssenceConsolidationMinigame implements IMinigame {

	private static final ResourceLocation ESSENCE_CONSOLIDATION = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/minigame/essence_consolidation_minigame.png");

	private static final double strand_cost = 200f;

	private final Point[] heartsLocation = new Point[]{
			new Point(84, 100), //red
			new Point(91, 100), //yellow
			new Point(98, 100), //green
			new Point(105, 100), //blue
			new Point(112, 100), //purple
			new Point(119, 100)  //white
	};

	private final Point[] dantianLocation = new Point[]{
			new Point(84, 110), //red
			new Point(95, 110), //yellow
			new Point(104, 110), //green
			new Point(113, 110), //blue
			new Point(122, 110), //purple
			new Point(131, 110)  //white
	};
	private final Point[] headsLocation = new Point[]{
			new Point(84, 119), //red
			new Point(102, 119), //yellow
			new Point(120, 119), //green
			new Point(138, 119), //blue
			new Point(156, 119), //purple
			new Point(174, 119)  //white
	};
	private final Point[] earsLocation = new Point[]{
			new Point(84, 134), //red
			new Point(89, 134), //yellow
			new Point(94, 134), //green
			new Point(99, 134), //blue
			new Point(104, 134), //purple
			new Point(109, 134)  //white
	};
	private final Point[] nosesLocation = new Point[]{
			new Point(84, 141), //red
			new Point(89, 141), //yellow
			new Point(94, 141), //green
			new Point(99, 141), //blue
			new Point(104, 141), //purple
			new Point(109, 141)  //white
	};
	private final Point[] eyesLocation = new Point[]{
			new Point(84, 148), //red
			new Point(90, 148), //yellow
			new Point(96, 148), //green
			new Point(102, 148), //blue
			new Point(108, 148), //purple
			new Point(114, 148)  //white
	};
	private final Point[] mouthLocations = new Point[]{
			new Point(84, 153), //red
			new Point(90, 153), //yellow
			new Point(96, 153), //green
			new Point(102, 153), //blue
			new Point(108, 153), //purple
			new Point(114, 153)  //white
	};
	private final Point[] throatsLocations = new Point[]{
			new Point(84, 157), //red
			new Point(90, 157), //yellow
			new Point(96, 157), //green
			new Point(102, 157), //blue
			new Point(108, 157), //purple
			new Point(114, 157)  //white
	};
	private final Point[] spineLocations = new Point[]{
			new Point(0, 100), //red
			new Point(14, 100), //yellow
			new Point(28, 100), //green
			new Point(42, 100), //blue
			new Point(56, 100), //purple
			new Point(70, 100)  //white
	};

	private final Point[] placesPositions = new Point[]{
			new Point(51, 24), //spine
			new Point(40, 2), //head
			new Point(37, 7), //eyes
			new Point(45, 12), //ears
			new Point(35, 10), //nose
			new Point(37, 19), //mouth
			new Point(44, 25), //throat
			new Point(46, 43),  //heart
			new Point(46, 72), //dantian
	};

	private final Point[] placesSizes = new Point[]{
			new Point(13, 68), //spine
			new Point(18, 15), //head
			new Point(6, 5), //eyes
			new Point(5, 7), //ears
			new Point(5, 7), //nose
			new Point(6, 4), //mouth
			new Point(6, 7), //throat
			new Point(7, 10),  //heart
			new Point(9, 9), //dantian
	};

	private final Point[] cultivationBallsLocations = new Point[]{
			new Point(68, 0), //red
			new Point(108, 0), //yellow
			new Point(148, 0), //green
			new Point(188, 0), //blue
			new Point(68, 40), //purple
			new Point(108, 40), //white
	};

	private final Point[][] organUVLocationsBySubLevel = new Point[][]{
			spineLocations,
			headsLocation,
			eyesLocation,
			earsLocation,
			nosesLocation,
			mouthLocations,
			throatsLocations,
			heartsLocation,
			dantianLocation
	};

	private final Point[] checkpoints = new Point[]{
			new Point(50, 76), //dantian
			new Point(58, 90), //spine low
			new Point(63, 76), //spine middle low
			new Point(62, 60), //spine middle
			new Point(59, 37), //spine middle up
			new Point(52, 25), //spine up
			new Point(52, 12), //head back
			new Point(43, 6), //head front
			new Point(39, 9), //eyes
			new Point(47, 14), //ears
			new Point(36, 15), //nose
			new Point(38, 21), //mouth
			new Point(46, 29), //throat
			new Point(49, 49), //heart
	};

	private final int[] targetCheckpointBySubLevel = new int[]{1, 6, 8, 9, 10, 11, 12, 13, 0};

	private int activeCheckPoint = 0;

	private Strand drawnStrand = null;

	@Override
	public void handleClick(int mouseX, int mouseY, MeditateScreen screen) {
	}

	@Override
	public void handleMouseDown(double mouseX, double mouseY, MeditateScreen screen) {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
		SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		if (MathUtils.inBounds(MeditateScreen.mousePosX, MeditateScreen.mousePosY, 66 + checkpoints[0].x - 4, 35 + checkpoints[0].y - 4, 9, 9)
				&& essenceStats.getEnergy() > strand_cost) {
			drawnStrand = new Strand().setBounds(66, 35, 132, 135);
			drawnStrand.isGrabbed = true;
			activeCheckPoint = 0;
		}
	}

	@Override
	public void handleMouseUp(double mouseX, double mouseY, MeditateScreen screen) {
		this.drawnStrand = null;
		activeCheckPoint = 0;
	}

	@Override
	public void renderBackground(MatrixStack stack, MeditateScreen screen) {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
		screen.getMinecraft().getTextureManager().bindTexture(ESSENCE_CONSOLIDATION);
		screen.blit(stack, 78, 35, 0, 0, 43, 100); //body figure
		SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		if (!MathUtils.between(essenceStats.getSubLevel(), 0, 9))
			return; //something is wrong
		double maxBase = essenceStats.getLevel().getBaseBySubLevel(essenceStats.getSubLevel()) / 3.0;
		double cultBaseOverMax = essenceStats.getBase() / maxBase;
		double foundationOverMax = essenceStats.getFoundation() / essenceStats.getLevel().getBaseBySubLevel(essenceStats.getSubLevel());
		Pair<Integer, Float> baseTemp = MeditateScreen.getIdAndFillFromMaxCultBase(cultBaseOverMax);
		Pair<Integer, Float> foundationTemp = MeditateScreen.getIdAndFillFromMaxCultBase(foundationOverMax);
		int baseFgId = baseTemp.getKey();
		int foundationFgId = foundationTemp.getKey();
		float baseFill = (float) baseTemp.getValue();
		float foundationFill = (float) foundationTemp.getValue();
		int subLevel = essenceStats.getSubLevel();
		stack.push();
		stack.push();
		//cult ball
		stack.translate(27, 56, 0);
		screen.blit(stack, -20, -20, cultivationBallsLocations[baseFgId - 1].x, cultivationBallsLocations[baseFgId - 1].y, 40, 40); //bg ball
		stack.scale(baseFill, baseFill, 1);
		screen.blit(stack, -20, -20, cultivationBallsLocations[baseFgId].x, cultivationBallsLocations[baseFgId].y, 40, 40); //fg ball
		stack.pop();
		//body
		screen.blit(stack, 66, 35, 0, 0, 68, 100);

		//organs
		for (int i = 0; i < 9; i++) {
			if (i < subLevel) {
				int fill = (int) (foundationFill * placesSizes[i].y);
				screen.blit(stack, 66 + placesPositions[i].x, 35 + placesPositions[i].y,
						organUVLocationsBySubLevel[i][foundationFgId - 1].x, organUVLocationsBySubLevel[i][foundationFgId - 1].y,
						placesSizes[i].x, placesSizes[i].y);
				screen.blit(stack, 66 + placesPositions[i].x, 35 + placesPositions[i].y + placesSizes[i].y - fill,
						organUVLocationsBySubLevel[i][foundationFgId].x, organUVLocationsBySubLevel[i][foundationFgId].y + placesSizes[i].y - fill,
						placesSizes[i].x, fill);
			} else if (i == subLevel) {
				int fill = (int) (baseFill * placesSizes[i].y);
				screen.blit(stack, 66 + placesPositions[i].x, 35 + placesPositions[i].y,
						organUVLocationsBySubLevel[i][baseFgId - 1].x, organUVLocationsBySubLevel[i][baseFgId - 1].y,
						placesSizes[i].x, placesSizes[i].y);
				screen.blit(stack, 66 + placesPositions[i].x, 35 + placesPositions[i].y + placesSizes[i].y - fill,
						organUVLocationsBySubLevel[i][baseFgId].x, organUVLocationsBySubLevel[i][baseFgId].y + placesSizes[i].y - fill,
						placesSizes[i].x, fill);
			} else {
				screen.blit(stack, 66 + placesPositions[i].x, 35 + placesPositions[i].y,
						organUVLocationsBySubLevel[i][0].x, organUVLocationsBySubLevel[i][0].y, //just red
						placesSizes[i].x, placesSizes[i].y);
			}
		}

		if (drawnStrand != null) {
			drawnStrand.draw(stack, screen, 68, 80);
			screen.blitColored(stack, 66 + checkpoints[activeCheckPoint].x - 4, 35 + checkpoints[activeCheckPoint].y - 4, 73, 80, 9, 9, 0.9f, 0.3f, 0.0f);
		}

		stack.pop();
	}

	@Override
	public void renderForeground(MatrixStack stack, MeditateScreen screen) {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
		screen.getFont().drawString(stack, "Essence Consolidation", 10, 23, 0xFFAA00);
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
		if (drawnStrand != null) {
			drawnStrand.tick();
			if (MathUtils.inBounds(drawnStrand.x, drawnStrand.y,
					66 + checkpoints[activeCheckPoint].x - 4, 35 + checkpoints[activeCheckPoint].y - 4, 9, 9)
					&& essenceStats.getEnergy() > strand_cost) {
				if (activeCheckPoint == targetCheckpointBySubLevel[essenceStats.getSubLevel()]) {
					KnownTechnique essenceTechnique = cultivation.getTechniqueBySystem(CultivationLevel.System.ESSENCE);
					double energy_conversion = essenceTechnique != null ? 1 + essenceTechnique.getCultivationSpeed(essenceStats.getModifier()) : 1f;
					essenceStats.addBase(strand_cost * energy_conversion);
					essenceStats.addEnergy(-strand_cost);
					divineStats.addEnergy(-strand_cost * 0.2);
					WuxiaPacketHandler.INSTANCE.sendToServer(new AddCultivationToPlayerMessage(CultivationLevel.System.ESSENCE, strand_cost, false));
					drawnStrand = null;
				} else {
					activeCheckPoint++;
					if (activeCheckPoint == checkpoints.length) activeCheckPoint = 0;
				}
			}
		}
	}
}
