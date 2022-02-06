package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestTechniqueDataChange {

	private final System system;

	private final CompoundTag techniqueData;

	public RequestTechniqueDataChange(System system, CompoundTag techniqueData) {
		this.system = system;
		this.techniqueData = techniqueData;
	}

	public static void encode(RequestTechniqueDataChange msg, FriendlyByteBuf buf) {
		buf.writeEnum(msg.system);
		buf.writeNbt(msg.techniqueData);
	}

	public static RequestTechniqueDataChange decode(FriendlyByteBuf buf) {
		System system = buf.readEnum(System.class);
		CompoundTag tag = buf.readAnySizeNbt();
		return new RequestTechniqueDataChange(system, tag);
	}

	public static void handleMessage(RequestTechniqueDataChange msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		if (side.isServer()) {
			ctx.setPacketHandled(true);
			ctx.enqueueWork(() -> {
				var player = ctx.getSender();
				if (player == null) return;
				var cultivation = Cultivation.get(player);
				var systemData = cultivation.getSystemData(msg.system);
				systemData.techniqueData.deserialize(msg.techniqueData);
			});
		}
	}
}
