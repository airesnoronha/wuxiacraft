package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ProgressMessageHandler implements IMessageHandler<ProgressMessage, IMessage> {
	@Override
	public IMessage onMessage(ProgressMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			final WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() -> {
				EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
				if(player != null) {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
					switch (message.op) {
						case 0:
							CultivationUtils.cultivatorAddProgress(player, cultivation, message.amount, message.techniques, message.allowBreakTrough, message.ignoreBottleneck);
							break;
						case 1:
							cultivation.addProgress(-message.amount, false);
							break;
						case 2:
							cultivation.setProgress(message.amount);
					}
				}
			});
		}
		return null;
	}
}
