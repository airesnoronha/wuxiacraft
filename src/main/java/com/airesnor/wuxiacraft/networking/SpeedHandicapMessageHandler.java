package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SpeedHandicapMessageHandler implements IMessageHandler {
	@Override
	public IMessage onMessage(IMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(()->{
				WuxiaCraft.logger.info("Receiving speed handicap " + ((SpeedHandicapMessage)message).handicap + " for player "+player.getDisplayNameString());

				ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);

				cultivation.setSpeedHandicap(((SpeedHandicapMessage)message).handicap);

				EventHandler.applyModifiers(player, cultivation);

			});
		}
		if(ctx.side == Side.CLIENT) {
			return new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap);
		}
		return null;
	}
}
