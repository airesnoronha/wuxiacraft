package wuxiacraft.network;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.skill.ISkillAction;
import wuxiacraft.init.WuxiaSkills;

import java.util.UUID;
import java.util.function.Supplier;

public class ActivateActionMessage {

	private String actionName;
	private double energyCost;
	private UUID target;

	private boolean isValid;

	public ActivateActionMessage(String actionName, double energyCost, UUID target) {
		this.actionName = actionName;
		this.energyCost = energyCost;
		this.target = target;
		this.isValid = true;
	}

	public ActivateActionMessage() {
		this.isValid = false;
	}


	public void encode(PacketBuffer buf) {
		buf.writeString(this.actionName,200);
		buf.writeDouble(this.energyCost);
		buf.writeUniqueId(target);
	}

	public static ActivateActionMessage decode(PacketBuffer buf) {
		ActivateActionMessage message = new ActivateActionMessage();
		message.actionName = buf.readString(200);
		message.energyCost = buf.readDouble();
		message.target = buf.readUniqueId();
		message.isValid = true;
		return message;
	}

	public static LivingEntity targeted;

	public static void handleMessage(ActivateActionMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		ctx.setPacketHandled(true);
		if (!message.isValid) return;
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();

		if (sideReceived.isServer()) {
			ctx.enqueueWork(() -> {
				ISkillAction skill = WuxiaSkills.getActionByName(message.actionName);
				ServerPlayerEntity actor = ctx.getSender();
				if (skill != null && actor != null) {
					targeted = (LivingEntity) actor.getServerWorld().getEntityByUuid(message.target);
					skill.activate(actor);
					Cultivation.get(actor).getStatsBySystem(CultivationLevel.System.ESSENCE).addEnergy(-message.energyCost);
					targeted = null;
				}
			});
		}
	}
}
