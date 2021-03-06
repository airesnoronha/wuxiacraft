package wuxiacraft.client.gui.minigame;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.gui.MeditateScreen;
import wuxiacraft.cultivation.*;
import wuxiacraft.network.AddCultivationToPlayerMessage;
import wuxiacraft.network.WuxiaPacketHandler;
import wuxiacraft.util.MathUtils;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class BodyCleansingMinigame implements IMinigame {

	private static final ResourceLocation BODY_CLEANSING = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/minigame/body_cleansing_minigame.png");

	private final List<Impurity> impurityList;
	private Strand drawnStrand; //by the moment it isn't grabbed then nullify again

	private static final Point[] bodyLocations = new Point[]{
			new Point(13, 0), //red
			new Point(56, 0), //yellow
			new Point(99, 0), //green
			new Point(142, 0), //blue
			new Point(185, 0), //purple
			new Point(13, 99), //white
	};

	private static final Point sourceLocation = new Point(99, 80);

	public BodyCleansingMinigame() {
		this.impurityList = new LinkedList<>();
		this.drawnStrand = null;
	}

	@Override
	public void handleClick(int mouseX, int mouseY, MeditateScreen screen) {

	}

	@Override
	public void handleMouseDown(double mouseX, double mouseY, MeditateScreen screen) {
		if (MathUtils.inBounds(MeditateScreen.mousePosX, MeditateScreen.mousePosY, sourceLocation.x - 4, sourceLocation.y - 4, 8, 8)) {
			drawnStrand = new Strand();
			drawnStrand.isGrabbed = true;
		}
	}

	@Override
	public void handleMouseUp(double mouseX, double mouseY, MeditateScreen screen) {
		this.drawnStrand = null;
	}

	@Override
	public void renderBackground(MatrixStack stack, MeditateScreen screen) {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
		screen.getMinecraft().getTextureManager().bindTexture(BODY_CLEANSING);
		SystemStats bodyStats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
		double baseOverMax = bodyStats.getBase() / (bodyStats.getLevel().getBaseBySubLevel(bodyStats.getSubLevel()) / 3);
		Point bg = bodyLocations[0];
		Point fg = bodyLocations[1];
		int fill = (int) (99 * baseOverMax);
		if (MathUtils.between(baseOverMax, 1, 3)) {
			bg = bodyLocations[1];
			fg = bodyLocations[2];
			fill = (int) (99 * (baseOverMax - 1) / 2);
		} else if (MathUtils.between(baseOverMax, 3, 6)) {
			bg = bodyLocations[2];
			fg = bodyLocations[3];
			fill = (int) (99 * (baseOverMax - 3) / 3);
		} else if (MathUtils.between(baseOverMax, 6, 10)) {
			bg = bodyLocations[3];
			fg = bodyLocations[4];
			fill = (int) (99 * (baseOverMax - 6) / 4);
		} else if (MathUtils.between(baseOverMax, 10, 20)) {
			bg = bodyLocations[4];
			fg = bodyLocations[5];
			fill = (int) (99 * (baseOverMax - 10) / 10);
		} else if (baseOverMax > 20) {
			bg = bodyLocations[5];
			fg = bodyLocations[5];
			fill = 99;
		}
		screen.blit(stack, 78, 35, bg.x, bg.y, 43, 99); //body figure
		screen.blit(stack, 78, 35 + 99 - fill, fg.x, fg.y + 99 - fill, 43, fill); //body figure
		screen.blitColored(stack, sourceLocation.x - 4, sourceLocation.y - 4, 0, 0, 8, 8, 0.9f, 0.95f, 0.2f);
		if (drawnStrand != null) {
			drawnStrand.draw(stack, screen, 8, 0);
		}
		for (Impurity impurity : this.impurityList) {
			impurity.draw(stack, screen);
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
		SystemStats bodyStats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
		SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		SystemStats divineStats = cultivation.getStatsBySystem(CultivationLevel.System.DIVINE);
		int max_impurities = 10;
		if (this.impurityList.size() < max_impurities) {
			int x = (int) (78 + 43 * Math.random());
			int y = (int) (35 + 99 * Math.random());
			this.impurityList.add(new Impurity(x, y));
		}
		if (this.impurityList.size() > max_impurities) {
			this.impurityList.remove(this.impurityList.size() - 1);
		}
		Impurity marked = null;
		if (drawnStrand != null) {
			drawnStrand.tick();
			for (Impurity impurity : this.impurityList) {
				if (MathUtils.inBounds(drawnStrand.x, drawnStrand.y, impurity.x - 6, impurity.y - 6, 12, 12)) {
					double strand_cost = 14f;
					KnownTechnique essenceTechnique = cultivation.getTechniqueBySystem(CultivationLevel.System.BODY);
					double energy_conversion = essenceTechnique != null ? 1 + essenceTechnique.getCultivationSpeed(essenceStats.getModifier()) : 1f;
					if (essenceStats.getEnergy() > strand_cost) {
						bodyStats.addBase(strand_cost * energy_conversion);
						essenceStats.addEnergy(-strand_cost);
						divineStats.addEnergy(-strand_cost * 0.2);
						WuxiaPacketHandler.INSTANCE.sendToServer(new AddCultivationToPlayerMessage(CultivationLevel.System.BODY, strand_cost, false));
						drawnStrand = null;
						marked = impurity;
						break;
					}
				}
			}
		}
		if (marked != null) {
			this.impurityList.remove(marked);
		}
	}
}
