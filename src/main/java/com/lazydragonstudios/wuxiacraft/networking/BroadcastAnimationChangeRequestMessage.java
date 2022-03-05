package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.capabilities.ClientAnimationState;
import com.lazydragonstudios.wuxiacraft.capabilities.IClientAnimationState;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.ICultivation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class BroadcastAnimationChangeRequestMessage {

	private final CompoundTag animationState;

	private final boolean combat;

	public BroadcastAnimationChangeRequestMessage(CompoundTag animationState, boolean combat) {
		this.animationState = animationState;
		this.combat = combat;
	}

	public BroadcastAnimationChangeRequestMessage(IClientAnimationState animationState, boolean combat) {
		this(animationState.serialize(), combat);
	}

	public static void encode(BroadcastAnimationChangeRequestMessage msg, FriendlyByteBuf buf) {
			buf.writeNbt(msg.animationState);
			buf.writeBoolean(msg.combat);
	}

	public static BroadcastAnimationChangeRequestMessage decode(FriendlyByteBuf buf) {
		return new BroadcastAnimationChangeRequestMessage(buf.readAnySizeNbt(), buf.readBoolean());
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
				ICultivation cultivation = Cultivation.get(player);
				cultivation.setExercising(animationStateInstance.isExercising());
				cultivation.setCombat(msg.combat);
				cultivation.setCombat(msg.combat);
				for (var target : level.players()) {
					WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) target), new AnimationChangeUpdateMessage(player.getUUID(), animationState, msg.combat));
				}
			});
		}
	}


}
