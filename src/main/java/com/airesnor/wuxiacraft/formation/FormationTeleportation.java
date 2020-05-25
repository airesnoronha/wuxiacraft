package com.airesnor.wuxiacraft.formation;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FormationTeleportation extends Formation {

	public FormationTeleportation(String name, double cost, double activationCost, double range) {
		super(name, cost, activationCost, range);
	}

	@Override
	public int doUpdate(@Nonnull World worldIn, @Nonnull BlockPos source, @Nonnull FormationTileEntity parent) {
		int timeAlive = parent.getTimeActivated();
		NBTTagCompound info = parent.getFormationInfo();
		if (timeAlive % 20 == 0) {
			List<String> names = new ArrayList<>();
			List<TileEntity> tileEntities = worldIn.loadedTileEntityList;
			for (TileEntity te : tileEntities) {
				if (te instanceof TileEntitySign && te.getPos().getDistance(source.getX(), source.getY(), source.getZ()) < 10) {
					ITextComponent[] lines = ((TileEntitySign) te).signText;
					for (ITextComponent line : lines) {
						if (!line.getUnformattedText().equals("")) {
							names.add(line.getUnformattedText());
						}
					}
				}
			}
			for (String name : names) {
				if(name.startsWith("x=")) {
					info.setInteger("target-x", Integer.parseInt(name.substring(2)));
				}
				if(name.startsWith("y=")) {
					info.setInteger("target-y", Integer.parseInt(name.substring(2)));
				}
				if(name.startsWith("z=")) {
					info.setInteger("target-z", Integer.parseInt(name.substring(2)));
				}
			}
			parent.setFormationInfo(info);
		}
		if(info.hasKey("target-x")&&info.hasKey("target-y")&&info.hasKey("target-z")) {
			double targetX = info.getInteger("target-x");
			double targetY = info.getInteger("target-y");
			double targetZ = info.getInteger("target-z");
			double distance = new Vec3d(targetX, targetY, targetZ).distanceTo(new Vec3d(source.getX(), source.getY(), source.getZ()));
			List<Entity> entities = worldIn.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(source).grow(2));
			for(Entity entity : entities) {
				if(parent.hasEnergy(this.getOperationCost()*99)) { //all energy
					parent.remEnergy(this.getOperationCost()*99);
					parent.stopFormation(); //only one person
					if (distance < this.getRange()) { //safe landing
						entity.setPositionAndUpdate(targetX, targetY, targetZ);
					} else { //thrown away
						double radius = distance / 2; //a radius to make an arc that will be the y position the player will be thrown in
						Vec3d direction = new Vec3d(targetX - source.getX(), 0, targetZ - source.getZ()).normalize(); //y not used because i just wanted in horizontal plane direction
						double newX = source.getX() + direction.x * this.getRange();
						double newZ = source.getZ() + direction.z * this.getRange();
						double angle = Math.acos((this.getRange() > radius ? this.getRange() - radius : this.getRange()) / radius);
						double newY = source.getY() + Math.sin(angle) * radius;
						double speed = distance / this.getRange() * 10; // i guess it'll be always something around 10 to 50, not too much
						double mx = direction.x * speed;
						double mz = direction.z * speed;
						double my = Math.cos(angle) * speed; //it's the differential of the position (the speed is the differential of position)
						my *= this.getRange() > radius ? -1 : 1; //falling or rising
						entity.motionX = mx;
						entity.motionY = my;
						entity.motionZ = mz;
						entity.velocityChanged = true;
						entity.setPositionAndUpdate(newX, newY, newZ);
					}
					break; //only one person
				}
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

		GlStateManager.translate(x,y,z);
		float angle = (System.currentTimeMillis()%36000)/100f;
		GlStateManager.rotate(angle, 0,1,0);
		GlStateManager.color(1,1,1,0.7f);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		builder.pos(-2, -256, -2).endVertex();//front
		builder.pos( 2, -256, -2).endVertex();
		builder.pos( 2,  256, -2).endVertex();
		builder.pos(-2,  256, -2).endVertex();
		builder.pos(-2, -256,  2).endVertex();//back
		builder.pos( 2, -256,  2).endVertex();
		builder.pos( 2,  256,  2).endVertex();
		builder.pos(-2,  256,  2).endVertex();
		builder.pos(-2, -256, -2).endVertex();//left
		builder.pos(-2, -256,  2).endVertex();
		builder.pos(-2,  256,  2).endVertex();
		builder.pos(-2,  256, -2).endVertex();
		builder.pos( 2, -256, -2).endVertex();//right
		builder.pos( 2, -256,  2).endVertex();
		builder.pos( 2,  256,  2).endVertex();
		builder.pos( 2,  256, -2).endVertex();
		tessellator.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
}
