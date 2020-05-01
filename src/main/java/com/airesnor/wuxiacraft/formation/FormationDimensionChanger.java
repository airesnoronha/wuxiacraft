package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.utils.TeleportationUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FormationDimensionChanger extends Formation {

	private final int targetDimension;

	public FormationDimensionChanger(String name, double cost, double activationCost, int targetDimension) {
		super(name, cost, activationCost, 2);
		this.targetDimension = targetDimension;
	}

	@Override
	public int doUpdate(@Nonnull World worldIn, @Nonnull BlockPos source, @Nonnull FormationTileEntity parent) {
		int timeAlive = parent.getTimeActivated();
		if (timeAlive % 20 == 0) { //avoid suffocation
			WorldServer world = DimensionManager.getWorld(this.targetDimension);
			if(world != null) {
				world.setBlockToAir(source);
				world.setBlockToAir(source.up());
			}
		}
		List<EntityPlayer> players = worldIn.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(source).grow(2));
		for (EntityPlayer player : players) {
			if (parent.hasEnergy(this.getOperationCost() * 99)) { //takes all energy with it
				parent.remEnergy(this.getOperationCost()*99);
				parent.stopFormation(); //can only take one person at a time
				int targetDimension = player.world.provider.getDimension() == this.targetDimension ? 0 : this.targetDimension; //this way they go back by the same way
				TeleportationUtil.teleportPlayerToDimension((EntityPlayerMP) player,  targetDimension, source.getX() + 0.5, source.getY()+0.01, source.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
				break; //only one person
			}
		}
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(double x, double y, double z) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		GlStateManager.translate(x, y, z);
		float angle = (System.currentTimeMillis() % 36000) / 100f;
		GlStateManager.rotate(angle, 0, 1, 0);
		GlStateManager.color(1, 1, 1, 0.7f);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		builder.pos(-2, -256, -2).endVertex();//front
		builder.pos(2, -256, -2).endVertex();
		builder.pos(2, 256, -2).endVertex();
		builder.pos(-2, 256, -2).endVertex();
		builder.pos(-2, -256, 2).endVertex();//back
		builder.pos(2, -256, 2).endVertex();
		builder.pos(2, 256, 2).endVertex();
		builder.pos(-2, 256, 2).endVertex();
		builder.pos(-2, -256, -2).endVertex();//left
		builder.pos(-2, -256, 2).endVertex();
		builder.pos(-2, 256, 2).endVertex();
		builder.pos(-2, 256, -2).endVertex();
		builder.pos(2, -256, -2).endVertex();//right
		builder.pos(2, -256, 2).endVertex();
		builder.pos(2, 256, 2).endVertex();
		builder.pos(2, 256, -2).endVertex();
		tessellator.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
}
