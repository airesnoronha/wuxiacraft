package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.container.InscriberMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RenameItemInInscriberMessage(String name) {

	public static void encode(RenameItemInInscriberMessage msg, FriendlyByteBuf buf) {
		buf.writeComponent(new TextComponent(msg.name));
	}

	public static RenameItemInInscriberMessage decode(FriendlyByteBuf buf) {
		return new RenameItemInInscriberMessage(buf.readComponent().getString());
	}

	public static void handleMessage(RenameItemInInscriberMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		if (side.isClient()) return;
		ctx.enqueueWork(() -> {
			var serverPlayer = ctx.getSender();
			if (serverPlayer.containerMenu instanceof InscriberMenu inscriber) {
				inscriber.setItemName(msg.name);
			}
		});
	}

}
