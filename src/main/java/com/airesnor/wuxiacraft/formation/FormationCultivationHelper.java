package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.ProgressMessage;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class FormationCultivationHelper extends Formation {

	private ResourceLocation displayFormation = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/formations/soul_gathering_formation.png");
	private ResourceLocation blood_a = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_a.png");
	private ResourceLocation blood_b = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_b.png");
	private ResourceLocation blood_c = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_c.png");
	private ResourceLocation blood_d = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_d.png");

	private double amount; //amount gained per tick;

	public FormationCultivationHelper(String name, double cost, double activationCost, double range, double amount) {
		super(name, cost, activationCost, range);
		this.amount = amount;
	}

	@SuppressWarnings("unused")
	public FormationCultivationHelper setDisplayFormation(ResourceLocation newTexture) {
		this.displayFormation = newTexture;
		return this;
	}

	@Override
	@ParametersAreNonnullByDefault
	public int doUpdate(World worldIn, BlockPos source, FormationTileEntity parent) {
		List<EntityPlayer> players = worldIn.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(source).grow(this.getRange()));
		int activated = 0;
		if (parent != null) {
			for (EntityPlayer player : players) {
				if (player.getDistance(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5) < this.getRange()) {
					ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
					if (skillCap.isCasting()) {
						Skill skill = skillCap.getSelectedSkill(CultivationUtils.getCultTechFromEntity(player));
						if (skill == Skills.CULTIVATE) {
							if (parent.hasEnergy(this.getOperationCost() * (activated + 1))) {
								ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
								if (!(this.amount <= cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()) * 0.06)) {
									worldIn.createExplosion(player, player.posX, player.posY + 0.9, player.posZ, 2f, true);
									player.attackEntityFrom(DamageSource.causeExplosionDamage(player), (float) this.amount * 2);
								}
								activated++;
							}
						}
					}
				}
			}
		}
		return activated;
	}

	@SideOnly(Side.CLIENT)
	@Override
	@ParametersAreNonnullByDefault
	public void doClientUpdate(World worldIn, BlockPos source, FormationTileEntity parent) {
		if (parent != null) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
			if (player.getDistance(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5) < this.getRange()) {
				if (skillCap.isCasting()) {
					Skill skill = skillCap.getSelectedSkill(CultivationUtils.getCultTechFromEntity(player));
					if (skill == Skills.CULTIVATE) {
						if (parent.hasEnergy(this.getOperationCost())) {
							ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
							if (this.amount <= cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()) * 0.06) {
								CultivationUtils.cultivatorAddProgress(player, cultivation, this.amount, false, false);
								NetworkWrapper.INSTANCE.sendToServer(new ProgressMessage(0, this.amount, false, false));
							}
						}
					}
				}
			}
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
		double place = 1.0625;
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
