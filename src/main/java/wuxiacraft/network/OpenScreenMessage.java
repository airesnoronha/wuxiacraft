package wuxiacraft.network;

import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import wuxiacraft.container.IntrospectionContainer;
import wuxiacraft.container.MeditationContainer;

import java.util.function.Supplier;

public class OpenScreenMessage {

	public enum TargetGui {
		MEDITATION,
		INTROSPECTION,
		SKILL
	}

	public TargetGui targetGui = TargetGui.MEDITATION;

	public OpenScreenMessage(TargetGui targetGui) {
		this.targetGui = targetGui;
	}

	public OpenScreenMessage() {
	}

	public void encode(PacketBuffer buf) {
		buf.writeEnumValue(this.targetGui);
	}

	public static OpenScreenMessage decode(PacketBuffer buf) {
		return new OpenScreenMessage(buf.readEnumValue(TargetGui.class));
	}

	public static void handleMessage(OpenScreenMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		if (sideReceived.isServer()) {
			ctx.enqueueWork(() -> {
				if (ctx.getSender() != null) {
					switch (message.targetGui) {
						case SKILL:
						case INTROSPECTION:
							NetworkHooks.openGui(ctx.getSender(), new SimpleNamedContainerProvider((id, inv, player) -> new IntrospectionContainer(id), new StringTextComponent("")));
							break;
						case MEDITATION:
							NetworkHooks.openGui(ctx.getSender(), new SimpleNamedContainerProvider((id, inv, player) -> new MeditationContainer(id), new StringTextComponent("")));
							break;
					}
				}
			});
		}
	}
}
