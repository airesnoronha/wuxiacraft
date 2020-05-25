package com.airesnor.wuxiacraft.entities.tileentity;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.MagicalGrinder;
import com.airesnor.wuxiacraft.items.ItemSpiritStone;
import com.airesnor.wuxiacraft.utils.OBJModelLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

@ParametersAreNonnullByDefault
public class GrinderTESR extends TileEntitySpecialRenderer<GrinderTileEntity> {

	private static Map<String, OBJModelLoader.Part> GRINDER_ROLL_MODEL;
	private static Map<String, OBJModelLoader.Part> SPIRIT_STONE_MODEL;

	private static final ResourceLocation ROLL_TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/grinder_roll.png");
	private static final ResourceLocation STONE_TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/spirit_stone_stack_block.png");

	public static void init() {
		try {
			GRINDER_ROLL_MODEL = OBJModelLoader.getPartsFromFile(new ResourceLocation(WuxiaCraft.MOD_ID, "models/block/magical_grinder_roll.obj"));
			SPIRIT_STONE_MODEL = OBJModelLoader.getPartsFromFile(new ResourceLocation(WuxiaCraft.MOD_ID, "models/block/spirit_stone.obj"));
		} catch (IOException e) {
			WuxiaCraft.logger.error("Couldn't find grinder models. Lame");
			e.printStackTrace();
		}
	}

	@Override
	public void render(GrinderTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z); //this is the delta from the player
		IBlockState state = te.getWorld().getBlockState(te.getPos()); //this will tell me which face to render
		EnumFacing direction = state.getValue(MagicalGrinder.FACING);
		switch (direction) {
			case NORTH:
				GlStateManager.translate(1,0,1);
				GlStateManager.rotate(180, 0, 1,0);
				break;
			case EAST:
				GlStateManager.translate(0,0,1);
				GlStateManager.rotate(90, 0, 1,0);
				break;
			case WEST:
				GlStateManager.translate(1,0,0);
				GlStateManager.rotate(-90, 0, 1,0);
				break;
		}
		Minecraft.getMinecraft().renderEngine.bindTexture(ROLL_TEXTURE);
		for(Map.Entry<String, OBJModelLoader.Part> part : GRINDER_ROLL_MODEL.entrySet()) {
			float angle = te.isGrinding() ? ((System.currentTimeMillis() % (18000)) / 50f) :0f;
			GlStateManager.pushMatrix();
			GlStateManager.translate(part.getValue().origin.x,part.getValue().origin.y,part.getValue().origin.z);
			GlStateManager.rotate(angle, 1,0,0);
			GlStateManager.translate(-part.getValue().origin.x,-part.getValue().origin.y,-part.getValue().origin.z);
			part.getValue().draw();
			GlStateManager.popMatrix();
		}
		if(!te.getStones().isEmpty() ) {
			Minecraft.getMinecraft().renderEngine.bindTexture(STONE_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.23587, 0.88819, 0.069589);

			int colorH = 0x10EFFF;
			if(te.getStones().getItem() instanceof ItemSpiritStone) {
				colorH = ((ItemSpiritStone) te.getStones().getItem()).color;
			}
			Color color = new Color(colorH);

			GlStateManager.color(color.getRed()/256f,color.getGreen()/256f,color.getBlue()/256f,0.9f);
			for(Map.Entry<String, OBJModelLoader.Part> part : SPIRIT_STONE_MODEL.entrySet()) {
				part.getValue().draw();
			}
			GlStateManager.color(1f,1f,1f,1f);
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}
}
