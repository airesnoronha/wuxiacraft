package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.*;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.Foundation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.SkillCap;
import com.airesnor.wuxiacraft.cultivation.techniques.CultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UnifiedCapabilitySyncMessage implements IMessage {

	public ICultivation cultivation;
	public ICultTech cultTech;
	public ISkillCap skillCap;
	public IFoundation foundation;
	public boolean shouldSetCdaCP;

	@SuppressWarnings("unused")
	public UnifiedCapabilitySyncMessage() {
		cultivation = new Cultivation();
		cultTech = new CultTech();
		skillCap = new SkillCap();
		foundation = new Foundation();
		this.shouldSetCdaCP = false;
	}

	public UnifiedCapabilitySyncMessage(ICultivation cultivation, ICultTech cultTech, ISkillCap skillCap, IFoundation foundation, boolean shouldSetCdaCP) {
		this.cultivation = cultivation;
		this.cultTech = cultTech;
		this.skillCap = skillCap;
		this.foundation = foundation;
		this.shouldSetCdaCP = shouldSetCdaCP;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		//noinspection ConstantConditions
		CultivationProvider.CULTIVATION_CAP.getStorage().readNBT(CultivationProvider.CULTIVATION_CAP, this.cultivation, null, tag);
		tag = ByteBufUtils.readTag(buf);
		//noinspection ConstantConditions
		CultTechProvider.CULT_TECH_CAPABILITY.getStorage().readNBT(CultTechProvider.CULT_TECH_CAPABILITY, this.cultTech, null, tag);
		tag = ByteBufUtils.readTag(buf);
		//noinspection ConstantConditions
		SkillsProvider.SKILL_CAP_CAPABILITY.getStorage().readNBT(SkillsProvider.SKILL_CAP_CAPABILITY, this.skillCap, null, tag);
		tag = ByteBufUtils.readTag(buf);
		//noinspection ConstantConditions
		FoundationProvider.FOUNDATION_CAPABILITY.getStorage().readNBT(FoundationProvider.FOUNDATION_CAPABILITY, this.foundation, null, tag);
		this.shouldSetCdaCP = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//noinspection ConstantConditions
		NBTTagCompound tag = (NBTTagCompound) CultivationProvider.CULTIVATION_CAP.getStorage().writeNBT(CultivationProvider.CULTIVATION_CAP, this.cultivation, null);
		ByteBufUtils.writeTag(buf, tag);
		//noinspection ConstantConditions
		tag = (NBTTagCompound) CultTechProvider.CULT_TECH_CAPABILITY.getStorage().writeNBT(CultTechProvider.CULT_TECH_CAPABILITY, this.cultTech, null);
		ByteBufUtils.writeTag(buf, tag);
		//noinspection ConstantConditions
		tag = (NBTTagCompound) SkillsProvider.SKILL_CAP_CAPABILITY.getStorage().writeNBT(SkillsProvider.SKILL_CAP_CAPABILITY, this.skillCap, null);
		ByteBufUtils.writeTag(buf, tag);
		//noinspection ConstantConditions
		tag = (NBTTagCompound) FoundationProvider.FOUNDATION_CAPABILITY.getStorage().writeNBT(FoundationProvider.FOUNDATION_CAPABILITY, this.foundation, null);
		ByteBufUtils.writeTag(buf, tag);
		buf.writeBoolean(shouldSetCdaCP);
	}

	public static class Handler implements IMessageHandler<UnifiedCapabilitySyncMessage, IMessage> {

		@Override
		public IMessage onMessage(UnifiedCapabilitySyncMessage message, MessageContext ctx) {
			if(ctx.side == Side.CLIENT) {
				return this.handleClientMessage(message);
			}
			return null;
		}

		@SideOnly(Side.CLIENT)
		private IMessage handleClientMessage(UnifiedCapabilitySyncMessage message) {
			Minecraft.getMinecraft().addScheduledTask(()->{
				EntityPlayer player = Minecraft.getMinecraft().player;
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
				ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
				IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
				cultivation.copyFrom(message.cultivation);
				cultTech.copyFrom(message.cultTech);
				skillCap.copyFrom(message.skillCap, message.shouldSetCdaCP);
				foundation.copyFrom(message.foundation);
				cultivation.setSpeedHandicap(WuxiaCraftConfig.speedHandicap);
				cultivation.setMaxSpeed(WuxiaCraftConfig.maxSpeed);
				cultivation.setHasteLimit(WuxiaCraftConfig.blockBreakLimit);
				cultivation.setJumpLimit(WuxiaCraftConfig.jumpLimit);
				NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap, WuxiaCraftConfig.maxSpeed, WuxiaCraftConfig.blockBreakLimit, WuxiaCraftConfig.jumpLimit, player.getUniqueID()));
			});
			return null;
		}
	}

}
