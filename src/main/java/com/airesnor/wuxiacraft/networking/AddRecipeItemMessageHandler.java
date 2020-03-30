package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.entities.tileentity.CauldronTileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class AddRecipeItemMessageHandler implements IMessageHandler<AddRecipeItemMessage, IMessage> {

	@Override
	public IMessage onMessage(AddRecipeItemMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				WorldServer world = ctx.getServerHandler().player.getServerWorld();
				CauldronTileEntity entity = (CauldronTileEntity) world.getTileEntity(message.pos);
				if(entity != null) {
					entity.addServerRecipeInput(message.item, message.temperature);
				}
			});
		}
		return null;
	}
}
