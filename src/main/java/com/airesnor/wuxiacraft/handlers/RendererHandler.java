package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.formation.FormationCoreBlock;
import com.airesnor.wuxiacraft.formation.FormationTileEntity;
import com.airesnor.wuxiacraft.profession.alchemy.Recipe;
import com.airesnor.wuxiacraft.blocks.Cauldron;
import com.airesnor.wuxiacraft.cultivation.BaseSystemLevel;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import com.airesnor.wuxiacraft.entities.tileentity.CauldronTileEntity;
import com.airesnor.wuxiacraft.gui.SkillsGui;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.MathUtils;
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

import java.util.*;
import java.util.concurrent.Callable;

@Mod.EventBusSubscriber
public class RendererHandler {

	//public static final ResourceLocation bar_bg = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/bar_bg.png");
	//public static final ResourceLocation energy_bar = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/energy_bar.png");
	public static final ResourceLocation hud_bars = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/hud_bars.png");
	public static final ResourceLocation life_bar = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/health_bar.png");
	public static final ResourceLocation icons = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/icons.png");
	public static final ResourceLocation skills_bg = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/skills_bg.png");
	public static final ResourceLocation cauldron_info = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/cauldron_info.png");
	public static final ResourceLocation formation_gui = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/formation_gui.png");

	public static class WorldRenderQueue {

		public static class RenderElement {
			private float duration; //in ticks
			private float prevPartialTicks;
			private final Callable<Void> rendering;

			public RenderElement(float duration, Callable<Void> rendering) {
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
		}

		private final List<RenderElement> drawingQueue;

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

		public void add(float duration, Callable<Void> rendering) {
			this.drawingQueue.add(new RenderElement(duration, rendering));
		}

		public WorldRenderQueue() {
			this.drawingQueue = new ArrayList<>();
		}

	}

	@SideOnly(Side.CLIENT)
	public static final WorldRenderQueue worldRenderQueue = new WorldRenderQueue();

	@SideOnly(Side.CLIENT)
	public static final Map<UUID, ICultivation> knownCultivations = new HashMap<>();

	@SideOnly(Side.CLIENT)
	private static int animationStep = 0;
	private static int selectedSkill = -1;

	@SubscribeEvent
	public void onRenderHud(RenderGameOverlayEvent.Post event) {
		if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
			return;
		}
		GlStateManager.pushAttrib();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		drawSkillCooldownBar(event.getResolution());
		drawCastProgressBar(event.getResolution());
		drawHudElements(event.getResolution());
		drawSkillsBar(event.getResolution());

		drawTileEntitiesInfo(event.getResolution());
		GlStateManager.popAttrib();
	}

