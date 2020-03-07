package com.airesnor.wuxiacraft.entities.tileentity;

import com.airesnor.wuxiacraft.blocks.Cauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class CauldronTESR extends TileEntitySpecialRenderer<CauldronTileEntity> {

    @Override
    public void render(CauldronTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x,y,z);
        //GlStateManager.disableRescaleNormal();

        if(te.isHasFirewood()) {
            renderFirewood(te);
        }

        if(te.isHasWater()) {
            renderWater(te);
        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderFirewood (CauldronTileEntity te) {
        GlStateManager.pushMatrix();

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

        World world = te.getWorld();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        IBlockState state = world.getBlockState(te.getPos()).getBlock().getDefaultState().withProperty(Cauldron.CAULDRON, 1);
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);
        dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), builder, true);
        tessellator.draw();

        GlStateManager.popMatrix();
    }

    private void renderWater (CauldronTileEntity te) {
        GlStateManager.pushMatrix();

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

        World world = te.getWorld();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        IBlockState state = world.getBlockState(te.getPos()).getBlock().getDefaultState().withProperty(Cauldron.CAULDRON, 2);
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);
        dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), builder, true);
        tessellator.draw();

        GlStateManager.popMatrix();
    }
}
