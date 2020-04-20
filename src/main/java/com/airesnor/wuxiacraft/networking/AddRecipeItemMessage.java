package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

	public AddRecipeItemMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		this.pos = new BlockPos(x,y,z);
		this.temperature = buf.readFloat();
		int length = buf.readInt();
		byte [] itemNameBytes = new byte [length];
		buf.readBytes(itemNameBytes, 0, length);
		String itemName = new String(itemNameBytes);
		this.item = GameRegistry.makeItemStack(itemName, 0, 1, "").getItem();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeFloat(this.temperature);
		String itemName = Objects.requireNonNull(this.item.getRegistryName()).toString();
		byte [] itemNameBytes = itemName.getBytes();
		buf.writeInt(itemNameBytes.length);
		buf.writeBytes(itemNameBytes);
	}
}
