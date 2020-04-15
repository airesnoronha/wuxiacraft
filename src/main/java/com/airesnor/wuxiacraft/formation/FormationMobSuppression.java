package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FormationMobSuppression extends Formation {

	private ResourceLocation displayFormation = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/formations/weak_mob_suppression_formation.png");

	private float strength;

	public FormationMobSuppression(String name, double cost, double activationCost, double range, float strength) {
		super(name, cost, activationCost, range);
		this.strength = strength;
	}

	public FormationMobSuppression setDisplayFormation(ResourceLocation newTexture) {
		this.displayFormation = newTexture;
		return this;
	}

	@Override
	public int doUpdate(@Nonnull World worldIn, @Nonnull BlockPos source, @Nonnull FormationTileEntity parent) {
		int activated = 0;
		double preferredRange = this.getRange();
		if (parent.getTimeActivated() % 20 == 0) {
			List<TileEntity> tileEntities = worldIn.loadedTileEntityList;
			for (TileEntity te : tileEntities) {
				if (te instanceof TileEntitySign && te.getPos().getDistance(source.getX(), source.getY(), source.getZ()) < 10) {
					ITextComponent[] lines = ((TileEntitySign) te).signText;
					for (ITextComponent line : lines) {
						String l = line.getUnformattedText();
						if(l.startsWith("range=")) {
							try {
								preferredRange = Math.min(this.getRange(), Double.parseDouble(l.substring("range=".length())));
								parent.getFormationInfo().setDouble("preferredRange", preferredRange);
							} catch (NumberFormatException e) {
								WuxiaCraft.logger.info("Range wasn't readable at " + te.getPos().toString());
							}
						}
					}
				}
			}
		}
		if(parent.getFormationInfo().hasKey("preferredRange")) {
			preferredRange = parent.getFormationInfo().getDouble("preferredRange");
		}
		List<EntityLivingBase> mobs = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(source).grow(preferredRange), input -> input instanceof IMob);
		for(EntityLivingBase mob : mobs) {
			if(mob.getDistance(source.getX(), source.getY(), source.getZ()) < preferredRange) {
				mob.attackEntityFrom(DamageSource.GENERIC, this.strength);
				activated++;
			}
		}
		return activated;
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
