package wuxiacraft.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.SystemStats;

import java.util.function.Supplier;

public class ExerciseMessage {

	private double energy;

	public ExerciseMessage(double energy) {
		this.energy = energy;
	}

	public ExerciseMessage() {
		this.energy = 0;
	}

	public void encode(PacketBuffer buf) {
		buf.writeDouble(this.energy);
	}

	public static ExerciseMessage decode(PacketBuffer buf) {
		return new ExerciseMessage(buf.readDouble());
	}


	public static void handleMessage(ExerciseMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		ctx.setPacketHandled(true);
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();

		if (sideReceived.isServer()) {
			ctx.enqueueWork(() -> {
				if (ctx.getSender() == null) return;
				ICultivation cultivation = Cultivation.get(ctx.getSender());
				double energy_conversion = 1;
				SystemStats bodyStats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
				SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
				bodyStats.addEnergy(-message.energy);
				essenceStats.addEnergy(message.energy * energy_conversion);
			});
		}
	}
}
