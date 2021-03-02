package wuxiacraft.network;

import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import wuxiacraft.container.MeditationContainer;

import java.util.function.Supplier;

public class OpenMeditationScreenMessage {

	public OpenMeditationScreenMessage() {
	}

	public void encode(PacketBuffer buf) {

	}

	public static OpenMeditationScreenMessage decode(PacketBuffer buf) {
		return new OpenMeditationScreenMessage();
	}

	public static void handleMessage(OpenMeditationScreenMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		if (sideReceived.isServer()) {
			ctx.enqueueWork(() -> {
				if (ctx.getSender() != null) {
					NetworkHooks.openGui(ctx.getSender(), new SimpleNamedContainerProvider((id, inv, player) -> new MeditationContainer(id), new StringTextComponent("")));
				}
			});
		}
	}
}
