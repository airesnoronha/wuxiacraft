package wuxiacraft.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.SystemStats;


public class RenderHudHandler {

	private final static ResourceLocation HEALTH_BAR = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/health_bar.png");
	private final static ResourceLocation ENERGY_BARS = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/energy_bars.png");

	@SubscribeEvent
	public void onPreRenderHud(RenderGameOverlayEvent.Pre event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
			assert Minecraft.getInstance().player != null;
			event.setCanceled(true);
			drawCustomHealthBar(event.getMatrixStack(), event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
		}
	}

	@SubscribeEvent
	public void onRenderHud(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
		drawEnergyBars(event.getMatrixStack(), event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
	}

	private static void drawHudElements(MatrixStack stack, int resX, int resY) {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);

		String text = String.format("HP: %d/%d", (int) cultivation.getHP(), (int) cultivation.getFinalModifiers().maxHealth);
		Minecraft.getInstance().ingameGUI.getFontRenderer().drawString(stack, text, 30, 30, 0xFFAA00);
	}

	private static void drawEnergyBars(MatrixStack stack, int resX, int resY) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return;
		ICultivation cultivation = Cultivation.get(mc.player);
		mc.getTextureManager().bindTexture(ENERGY_BARS);
		stack.push();
		stack.translate(resX / 2.0 + 95, resY - 47, 0);
		stack.scale(0.4f, 0.4f, 1f);
		mc.ingameGUI.blit(stack, 0, 0, 34, 140, 188, 116);
		//lets start with energy bar
		stack.push();
		SystemStats stats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		float energyFill = (float) (stats.getEnergy() / (cultivation.getMaxEssenceEnergy()));
		if(cultivation.getMaxEssenceEnergy() == 0) energyFill = 0;
		stack.translate(94.5, 56, 0);
		stack.scale(energyFill, energyFill, 0);

		float rotationSpeed = 1.50f - energyFill*1.45f;//in hertz
		float rotationAngle = 2 * (float)Math.PI * ((float)(System.currentTimeMillis() % (int)(1000f/rotationSpeed)))/(1000f/rotationSpeed);
		stack.rotate(Vector3f.ZP.rotation(-rotationAngle));

		mc.ingameGUI.blit(stack, -53, -53, 75, 0, 103, 103);
		stack.pop();
		//then body
		stats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
		int fill = (int) Math.ceil((stats.getEnergy() * 106.0) / (cultivation.getMaxBodyEnergy()));
		if(cultivation.getMaxBodyEnergy() == 0) fill = 0;
		mc.ingameGUI.blit(stack, 4, 4 + 106-fill, 0, 106-fill, 64, fill);
		//the divine
		stats = cultivation.getStatsBySystem(CultivationLevel.System.DIVINE);
		fill = (int) Math.ceil((stats.getEnergy() * 106.0) / (cultivation.getMaxDivineEnergy()));
		if(cultivation.getMaxDivineEnergy() == 0) fill = 0;
		mc.ingameGUI.blit(stack, 118, 4 + 106-fill, 190, 106-fill, 66, fill);

		stack.pop();
	}

	private static void drawCustomHealthBar(MatrixStack stack, int resX, int resY) {
		Minecraft mc = Minecraft.getInstance();
		int i = resX / 2 - 91;
		int j = resY - ForgeIngameGui.left_height;
		//health
		mc.getTextureManager().bindTexture(HEALTH_BAR);
		AbstractGui.blit(stack, i, j, 81, 9, 0, 0, 81, 9, 81, 18);
		assert mc.player != null;
		double max_hp = Cultivation.get(mc.player).getFinalModifiers().maxHealth;
		double hp = Cultivation.get(mc.player).getHP();
		int fill = (int) Math.min(Math.ceil((hp / max_hp) * 81), 81);
		AbstractGui.blit(stack, i, j, fill, 9, 0, 9, fill, 9, 81, 18);
		//text
		String life = getShortHealthAmount((int) hp) + "/" + getShortHealthAmount((int) max_hp);
		int width = mc.fontRenderer.getStringWidth(life);
		mc.fontRenderer.drawString(stack, life, (i + (81f - width) / 2), j + 1, 0xFFFFFF);
		mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
		ForgeIngameGui.left_height += 11;
	}

	public static String getShortHealthAmount(int amount) {
		String value = "";
		if (amount < 0) {
			value += "-";
		}
		amount = Math.abs(amount);
		if (amount < 1000) {
			value += amount;
		} else if (amount < 10000) {
			float mills = amount / 1000f;
			value += String.format("%.1fk", mills);
		} else if (amount < 100000) {
			float mills = amount / 1000f;
			value += String.format("%.0fk", mills);
		} else if (amount < 1000000) {
			float mills = amount / 1000000f;
			value += String.format("%.2fM", mills);
		} else if (amount < 10000000) {
			float mills = amount / 1000000f;
			value += String.format("%.1fM", mills);
		} else if (amount < 100000000) {
			float mills = amount / 1000000f;
			value += String.format("%.0fM", mills);
		} else if (amount < 10000000000.0) {
			float mills = amount / 1000000000f;
			value += String.format("%.2fG", mills);
		} else if (amount < 100000000000.0) {
			float mills = amount / 1000000000f;
			value += String.format("%.1fG", mills);
		} else if (amount < 1000000000000.0) {
			float mills = amount / 1000000000f;
			value += String.format("%.0fG", mills);
		} else if (amount < 10000000000000.0) {
			float mills = amount / 1000000000000f;
			value += String.format("%.2fT", mills);
		} else if (amount < 100000000000000.0) {
			float mills = amount / 1000000000000f;
			value += String.format("%.1fT", mills);
		}
		return value;
	}

}
