package com.lazydragonstudios.wuxiacraft.client.render;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.client.render.models.GhostModel;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GhostRenderer extends EntityRenderer<AbstractClientPlayer> {

	private final static ResourceLocation GHOST_TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/entity/ghost.png");

	public static EntityType<AbstractClientPlayer> ghostEntityType;

	private GhostModel model;

	public GhostRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractClientPlayer player) {
		return GHOST_TEXTURE;
	}
}
