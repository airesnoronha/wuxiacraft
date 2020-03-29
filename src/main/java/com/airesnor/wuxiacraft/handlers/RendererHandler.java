package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.alchemy.Recipe;
import com.airesnor.wuxiacraft.blocks.Cauldron;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import com.airesnor.wuxiacraft.entities.tileentity.CauldronTileEntity;
import com.airesnor.wuxiacraft.gui.SkillsGui;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Mod.EventBusSubscriber
public class RendererHandler {

	public static final ResourceLocation bar_bg = new ResourceLocation(WuxiaCraft.MODID, "textures/gui/overlay/bar_bg.png");
	public static final ResourceLocation energy_bar = new ResourceLocation(WuxiaCraft.MODID, "textures/gui/overlay/energy_bar.png");
	public static final ResourceLocation hud_bars = new ResourceLocation(WuxiaCraft.MODID, "textures/gui/overlay/hud_bars.png");
	public static final ResourceLocation life_bar = new ResourceLocation(WuxiaCraft.MODID, "textures/gui/overlay/health_bar.png");
	public static final ResourceLocation icons = new ResourceLocation(WuxiaCraft.MODID, "textures/gui/overlay/icons.png");
	public static final ResourceLocation skills_bg = new ResourceLocation(WuxiaCraft.MODID, "textures/gui/overlay/skills_bg.png");
	public static final ResourceLocation cauldron_info = new ResourceLocation(WuxiaCraft.MODID, "textures/gui/overlay/cauldron_info.png");

	public static class WorldRenderQueue {

		public class RenderElement {
			private float duration; //in ticks
			private float prevPartialTicks;
			private Callable rendering;

			public RenderElement(float duration, Callable rendering) {
				this.duration = duration;
				this.rendering = rendering;
				this.prevPartialTicks = 0;
			}

			public boolean call(float partialTicks) {
				try {
					rendering.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
				prevPartialTicks = prevPartialTicks > partialTicks ? 0 : prevPartialTicks;
				this.duration -= partialTicks - prevPartialTicks;
				//prevPartialTicks = partialTicks;
				return this.duration <= 0;
			}

			public float getDuration() {
				return duration;
			}
		}

		private List<RenderElement> drawingQueue;

		public void renderQueue(float partialTicks) {
			List<RenderElement> toRemove = new ArrayList<>();
			for (RenderElement re : drawingQueue) {
				if (re.call(partialTicks)) {
					toRemove.add(re);
				}
			}
			for (RenderElement re : toRemove) {
				drawingQueue.remove(re);
			}
		}

		public void add(float duration, Callable rendering) {
			this.drawingQueue.add(new RenderElement(duration, rendering));
		}

		public WorldRenderQueue() {
			this.drawingQueue = new ArrayList<>();
		}

	}

	@SideOnly(Side.CLIENT)
	public static WorldRenderQueue worldRenderQueue = new WorldRenderQueue();

	@SideOnly(Side.CLIENT)
	public static Map<String, ICultivation> knownCultivations = new HashMap<>();

	@SideOnly(Side.CLIENT)
	private static int animationStep = 0;
	private static int selectedSkill = -1;

	@SubscribeEvent
	public void onRenderHud(RenderGameOverlayEvent.Post event) {
		if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
			return;
		}
		drawSkillCooldownBar(event.getResolution());
		drawCastProgressBar(event.getResolution());
		drawHudElements(event.getResolution());
		drawSkillsBar(event.getResolution());

		drawCauldronInfo(event.getResolution());
	}

