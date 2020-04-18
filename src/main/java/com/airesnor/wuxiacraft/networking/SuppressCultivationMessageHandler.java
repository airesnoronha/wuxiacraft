package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SuppressCultivationMessageHandler implements IMessageHandler<SuppressCultivationMessage, IMessage> {

	@Override
	public IMessage onMessage(SuppressCultivationMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			final WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() -> {
				EntityPlayer player = world.getPlayerEntityByName(message.sender);
				if(player != null) {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
					cultivation.setSuppress(message.suppress);
				}
			});
		}
		return null;
	}
}
