package wuxiacraft.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;

import java.util.UUID;
import java.util.function.Supplier;

public class EnergyMessage {

	private double amount;
	private int operation;
	private CultivationLevel.System system;

	private boolean isValid;

	public EnergyMessage(double amount, CultivationLevel.System system, int operation) {
		this.amount = amount;
		this.operation = operation;
		this.system = system;
		this.isValid = true;
	}

	public EnergyMessage() {
		this.isValid = false;
	}

	public void encode(PacketBuffer buf) {
		if (!this.isValid) return;
		buf.writeDouble(this.amount);
		buf.writeInt(this.operation);
		buf.writeEnumValue(system);
	}

	public static EnergyMessage decode(PacketBuffer buf) {
		EnergyMessage message = new EnergyMessage();
		try {
			message.amount = buf.readDouble();
			message.operation = buf.readInt();
			message.system = buf.readEnumValue(CultivationLevel.System.class);
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
				//I hope it ain't screwed as it was on 1.12.2
				ICultivation cultivation = Cultivation.get(ctx.getSender());
				switch (message.operation) {
					case 0: // add
						cultivation.getStatsBySystem(message.system).addEnergy(message.amount);
						break;
					case 1: // rem
						cultivation.getStatsBySystem(message.system).addEnergy(-message.amount);
						break;
					case 2: // set
						cultivation.getStatsBySystem(message.system).setEnergy(message.amount);
						break;
					default:
						cultivation.getStatsBySystem(message.system).setEnergy(0);
						break;
				}
			});
		}

	}

}
