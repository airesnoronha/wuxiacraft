package wuxiacraft.client.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;

public class HealthOverlay implements IIngameOverlay {

	private static final ResourceLocation HEALTH_BAR = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/health_bar.png");

	@Override
	public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		Minecraft mc = Minecraft.getInstance();
		if(mc.player == null) return;
		int i = width / 2 - 91;
		int j = height - gui.left_height;
		ICultivation cultivation = Cultivation.get(mc.player);
		//health
		RenderSystem.setShaderTexture(0, HEALTH_BAR);
		GuiComponent.blit(mStack, i, j, 81, 9, 0, 0, 81, 9, 81, 18);
		double max_hp = Cultivation.get(mc.player).getMaxHealth();
		double hp = Cultivation.get(mc.player).getHealth();
		int fill = (int) Math.min(Math.ceil((hp / max_hp) * 81), 81);
		GuiComponent.blit(mStack, i, j, fill, 9, 0, 9, fill, 9, 81, 18);
		//text
		String life = getShortHealthAmount((int) hp) + "/" + getShortHealthAmount((int) max_hp);
		int healthStringWidth = gui.getFont().width(life);
		gui.getFont().draw(mStack, life, (i + (81f - healthStringWidth) / 2), j + 1, 0xFFFFFF);
		gui.left_height += 11;
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
