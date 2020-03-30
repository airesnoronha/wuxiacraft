package com.airesnor.wuxiacraft.networking;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class ShrinkEntityItemMessageHandler implements IMessageHandler<ShrinkEntityItemMessage, IMessage> {

	@Override
	public IMessage onMessage(ShrinkEntityItemMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				WorldServer world = ctx.getServerHandler().player.getServerWorld();
				Entity entity = world.getEntityFromUuid(UUID.fromString(message.entityItemUID));
				if(entity instanceof EntityItem) {
					ItemStack stack = ((EntityItem) entity).getItem();
					stack.shrink(1);
					if(stack.isEmpty()) {
						entity.setDead();
					} else {
						((EntityItem) entity).setItem(stack);
					}
				}
			});
		}
		return null;
	}
}
