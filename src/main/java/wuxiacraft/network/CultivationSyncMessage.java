package wuxiacraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.capabilities.CultivationProvider;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.util.CultivationUtils;

import java.util.UUID;
import java.util.function.Supplier;

public class CultivationSyncMessage {

	private ICultivation cultivation;
	private UUID target;

	private boolean isValid;

	public CultivationSyncMessage(ICultivation cultivation, UUID target) {
		this.cultivation = cultivation;
		this.target = target;
		this.isValid = true;
	}

	public CultivationSyncMessage() {
		this.isValid = false;
	}

	public void encode(PacketBuffer buffer) {
		if (!this.isValid) return;
		CompoundNBT tag = (CompoundNBT) CultivationProvider.CULTIVATION_PROVIDER.writeNBT(this.cultivation, null);
		buffer.writeCompoundTag(tag);
		buffer.writeUniqueId(this.target);
	}

	public static CultivationSyncMessage decode(PacketBuffer buffer) {
		CultivationSyncMessage message = new CultivationSyncMessage();
		CultivationProvider.CULTIVATION_PROVIDER.readNBT(message.cultivation, null, buffer.readCompoundTag());
		message.target = buffer.readUniqueId();
		message.isValid = true;
		return message;
	}

	public static void HandleMessage(CultivationSyncMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);

		if (sideReceived.isClient()) {
			assert Minecraft.getInstance().player != null;
			if (Minecraft.getInstance().player.getUniqueID() == message.target) {
				ctx.enqueueWork(() -> {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(Minecraft.getInstance().player);
					cultivation.copyFrom(message.cultivation);
				});
			}
		}
	}
}
