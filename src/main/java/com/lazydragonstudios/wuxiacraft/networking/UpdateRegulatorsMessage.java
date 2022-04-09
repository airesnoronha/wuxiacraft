package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record UpdateRegulatorsMessage(double strength, double agility) {

	public static void encode(UpdateRegulatorsMessage msg, FriendlyByteBuf buf) {
		buf.writeDouble(msg.strength);
		buf.writeDouble(msg.agility);
	}

	public static UpdateRegulatorsMessage decode(FriendlyByteBuf buf) {
		return new UpdateRegulatorsMessage(buf.readDouble(), buf.readDouble());
	}

	public static void handleMessage(UpdateRegulatorsMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var direction = ctx.getDirection().getReceptionSide();
		if (direction != LogicalSide.SERVER) return;
		ctx.enqueueWork(() -> {
			var sender = ctx.getSender();
			if (sender == null) return;
			var cultivation = Cultivation.get(sender);
			cultivation.setAgilityRegulator(msg.agility);
			cultivation.setStrengthRegulator(msg.strength);
		});
	}

}
