package com.airesnor.wuxiacraft.networking;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpeedHandicapMessageHandler implements IMessageHandler<SpeedHandicapMessage, SpeedHandicapMessage> {
	@Override
	public SpeedHandicapMessage onMessage(SpeedHandicapMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {

				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);

				cultivation.setSpeedHandicap(message.handicap);
				cultivation.setMaxSpeed(message.maxSpeed);
				cultivation.setHasteLimit(message.hasteLimit);
				cultivation.setJumpLimit(message.jumpLimit);

			});
		}
		if (ctx.side == Side.CLIENT) {
			return handleClientMessage(message, ctx);
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public static SpeedHandicapMessage handleClientMessage(SpeedHandicapMessage message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(Minecraft.getMinecraft().player);
			cultivation.setSpeedHandicap(WuxiaCraftConfig.speedHandicap);
			cultivation.setMaxSpeed(WuxiaCraftConfig.maxSpeed);
			cultivation.setHasteLimit(WuxiaCraftConfig.blockBreakLimit);
			cultivation.setJumpLimit(WuxiaCraftConfig.jumpLimit);
		});
		return new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap, WuxiaCraftConfig.maxSpeed, WuxiaCraftConfig.maxSpeed, WuxiaCraftConfig.jumpLimit);
	}
}
