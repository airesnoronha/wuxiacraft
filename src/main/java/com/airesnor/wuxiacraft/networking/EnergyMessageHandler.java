package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class EnergyMessageHandler implements IMessageHandler<EnergyMessage, IMessage> {
	@Override
	public IMessage onMessage(EnergyMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			final WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() -> {
				EntityPlayer player = world.getPlayerEntityByName(message.sender);
				if(player != null) {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
					switch (message.op) {
						case 0:
							cultivation.addEnergy(message.amount);
							break;
						case 1:
							cultivation.remEnergy(message.amount);
							break;
						case 2:
							cultivation.setEnergy(message.amount);
							break;
					}
				}
			});
		}
		return null;
	}
}
