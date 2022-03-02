package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record CultivationStateChangeMessage(int slot, boolean casting, boolean combat) {

	public static void encode(CultivationStateChangeMessage msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.slot);
		buf.writeBoolean(msg.casting);
		buf.writeBoolean(msg.combat);
	}

	public static CultivationStateChangeMessage decode(FriendlyByteBuf buf) {
		return new CultivationStateChangeMessage(buf.readInt(), buf.readBoolean(), buf.readBoolean());
	}

	public static void handleMessage(CultivationStateChangeMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		if (!side.isServer()) return;

		ctx.enqueueWork(() -> {
			var player = ctx.getSender();
			if (player == null) return;
			var cultivation = Cultivation.get(player);
			cultivation.getSkills().casting = msg.casting;
			cultivation.getSkills().selectedSkill = msg.slot;
			cultivation.setCombat(msg.combat);
		});
	}
}
