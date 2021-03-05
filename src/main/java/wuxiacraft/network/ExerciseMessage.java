package wuxiacraft.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.cultivation.*;
import wuxiacraft.cultivation.technique.BodyTechnique;

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
				KnownTechnique bodyKT = cultivation.getTechniqueBySystem(CultivationLevel.System.BODY);
				if(bodyKT != null) {
					BodyTechnique bodyTech = (BodyTechnique) bodyKT.getTechnique();
					double energy_conversion = 1 + (bodyTech.getConversionRate() * bodyKT.getReleaseFactor());
					SystemStats bodyStats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
					SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
					bodyStats.addEnergy(-message.energy);
					essenceStats.addEnergy(message.energy * energy_conversion);
				}
			});
		}
	}
}
