package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CultivationMessageHandler implements IMessageHandler {
	@Override
	public IMessage onMessage(IMessage message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			return handleClientMessage(message, ctx);
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	private IMessage handleClientMessage(IMessage message, MessageContext ctx) {
		if (message instanceof CultivationMessage) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				CultivationMessage cm = (CultivationMessage) message;
				EntityPlayer player = Minecraft.getMinecraft().player;
				ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
				if (cultivation != null) {
					cultivation.setCurrentLevel(cm.messageLevel);
					cultivation.setCurrentSubLevel(cm.messageSubLevel);
					cultivation.setProgress(cm.messageProgress);
					cultivation.setEnergy(cm.messageEnergy);
					cultivation.setPelletCooldown(cm.pelletCooldown);
				} else {
					WuxiaCraft.logger.info("He ain't a cultivator. Weeird");
				}
			});
		}
		return null;
	}
}
