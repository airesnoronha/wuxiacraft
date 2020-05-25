package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CapabilityRequestMessageHandler implements IMessageHandler<CapabilityRequestMessage, UnifiedCapabilitySyncMessage> {

	@Override
	public UnifiedCapabilitySyncMessage onMessage(CapabilityRequestMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			EntityPlayer player = world.getPlayerEntityByUUID(message.sender);
			if(player != null) {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
				ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
				IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
				return new UnifiedCapabilitySyncMessage(cultivation, cultTech, skillCap, foundation, true);
			}
		}
		return null;
	}
}
