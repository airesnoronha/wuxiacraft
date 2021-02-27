package wuxiacraft.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.skill.ISkillAction;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.init.WuxiaSkills;

import java.util.function.Supplier;

public class ActivateActionMessage {

	private String actionName;
	private double energyCost;

	private boolean isValid;

	public ActivateActionMessage(String actionName, double energyCost) {
		this.actionName = actionName;
		this.energyCost = energyCost;
		this.isValid = true;
	}

	public ActivateActionMessage() {
		this.isValid = false;
	}


	public void encode(PacketBuffer buf) {
		buf.writeString(this.actionName);
		buf.writeDouble(this.energyCost);
	}

	public static ActivateActionMessage decode(PacketBuffer buf) {
		ActivateActionMessage message = new ActivateActionMessage();
		message.actionName = buf.readString();
		message.energyCost = buf.readDouble();
		message.isValid = true;
		return message;
	}

	public static void handleMessage(ActivateActionMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		ctx.setPacketHandled(true);
		if(!message.isValid) return;
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();

		if(sideReceived.isServer()) {
			ctx.enqueueWork(() -> {
				ISkillAction skill = WuxiaSkills.getActionByName(message.actionName);
				ServerPlayerEntity actor = ctx.getSender();
				if(skill != null && actor != null) {
					skill.activate(actor);
					Cultivation.get(actor).getStatsBySystem(CultivationLevel.System.ESSENCE).addEnergy(-message.energyCost);
				}
			});
		}
	}
}
