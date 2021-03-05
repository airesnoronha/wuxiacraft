package wuxiacraft.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.SystemStats;

import java.util.function.Supplier;

public class AddCultivationToPlayerMessage {

	private CultivationLevel.System system; //the cultivate system
	/**
	 * the amount of energy to be converted to cultivation
	 * this way, clients won't tell how much cultivation base will be added
	 * but rather server will know based on the amount of energy
	 * then, if players don't have energy, they won't be able to gain cultivation base
	 * this is a hack client prevention
	 */
	private double energy;
	private boolean special; //adds a boost depending on the minigame rules

	private boolean isValid;

	public AddCultivationToPlayerMessage(CultivationLevel.System system, double energy, boolean special) {
		this.system = system;
		this.energy = energy;
		this.special = special;
		this.isValid = true;
	}

	public AddCultivationToPlayerMessage() {
		this.isValid = false;
	}

	public void encode(PacketBuffer buf) {
		buf.writeEnumValue(this.system);
		buf.writeDouble(this.energy);
		buf.writeBoolean(this.special);
	}

	public static AddCultivationToPlayerMessage decode(PacketBuffer buf) {
		AddCultivationToPlayerMessage message = new AddCultivationToPlayerMessage();
		message.system = buf.readEnumValue(CultivationLevel.System.class);
		message.energy = buf.readDouble();
		message.special = buf.readBoolean();
		message.isValid = true;
		return message;
	}

	public static void handleMessage(AddCultivationToPlayerMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		ctx.setPacketHandled(true);
		if (!message.isValid) return;
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();

		if (sideReceived.isServer()) {
			ctx.enqueueWork(() -> {
				if (ctx.getSender() == null) return;
				ICultivation cultivation = Cultivation.get(ctx.getSender());
				SystemStats stats = cultivation.getStatsBySystem(message.system);
				double energy_conversion = 1;
				if (stats.getEnergy() >= message.energy) {
					double added_base = message.energy * energy_conversion * (message.special ? 1.1 : 1);
					stats.addBase(added_base);
					stats.addEnergy(-message.energy);
					if (message.system == CultivationLevel.System.ESSENCE) {
						cultivation.getStatsBySystem(CultivationLevel.System.DIVINE).addEnergy(-message.energy * 0.1);
						//i don't care if mental energy is used all over, probably i'll make then sleep
					}
					//i won't sync because probably in the client math has been done equally
					//but it'll sync eventually, if there is sync problems i'll investigate
				}
			});
		}
	}
}
