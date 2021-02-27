package wuxiacraft.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.init.WuxiaSkills;

import java.util.function.Supplier;

public class ActivateSkillMessage {

	private String skillName;
	private double energyCost;

	private boolean isValid;

	public ActivateSkillMessage(String skillName, double energyCost) {
		this.skillName = skillName;
		this.energyCost = energyCost;
		this.isValid = true;
	}

	public ActivateSkillMessage() {
		this.isValid =false;
	}

	public void encode(PacketBuffer buf) {
		buf.writeString(this.skillName);
		buf.writeDouble(this.energyCost);
	}

	public static ActivateSkillMessage decode(PacketBuffer buf) {
		ActivateSkillMessage message = new ActivateSkillMessage();
		message.skillName = buf.readString();
		message.energyCost = buf.readDouble();
		message.isValid = true;
		return message;
	}

	public static void handleMessage(ActivateSkillMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		ctx.setPacketHandled(true);
		if(!message.isValid) return;
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();

		if(sideReceived.isServer()) {
			ctx.enqueueWork(() -> {
				Skill skill = WuxiaSkills.getSkillByName(message.skillName);
				ServerPlayerEntity actor = ctx.getSender();
				if(skill != null && actor != null) {
					skill.activate(actor);
					Cultivation.get(actor).getStatsBySystem(CultivationLevel.System.ESSENCE).addEnergy(-message.energyCost);
				}
			});
		}
	}
}
