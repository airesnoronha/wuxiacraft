package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FormationCultivationHelper extends Formation {

	private ResourceLocation displayFormation = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/formations/soul_gathering_formation.png");
	private final ResourceLocation blood_a = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_a.png");
	private final ResourceLocation blood_b = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_b.png");
	private final ResourceLocation blood_c = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_c.png");
	private final ResourceLocation blood_d = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/runes/blood_d.png");

	private final double amount; //amount gained per tick;

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
		List<EntityPlayer> targets = new ArrayList<>();
		NBTTagCompound info = parent.getFormationInfo();
		if (parent.getTimeActivated() % 10 == 0) { //find players every half second
			List<EntityPlayer> players = worldIn.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(source).grow(this.getRange()));
			for (EntityPlayer player : players) {
				if (player.getDistance(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5) < this.getRange()) {
					targets.add(player);
				}
			}
			info.setInteger("targets", targets.size());
			for (EntityPlayer target : targets) {
				int index = targets.indexOf(target);
				info.setUniqueId("p-" + index, target.getUniqueID());
			}
		} else { //get loaded players
			if (info.hasKey("targets")) {
				int length = info.getInteger("targets");
				for (int i = 0; i < length; i++) {
					UUID uuid = info.getUniqueId("p-" + i);
					if (uuid != null) {
						EntityPlayer player = worldIn.getPlayerEntityByUUID(uuid);
						if (player != null)
							targets.add(player);
					}
				}
			}
		}
		List<EntityPlayer> selected = new ArrayList<>();
		for (EntityPlayer player : targets) {
			ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
			Skill skill = skillCap.getSelectedSkill(CultivationUtils.getCultTechFromEntity(player));
			if (skill == Skills.CULTIVATE_ESSENCE) {
				if (parent.hasEnergy(this.getOperationCost() * (selected.size() + 1))) {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
					if (!(this.amount <= cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel()) * 0.06)) {
						worldIn.createExplosion(player, player.posX, player.posY + 0.9, player.posZ, 2f, true);
						player.attackEntityFrom(DamageSource.causeExplosionDamage(player), (float) this.amount * 2);
					}
					selected.add(player);
				}
			}
		}
		info.setInteger("selected", selected.size()); //so client can know who to cultivate
		for (EntityPlayer player : selected) {
			int index = selected.indexOf(player);
			info.setUniqueId("s-" + index, player.getUniqueID());
		}
		parent.setFormationInfo(info);
		return selected.size();
	}

	@SideOnly(Side.CLIENT)
	@Override
	@ParametersAreNonnullByDefault
	public void doClientUpdate(World worldIn, BlockPos source, FormationTileEntity parent) {
		if (parent != null) {
			if (parent.getFormationInfo().hasKey("selected")) {
				NBTTagCompound info = parent.getFormationInfo();
				int length = info.getInteger("selected");
				EntityPlayer player = Minecraft.getMinecraft().player;
				boolean selected = false;
				for (int i = 0; i < length; i++) {
					UUID uuid = info.getUniqueId("s-" + i);
					if (uuid != null) {
						if (uuid.equals(player.getUniqueID())) {
							selected = true;
							break;
						}
					}
				}
				if (selected) {
					ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
					if (skillCap.isCasting()) {
						Skill skill = skillCap.getSelectedSkill(CultivationUtils.getCultTechFromEntity(player));
						if (!skillCap.hasFormationActivated()) {
							if (skill == Skills.CULTIVATE_ESSENCE) {
								skillCap.setFormationActivated(true);
								ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
								if (this.amount <= cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel()) * 0.06) {
									CultivationUtils.cultivatorAddProgress(player, Cultivation.System.ESSENCE, this.amount, true, false);
									NetworkWrapper.INSTANCE.sendToServer(new ProgressMessage(0, Cultivation.System.ESSENCE, this.amount, true, false, player.getUniqueID()));
								}
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
