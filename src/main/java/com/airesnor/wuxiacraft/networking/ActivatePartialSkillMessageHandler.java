package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ActivatePartialSkillMessageHandler implements IMessageHandler<ActivatePartialSkillMessage, IMessage> {

	@Override
	public IMessage onMessage(ActivatePartialSkillMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() -> {
				EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
				if(player!=null){
					if ("barrageMinorBeam".equals(message.skillName)) {
						Skills.BARRAGE_MINOR_BEAM.activate(player);
						CultivationUtils.getCultivationFromEntity(player).remEnergy(message.energy);
					}
					if ("applySlowness".equals(message.skillName)) {
						Skills.APPLY_SLOWNESS.activate(player);
						CultivationUtils.getCultivationFromEntity(player).remEnergy(message.energy);
					}
				}
			});
		}
		return null;
	}
}
