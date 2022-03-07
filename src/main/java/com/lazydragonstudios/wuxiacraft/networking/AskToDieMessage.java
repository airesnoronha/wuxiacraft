package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AskToDieMessage {

	public final boolean rtp;

	public AskToDieMessage(boolean rtp) {
		this.rtp = rtp;
	}

	public static void encode(AskToDieMessage msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.rtp);
	}

	public static AskToDieMessage decode(FriendlyByteBuf buf) {
		return new AskToDieMessage(buf.readBoolean());
	}

	public static void handleMessage(AskToDieMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);
		if (side.isServer()) {
			ctx.enqueueWork(() -> {
				var serverPlayer = ctx.getSender();
				if (serverPlayer == null) return;
				if (msg.rtp) {
					serverPlayer.addTag("PLEASE_RTP_ME");
				}
				var server = serverPlayer.getServer();
				if (server == null) return;
				serverPlayer.setHealth(-1);
				Cultivation.get(serverPlayer).setSemiDeadState(false);
				//server.getPlayerList().respawn(serverPlayer, false);
			});
		}
	}
}
