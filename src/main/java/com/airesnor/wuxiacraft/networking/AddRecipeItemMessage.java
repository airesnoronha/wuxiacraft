package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.entities.tileentity.CauldronTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

public class AddRecipeItemMessage implements IMessage {

	public BlockPos pos;
	public Item item;
	public float temperature;

	public AddRecipeItemMessage(BlockPos pos, Item item, float temperature) {
		this.pos = pos;
		this.item = item;
		this.temperature = temperature;
	}

	public AddRecipeItemMessage() {}

	@Override
	public void fromBytes(ByteBuf buf) {
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		this.pos = new BlockPos(x,y,z);
		this.temperature = buf.readFloat();
		this.item = ByteBufUtils.readItemStack(buf).getItem();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeFloat(this.temperature);
		ByteBufUtils.writeItemStack(buf, this.item.getDefaultInstance());
	}

	public static class Handler implements IMessageHandler<AddRecipeItemMessage, IMessage> {

		@Override
		public IMessage onMessage(AddRecipeItemMessage message, MessageContext ctx) {
			if(ctx.side == Side.SERVER) {
				final EntityPlayerMP player = ctx.getServerHandler().player;
				player.getServerWorld().addScheduledTask(() -> {
					WorldServer world = player.getServerWorld();
					CauldronTileEntity entity = (CauldronTileEntity) world.getTileEntity(message.pos);
					if(entity != null) {
						entity.addServerRecipeInput(message.item, message.temperature);
					}
				});
			}
			return null;
		}
	}

}