	@SubscribeEvent
	public void onRenderHealthBar(RenderGameOverlayEvent.Pre event) {
		if (event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
			GuiIngameForge.left_height += 5;
			GuiIngameForge.right_height += 5;
			if (Minecraft.getMinecraft().player.getMaxHealth() > 40f) {
				event.setCanceled(true);
				drawCustomHealthBar(event.getResolution());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDescribeCultivationLevel(RenderLivingEvent.Specials.Post e) {
		if (e.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntity();
			String name = player.getName();
			if (knownCultivations.containsKey(name)) {
				ICultivation cultivation = knownCultivations.get(name);
				Minecraft mc = Minecraft.getMinecraft();
				boolean sneaking = player.isSneaking();
				boolean thirdPerson = mc.getRenderManager().options.thirdPersonView == 2;
				float x = (float) e.getX();
				float y = (float) e.getY() + 0.75f + player.height - (sneaking ? 0.25f : 0f);
				float z = (float) e.getZ();
				float f = mc.getRenderManager().playerViewY;
				float f1 = mc.getRenderManager().playerViewX;
				drawCultivationPlate(cultivation, x, y, z, f, f1, thirdPerson, sneaking);
			}
		}
		else if (e.getEntity() instanceof WanderingCultivator) {
			WanderingCultivator cultivator = (WanderingCultivator) e.getEntity();
			ICultivation cultivation = new Cultivation();
			cultivation.setCurrentLevel(cultivator.getCultivationLevel());
			cultivation.setCurrentSubLevel(cultivator.getCultivationSubLevel());
			Minecraft mc = Minecraft.getMinecraft();
			boolean sneaking = cultivator.isSneaking();
			boolean thirdPerson = mc.getRenderManager().options.thirdPersonView == 2;
			float x = (float) e.getX();
			float y = (float) e.getY() + 0.75f + cultivator.height - (sneaking ? 0.25f : 0f);
			float z = (float) e.getZ();
			float f = mc.getRenderManager().playerViewY;
			float f1 = mc.getRenderManager().playerViewX;
			drawCultivationPlate(cultivation, x, y, z, f, f1, thirdPerson, sneaking);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) throws Exception {
		worldRenderQueue.renderQueue(event.getPartialTicks());
	}

	public static void enableBoxRendering() {
		GlStateManager.disableTexture2D();
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.glLineWidth(2f);
	}

	public static void disableBoxRendering() {
		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
	}

	@SideOnly(Side.CLIENT)
	public void drawHudElements(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();//res.getScaledWidth();


		EntityPlayer player = mc.world.getPlayerEntityByUUID(mc.player.getUniqueID());
		ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);

		mc.renderEngine.bindTexture(hud_bars);

		float energy_fill = cultivation.getEnergy() / cultivation.getCurrentLevel().getMaxEnergyByLevel(cultivation.getCurrentSubLevel());
		float progress_fill = cultivation.getCurrentProgress() * 100f / cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel());

		int middleX = (width) / 2;

		GlStateManager.pushMatrix();

		//Show spinning dantian
		GlStateManager.pushMatrix();
		GlStateManager.translate(middleX, height - 45, 0);
		float showDiameter = 30;
		GlStateManager.scale(showDiameter / 256f, showDiameter / 256f, 1); //scale to certain size
		GlStateManager.scale(energy_fill, energy_fill, 1); //fill the bar
		float rotationSpeed = 0.07f;
		GlStateManager.rotate(((short) System.currentTimeMillis()) * -rotationSpeed, 0, 0, 1f);
		mc.ingameGUI.drawTexturedModalRect(-103, -103, 0, 0, 206, 206);
		GlStateManager.popMatrix();

		//Show Progress dragon
		GlStateManager.pushMatrix();
		int dragonWidth = 228;
		GlStateManager.translate(middleX - dragonWidth / 2f, height - 40f, 0);
		GlStateManager.rotate(0.65f, 0, 0, 1);
		GlStateManager.scale(1, 0.35, 1);
		mc.ingameGUI.drawTexturedModalRect(0, 0, 5, 206, (int) (dragonWidth * progress_fill / 100f), 50);
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();

		/*
		String message = String.format("Energy: %.0f (%.2f%%)",cultivation.getEnergy(), energy_fill);
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 20, Integer.parseInt("FFAA00",16));

		message = String.format("Progress: %.2f (%.2f%%)",cultivation.getCurrentProgress(), progress_fill);
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 30, Integer.parseInt("FFAA00",16));

		message = String.format("Player: %s, %s",player.getDisplayNameString(), cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()));
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 10, Integer.parseInt("FFAA00",16));

		message = String.format("Speed: %.3f(%.3f->%d%%)",player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue(),cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel()), WuxiaCraftConfig.speedHandicap);
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 40, Integer.parseInt("FFAA00",16));

		message = String.format("Strength: %.1f(%.3f)",player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue(),cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel()));
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 50, Integer.parseInt("FFAA00",16));

		message = String.format("Fall Distance: %.2f",player.fallDistance);
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 60, Integer.parseInt("FFAA00",16))
		*/
	}

	@SideOnly(Side.CLIENT)
	public void drawSkillCooldownBar(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.pushMatrix();
		GlStateManager.translate(res.getScaledWidth() / 2f - 94f, res.getScaledHeight() - 32, 0);
		Minecraft.getMinecraft().getTextureManager().bindTexture(icons);
		ISkillCap skillCap = mc.player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
		int coolDownBar = (int)(skillCap.getCooldown()/skillCap.getMaxCooldown() * 188);
		mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 10, coolDownBar, 11);
		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	public void drawCastProgressBar(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.pushMatrix();
		GlStateManager.translate(res.getScaledWidth() / 2f - 91f, res.getScaledHeight() - 29, 0);
		Minecraft.getMinecraft().getTextureManager().bindTexture(icons);
		ISkillCap skillCap = mc.player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
		if (skillCap.isCasting()) {
			mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 182, 5);
			if (skillCap.getActiveSkill() != -1) {
				Skill skill = skillCap.getSelectedSkills().get(skillCap.getActiveSkill());
				int progress = (int) (skillCap.getCastProgress() / skill.getCastTime() * 182);
				mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 5, progress, 5);
			}
		}
		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	public void drawSkillsBar(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		ISkillCap skillCap = Minecraft.getMinecraft().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
		if (selectedSkill != skillCap.getActiveSkill()) {
			selectedSkill = skillCap.getActiveSkill();
			animationStep = 0;
		} else {
			animationStep = Math.min(animationStep + 1, 20);
		}
		GlStateManager.pushMatrix();
		int h = 0;
		int x = res.getScaledWidth() - 20;
		int y = res.getScaledHeight() - 40;
		for (int i = 0; i < skillCap.getSelectedSkills().size(); i++) {
			if (i == skillCap.getActiveSkill()) {
				mc.renderEngine.bindTexture(skills_bg);
				drawTexturedRect(x, y - h - (20 + animationStep), 20, 20 + animationStep);
				Skill skill = skillCap.getSelectedSkills().get(i);
				mc.renderEngine.bindTexture(SkillsGui.skillIcons.get(skill.getUName()));
				drawTexturedRect(x + 2 - animationStep + animationStep / 10, y - h - 18 - (animationStep) + animationStep / 10, 16 + animationStep * 4 / 5, 16 + animationStep * 4 / 5);
				h += 20 + animationStep;
			} else {
				mc.renderEngine.bindTexture(skills_bg);
				drawTexturedRect(x, y - h - 20, 20, 20);
				Skill skill = skillCap.getSelectedSkills().get(i);
				mc.renderEngine.bindTexture(SkillsGui.skillIcons.get(skill.getUName()));
				drawTexturedRect(x + 2, y - h - 18, 16, 16);
				h += 20;
			}
		}
		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	public void drawCustomHealthBar(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		int i = res.getScaledWidth() / 2 - 91;
		int j = res.getScaledHeight() - GuiIngameForge.left_height;
		mc.getTextureManager().bindTexture(life_bar);
		drawTexturedRect(i, j, 81, 9, 0, 0, 1f, 0.5f);
		float max_hp = mc.player.getMaxHealth();
		float hp = mc.player.getHealth();
		int fill = (int) Math.ceil((hp / max_hp) * 81);
		drawTexturedRect(i, j, fill, 9, 0f, 0.5f, (hp / max_hp), 1f);
		String life = getShortHealthAmount((int) hp) + "/" + getShortHealthAmount((int) max_hp);
		int width = mc.fontRenderer.getStringWidth(life);
		mc.fontRenderer.drawString(life, (i + (81 - width) / 2), j + 1, 0xFFFFFF);
		mc.getTextureManager().bindTexture(Gui.ICONS);
		GuiIngameForge.left_height += 11;
	}

	@SideOnly(Side.CLIENT)
	public void drawCauldronInfo(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult rtr = mc.player.rayTrace(4.0, 0);
		if (rtr != null) {
			if (rtr.typeOfHit == RayTraceResult.Type.BLOCK) {
				IBlockState state = mc.player.world.getBlockState(rtr.getBlockPos());
				if (state.getBlock() instanceof Cauldron) {
					GlStateManager.pushMatrix();
					mc.getTextureManager().bindTexture(cauldron_info);
					CauldronTileEntity te = (CauldronTileEntity) mc.player.world.getTileEntity(rtr.getBlockPos());

					float fireStrength = te.getBurnSpeed() / 25f;
					float timeLeft = te.getTimeLit() / te.getMaxTimeLit();
					float temperature = te.getTemperature() / te.getMaxTemperature();

					int x = res.getScaledWidth() / 2 - (93 / 2) - 100;
					int y = res.getScaledHeight() / 2;

					int progress = (int) Math.floor(92 * fireStrength);
					GlStateManager.pushMatrix();
					GlStateManager.translate(x, y, 0);
					GlStateManager.scale(0.5, 0.5, 0);
					mc.ingameGUI.drawTexturedModalRect(0, -progress * 2, 0, (92 - progress) * 2, 92 * 2, progress * 2);
					//drawTexturedRect(x, y-progress, 92, progress, 0, (1f-fireStrength)*92f/128f,92f/128f, 92f/128f);
					GlStateManager.popMatrix();

					x = res.getScaledWidth() / 2 - (93 / 2);
					y = res.getScaledHeight() / 2 + 55;

					progress = (int) (94 * timeLeft);
					drawTexturedRect(x, y, 94, 17, 0, 111f / 128f, 93f / 128f, 128f / 128f);
					drawTexturedRect(x, y, progress, 17, 0, 94f / 128f, timeLeft * 93f / 128f, 110f / 128f);

					x = res.getScaledWidth() / 2 + 120 - (18 / 2);
					y = res.getScaledHeight() / 2 + 128 / 2;

					progress = (int) (128 * temperature);
					drawTexturedRect(x + 1, y - progress, 16, progress, 94f / 128f, 1f - temperature, 109f / 128f, 128f / 128f);
					drawTexturedRect(x, y - 128, 18, 128, 110f / 128f, 0, 128f / 128f, 128f / 128f);

					String temp = String.format("%.2f -", te.getTemperature());
					int tempWidth = mc.fontRenderer.getStringWidth(temp);
					mc.fontRenderer.drawStringWithShadow(temp, x - tempWidth + 10, y - progress - 4, 0xCF9D15);


					//Only show when debugging commentary to debug
					if (System.getProperties().contains("debug")) {
						if (System.getProperty("debug").equals("true")) {
							Recipe recipe = te.getActiveRecipe();
							if (recipe != null) {
								y = res.getScaledHeight() / 2 - 60;
								temp = recipe.getName();
								tempWidth = mc.fontRenderer.getStringWidth(temp);
								x = res.getScaledWidth() / 2 - tempWidth / 2;
								mc.fontRenderer.drawStringWithShadow(temp, x, y, 0xFFFF17);
							}
						}
					}

					GlStateManager.popMatrix();
				}
			}
		}
	}

	public static void drawTexturedRect(int x, int y, int w, int h) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2i(x, y);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2i(x, y + h);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2i(x + w, y + h);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2i(x + w, y);
		GL11.glEnd();
	}

	public static void drawTexturedRect(int x, int y, int w, int h, float itx, float ity, float ftx, float fty) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(itx, ity);
		GL11.glVertex2i(x, y);
		GL11.glTexCoord2f(itx, fty);
		GL11.glVertex2i(x, y + h);
		GL11.glTexCoord2f(ftx, fty);
		GL11.glVertex2i(x + w, y + h);
		GL11.glTexCoord2f(ftx, ity);
		GL11.glVertex2i(x + w, y);
		GL11.glEnd();
	}

