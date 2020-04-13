package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
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

public class FormationSlaughter extends Formation {

	private ResourceLocation displayFormation = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/formations/weak_slaughter_formation.png");
	private ResourceLocation[] blood_runes = new ResourceLocation[]{
			new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_h.png"),
			new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_i.png"),
			new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_j.png"),
			new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_k.png"),
			new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_l.png")
	};

	float strength;

	public FormationSlaughter(String name, double cost, double activationCost, double range, float strength) {
		super(name, cost, activationCost, range);
		this.strength = strength;
	}

	@Nonnull
	public static List<String> getNamesFromInfo(NBTTagCompound info) {
		List<String> names = new ArrayList<>();
		if (info.hasKey("names-length")) {
			int length = info.getInteger("names-length");
			for (int i = 0; i < length; i++) {
				String name = info.getString("names-" + i);
				names.add(name);
			}
		}
		return names;
	}

	@Override
	public int doUpdate(@Nonnull World worldIn, @Nonnull BlockPos source, @Nonnull FormationTileEntity parent) {
		double preferredRange = this.getRange();
		int timeAlive = parent.getTimeActivated();
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
			NBTTagCompound info = parent.getFormationInfo();
			info.setInteger("names-length", names.size());
			for (String name : names) {
				info.setString("names-" + names.indexOf(name), name);
			}
			parent.setFormationInfo(info);
		}
		List<EntityPlayer> playersWhiteListed = new ArrayList<>();
		List<String> names = getNamesFromInfo(parent.getFormationInfo());
		for (String name : names) {
			if (name.startsWith("range=")) {
				double range = Double.parseDouble(name.substring("range=".length()));
				preferredRange = Math.min(this.getRange(), range);
				continue;
			}
			EntityPlayer player = worldIn.getPlayerEntityByName(name);
			if (player != null) {
				playersWhiteListed.add(player);
			}
		}
		List<EntityLivingBase> targets = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(source).grow(preferredRange), input -> {
			if (input instanceof EntityPlayer)
				return !playersWhiteListed.contains(input);
			else return true;
		});
		int activated = 0;
		for (EntityLivingBase target : targets) {
			if(parent.hasEnergy(this.getOperationCost() * (activated+1))) {
				target.attackEntityFrom(DamageSource.GENERIC.setDamageBypassesArmor(), this.strength);
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
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		GlStateManager.translate(x, y - 0.4, z);
		float rotationAngle = (System.currentTimeMillis() % 36000) / 100f;
		GlStateManager.rotate(rotationAngle, 0, 1, 0);
		Minecraft.getMinecraft().renderEngine.bindTexture(displayFormation);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		builder.pos(-3, 0, -3).tex(0, 0).endVertex();
		builder.pos(3, 0, -3).tex(1, 0).endVertex();
		builder.pos(3, 0, 3).tex(1, 1).endVertex();
		builder.pos(-3, 0, 3).tex(0, 1).endVertex();
		tessellator.draw();
		double size = 0.234375;
		float fasterAngle = (System.currentTimeMillis() % 3600) / 10f;
		for (int i = 0; i < 5; i++) {
			GlStateManager.pushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(blood_runes[i]);
			GlStateManager.rotate(i * 360f / 5f, 0, 1, 0);
			GlStateManager.translate(0, 0, -2.765625);
			GlStateManager.rotate(fasterAngle, 0, 1, 0);
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			builder.pos(-size, 0, -size).tex(0, 0).endVertex();
			builder.pos(size, 0, -size).tex(1, 0).endVertex();
			builder.pos(size, 0, size).tex(1, 1).endVertex();
			builder.pos(-size, 0, size).tex(0, 1).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}

		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
}
