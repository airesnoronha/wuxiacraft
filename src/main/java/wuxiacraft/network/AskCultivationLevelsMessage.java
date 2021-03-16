package wuxiacraft.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import wuxiacraft.cultivation.Cultivation;

import java.util.List;
import java.util.function.Supplier;

public class AskCultivationLevelsMessage {

	public AskCultivationLevelsMessage() {
	}

	public void encode(PacketBuffer buf) {

	}

	public static AskCultivationLevelsMessage decode(PacketBuffer buf) {
		return new AskCultivationLevelsMessage();
	}

	public static void handleMessage(AskCultivationLevelsMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);

		if (sideReceived.isServer()) {
			ctx.enqueueWork(() -> {
				if (ctx.getSender() == null) return;
				ServerWorld world = ctx.getSender().getServerWorld();
				AxisAlignedBB aabb = ctx.getSender().getBoundingBox();
				aabb.grow(128);
				List<Entity> targets = world.getEntities(EntityType.PLAYER, target -> aabb.contains(target.getPositionVec()));
				for (Entity target : targets) {
					if (target instanceof LivingEntity) {
						WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(ctx::getSender),
								new RespondCultivationLevelMessage(target.getUniqueID(), Cultivation.get((LivingEntity) target)));
					}
				}
			});
		}
	}
}
