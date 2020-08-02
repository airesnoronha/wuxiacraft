package com.airesnor.wuxiacraft.entities.tileentity;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.Cauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class CauldronTESR extends TileEntitySpecialRenderer<CauldronTileEntity> {

	private static boolean IS_RENDER_ERROR = false;

	@Override
	public void render(CauldronTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();

		GlStateManager.translate(x, y, z);
		//GlStateManager.disableRescaleNormal();

		if (te.hasFirewood()) {
			renderFirewood(te);
		}

		if (te.hasWater()) {
			renderWater(te);
		}

		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}

	private void renderFirewood(CauldronTileEntity te) {
		GlStateManager.pushMatrix();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		try {
			this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			if (Minecraft.isAmbientOcclusionEnabled()) {
				GlStateManager.shadeModel(GL11.GL_SMOOTH);
			} else {
				GlStateManager.shadeModel(GL11.GL_FLAT);
			}
			GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

			World world = te.getWorld();

			IBlockState state = world.getBlockState(te.getPos()).getBlock().getDefaultState().withProperty(Cauldron.CAULDRON, 1);
			BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			IBakedModel model = dispatcher.getModelForState(state);
			dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), builder, true);
		}
		catch(Exception e) {
			if(!IS_RENDER_ERROR) {
				WuxiaCraft.logger.error("Error rendering the firewood, something is not right my friend");
				WuxiaCraft.logger.error(e.getMessage());
				IS_RENDER_ERROR = true;
			}
		} finally {
			tessellator.draw();
		}
		GlStateManager.popMatrix();
	}

	private void renderWater(CauldronTileEntity te) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();

		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		if (Minecraft.isAmbientOcclusionEnabled()) {
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
		} else {
			GlStateManager.shadeModel(GL11.GL_FLAT);
		}

		CauldronTileEntity.EnumCauldronState cauldronState = te.getCauldronState();


		World world = te.getWorld();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		GlStateManager.color(cauldronState.getColor().getRed() / 255f, cauldronState.getColor().getGreen() / 255f, cauldronState.getColor().getBlue() / 255f, 1f);

		IBlockState state = world.getBlockState(te.getPos()).getBlock().getDefaultState().withProperty(Cauldron.CAULDRON, 2);
		BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		IBakedModel model = dispatcher.getModelForState(state);
		for (BakedQuad quad : model.getQuads(state, null, 0)) {
			int[] vdata = new int[builder.getVertexFormat().getIntegerSize() * 4];
			int[] qdata = quad.getVertexData();
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < builder.getVertexFormat().getIntegerSize(); j++) {
					int k = j > 2 ? j + 1 : j;
					vdata[i * builder.getVertexFormat().getIntegerSize() + j] = qdata[i * quad.getFormat().getIntegerSize() + k];
				}
			}
			builder.addVertexData(vdata);
		}

		tessellator.draw();

		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
}
