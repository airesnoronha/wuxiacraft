package wuxiacraft.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.SkillValues;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.SystemStats;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.util.MathUtils;

import java.util.List;


public class RenderHudHandler {

	private final static ResourceLocation HEALTH_BAR = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/health_bar.png");
	private final static ResourceLocation ENERGY_BARS = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/energy_bars.png");
	private final static ResourceLocation SKILLS_BG = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/skills_bg.png");
	private final static ResourceLocation ICONS = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/icons.png");

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
		int resX = event.getWindow().getScaledWidth();
		int resY = event.getWindow().getScaledHeight();
		//drawDebugInfo(event.getMatrixStack(), resX, resY);
		drawEnergyBars(event.getMatrixStack(), resX, resY);
		drawSkillsBar(event.getMatrixStack(), resX, resY);
		drawCastProgressBar(event.getMatrixStack(), resX, resY);
	}

	private static void drawDebugInfo(MatrixStack stack, int resX, int resY) {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);

		SystemStats stats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);

		String text = "Essence Level: " + stats.getLevel().levelName;
		Minecraft.getInstance().ingameGUI.getFontRenderer().drawString(stack, text, 10, 10, 0);

		text = "Essence subLevel: " + stats.getSubLevel();
		Minecraft.getInstance().ingameGUI.getFontRenderer().drawString(stack, text, 10, 20, 0);

		text = "Essence Modifier: " + stats.getModifier();
		Minecraft.getInstance().ingameGUI.getFontRenderer().drawString(stack, text, 10, 30, 0);

		text = String.format("Essence Foundation + cBase: %.0f (%.2fx) <-> %.0f/%.0f (%.2f%%)", stats.getFoundation(),
				stats.getFoundation() / stats.getLevel().getBaseBySubLevel(stats.getSubLevel()),
				stats.getBase(), stats.getLevel().getBaseBySubLevel(stats.getSubLevel()),
				stats.getBase() * 100f / stats.getLevel().getBaseBySubLevel(stats.getSubLevel()));
		Minecraft.getInstance().ingameGUI.getFontRenderer().drawString(stack, text, 10, 40, 0);
	}

	public static float rotationAngle = 0;
	private static long lastMillis = 0;

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
		if (cultivation.getMaxEssenceEnergy() == 0) energyFill = 0;
		stack.translate(94.5, 56, 0);
		stack.scale(energyFill, energyFill, 0);

		float rotationSpeed = 1.8f - energyFill * 1.803f;//in hertz
		if (rotationSpeed == 0) rotationSpeed = 0.000001f;
		long timeDiff = System.currentTimeMillis() - lastMillis;
		rotationAngle += (InputHandler.isExercising ? 4f : 1f) * rotationSpeed * 2 * Math.PI * timeDiff / 1000f;// timeDiff;
		if (rotationAngle > 2 * Math.PI) {
			rotationAngle -= 2 * Math.PI;
		} else if (rotationAngle < -2 * Math.PI) {
			rotationAngle += 2 * Math.PI;
		}
		rotationAngle = (float) MathUtils.clamp(rotationAngle, -2 * Math.PI, 2 * Math.PI);
		stack.rotate(Vector3f.ZP.rotation(-rotationAngle));
		lastMillis = System.currentTimeMillis();
		mc.ingameGUI.blit(stack, -53, -53, 75, 0, 103, 103);
		stack.pop();
		//then body
		stats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
		int fill = (int) Math.ceil((stats.getEnergy() * 106.0) / (cultivation.getMaxBodyEnergy()));
		if (cultivation.getMaxBodyEnergy() == 0) fill = 0;
		mc.ingameGUI.blit(stack, 4, 4 + 106 - fill, 0, 106 - fill, 64, fill);
		//the divine
		stats = cultivation.getStatsBySystem(CultivationLevel.System.DIVINE);
		fill = (int) Math.ceil((stats.getEnergy() * 106.0) / (cultivation.getMaxDivineEnergy()));
		if (cultivation.getMaxDivineEnergy() == 0) fill = 0;
		mc.ingameGUI.blit(stack, 118, 4 + 106 - fill, 190, 106 - fill, 66, fill);

		stack.pop();
	}

	public static int lastKnownActive = 0;
	public static int animationStep = 0;

	private static void drawSkillsBar(MatrixStack stack, int resX, int resY) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return;
		ICultivation cultivation = Cultivation.get(mc.player);
		GlStateManager.enableBlend();
		stack.push();
		stack.translate(resX - 20, resY - 50, 0);
		List<Skill> knownSkills = cultivation.getAllKnownSkills();
		int usedUpwards = 0;
		for (Integer index : cultivation.getSelectedSkills()) {
			Skill skill = knownSkills.get(index);
			if (skill != null) {
				int size = lastKnownActive == SkillValues.activeSkill && lastKnownActive == cultivation.getSelectedSkills().indexOf(index) ? animationStep : 0;
				mc.getTextureManager().bindTexture(SKILLS_BG);
				AbstractGui.blit(stack, 0, -20 - usedUpwards - size, 20, 20 + size, 0, 0, 20, 20, 20, 20);
				mc.getTextureManager().bindTexture(new ResourceLocation(WuxiaCraft.MOD_ID, "textures/skills/icons/" + skill.name + ".png"));
				AbstractGui.blit(stack, -size, -20 - usedUpwards - size, 20 + size, 20 + size, 0, 0, 32, 32, 32, 32);
				usedUpwards += 20 + size;
			}
		}
		if (animationStep < 20) {
			animationStep++;
		}
		if (lastKnownActive != SkillValues.activeSkill) {
			lastKnownActive = SkillValues.activeSkill;
			animationStep = 0;
		}
		GlStateManager.disableBlend();
		stack.pop();
	}

	private static void drawCastProgressBar(MatrixStack stack, int resX, int resY) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return;
		ICultivation cultivation = Cultivation.get(mc.player);

		if (SkillValues.isCastingSkill) {
			stack.push();
			mc.getTextureManager().bindTexture(ICONS);
			mc.ingameGUI.blit(stack, resX / 2 - 91, resY - 29, 0, 0, 182, 5);
			Skill skill = cultivation.getActiveSkill(SkillValues.activeSkill);
			if (skill != null) {
				int castProgress = (int) (182 * SkillValues.castProgress / skill.castTime);
				mc.ingameGUI.blit(stack, resX / 2 - 91, resY - 29, 0, 5, castProgress, 5);
			}
			stack.pop();
		}
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
