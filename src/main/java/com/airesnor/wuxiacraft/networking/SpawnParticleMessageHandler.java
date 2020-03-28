package com.airesnor.wuxiacraft.networking;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SpawnParticleMessageHandler implements IMessageHandler<SpawnParticleMessage, IMessage> {
	@Override
	public IMessage onMessage(SpawnParticleMessage message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				Minecraft.getMinecraft().world.spawnParticle(message.getParticleType(),
						message.isIgnoreRange(),
						message.getPosX(), message.getPosY(), message.getPosZ(),
						message.getMotionX(), message.getMotionY(), message.getMotionZ(),
						message.getArguments());
			});
		}
		return null;
	}
}
