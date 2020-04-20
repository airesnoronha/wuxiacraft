package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.block.BlockFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class FormationFurnacePower extends Formation {

	private ResourceLocation displayFormation = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/formations/furnace_powering_formation.png");

	public FormationFurnacePower(String name, double cost, double activationCost, double range) {
		super(name, cost, activationCost, range);
	}

	public FormationFurnacePower setDisplayFormation(ResourceLocation newTexture) {
		this.displayFormation = newTexture;
		return this;
	}

	@Override
	@ParametersAreNonnullByDefault
	public int doUpdate(World worldIn, BlockPos source, FormationTileEntity parent) {
		NBTTagCompound info = parent.getFormationInfo();
		List<TileEntity> tes = worldIn.loadedTileEntityList;
		List<TileEntityFurnace> targets = new ArrayList<>();
		if (parent.getTimeActivated() % 30 == 0) { //load the info
			for (TileEntity te : tes) {
				if (te instanceof TileEntityFurnace && te.getPos().getDistance(source.getX(), source.getY(), source.getZ()) < this.getRange()) {
					targets.add((TileEntityFurnace) te);
				}
			}
			info.setInteger("targets", targets.size());
			for (TileEntityFurnace target : targets) {
				int index = targets.indexOf(target);
				info.setTag("t-" + index, writeBlockPosToNBT(target.getPos()));
			}
			parent.setFormationInfo(info);
		} else { //load from the info
			if (info.hasKey("targets")) {
				int length = info.getInteger("targets");
				for (int i = 0; i < length; i++) {
					TileEntity te = worldIn.getTileEntity(readBlockPosFromNBT(info.getCompoundTag("t-" + i)));
					if(te instanceof TileEntityFurnace)
						targets.add((TileEntityFurnace) te);
				}
			}
		}

		int activated = 0;
		for (TileEntityFurnace furnace : targets) {
			if (parent.hasEnergy(this.getOperationCost() * (activated + 1))) {
				if (!furnace.isBurning() && !furnace.getStackInSlot(0).isEmpty()) {
					furnace.setField(0, 201);
					activated++;
					BlockFurnace.setState(true, furnace.getWorld(), furnace.getPos());
				}
			}
		}
		return activated;
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
	public void render(double x, double y, double z) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(this.displayFormation);
		GlStateManager.translate(x, y - 0.4, z);
		float angle = -180f + ((System.currentTimeMillis() % 36000) / 100f);
		GlStateManager.rotate(angle, 0, 1, 0);
		GlStateManager.scale(1.6, 1, 1.6);
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
