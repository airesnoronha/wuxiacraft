package wuxiacraft.client.gui.minigame;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.gui.MeditateScreen;
import wuxiacraft.cultivation.*;
import wuxiacraft.network.AddCultivationToPlayerMessage;
import wuxiacraft.network.WuxiaPacketHandler;
import wuxiacraft.util.MathUtils;

import java.awt.*;

public class SoulCondensingMinigame implements IMinigame {

	private static final ResourceLocation SOUL_CONDENSING = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/minigame/soul_condensing_minigame.png");

	private static final double strand_cost = 14;

	private static final Point[] soulLocations = new Point[]{
			new Point(13, 0), //red
			new Point(56, 0), //yellow
			new Point(99, 0), //green
			new Point(142, 0), //blue
			new Point(185, 0), //purple
			new Point(13, 99), //white
	};

	private static final Point sourcePosition = new Point(69, 80);

	private final Point bodyPosition = new Point(48, 35);
	private final Point soulPosition = new Point(106, 35);

	private Strand drawnStrand = null;

	private Point destPosition = null;

	public SoulCondensingMinigame() {
	}

	@Override
	public void handleClick(int mouseX, int mouseY, MeditateScreen screen) {

	}

	@Override
	public void handleMouseDown(double mouseX, double mouseY, MeditateScreen screen) {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
		SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		if (MathUtils.inBounds(MeditateScreen.mousePosX, MeditateScreen.mousePosY, sourcePosition.x - 4, sourcePosition.y - 4, 8, 8)
				&& essenceStats.getEnergy() > strand_cost) {
			drawnStrand = new Strand().setBounds(48, 35, 154, 135);
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
		screen.getMinecraft().getTextureManager().bindTexture(SOUL_CONDENSING);
		SystemStats divineStats = cultivation.getStatsBySystem(CultivationLevel.System.DIVINE);
		double baseOverMax = divineStats.getBase() / (divineStats.getLevel().getBaseBySubLevel(divineStats.getSubLevel()) / 3);
		Point bg = soulLocations[0];
		Point fg = soulLocations[1];
		int fill = (int) (99 * baseOverMax);
		if (MathUtils.between(baseOverMax, 1, 3)) {
			bg = soulLocations[1];
			fg = soulLocations[2];
			fill = (int) (99 * (baseOverMax - 1) / 2);
		} else if (MathUtils.between(baseOverMax, 3, 6)) {
			bg = soulLocations[2];
			fg = soulLocations[3];
			fill = (int) (99 * (baseOverMax - 3) / 3);
		} else if (MathUtils.between(baseOverMax, 6, 10)) {
			bg = soulLocations[3];
			fg = soulLocations[4];
			fill = (int) (99 * (baseOverMax - 6) / 4);
		} else if (MathUtils.between(baseOverMax, 10, 20)) {
			bg = soulLocations[4];
			fg = soulLocations[5];
			fill = (int) (99 * (baseOverMax - 10) / 10);
		} else if (baseOverMax > 20) {
			bg = soulLocations[5];
			fg = soulLocations[5];
			fill = 99;
		}
		screen.blit(stack, bodyPosition.x, bodyPosition.y, 56, 99, 43, 99);
		screen.blit(stack, soulPosition.x, soulPosition.y, bg.x, bg.y, 43, 99);
		screen.blit(stack, soulPosition.x, soulPosition.y + 99 - fill, fg.x, fg.y + 99 - fill, 43, fill);
		screen.blitColored(stack, sourcePosition.x - 4, sourcePosition.y - 4, 0, 0, 8, 8, 0.9f, 0.2f, 0.2f);

		if (drawnStrand != null) {
			drawnStrand.draw(stack, screen, 8, 0);
		}
	}

	@Override
	public void renderForeground(MatrixStack stack, MeditateScreen screen) {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
		screen.getFont().drawString(stack, "Soul Condensing", 10, 23, 0xFFAA00);
		KnownTechnique kt = cultivation.getTechniqueBySystem(CultivationLevel.System.DIVINE);
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
		if (destPosition == null) {
			int x = (int) (soulPosition.x + Math.random() * 43);
			int y = (int) (soulPosition.y + Math.random() * 99);
			destPosition = new Point(x, y);
		}
		if (drawnStrand != null) {
			drawnStrand.tick();
			if (MathUtils.inBounds(drawnStrand.x, drawnStrand.y, soulPosition.x + 10, soulPosition.y + 10, 23, 79)) {
				KnownTechnique divineTechnique = cultivation.getTechniqueBySystem(CultivationLevel.System.BODY);
				double energy_conversion = divineTechnique != null ? 1 + divineTechnique.getCultivationSpeed(essenceStats.getModifier()) : 1f;
				if (essenceStats.getEnergy() > strand_cost) {
					divineStats.addBase(strand_cost * energy_conversion);
					essenceStats.addEnergy(-strand_cost);
					divineStats.addEnergy(-strand_cost * 0.2);
					WuxiaPacketHandler.INSTANCE.sendToServer(new AddCultivationToPlayerMessage(CultivationLevel.System.DIVINE, strand_cost, false));
					drawnStrand = null;
					destPosition = null;
				}
			}
		}
	}
}
