package wuxiacraft.client.render.model;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class WuxiaRenderTypes extends RenderType {

	public static final RenderType getEntitySeeTrough(ResourceLocation location) {
		return makeType("see_through",
				DefaultVertexFormats.ENTITY,
				7,
				DefaultVertexFormats.ENTITY.getSize(),
				State.getBuilder()
						.cull(RenderState.CULL_ENABLED)
						.depthTest(RenderState.DEPTH_ALWAYS)
						.alpha(RenderState.DEFAULT_ALPHA)
						.transparency(RenderState.TRANSLUCENT_TRANSPARENCY)
						.lightmap(RenderState.LIGHTMAP_ENABLED)
						.shadeModel(RenderState.SHADE_ENABLED)
						.texture(new RenderState.TextureState(location, false, false))
						.build(false));
	}

	public WuxiaRenderTypes(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}


}
