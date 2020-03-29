package com.airesnor.wuxiacraft.utils;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class OreUtils {

	public static final List<BlockPos> foundOres = new ArrayList<>();

	public static final List<Block> oresToFind = new ArrayList<>();

	public static final List<Block> earthTypes = new ArrayList<>();

	public static void loadOresToFind() {
		oresToFind.add(Blocks.IRON_ORE);
		oresToFind.add(Blocks.COAL_ORE);
		oresToFind.add(Blocks.GOLD_ORE);
		oresToFind.add(Blocks.DIAMOND_ORE);
		oresToFind.add(Blocks.EMERALD_ORE);
		oresToFind.add(Blocks.QUARTZ_ORE);
		earthTypes.add(Blocks.DIRT);
		earthTypes.add(Blocks.SAND);
		earthTypes.add(Blocks.STONE);
		earthTypes.add(Blocks.COBBLESTONE);
		earthTypes.add(Blocks.MOSSY_COBBLESTONE);
		earthTypes.add(Blocks.GRAVEL);
		earthTypes.add(Blocks.GRASS);
		earthTypes.add(Blocks.GRASS_PATH);
		earthTypes.add(Blocks.NETHERRACK);
		earthTypes.add(Blocks.SOUL_SAND);
	}

	public static List<BlockPos> findOres(World world, BlockPos source, int radius) {
		List<BlockPos> found = new ArrayList<>();
		int minX = source.getX() - radius;
		int minY = Math.max(0, source.getY() - radius);
		int minZ = source.getZ() - radius;
		foundOres.clear();
		for (int x = minX; x < minX + 2 * radius; x++) {
			for (int z = minZ; z < minZ + 2 * radius; z++) {
				for (int y = minY; y < minY + 2 * radius; y++) {
					BlockPos aux = new BlockPos(x, y, z);
					if (oresToFind.contains(world.getBlockState(aux).getBlock())) {
						foundOres.add(aux);
						found.add(aux);
					}
				}
			}
		}
		return found;
	}

	public static void gatherOres(EntityPlayer player, List<BlockPos> ores) {

	}

	public static void drawFoundOres(EntityPlayer player) {
		apply();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		for (BlockPos pos : foundOres) {
			final float size = 1.0f;
			float x = pos.getX() - (float) player.posX;
			float y = pos.getY() - (float) player.posY;
			float z = pos.getZ() - (float) player.posZ;
			float red = 1f;
			float green = 0f;
			float blue = 0f;
			float opacity = 1f;
			//x faze z:0
			buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(x, y, z).color(red, green, blue, opacity).endVertex();
			buffer.pos(x + size, y, z).color(red, green, blue, opacity).endVertex();
			buffer.pos(x + size, y + size, z).color(red, green, blue, opacity).endVertex();
			buffer.pos(x, y + size, z).color(red, green, blue, opacity).endVertex();
			tessellator.draw();
			//z face x:1
			buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(x + size, y, z).color(red, green, blue, opacity).endVertex();
			buffer.pos(x + size, y, z + size).color(red, green, blue, opacity).endVertex();
			buffer.pos(x + size, y + size, z + size).color(red, green, blue, opacity).endVertex();
			buffer.pos(x + size, y + size, z).color(red, green, blue, opacity).endVertex();
			tessellator.draw();
			//x face z:1
			buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(x + size, y, z + size).color(red, green, blue, opacity).endVertex();
			buffer.pos(x, y, z + size).color(red, green, blue, opacity).endVertex();
			buffer.pos(x, y + size, z + size).color(red, green, blue, opacity).endVertex();
			buffer.pos(x + size, y + size, z + size).color(red, green, blue, opacity).endVertex();
			tessellator.draw();
			//z face x:0
			buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(x, y, z + size).color(red, green, blue, opacity).endVertex();
			buffer.pos(x, y, z).color(red, green, blue, opacity).endVertex();
			buffer.pos(x, y + size, z).color(red, green, blue, opacity).endVertex();
			buffer.pos(x, y + size, z + size).color(red, green, blue, opacity).endVertex();
			tessellator.draw();
		}
		clean();
	}

	public static void apply() {
		GlStateManager.disableTexture2D();
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.glLineWidth(2f);
	}

	public static void clean() {
		GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
	}


}
