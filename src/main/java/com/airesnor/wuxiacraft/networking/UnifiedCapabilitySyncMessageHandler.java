package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UnifiedCapabilitySyncMessageHandler implements IMessageHandler<UnifiedCapabilitySyncMessage, IMessage> {

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