	@SubscribeEvent
	public void onRenderHealthBar(RenderGameOverlayEvent.Pre event) {
		if (event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
			if (Minecraft.getMinecraft().player.getMaxHealth() > 40f) {
				event.setCanceled(true);
				drawCustomHealthBar(event.getResolution());
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDescribeCultivationLevel(RenderLivingEvent.Specials.Post e) {
		if (e.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntity();
			UUID uuid = player.getUniqueID();
			if (knownCultivations.containsKey(uuid)) {
				ICultivation cultivation = knownCultivations.get(uuid);
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
		} else if (e.getEntity() instanceof WanderingCultivator) {
			WanderingCultivator cultivator = (WanderingCultivator) e.getEntity();
			ICultivation playerCultivation = CultivationUtils.getCultivationFromEntity(Minecraft.getMinecraft().player);
			ICultivation cultivation = new Cultivation();
			if (cultivator.getCultivation().getEssenceLevel().greaterThan(playerCultivation.getEssenceLevel(), BaseSystemLevel.ESSENCE_LEVELS)) {
				cultivation.setEssenceLevel(playerCultivation.getEssenceLevel().nextLevel(BaseSystemLevel.ESSENCE_LEVELS));
				cultivation.setEssenceSubLevel(-1);
			} else {
				cultivation.setEssenceLevel(cultivator.getCultivation().getEssenceLevel());
				cultivation.setEssenceSubLevel(cultivator.getCultivation().getEssenceSubLevel());
			}
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
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		worldRenderQueue.renderQueue(event.getPartialTicks());
	}

	@SuppressWarnings("unused")
	public static void enableBoxRendering() {
		GlStateManager.disableTexture2D();
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.glLineWidth(2f);
	}

	@SuppressWarnings("unused")
	public static void disableBoxRendering() {
		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
	}

	@SuppressWarnings("SpellCheckingInspection")
	@SideOnly(Side.CLIENT)
	public void drawHudElements(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();//res.getScaledWidth();


		EntityPlayer player = mc.world.getPlayerEntityByUUID(mc.player.getUniqueID());
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);

		mc.renderEngine.bindTexture(hud_bars);

		double energy_fill = cultivation.getEnergy() / CultivationUtils.getMaxEnergy(player);

		int middleX = (width) / 2;

		GlStateManager.pushMatrix();

		//Show spinning dantian
		GlStateManager.pushMatrix();
		GlStateManager.translate(middleX, height - 40, 0);
		float showDiameter = 30;
		GlStateManager.scale(showDiameter / 256f, showDiameter / 256f, 1); //scale to certain size
		GlStateManager.scale(energy_fill, energy_fill, 1); //fill the bar
		float rotationSpeed = 0.07f;
		GlStateManager.rotate(((short) System.currentTimeMillis()) * -rotationSpeed, 0, 0, 1f);
		mc.ingameGUI.drawTexturedModalRect(-103, -103, 0, 0, 206, 206);
		GlStateManager.popMatrix();

		//Show Progress dragon
		/*GlStateManager.pushMatrix();
		int dragonWidth = 228;
		GlStateManager.translate(middleX - dragonWidth / 2f, height - 40f, 0);
		GlStateManager.rotate(0.65f, 0, 0, 1);
		GlStateManager.scale(1, 0.35, 1);
		mc.ingameGUI.drawTexturedModalRect(0, 0, 5, 206, (int) (dragonWidth * progress_fill / 100f), 50);
		GlStateManager.popMatrix();*/

		GlStateManager.popMatrix();

		//Show dragons now
		GlStateManager.pushMatrix();
		GlStateManager.translate(5, height-16, 0);
		int bodyProgress = (int)Math.min(124, (124*cultivation.getBodyProgress()/cultivation.getBodyLevel().getProgressBySubLevel(cultivation.getBodySubLevel())));
		int divineProgress = (int)Math.min(124, (124*cultivation.getDivineProgress()/cultivation.getDivineLevel().getProgressBySubLevel(cultivation.getDivineSubLevel())));
		int essenceProgress = (int)Math.min(124, (124*cultivation.getEssenceProgress()/cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel())));
		mc.ingameGUI.drawTexturedModalRect(0,-16, 0,208,bodyProgress,16);
		mc.ingameGUI.drawTexturedModalRect(0,-8, 0,240,divineProgress, 16);
		mc.ingameGUI.drawTexturedModalRect(0,0, 0,224,essenceProgress,16);
		GlStateManager.popMatrix();

		/*ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
		if (player != null) {
			String message = String.format("Speed: %.2f %.4f %.4f", cultivation.getMaxSpeed(),
					player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue(),
					player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue());
			mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 100, Integer.parseInt("FFAA00", 16));
			GlStateManager.color(1,1,1,1);
		}

		String message = String.format("Body Mod: %.4f",
				cultivation.getBodyModifier());
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 90, Integer.parseInt("FFAA00", 16));

		message = String.format("Divine Mod: %.4f",
				cultivation.getDivineModifier());
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 100, Integer.parseInt("FFAA00", 16));

		message = String.format("Essece Mod: %.4f",
				cultivation.getEssenceModifier());
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 110, Integer.parseInt("FFAA00", 16));

		/*if (cultTech.getEssenceTechnique() != null) {
			message = String.format("Essence Technique: %s %.4f", cultTech.getEssenceTechnique().getTechnique().getName(),
					cultTech.getEssenceTechnique().getProficiency());
			mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 120, Integer.parseInt("FFAA00", 16));
		}
		if (cultTech.getDivineTechnique() != null) {
			message  = String.format("Divine Technique: %s %.4f", cultTech.getDivineTechnique().getTechnique().getName(),
					cultTech.getDivineTechnique().getProficiency());
			mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 130, Integer.parseInt("FFAA00", 16));
		}
		/*
		message = String.format("Speed: %.3f(%.3f->%d%%)",player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue(),cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel()), WuxiaCraftConfig.speedHandicap);
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 40, Integer.parseInt("FFAA00",16));

		message = String.format("Strength: %.1f(%.3f)",player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue(),cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel()));
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 50, Integer.parseInt("FFAA00",16));

		message = String.format("Fall Distance: %.2f",player.fallDistance);
		mc.ingameGUI.drawString(mc.fontRenderer, message, 5, 60, Integer.parseInt("FFAA00",16))
		*/
		GlStateManager.color(1,1,1,1);
	}

	@SideOnly(Side.CLIENT)
	public void drawSkillCooldownBar(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.pushMatrix();
		GlStateManager.translate(res.getScaledWidth() / 2f - 94f, res.getScaledHeight() - 32, 0);
		Minecraft.getMinecraft().getTextureManager().bindTexture(icons);
		ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(mc.player);
		int coolDownBar = MathUtils.clamp((int) (skillCap.getCooldown() / skillCap.getMaxCooldown() * 188), 0, 188);
		mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 10, coolDownBar, 11);
		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	public void drawCastProgressBar(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.pushMatrix();
		GlStateManager.translate(res.getScaledWidth() / 2f - 91f, res.getScaledHeight() - 29, 0);
		Minecraft.getMinecraft().getTextureManager().bindTexture(icons);
		ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(mc.player);
		if (skillCap.isCasting()) {
			mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 182, 5);
			if (skillCap.getActiveSkill() != -1) {
				Skill skill = skillCap.getSelectedSkill(CultivationUtils.getCultTechFromEntity(mc.player));
				int progress = MathUtils.clamp((int) (skillCap.getCastProgress() / skill.getCastTime() * 182), 0, 182);
				mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 5, progress, 5);
			}
		}
		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	public void drawSkillsBar(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		ICultTech cultTech = CultivationUtils.getCultTechFromEntity(mc.player);
		ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(mc.player);
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
		List<Skill> totalKnown = skillCap.getTotalKnowSkill(cultTech);
		for (int i = 0; i < skillCap.getSelectedSkills().size(); i++) {
			if (totalKnown.size() > 0 && MathUtils.between(skillCap.getSelectedSkills().get(i), 0, totalKnown.size())) { // this way won't select unwanted skills
				Skill skill = totalKnown.get(MathUtils.clamp(skillCap.getSelectedSkills().get(i), 0, totalKnown.size() - 1));
				if (i == skillCap.getActiveSkill()) {
					mc.renderEngine.bindTexture(skills_bg);
					drawTexturedRect(x, y - h - (20 + animationStep), 20, 20 + animationStep);
					mc.renderEngine.bindTexture(SkillsGui.skillIcons.get(skill.getUName()));
					drawTexturedRect(x + 2 - animationStep + animationStep / 10, y - h - 18 - (animationStep) + animationStep / 10, 16 + animationStep * 4 / 5, 16 + animationStep * 4 / 5);
					h += 20 + animationStep;
				} else {
					mc.renderEngine.bindTexture(skills_bg);
					drawTexturedRect(x, y - h - 20, 20, 20);
					mc.renderEngine.bindTexture(SkillsGui.skillIcons.get(skill.getUName()));
					drawTexturedRect(x + 2, y - h - 18, 16, 16);
					h += 20;
				}
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
		int fill = (int) Math.min(Math.ceil((hp / max_hp) * 81), 81);
		drawTexturedRect(i, j, fill, 9, 0f, 0.5f, (hp / max_hp), 1f);
		String life = getShortHealthAmount((int) hp) + "/" + getShortHealthAmount((int) max_hp);
		int width = mc.fontRenderer.getStringWidth(life);
		mc.fontRenderer.drawString(life, (i + (81 - width) / 2), j + 1, 0xFFFFFF);
		mc.getTextureManager().bindTexture(Gui.ICONS);
		GuiIngameForge.left_height += 11;
	}

	private static int stepAnimation = 0;

	@SideOnly(Side.CLIENT)
	public void drawTileEntitiesInfo(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult rtr = mc.player.rayTrace(4.0, 0);
		if (rtr != null) {
			if (rtr.typeOfHit == RayTraceResult.Type.BLOCK) {
				IBlockState state = mc.player.world.getBlockState(rtr.getBlockPos());
				if (state.getBlock() instanceof Cauldron) {
					GlStateManager.pushMatrix();
					mc.getTextureManager().bindTexture(cauldron_info);
					CauldronTileEntity te = (CauldronTileEntity) mc.player.world.getTileEntity(rtr.getBlockPos());
					if (te != null) {
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
					}

					GlStateManager.popMatrix();
				}
				else  if(state.getBlock() instanceof FormationCoreBlock) {
					FormationTileEntity formation = (FormationTileEntity) mc.player.world.getTileEntity(rtr.getBlockPos());
					if(formation != null) {
						mc.getTextureManager().bindTexture(formation_gui);
						int y = res.getScaledHeight() - Math.max(GuiIngameForge.left_height, GuiIngameForge.right_height) - 18;
						int x = res.getScaledWidth() / 2 - 95;
						mc.ingameGUI.drawTexturedModalRect(x, y, 0, 0, 190, 18);
						int energyFill = (int)(formation.getEnergy() * 181/ formation.getMaxEnergy());
						int animation = stepAnimation / 3;
						mc.ingameGUI.drawTexturedModalRect(x + 5, y + 4, 0, 22 + animation * 9, energyFill, 9);
						stepAnimation++;
						if(stepAnimation >= 24) stepAnimation = 0;
						//mc.fontRenderer.drawStringWithShadow(String.format("Energy: %.2f", formation.getEnergy()), x + 40, y + 5, 0xCF9D15);
					}
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
		String str = cultivation.getEssenceLevel().getLevelName(cultivation.getEssenceSubLevel());
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
		} else if (amount < 1000000000) {
			float mills = amount / 1000000000f;
			value += String.format("%.2fG", mills);
		}
		return value;
	}
}
