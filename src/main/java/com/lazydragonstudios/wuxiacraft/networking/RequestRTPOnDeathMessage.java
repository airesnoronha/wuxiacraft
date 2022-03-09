package com.lazydragonstudios.wuxiacraft.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RequestRTPOnDeathMessage(boolean rtp) {

	public static void encode(RequestRTPOnDeathMessage msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.rtp);
	}

	public static RequestRTPOnDeathMessage decode(FriendlyByteBuf buf) {
		return new RequestRTPOnDeathMessage(buf.readBoolean());
	}

	public static void handleMessage(RequestRTPOnDeathMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		if(side.isClient()) return;

		ctx.enqueueWork(() -> {
			var player = ctx.getSender();
			if(player == null) return;
			if (msg.rtp) {
				player.addTag("PLEASE_RTP_ME");
			}
		});
	}

}
