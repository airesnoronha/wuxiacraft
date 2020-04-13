package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class FormationPlayerBarrier extends Formation {

	private ResourceLocation displayFormation = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/formations/soul_land_barrier.png");
	private ResourceLocation blood_a = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_e.png");
	private ResourceLocation blood_b = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_f.png");
	private ResourceLocation blood_c = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_g.png");
	private ResourceLocation blood_d = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_h.png");

	private double strength;

	public FormationPlayerBarrier(String name, double cost, double activationCost, double range, double strength) {
		super(name, cost, activationCost, range);
		this.strength = strength;
	}

	public FormationPlayerBarrier setDisplayFormation(ResourceLocation newTexture) {
		this.displayFormation = newTexture;
		return this;
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
	@ParametersAreNonnullByDefault
	public int doUpdate(World worldIn, BlockPos source, FormationTileEntity parent) {
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
		List<EntityPlayer> players = worldIn.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(source).grow(preferredRange));
		for (EntityPlayer player : players) {
			if (!playersWhiteListed.contains(player)) {
				double dx = player.posX - (source.getX() + 0.5);
				double dy = player.posY - (source.getY() + 0.5);
				double dz = player.posZ - (source.getZ() + 0.5);
				double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
				if (distance < preferredRange) {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
					if (cultivation.getStrengthIncrease() < this.strength * 2) {
						if (!player.isCreative()) {
							double inverseDistance = preferredRange - distance;
							double mx = (dx / distance) * inverseDistance * this.strength / cultivation.getStrengthIncrease();
							double my = (dy / distance) * inverseDistance * this.strength / cultivation.getStrengthIncrease();
							double mz = (dz / distance) * inverseDistance * this.strength / cultivation.getStrengthIncrease();
							player.motionX += mx;
							player.motionY += my;
							player.motionZ += mz;
							player.velocityChanged = true;
						}
					} else {
						if (!player.isCreative()) {
							player.motionX *= (cultivation.getStrengthIncrease() - this.strength) / cultivation.getStrengthIncrease(); //slows players
							player.motionY *= player.motionY > 0 ? (cultivation.getStrengthIncrease() - this.strength) / cultivation.getStrengthIncrease() //slows jumping
									: (cultivation.getStrengthIncrease() + this.strength) / cultivation.getStrengthIncrease(); //increase fall speed
							player.motionZ *= (cultivation.getStrengthIncrease() - this.strength) / cultivation.getStrengthIncrease(); //slows players
							player.velocityChanged = true;
						}
					}
				}
			}
		}
		return 1;
	}

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
		GlStateManager.scale(1.5, 1, 1.5);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		builder.pos(-2, 0, -2).tex(0, 0).endVertex();
		builder.pos(2, 0, -2).tex(1, 0).endVertex();
		builder.pos(2, 0, 2).tex(1, 1).endVertex();
		builder.pos(-2, 0, 2).tex(0, 1).endVertex();
		tessellator.draw();
		float fasterAngle = -180f + ((System.currentTimeMillis() % (360 * 3)) / 3f);
		double size = 38.0 / 256.0;
		double place = 1.3828125;
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(this.blood_a);
		GlStateManager.translate(-place, 0, -place);
		GlStateManager.rotate(fasterAngle, 0, 1, 0);
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		builder.pos(-size, 0, -size).tex(0, 0).endVertex();
		builder.pos(size, 0, -size).tex(1, 0).endVertex();
		builder.pos(size, 0, size).tex(1, 1).endVertex();
		builder.pos(-size, 0, size).tex(0, 1).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(this.blood_b);
		GlStateManager.translate(place, 0, -place);
		GlStateManager.rotate(fasterAngle, 0, 1, 0);
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		builder.pos(-size, 0, -size).tex(0, 0).endVertex();
		builder.pos(size, 0, -size).tex(1, 0).endVertex();
		builder.pos(size, 0, size).tex(1, 1).endVertex();
		builder.pos(-size, 0, size).tex(0, 1).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(this.blood_c);
		GlStateManager.translate(place, 0, place);
		GlStateManager.rotate(fasterAngle, 0, 1, 0);
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		builder.pos(-size, 0, -size).tex(0, 0).endVertex();
		builder.pos(size, 0, -size).tex(1, 0).endVertex();
		builder.pos(size, 0, size).tex(1, 1).endVertex();
		builder.pos(-size, 0, size).tex(0, 1).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(this.blood_d);
		GlStateManager.translate(-place, 0, place);
		GlStateManager.rotate(fasterAngle, 0, 1, 0);
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		builder.pos(-size, 0, -size).tex(0, 0).endVertex();
		builder.pos(size, 0, -size).tex(1, 0).endVertex();
		builder.pos(size, 0, size).tex(1, 1).endVertex();
		builder.pos(-size, 0, size).tex(0, 1).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
}
