package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CultivationLevelsMessageHandler implements IMessageHandler<CultivationLevelsMessage, IMessage> {

	@Override
	public IMessage onMessage(CultivationLevelsMessage message, MessageContext ctx) {
		if(ctx.side == Side.CLIENT) {
			CultivationLevel.LOADED_LEVELS.clear();
			for(CultivationLevel level : message.levels) {
				CultivationLevel.LOADED_LEVELS.put(level.levelName, level);
			}
			CultivationLevel.BASE_LEVEL = CultivationLevel.LOADED_LEVELS.get(message.baseLevelName);
		}
		return null;
	}
}
