package wuxiacraft.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.SystemStats;

import java.util.function.Supplier;

public class AttemptBreakthroughMessage {

	CultivationLevel.System system;

	public AttemptBreakthroughMessage(CultivationLevel.System system) {
		this.system = system;
	}

	public AttemptBreakthroughMessage() {
	}

	public void encode(PacketBuffer buf) {
		buf.writeEnumValue(this.system);
	}

	public static AttemptBreakthroughMessage decode(PacketBuffer buf) {
		return new AttemptBreakthroughMessage(buf.readEnumValue(CultivationLevel.System.class));
	}

	public static void handleMessage(AttemptBreakthroughMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);

		if (sideReceived.isServer()) {
			ctx.enqueueWork(() -> {
				if (ctx.getSender() == null) return;
				ICultivation cultivation = Cultivation.get(ctx.getSender());
				if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() == CultivationLevel.DEFAULT_ESSENCE_LEVEL) {
					cultivation.advanceRank(message.system);
				} else {
					SystemStats stats = cultivation.getStatsBySystem(message.system);
					double random = stats.getLevel().getModifierBySubLevel(stats.getSubLevel()) * (0.6f + ctx.getSender().getRNG().nextDouble() * 0.5f);
					if (stats.getBase() > random) {
						cultivation.advanceRank(message.system);
					}
				}
				WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(ctx::getSender), new CultivationSyncMessage(cultivation));
			});
		}
	}
}
