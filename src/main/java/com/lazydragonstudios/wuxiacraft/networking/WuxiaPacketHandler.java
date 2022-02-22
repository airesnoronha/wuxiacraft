package com.lazydragonstudios.wuxiacraft.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import com.lazydragonstudios.wuxiacraft.WuxiaCraft;

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
		INSTANCE.registerMessage(serverMessagesID++, OpenScreenMessage.class, OpenScreenMessage::encode, OpenScreenMessage::decode, OpenScreenMessage::handleMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(serverMessagesID++, BroadcastAnimationChangeRequestMessage.class, BroadcastAnimationChangeRequestMessage::encode, BroadcastAnimationChangeRequestMessage::decode, BroadcastAnimationChangeRequestMessage::handleMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(serverMessagesID++, RequestTechniqueDataChange.class, RequestTechniqueDataChange::encode, RequestTechniqueDataChange::decode, RequestTechniqueDataChange::handleMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(serverMessagesID++, RequestRandomTeleportMessage.class, RequestRandomTeleportMessage::encode, RequestRandomTeleportMessage::decode, RequestRandomTeleportMessage::handleMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(serverMessagesID++, RequestSkillSaveMessage.class, RequestSkillSaveMessage::encode, RequestSkillSaveMessage::decode, RequestSkillSaveMessage::handleMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(serverMessagesID++, SkillCastingMessage.class, SkillCastingMessage::encode, SkillCastingMessage::decode, SkillCastingMessage::handleMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));

		int clientMessagesID = 200;
		INSTANCE.registerMessage(clientMessagesID++, CultivationSyncMessage.class, CultivationSyncMessage::encode, CultivationSyncMessage::decode, CultivationSyncMessage::handleMessage, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		INSTANCE.registerMessage(clientMessagesID++, AnimationChangeUpdateMessage.class, AnimationChangeUpdateMessage::encode, AnimationChangeUpdateMessage::decode, AnimationChangeUpdateMessage::handleMessageCommon, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}
}
