package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpawnEntityOnClientMessage implements IMessage {

	public ResourceLocation entityEntry;
	public int entityId;
	public double posX, posY, posZ;
	public float entityYaw, entityPitch;
	public double motionX, motionY, motionZ;

	public SpawnEntityOnClientMessage() {
	}

	public SpawnEntityOnClientMessage(ResourceLocation entityEntry, int entityId, double posX, double posY, double posZ, float entityYaw, float entityPitch, double motionX, double motionY, double motionZ) {
		this.entityEntry = entityEntry;
		this.entityId = entityId;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.entityYaw = entityYaw;
		this.entityPitch = entityPitch;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public SpawnEntityOnClientMessage(Entity entity) {
		for(EntityEntry entry : ForgeRegistries.ENTITIES.getValuesCollection()) {
			if(entity.getClass().equals(entry.getEntityClass())) {
				this.entityEntry = ForgeRegistries.ENTITIES.getKey(entry);
			}
		}
		this.entityId = entity.getEntityId();
		this.posX = entity.posX;
		this.posY = entity.posY;
		this.posZ = entity.posZ;
		this.entityYaw = entity.rotationYaw;
		this.entityPitch = entity.rotationPitch;
		this.motionX = entity.motionX;
		this.motionY = entity.motionY;
		this.motionZ = entity.motionZ;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityEntry = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
		this.entityId = buf.readInt();
		this.posX = buf.readDouble();
		this.posY = buf.readDouble();
		this.posZ = buf.readDouble();
		this.motionX = buf.readDouble();
		this.motionY = buf.readDouble();
		this.motionZ = buf.readDouble();
		this.entityYaw = buf.readFloat();
		this.entityPitch = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.entityEntry.toString());
		buf.writeInt(this.entityId);
		buf.writeDouble(this.posX);
		buf.writeDouble(this.posY);
		buf.writeDouble(this.posZ);
		buf.writeDouble(this.motionX);
		buf.writeDouble(this.motionY);
		buf.writeDouble(this.motionZ);
		buf.writeFloat(this.entityYaw);
		buf.writeFloat(this.entityPitch);
	}

	public static class Handler implements IMessageHandler<SpawnEntityOnClientMessage, IMessage> {

		@Override
		public IMessage onMessage(SpawnEntityOnClientMessage message, MessageContext ctx) {
			if(ctx.side == Side.CLIENT) {
				handleClientMessage(message);
			}
			return null;
		}

		@SideOnly(Side.CLIENT)
		public void handleClientMessage(SpawnEntityOnClientMessage message ) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				EntityEntry entry = ForgeRegistries.ENTITIES.getValue(message.entityEntry);
				if(entry != null) {
					Entity entity = entry.newInstance(Minecraft.getMinecraft().world);
					entity.posX = message.posX;
					entity.posY = message.posY;
					entity.posZ = message.posZ;
					entity.motionX = message.motionX;
					entity.motionY = message.motionY;
					entity.motionZ = message.motionZ;
					entity.rotationPitch = message.entityPitch;
					entity.rotationYaw = message.entityYaw;
					entity.setEntityId(message.entityId);
					Minecraft.getMinecraft().world.spawnEntity(entity);
				}

			});
		}
	}
}
