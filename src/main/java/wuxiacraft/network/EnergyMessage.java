package wuxiacraft.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.util.CultivationUtils;

import java.util.UUID;
import java.util.function.Supplier;

public class EnergyMessage {

	private double amount;
	private int operation;
	private UUID target;

	private boolean isValid;

	public EnergyMessage(double amount, int operation, UUID target) {
		this.amount = amount;
		this.operation = operation;
		this.target = target;
		this.isValid = true;
	}

	public EnergyMessage() {
		this.isValid = false;
	}

	public void encode(PacketBuffer buf) {
		if (!this.isValid) return;
		buf.writeDouble(this.amount);
		buf.writeInt(this.operation);
		buf.writeUniqueId(this.target);
	}

	public static EnergyMessage decode(PacketBuffer buf) {
		EnergyMessage message = new EnergyMessage();
		try {
			message.amount = buf.readDouble();
			message.operation = buf.readInt();
			message.target = buf.readUniqueId();
			message.isValid = true;
		} catch (Exception e) {
			WuxiaCraft.LOGGER.error("Couldn't read energy message due to exception: " + e.getMessage());
		}
		return message;
	}

	public static void HandleMessage(EnergyMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);

		if (sideReceived.isServer()) {
			ctx.enqueueWork(() -> {
				if (ctx.getSender() == null) return;
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(ctx.getSender().world.getPlayerByUuid(message.target));
				switch (message.operation) {
					case 0: // add
						cultivation.addEnergy(message.amount);
						break;
					case 1: // rem
						cultivation.addEnergy(-message.amount);
						break;
					case 2: // set
						cultivation.setEnergy(message.amount);
						break;
					default:
						cultivation.setEnergy(0);
						break;
				}
			});
		}

	}

}
