package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
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

public class ActivateSkillMessage implements IMessage {

	public int selectedSkill = 0;
	public UUID senderUUID;

	@SuppressWarnings("unused")
	public ActivateSkillMessage() {
	}

	public ActivateSkillMessage(int selectedSkill, UUID senderUUID) {
		this.selectedSkill = selectedSkill;
		this.senderUUID = senderUUID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.selectedSkill = buf.readInt();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.selectedSkill);
		packetBuffer.writeUniqueId(this.senderUUID);
	}

	public static class Handler implements IMessageHandler<ActivateSkillMessage, IMessage> {

		@Override
		public IMessage onMessage(ActivateSkillMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
					if(player != null) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
						ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
						skillCap.setActiveSkill(message.selectedSkill);
						Skill selectedSkill = skillCap.getSelectedSkill(CultivationUtils.getCultTechFromEntity(player));
						if (selectedSkill != null) {
							if (selectedSkill.activate(player)) {
								if (!player.isCreative())
									cultivation.remEnergy(selectedSkill.getCost());
								skillCap.stepCooldown(selectedSkill.getCooldown());
								//CultivationUtils.cultivatorAddProgress(player, cultivation, selectedSkill.getProgress(), true, false, false);
							}
						}
					}
				});
			}
			return null;
		}
	}

}
