package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.capabilities.ClientAnimationState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class AnimationChangeUpdateMessage {

	private UUID playerId;

	private CompoundTag animationState;

	private boolean isValid;

	public AnimationChangeUpdateMessage() {
		this.isValid =false;
		this.playerId = null;
		this.animationState = null;
	}

	public AnimationChangeUpdateMessage(UUID playerId, CompoundTag animationState) {
		this.playerId = playerId;
		this.animationState = animationState;
		this.isValid = true;
	}

	public static void encode(AnimationChangeUpdateMessage msg, FriendlyByteBuf buf) {
		buf.writeUUID(msg.playerId);
		buf.writeNbt(msg.animationState);
	}

	public static AnimationChangeUpdateMessage decode(FriendlyByteBuf buf) {
		UUID playerId = buf.readUUID();
		CompoundTag animationState = buf.readAnySizeNbt();
		return new AnimationChangeUpdateMessage(playerId, animationState);
	}

	public static void handleMessageCommon(AnimationChangeUpdateMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		ctx.setPacketHandled(true);
		var side = ctx.getDirection().getReceptionSide();
		if (side.isClient()) {
			handleMessageClient(msg, ctx);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void handleMessageClient(AnimationChangeUpdateMessage msg, NetworkEvent.Context ctx) {
		ctx.enqueueWork(() -> {
			var player = Minecraft.getInstance().player;
			if (player == null) return;
			var level = player.level;
			var target = level.getPlayerByUUID(msg.playerId);
			if(target == null) return;
			ClientAnimationState.get(target).deserialize(msg.animationState);
		});
	}
}
