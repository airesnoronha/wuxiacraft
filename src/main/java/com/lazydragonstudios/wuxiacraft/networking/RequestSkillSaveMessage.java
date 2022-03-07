package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillDescriptor;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestSkillSaveMessage {

	private final int slot;
	private final CompoundTag skill;

	public RequestSkillSaveMessage(int slot, CompoundTag skill) {
		this.slot = slot;
		this.skill = skill;
	}

	public static void encode(RequestSkillSaveMessage msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.slot);
		buf.writeNbt(msg.skill);
	}

	public static RequestSkillSaveMessage decode(FriendlyByteBuf buf) {
		return new RequestSkillSaveMessage(buf.readInt(), buf.readAnySizeNbt());
	}

	public static void handleMessage(RequestSkillSaveMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		if (!side.isServer()) return;
		ctx.setPacketHandled(true);
		ctx.enqueueWork(() -> {
			var player = ctx.getSender();
			if (player == null) return;
			var cultivation = Cultivation.get(player);
			var skillDescriptor = new SkillDescriptor();
			skillDescriptor.deserialize(msg.skill);
			cultivation.getSkills().setSkillAt(msg.slot, skillDescriptor);
		});
	}
}
