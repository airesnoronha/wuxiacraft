package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.world.dimensions.WuxiaDimensions;
import com.airesnor.wuxiacraft.utils.TeleportationUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
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
		int targetDimension = worldIn.provider.getDimension() == this.targetDimension ? 0 : this.targetDimension; //this way they go back by the same way
		if (timeAlive % 20 == 0) { //avoid suffocation
			WorldServer world = DimensionManager.getWorld(targetDimension);
			if(world != null) {
				world.setBlockToAir(source);
				world.setBlockToAir(source.up());
			}
		}
		List<EntityPlayer> players = worldIn.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(source).grow(2));
		int sourceX = source.getX();
		int sourceY = source.getY();
		int sourceZ = source.getZ();
		for (EntityPlayer player : players) {
			if (parent.hasEnergy(this.getOperationCost() * 99)) { //takes all energy with it
				parent.remEnergy(this.getOperationCost() * 99);
				parent.stopFormation(); //can only take one person at a time
				if(targetDimension != -1 || targetDimension != 0 || targetDimension != 1 || targetDimension != WuxiaDimensions.MINING.getId()) {
					if(sourceX >= 2000000) {
						sourceX = 1999990;
					}
					if(sourceZ >= 2000000) {
						sourceZ = 1999990;
					}
					if(sourceX <= -2000000) {
						sourceX = -1999990;
					}
					if(sourceZ <= -2000000) {
						sourceZ = -1999990;
					}
					TeleportationUtil.teleportPlayerToDimension((EntityPlayerMP) player,  targetDimension, sourceX + 0.5, sourceY + 0.01, sourceZ + 0.5, player.rotationYaw, player.rotationPitch);
				} else if (targetDimension == WuxiaDimensions.MINING.getId()){
					if(sourceX >= 3000000) {
						sourceX = 2999990;
					}
					if(sourceZ >= 3000000) {
						sourceZ = 2999990;
					}
					if(sourceX <= -3000000) {
						sourceX = -2999990;
					}
					if(sourceZ <= -3000000) {
						sourceZ = -2999990;
					}
					TeleportationUtil.teleportPlayerToDimension((EntityPlayerMP) player,  targetDimension, sourceX + 0.5, sourceY + 0.01, sourceZ + 0.5, player.rotationYaw, player.rotationPitch);
				} else {
					TeleportationUtil.teleportPlayerToDimension((EntityPlayerMP) player,  targetDimension, sourceX + 0.5, sourceY + 0.01, sourceZ + 0.5, player.rotationYaw, player.rotationPitch);
				}
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
