package wuxiacraft.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import wuxiacraft.WuxiaCraft;

import java.util.Optional;

public class WuxiaPacketHandler {

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(WuxiaCraft.MOD_ID, "network"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	//register the messages from this instance
	public static void registerMessages() {
		int serverMessagesID = 100;
		INSTANCE.registerMessage(serverMessagesID++, EnergyMessage.class, EnergyMessage::encode, EnergyMessage::decode, EnergyMessage::HandleMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(serverMessagesID++, ActivateActionMessage.class, ActivateActionMessage::encode, ActivateActionMessage::decode, ActivateActionMessage::handleMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(serverMessagesID++, ActivateSkillMessage.class, ActivateSkillMessage::encode, ActivateSkillMessage::decode, ActivateSkillMessage::handleMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(serverMessagesID++, OpenMeditationScreenMessage.class, OpenMeditationScreenMessage::encode, OpenMeditationScreenMessage::decode, OpenMeditationScreenMessage::handleMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(serverMessagesID++, AddCultivationToPlayerMessage.class, AddCultivationToPlayerMessage::encode, AddCultivationToPlayerMessage::decode, AddCultivationToPlayerMessage::handleMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));

		int clientMessagesID = 200;
		INSTANCE.registerMessage(clientMessagesID++, CultivationSyncMessage.class, CultivationSyncMessage::encode, CultivationSyncMessage::decode, CultivationSyncMessage::HandleMessage, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}

}
