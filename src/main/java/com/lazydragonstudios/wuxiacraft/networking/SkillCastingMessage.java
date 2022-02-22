package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SkillCastingMessage {

	public final int slot;
	public final boolean casting;

	public SkillCastingMessage(int slot, boolean casting) {
		this.slot = slot;
		this.casting = casting;
	}

	public static void encode(SkillCastingMessage msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.slot);
		buf.writeBoolean(msg.casting);
	}

	public static SkillCastingMessage decode(FriendlyByteBuf buf) {
		return new SkillCastingMessage(buf.readInt(), buf.readBoolean());
	}

	public static void handleMessage(SkillCastingMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		if(!side.isServer()) return;

		ctx.enqueueWork(() ->{
			var player = ctx.getSender();
			if(player == null) return;
			var cultivation = Cultivation.get(player);
			cultivation.getSkills().casting = msg.casting;
			cultivation.getSkills().selectedSkill = msg.slot;
		});
	}
}
