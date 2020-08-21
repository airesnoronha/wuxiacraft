package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class SelectSkillMessage implements IMessage {

	public int selectSkill;
	public UUID senderUUID;

	public SelectSkillMessage(int selectSkill, UUID senderUUID) {
		this.selectSkill = selectSkill;
		this.senderUUID = senderUUID;
	}

	public SelectSkillMessage() {
		this.senderUUID = null;
		this.selectSkill = 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.selectSkill = buf.readInt();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.selectSkill);
		packetBuffer.writeUniqueId(this.senderUUID);
	}

	public static class Handler implements IMessageHandler<SelectSkillMessage, IMessage> {

		@Override
		public IMessage onMessage(SelectSkillMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
					if (player != null) {
						ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
						selectSkill(skillCap, message.selectSkill);
					}
				});
			}
			return null;
		}

		public static void selectSkill(ISkillCap skillCap, int i) {
			skillCap.setActiveSkill(Math.min(skillCap.getSelectedSkills().size() - 1, Math.max(0, i)));
			if (skillCap.getSelectedSkills().size() == 0) {
				skillCap.setActiveSkill(-1);
			}
		}
	}

}
