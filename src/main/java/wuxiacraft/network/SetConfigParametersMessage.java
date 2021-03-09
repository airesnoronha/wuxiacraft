package wuxiacraft.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;

import java.util.function.Supplier;

public class SetConfigParametersMessage {

	private double movementSpeed;
	private double breakSpeed;
	private double jumpSpeed;
	private double stepHeight;

	public SetConfigParametersMessage(double movementSpeed, double breakSpeed, double jumpSpeed, double stepHeight) {
		this.movementSpeed = movementSpeed;
		this.breakSpeed = breakSpeed;
		this.jumpSpeed = jumpSpeed;
		this.stepHeight = stepHeight;
	}

	public SetConfigParametersMessage() {
	}

	public void encode(PacketBuffer buf) {
		buf.writeDouble(movementSpeed);
		buf.writeDouble(breakSpeed);
		buf.writeDouble(jumpSpeed);
		buf.writeDouble(stepHeight);
	}

	public static SetConfigParametersMessage decode(PacketBuffer buf) {
		SetConfigParametersMessage message = new SetConfigParametersMessage();
		message.movementSpeed = buf.readDouble();
		message.breakSpeed = buf.readDouble();
		message.jumpSpeed = buf.readDouble();
		message.stepHeight = buf.readDouble();
		return message;
	}

	public static void handleMessage(SetConfigParametersMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);

		if (sideReceived.isServer()) {
			ctx.enqueueWork(() -> {
				if (ctx.getSender() == null) return;
				ICultivation cultivation = Cultivation.get(ctx.getSender());
				cultivation.setMovementSpeed(message.movementSpeed);
				cultivation.setBreakSpeed(message.breakSpeed);
				cultivation.setJumpSpeed(message.jumpSpeed);
				cultivation.setStepHeight(message.stepHeight);
			});
		}
	}
}
