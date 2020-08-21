package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.BaseSystemLevel;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.UUID;

public class AskCultivationLevelMessage implements IMessage {

	public BaseSystemLevel askerLevel;
	public int askerSubLevel;
	public UUID askerUUID;

	public AskCultivationLevelMessage(BaseSystemLevel askerLevel, int askerSubLevel, UUID askerUUID) {
		this.askerLevel = askerLevel;
		this.askerSubLevel = askerSubLevel;
		this.askerUUID = askerUUID;
	}

	public AskCultivationLevelMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.askerSubLevel = buf.readInt();
		int length = buf.readInt();
		byte[] bytes = new byte[30];
		buf.readBytes(bytes, 0, length);
		bytes[length] = '\0';
		String cultLevelName = new String(bytes, 0, length);
		this.askerLevel = BaseSystemLevel.getLevelInListByName(BaseSystemLevel.ESSENCE_LEVELS, cultLevelName);
		this.askerUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.askerSubLevel);
		byte[] bytes = this.askerLevel.levelName.getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
		packetBuffer.writeUniqueId(this.askerUUID);
	}

	public static class Handler implements IMessageHandler<AskCultivationLevelMessage, RespondCultivationLevelMessage> {

		@Override
		public RespondCultivationLevelMessage onMessage(AskCultivationLevelMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.askerUUID);
					if(player != null) {
						List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class, player.getEntityBoundingBox().grow(64, 32, 64));
						for (Entity entity : entities) {
							if (entity instanceof EntityPlayer) {
								ICultivation cultivation = CultivationUtils.getCultivationFromEntity((EntityLivingBase) entity);
								BaseSystemLevel level = cultivation.getEssenceLevel();
								int subLevel = cultivation.getEssenceSubLevel();
								if (level.greaterThan(message.askerLevel, BaseSystemLevel.ESSENCE_LEVELS)) {
									level = message.askerLevel.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
									subLevel = -1;
								}
								RespondCultivationLevelMessage respondCultivationLevelMessage = new RespondCultivationLevelMessage(level, subLevel, entity.getUniqueID());
								NetworkWrapper.INSTANCE.sendTo(respondCultivationLevelMessage, (EntityPlayerMP) player);
							}
						}
					}
				});
			}
			return null;
		}
	}

}
