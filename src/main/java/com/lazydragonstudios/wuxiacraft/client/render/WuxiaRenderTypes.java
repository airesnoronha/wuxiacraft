package com.lazydragonstudios.wuxiacraft.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;
import java.util.function.Supplier;

public class WuxiaRenderTypes extends RenderType {

	public static final Function<ResourceLocation, RenderType> getEntitySeeTrough = (ResourceLocation location) -> create("see_through",
			DefaultVertexFormat.NEW_ENTITY,
			VertexFormat.Mode.QUADS,
			DefaultVertexFormat.NEW_ENTITY.getVertexSize(),
			false, false,
			CompositeState.builder()
					.setCullState(CULL)
					.setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
					.setLightmapState(LIGHTMAP)
					.setWriteMaskState(RenderStateShard.COLOR_WRITE)
					.setShaderState(RenderStateShard.NEW_ENTITY_SHADER)
					.setTextureState(new TextureStateShard(location, false, false))
					.createCompositeState(false));

	public static final Function<ResourceLocation, RenderType> getAuraRenderer = (ResourceLocation location) -> create("aura_renderer",
			DefaultVertexFormat.NEW_ENTITY,
			VertexFormat.Mode.QUADS,
			DefaultVertexFormat.NEW_ENTITY.getVertexSize(),
			false, false,
			CompositeState.builder()
					.setCullState(NO_CULL)
					.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
					.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
					.setLightmapState(LIGHTMAP)
					.setShaderState(RenderStateShard.NEW_ENTITY_SHADER)
					.setWriteMaskState(RenderStateShard.COLOR_WRITE)
					.setTextureState(new TextureStateShard(location, false, false))
					.createCompositeState(false));

	public static final Supplier<RenderType> getTranslucentNoTexture = () -> create("translucent_no_texture",
			DefaultVertexFormat.POSITION_COLOR,
			VertexFormat.Mode.QUADS,
			DefaultVertexFormat.POSITION_COLOR.getVertexSize(),
			false, false,
			CompositeState.builder()
					.setCullState(CULL)
					.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
					.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
					.setLightmapState(LIGHTMAP)
					.setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
					.setWriteMaskState(RenderStateShard.COLOR_WRITE)
					.createCompositeState(false));

	public WuxiaRenderTypes(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}

}
