package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.WuxiaCraft;
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
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpawnEntityOnClientMessage implements IMessage {

	private ResourceLocation entityEntry;
	private int entityId;
	private double posX, posY, posZ;
	private float entityYaw, entityPitch;
	private double motionX, motionY, motionZ;
	public Entity entity;

	public SpawnEntityOnClientMessage() {
	}

	public SpawnEntityOnClientMessage(Entity entity) {
		this.entity = entity;
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
		EntityEntry entry = ForgeRegistries.ENTITIES.getValue(this.entityEntry);
		if (entry != null) {
			handleClientEntityEntry(entry, buf);
		}
	}

	@SideOnly(Side.CLIENT)
	public void handleClientEntityEntry(EntityEntry entry, ByteBuf buf) {
		if (entry != null) {
			this.entity = entry.newInstance(null);
			entity.posX = this.posX;
			entity.posY = this.posY;
			entity.posZ = this.posZ;
			entity.motionX = this.motionX;
			entity.motionY = this.motionY;
			entity.motionZ = this.motionZ;
			entity.rotationPitch = this.entityPitch;
			entity.rotationYaw = this.entityYaw;
			entity.setEntityId(this.entityId);
			if (this.entity instanceof IEntityAdditionalSpawnData) {
				((IEntityAdditionalSpawnData) (this.entity)).readSpawnData(buf);
			}
		}
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
		if(this.entity instanceof IEntityAdditionalSpawnData) {
			((IEntityAdditionalSpawnData)(this.entity)).writeSpawnData(buf);
		}
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
				if(message.entity != null) {
					message.entity.world = Minecraft.getMinecraft().world;
				}

			});
		}
	}
}