	public static void drawCultivationPlate(ICultivation cultivation, float x, float y, float z, float viewYaw, float viewPitch, boolean thirdPerson, boolean sneaking) {
		Minecraft mc = Minecraft.getMinecraft();
		String str = cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel());
		FontRenderer fr = mc.getRenderManager().getFontRenderer();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-viewYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float) (thirdPerson ? -1 : 1) * viewPitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-0.025F, -0.025F, 0.025F);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);

		if (!sneaking) {
			GlStateManager.disableDepth();
		}

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		int i = fr.getStringWidth(str) / 2;
		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((-i - 1), -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		bufferbuilder.pos((-i - 1), 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		bufferbuilder.pos((i + 1), 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		bufferbuilder.pos((i + 1), -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();

		if (!sneaking) {
			fr.drawString(str, -fr.getStringWidth(str) / 2, 0, 553648127);
			GlStateManager.enableDepth();
		}

		GlStateManager.depthMask(true);
		fr.drawString(str, -fr.getStringWidth(str) / 2, 0, sneaking ? 553648127 : -1);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	public static String getShortHealthAmount(int amount) {
		String value = "";
		if(amount < 0) {
			value += "-";
		}
		amount = Math.abs(amount);
		if(amount < 1000) {
			value += amount;
		}
		else if(amount < 10000) {
			float mills =  amount / 1000f;
			value += String.format("%.1fk", mills);
		}
		else if(amount < 100000) {
			float mills =  amount / 1000f;
			value += String.format("%.0fk", mills);
		}
		else if(amount < 1000000) {
			float mills =  amount / 1000000f;
			value += String.format("%.2fM", mills);
		}
		else if(amount < 10000000) {
			float mills =  amount / 1000000f;
			value += String.format("%.1fM", mills);
		}
		else if(amount < 100000000) {
			float mills =  amount / 1000000f;
			value += String.format("%.0fM", mills);
		}
		else if(amount < 1000000000) {
			float mills =  amount / 1000000000f;
			value += String.format("%.2fG", mills);
		}
		return value;
	}
}
