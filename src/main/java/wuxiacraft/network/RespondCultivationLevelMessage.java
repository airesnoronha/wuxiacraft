package wuxiacraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.capabilities.CultivationProvider;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;

import java.util.UUID;
import java.util.function.Supplier;

public class RespondCultivationLevelMessage {

	private UUID playerId;

	private ICultivation playerCultivation;

	public RespondCultivationLevelMessage(UUID playerId, ICultivation playerCultivation) {
		this.playerId = playerId;
		this.playerCultivation = playerCultivation;
	}

	public RespondCultivationLevelMessage() {
	}

	public void encode(PacketBuffer buf) {
		buf.writeUniqueId(this.playerId);
		CompoundNBT tag = (CompoundNBT) CultivationProvider.CULTIVATION_PROVIDER.writeNBT(this.playerCultivation, null);
		buf.writeCompoundTag(tag);
	}

	public static RespondCultivationLevelMessage decode(PacketBuffer buf) {
		RespondCultivationLevelMessage message = new RespondCultivationLevelMessage();
		message.playerId = buf.readUniqueId();
		CultivationProvider.CULTIVATION_PROVIDER.readNBT(message.playerCultivation, null, buf.readCompoundTag());
		return message;
	}

	public static void handleMessage(RespondCultivationLevelMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);

		if (sideReceived.isClient()) {
			handleClientSide(message, ctx);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void handleClientSide(RespondCultivationLevelMessage message, NetworkEvent.Context ctx) {
		assert Minecraft.getInstance().player != null;
		assert Minecraft.getInstance().player.worldClient != null;
		ctx.enqueueWork(() -> {
			ClientWorld world = Minecraft.getInstance().player.worldClient;
			PlayerEntity target = world.getPlayerByUuid(message.playerId);
			if (target != null) {
				ICultivation cultivation = Cultivation.get(target);
				cultivation.copyFrom(message.playerCultivation);
			}
		});
	}
}
