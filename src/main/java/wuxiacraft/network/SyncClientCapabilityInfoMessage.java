package wuxiacraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncClientCapabilityInfoMessage {

	private UUID targetPlayer;
	private boolean isMeditating = false;
	private boolean isExercising = false;

	private boolean isValid = false;

	public SyncClientCapabilityInfoMessage(UUID targetPlayer, boolean isMeditating, boolean isExercising) {
		this.targetPlayer = targetPlayer;
		this.isMeditating = isMeditating;
		this.isExercising = isExercising;
		this.isValid = true;
	}

	public SyncClientCapabilityInfoMessage() {
	}

	public void encode(PacketBuffer buffer) {
		if(this.isValid) {
			buffer.writeUniqueId(this.targetPlayer);
			buffer.writeBoolean(this.isMeditating);
			buffer.writeBoolean(this.isExercising);
		}
	}

	public static SyncClientCapabilityInfoMessage decode(PacketBuffer buffer) {
		SyncClientCapabilityInfoMessage message = new SyncClientCapabilityInfoMessage();
		message.targetPlayer = buffer.readUniqueId();
		message.isMeditating = buffer.readBoolean();
		message.isExercising = buffer.readBoolean();
		message.isValid = true;
		return message;
	}

	public static void HandleMessage(SyncClientCapabilityInfoMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);
		if (sideReceived.isClient()) {
			handleClientSide(message, ctx);
		}
		if (sideReceived.isServer()) {
			handleServerSide(message, ctx);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void handleClientSide(SyncClientCapabilityInfoMessage message, NetworkEvent.Context ctx) {
		assert Minecraft.getInstance().player != null;
		ctx.enqueueWork(() -> {
			ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
			cultivation.setExercising(message.isExercising);
			cultivation.setMeditating(message.isMeditating);
		});
	}

	@OnlyIn(Dist.DEDICATED_SERVER)
	public static void handleServerSide(SyncClientCapabilityInfoMessage message, NetworkEvent.Context ctx) {

	}
}
