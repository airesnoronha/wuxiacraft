package com.lazydragonstudios.wuxiacraft.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RequestRandomTeleportMessage {

	private final UUID playerId;

	public RequestRandomTeleportMessage() {
		this.playerId = null;
	}

	public RequestRandomTeleportMessage(UUID playerId) {
		this.playerId = playerId;
	}

	public static void encode(RequestRandomTeleportMessage msg, FriendlyByteBuf buf) {
		buf.writeUUID(msg.playerId);
	}

	public static RequestRandomTeleportMessage decode(FriendlyByteBuf buf) {
		return new RequestRandomTeleportMessage(buf.readUUID());
	}

	public static void handleMessage(RequestRandomTeleportMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);
		if (side.isServer()) {
			ctx.enqueueWork(() -> {
				if (msg.playerId == null) return;
				var serverPlayer = ctx.getSender();
				if (serverPlayer == null) return;
				if (!serverPlayer.getUUID().equals(msg.playerId)) return;
				serverPlayer.addTag("PLEASE_RTP_ME");
			});
		}
	}
}
