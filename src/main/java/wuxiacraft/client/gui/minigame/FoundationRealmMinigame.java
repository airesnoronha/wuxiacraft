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

public class FoundationRealmMinigame implements IMinigame {

	private static final ResourceLocation ESSENCE_GATHERING = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/minigame/essence_gathering_minigame.png");

	private static final Point[] locations = new Point[]{
			new Point(56, 80), //red
			new Point(116, 80), //yellow
			new Point(176, 80), //green
			new Point(56, 140), //blue
			new Point(116, 140), //purple
			new Point(176, 140), //white
	};

	private List<Strand> strands;

	public FoundationRealmMinigame() {
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
		SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		double foundationOver = essenceStats.getBase() / essenceStats.getLevel().getBaseBySubLevel(0);
		Point bg = locations[0]; //red
		Point fg = locations[1]; //yellow
		int fill = (int) (foundationOver * 60);
		if (MathUtils.between(foundationOver, 1, 3)) {
			bg = locations[1]; //yellow
			fg = locations[2]; //green
			fill = (int) ((foundationOver - 1) * 60f / 2f);
		} else if (MathUtils.between(foundationOver, 3, 6)) {
			bg = locations[2]; //green
			fg = locations[3]; //blue
			fill = (int) ((foundationOver - 3) * 60f / 3f);
		} else if (MathUtils.between(foundationOver, 6, 10)) {
			bg = locations[3]; //blue
			fg = locations[4]; //purple
			fill = (int) ((foundationOver - 6) * 60f / 4f);
		} else if (MathUtils.between(foundationOver, 10, 20)) {
			bg = locations[4]; //purple
			fg = locations[5]; //white
			fill = (int) ((foundationOver - 10) * 60f / 10f);
		} else if (foundationOver > 20) {
			bg = locations[5]; //white
			fg = locations[5]; //white
			fill = 60;
		}
		screen.blit(stack, 70, 65, bg.x, bg.y, 60, 60);
		screen.blit(stack, 70, 65 + 60 - fill, fg.x, fg.y + 60 - fill, 60, fill);
		screen.blitColored(stack, 96, 108, 48, 0, 8, 8, 0.9f, 0.95f, 0.2f);
		for (Strand strand : strands) {
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
		SystemStats divineStats = cultivation.getStatsBySystem(CultivationLevel.System.DIVINE);
		double energy = essenceStats.getEnergy();
		double strand_cost = 2;
		int total_Strands = (int) (energy / strand_cost);
		while (this.strands.size() < total_Strands) {
			this.strands.add(new Strand().setBounds(60, 35, 140, 145));
		}
		while (this.strands.size() > total_Strands) {
			this.strands.remove(this.strands.size() - 1);
		}
		List<Strand> markedForRemoval = new LinkedList<>();
		for (Strand strand : this.strands) {
			strand.tick();
			if (MathUtils.between(strand.x, 90, 110) &&
					MathUtils.between(strand.y, 105, 125)) {
				markedForRemoval.add(strand);
				KnownTechnique essenceTechnique = cultivation.getTechniqueBySystem(CultivationLevel.System.ESSENCE);
				double energy_conversion = 6f * (essenceTechnique != null ? 1 + essenceTechnique.getCultivationSpeed(essenceStats.getModifier()) : 1f);
				essenceStats.addBase(strand_cost * energy_conversion);
				essenceStats.addEnergy(-strand_cost);
				divineStats.addEnergy(-strand_cost * 0.2);
				WuxiaPacketHandler.INSTANCE.sendToServer(new AddCultivationToPlayerMessage(CultivationLevel.System.ESSENCE, strand_cost, false));
			}
		}
		for (Strand marked : markedForRemoval) {
			this.strands.remove(marked);
		}
	}

}
