package wuxiacraft.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import wuxiacraft.WuxiaCraft;

import java.util.Optional;

public class WuxiaPacketHandler {

	private static final String PROTOCOL_VERSION = "1";

	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
					new ResourceLocation(WuxiaCraft.MOD_ID, "networking"),
					() -> PROTOCOL_VERSION,
					PROTOCOL_VERSION::equals,
					PROTOCOL_VERSION::equals
	);

	@SuppressWarnings("UnusedAssignment")
	public static void registerMessages() {
		int serverMessagesID = 100;

		int clientMessagesID = 200;
		INSTANCE.registerMessage(clientMessagesID++, CultivationSyncMessage.class, CultivationSyncMessage::encode, CultivationSyncMessage::decode, CultivationSyncMessage::handleMessage, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}
}
