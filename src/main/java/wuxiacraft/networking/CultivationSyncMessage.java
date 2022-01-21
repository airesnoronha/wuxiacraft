package wuxiacraft.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CultivationSyncMessage {

	public static void encode(CultivationSyncMessage msg, FriendlyByteBuf buf) {

	}

	public static CultivationSyncMessage decode(FriendlyByteBuf buf) {
		return new CultivationSyncMessage();
	}

	public static void handleMessage(CultivationSyncMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {

	}
}
