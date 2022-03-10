package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record MeditateMessage(System system, boolean success) {

	public static void encode(MeditateMessage msg, FriendlyByteBuf buf) {
		buf.writeEnum(msg.system);
		buf.writeBoolean(msg.success);
	}

	public static MeditateMessage decode(FriendlyByteBuf buf) {
		return new MeditateMessage(buf.readEnum(System.class), buf.readBoolean());
	}

	public static void handleMessage(MeditateMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		if (side.isClient()) return;
		ctx.enqueueWork(() -> {
			var serverPlayer = ctx.getSender();
			if (serverPlayer == null) return;
			var cultivation = Cultivation.get(serverPlayer);
			var stage = cultivation.getSystemData(msg.system).getStage();
			if (stage == null) return;
			if (msg.success) {
				stage.cultivate(serverPlayer);
			} else {
				stage.cultivationFailure(serverPlayer);
			}
		});
	}
}
