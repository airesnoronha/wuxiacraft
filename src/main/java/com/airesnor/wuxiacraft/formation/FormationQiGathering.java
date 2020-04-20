package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FormationQiGathering extends Formation {

	private ResourceLocation displayFormation = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/formations/weak_qi_gathering_formation.png");

	private final double generation;

	public FormationQiGathering(String name, double generation, double range) {
		super(name, 0, 0, range);
		this.generation = generation;
	}

	public FormationQiGathering setDisplayFormation(ResourceLocation newTexture) {
		this.displayFormation = newTexture;
		return this;
	}

	@Override
	@ParametersAreNonnullByDefault
	public int doUpdate(World worldIn, BlockPos source, FormationTileEntity parent) {
		NBTTagCompound info = parent.getFormationInfo();
		List<FormationTileEntity> formations = new ArrayList<>();
		if (parent.getTimeActivated() % 40 == 0) { //search for other formations
			for (TileEntity te : worldIn.loadedTileEntityList) {
				if (te instanceof FormationTileEntity && te.getPos().getDistance(source.getX(), source.getY(), source.getZ()) < this.getRange()) {
					formations.add((FormationTileEntity) te);
				}
			}
			info.setInteger("formations-length", formations.size());
			for (FormationTileEntity fte : formations) { //store the formations blockpos
				int index = formations.indexOf(fte);
				info.setTag("f-" + index, writeBlockPosToNBT(fte.getPos()));
			}
			parent.setFormationInfo(info);
		} else { //read from info
			if (info.hasKey("formations-length")) {
				int length = info.getInteger("formations-length");
				for (int i = 0; i < length; i++) {
					TileEntity te = worldIn.getTileEntity(readBlockPosFromNBT((NBTTagCompound) info.getTag("f-" + i)));
					if (te instanceof FormationTileEntity) {
						formations.add((FormationTileEntity) te);
					}
				}
			}
		}//do stuff intended with those targets
		for (FormationTileEntity formation : formations) {
			if (!formation.hasEnergy(formation.getMaxEnergy())) {
				formation.addEnergy(this.generation);
				break;
			}
		}
		List<EntityPlayer> targets = new ArrayList<>();
		if (parent.getTimeActivated() % 10 == 0) { //search world for players
			List<EntityPlayer> playersNearby = worldIn.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(source).grow(this.getRange() / 8));
			for (EntityPlayer player : playersNearby) {
				if (player.getDistanceSq(source) < (this.getRange() / 8) * (this.getRange() / 8)) {
					targets.add(player);
				}
			}
			info.setInteger("targets", targets.size());
			for (EntityPlayer target : targets) {
				int index = targets.indexOf(target);
				info.setUniqueId("p-" + index, target.getPersistentID());
			}
		} else { //load from found players
			if (info.hasKey("targets")) {
				int length = info.getInteger("targets");
				for (int i = 0; i < length; i++) {
					UUID uuid = info.getUniqueId("p-" + i);
					if(uuid != null) {
						EntityPlayer player = worldIn.getPlayerEntityByUUID(uuid);
						if (player != null)
							targets.add(player);
					}
				}
			}
		}
		for (EntityPlayer target : targets) {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(target);
			cultivation.addEnergy((float) this.generation * 2);
		}
		return 0;
	}

	private static BlockPos readBlockPosFromNBT(NBTTagCompound tag) {
		int x = tag.getInteger("x");
		int y = tag.getInteger("y");
		int z = tag.getInteger("z");
		return new BlockPos(x, y, z);
	}

	private static NBTTagCompound writeBlockPosToNBT(BlockPos pos) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("x", pos.getX());
		tag.setInteger("y", pos.getY());
		tag.setInteger("z", pos.getZ());
		return tag;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void doClientUpdate(@Nonnull World worldIn, @Nonnull BlockPos source, @Nonnull FormationTileEntity parent) {
		if (Minecraft.getMinecraft().player.getDistanceSq(source) < (this.getRange() / 8) * (this.getRange() / 8)) {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(Minecraft.getMinecraft().player);
			cultivation.addEnergy((float) this.generation * 2);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(double x, double y, double z) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(this.displayFormation);
		GlStateManager.translate(x, y, z);
		float angle = -180f + ((System.currentTimeMillis() % 36000) / 100f);
		GlStateManager.rotate(angle, 0, 1, 0);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		builder.pos(-2, 0, -2).tex(0, 0).endVertex();
		builder.pos(2, 0, -2).tex(1, 0).endVertex();
		builder.pos(2, 0, 2).tex(1, 1).endVertex();
		builder.pos(-2, 0, 2).tex(0, 1).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
}
