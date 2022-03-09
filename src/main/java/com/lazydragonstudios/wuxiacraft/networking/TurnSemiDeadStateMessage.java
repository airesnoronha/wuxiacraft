package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.capabilities.ClientAnimationState;
import com.lazydragonstudios.wuxiacraft.client.gui.WuxiaDeathScreen;
import com.lazydragonstudios.wuxiacraft.client.gui.WuxiaSemiDeadScreen;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TurnSemiDeadStateMessage {

	private final boolean semiDead;

	private final Component causeOfDeath;

	private final boolean hardcore;

	public TurnSemiDeadStateMessage(boolean semiDead, Component component, boolean hardcore) {
		this.semiDead = semiDead;
		this.causeOfDeath = component;
		this.hardcore = hardcore;
	}

	public static void encode(TurnSemiDeadStateMessage msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.semiDead);
		buf.writeComponent(msg.causeOfDeath);
		buf.writeBoolean(msg.hardcore);
	}

	public static TurnSemiDeadStateMessage decode(FriendlyByteBuf buf) {
		return new TurnSemiDeadStateMessage(buf.readBoolean(), buf.readComponent(), buf.readBoolean());
	}

	public static void handleMessageCommon(TurnSemiDeadStateMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var direction = ctx.getDirection().getReceptionSide();
		if (direction.isServer()) return;
		ctx.setPacketHandled(true);
		handleMessageClient(msg, ctxSupplier);
	}

	@OnlyIn(Dist.CLIENT)
	private static void handleMessageClient(TurnSemiDeadStateMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		ctx.enqueueWork(() -> {
			var player = Minecraft.getInstance().player;
			if (player == null) return;

			var mc = Minecraft.getInstance();
			if (msg.semiDead) {
				if (!(mc.screen instanceof WuxiaSemiDeadScreen)) {
					mc.setScreen(new WuxiaSemiDeadScreen());
				}
			} else {
				if (mc.screen == null) return;
				var screen = mc.screen;
				if (screen instanceof WuxiaSemiDeadScreen)
					mc.setScreen(null);
			}
			WuxiaPacketHandler.INSTANCE.sendToServer(new BroadcastAnimationChangeRequestMessage(ClientAnimationState.get(player), Cultivation.get(player).isCombat()));
		});
	}
}
