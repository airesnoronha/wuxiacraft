package com.lazydragonstudios.wuxiacraft.client;

import com.lazydragonstudios.wuxiacraft.client.render.renderer.AnimatedPlayerRenderer;
import com.lazydragonstudios.wuxiacraft.client.render.renderer.AuraRenderer;
import com.lazydragonstudios.wuxiacraft.client.render.renderer.GhostRenderer;
import com.lazydragonstudios.wuxiacraft.client.render.models.GhostModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEventHandler {

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event) {
	}

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Post event) {
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
	}

	@SubscribeEvent
	public static void onRenderingRegistry(EntityRenderersEvent.RegisterRenderers event) {
		AnimatedPlayerRenderer.animatedEntityType = EntityType.Builder.<AbstractClientPlayer>createNothing(MobCategory.MISC).build("animated_player_entity");
		GhostRenderer.ghostEntityType = EntityType.Builder.<AbstractClientPlayer>createNothing(MobCategory.MISC).build("ghost_entity");
		AuraRenderer.auraEntityType = EntityType.Builder.<AbstractClientPlayer>createNothing(MobCategory.MISC).build("aura_entity");
		event.registerEntityRenderer(AnimatedPlayerRenderer.animatedEntityType, ctx -> new AnimatedPlayerRenderer(ctx, false));
		event.registerEntityRenderer(GhostRenderer.ghostEntityType, GhostRenderer::new);
		event.registerEntityRenderer(AuraRenderer.auraEntityType, AuraRenderer::new);
	}

	@SubscribeEvent
	public static void onRegisterModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(GhostModel.LOCATION, () -> LayerDefinition.create(GhostModel.createMesh(CubeDeformation.NONE, 0f), 64, 64));
	}
}
