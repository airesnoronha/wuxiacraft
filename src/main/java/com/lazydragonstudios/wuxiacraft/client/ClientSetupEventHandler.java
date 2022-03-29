package com.lazydragonstudios.wuxiacraft.client;

import com.lazydragonstudios.wuxiacraft.client.gui.InscriberScreen;
import com.lazydragonstudios.wuxiacraft.client.gui.IntrospectionScreen;
import com.lazydragonstudios.wuxiacraft.client.gui.RunemakingScreen;
import com.lazydragonstudios.wuxiacraft.client.overlays.DebugOverlay;
import com.lazydragonstudios.wuxiacraft.client.overlays.EnergiesOverlay;
import com.lazydragonstudios.wuxiacraft.client.overlays.HealthOverlay;
import com.lazydragonstudios.wuxiacraft.client.overlays.SkillWheel;
import com.lazydragonstudios.wuxiacraft.client.particle.QiFogParticle;
import com.lazydragonstudios.wuxiacraft.client.render.renderer.AnimatedPlayerRenderer;
import com.lazydragonstudios.wuxiacraft.client.render.renderer.AuraRenderer;
import com.lazydragonstudios.wuxiacraft.client.render.renderer.GhostRenderer;
import com.lazydragonstudios.wuxiacraft.client.render.models.GhostModel;
import com.lazydragonstudios.wuxiacraft.client.render.renderer.ThrowSkillRenderer;
import com.lazydragonstudios.wuxiacraft.container.InscriberMenu;
import com.lazydragonstudios.wuxiacraft.container.IntrospectionMenu;
import com.lazydragonstudios.wuxiacraft.container.RunemakingMenu;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.init.WuxiaEntities;
import com.lazydragonstudios.wuxiacraft.init.WuxiaParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEventHandler {

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event) {
	}

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Post event) {
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		InputHandler.registerKeyBindings();

		MinecraftForge.EVENT_BUS.register(RenderHUDEventHandler.class);
		MinecraftForge.EVENT_BUS.register(InputHandler.class);

		OverlayRegistry.registerOverlayAbove(ForgeIngameGui.PLAYER_HEALTH_ELEMENT, "wuxiacraft_health_bar", new HealthOverlay());
		OverlayRegistry.registerOverlayBelow(ForgeIngameGui.CHAT_PANEL_ELEMENT, "wuxiacraft_energies", new EnergiesOverlay());
		OverlayRegistry.registerOverlayTop("wuxiacraft_skill_wheel", new SkillWheel());
		OverlayRegistry.registerOverlayTop("wuxiacraft_debug", new DebugOverlay());

		MenuScreens.register(IntrospectionMenu.registryType, IntrospectionScreen::new);
		MenuScreens.register(InscriberMenu.registryType, InscriberScreen::new);
		MenuScreens.register(RunemakingMenu.registryType, RunemakingScreen::new);
	}

	@SubscribeEvent
	public static void onRenderingRegistry(EntityRenderersEvent.RegisterRenderers event) {
		AnimatedPlayerRenderer.animatedEntityType = EntityType.Builder.<AbstractClientPlayer>createNothing(MobCategory.MISC).build("animated_player_entity");
		GhostRenderer.ghostEntityType = EntityType.Builder.<AbstractClientPlayer>createNothing(MobCategory.MISC).build("ghost_entity");
		AuraRenderer.auraEntityType = EntityType.Builder.<AbstractClientPlayer>createNothing(MobCategory.MISC).build("aura_entity");
		event.registerEntityRenderer(AnimatedPlayerRenderer.animatedEntityType, ctx -> new AnimatedPlayerRenderer(ctx, false));
		event.registerEntityRenderer(GhostRenderer.ghostEntityType, GhostRenderer::new);
		event.registerEntityRenderer(AuraRenderer.auraEntityType, AuraRenderer::new);
		event.registerEntityRenderer(WuxiaEntities.THROW_SKILL_TYPE.get(), ThrowSkillRenderer::new);
	}

	@SubscribeEvent
	public static void onRegisterModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(GhostModel.LOCATION, () -> LayerDefinition.create(GhostModel.createMesh(CubeDeformation.NONE, 0f), 64, 64));
	}

	@SubscribeEvent
	public static void onRegisterParticles(ParticleFactoryRegisterEvent event) {
		Minecraft.getInstance().particleEngine.register(WuxiaParticleTypes.BODY_QI_FOG.get(),
				QiFogParticle.Provider::new
		);
		Minecraft.getInstance().particleEngine.register(WuxiaParticleTypes.DIVINE_QI_FOG.get(),
				QiFogParticle.Provider::new
		);
		Minecraft.getInstance().particleEngine.register(WuxiaParticleTypes.ESSENCE_QI_FOG.get(),
				QiFogParticle.Provider::new
		);
	}
}
