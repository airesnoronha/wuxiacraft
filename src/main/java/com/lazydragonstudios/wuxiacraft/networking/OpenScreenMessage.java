package com.lazydragonstudios.wuxiacraft.networking;

import com.lazydragonstudios.wuxiacraft.container.IntrospectionMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class OpenScreenMessage {

	public enum ScreenType {
		INTROSPECTION,
		SPATIAL_ITEM
	}

	public ScreenType screen;
	public boolean isValid;

	public OpenScreenMessage(ScreenType screen) {
		this.screen = screen;
		isValid = true;
	}

	public OpenScreenMessage() {
		isValid = false;
	}

	public static void encode(OpenScreenMessage message, FriendlyByteBuf buf) {
		buf.writeEnum(message.screen);
	}

	public static OpenScreenMessage decode(FriendlyByteBuf buf) {
		return new OpenScreenMessage(buf.readEnum(ScreenType.class));
	}

	public static void handleMessage(OpenScreenMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		var side = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);
		if (side.isServer()) {
			ctx.enqueueWork(() -> {
				if (ctx.getSender() != null) {
					switch (message.screen) {
						case INTROSPECTION:
							NetworkHooks.openGui(ctx.getSender(),
									new SimpleMenuProvider(
											(id, inv, player) -> new IntrospectionMenu(IntrospectionMenu.registryType, id),
											new TextComponent("")));
							break;
						case SPATIAL_ITEM:
							break;
					}
				}
			});
		}
	}
}
