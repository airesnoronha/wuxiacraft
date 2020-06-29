package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CastSkillMessage implements IMessage {

	public boolean casting;

	public CastSkillMessage() {
		this.casting = false;
	}

	public CastSkillMessage(boolean casting) {
		this.casting = casting;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.casting = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.casting);
	}

	public static class Handler implements IMessageHandler<CastSkillMessage, IMessage> {

		/**
		 * Server must know this only because of storage
		 * Cooldown is set by ActivateSkillMessageHandler
		 */
		@Override
		public IMessage onMessage(CastSkillMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				final EntityPlayerMP player = ctx.getServerHandler().player;
				ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
					ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
					if (message.casting)
						skillCap.setCasting(true);
					else {
						skillCap.setCasting(false);
						skillCap.resetCastProgress();
					}
				});
			}
			return null;
		}
	}

}
