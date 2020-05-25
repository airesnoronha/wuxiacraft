package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SelectFoundationAttributeMessageHandler implements IMessageHandler<SelectFoundationAttributeMessage, IMessage> {

	@Override
	public IMessage onMessage(SelectFoundationAttributeMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			final WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() -> {
				EntityPlayer player = world.getPlayerEntityByUUID(message.sender);
				if(player!= null) {
					IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
					int attribute = MathUtils.clamp(message.attribute, -1, 5);
					foundation.selectAttribute(attribute);
				}
			});
		}
		return null;
	}
}
