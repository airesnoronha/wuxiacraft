package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record AttemptBreakthroughMessage(System system) {

	public static void encode(AttemptBreakthroughMessage msg, FriendlyByteBuf buf) {
		buf.writeEnum(msg.system);
	}

	public static AttemptBreakthroughMessage decode(FriendlyByteBuf buf) {
		return new AttemptBreakthroughMessage(buf.readEnum(System.class));
	}

	public static void handleMessage(AttemptBreakthroughMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		if (side.isClient()) return;

		ctx.enqueueWork(() -> {
			var serverPlayer = ctx.getSender();
			if (serverPlayer == null) return;
			var cultivation = Cultivation.get(serverPlayer);
			cultivation.attemptBreakthrough(msg.system);
			WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new CultivationSyncMessage(cultivation));
			cultivation.calculateStats();
		});
	}
}
