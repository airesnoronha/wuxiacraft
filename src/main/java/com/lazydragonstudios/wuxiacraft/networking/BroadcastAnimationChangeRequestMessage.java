package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.capabilities.ClientAnimationState;
import com.lazydragonstudios.wuxiacraft.capabilities.IClientAnimationState;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class BroadcastAnimationChangeRequestMessage {

	private final CompoundTag animationState;
	private final boolean isValid;

	public BroadcastAnimationChangeRequestMessage() {
		this.isValid = false;
		this.animationState = null;
	}

	public BroadcastAnimationChangeRequestMessage(CompoundTag animationState) {
		this.animationState = animationState;
		this.isValid = true;
	}

	public BroadcastAnimationChangeRequestMessage(IClientAnimationState animationState) {
		this.animationState = animationState.serialize();
		this.isValid = true;
	}

	public static void encode(BroadcastAnimationChangeRequestMessage msg, FriendlyByteBuf buf) {
		if (msg.isValid)
			buf.writeNbt(msg.animationState);
	}

	public static BroadcastAnimationChangeRequestMessage decode(FriendlyByteBuf buf) {
		return new BroadcastAnimationChangeRequestMessage(buf.readAnySizeNbt());
	}

	public static void handleMessage(BroadcastAnimationChangeRequestMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);
		if (side.isServer()) {
			ctx.enqueueWork(() -> {
				var player = ctx.getSender();
				if (player == null) return;
				var animationState = msg.animationState;
				var level = player.level;
				var animationStateInstance = new ClientAnimationState();
				animationStateInstance.deserialize(animationState);
				Cultivation.get(player).setExercising(animationStateInstance.isExercising());
				for (var target : level.players()) {
					WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) target), new AnimationChangeUpdateMessage(player.getUUID(), animationState));
				}
			});
		}
	}


}
