package wuxiacraft.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;

import java.util.function.Supplier;

public class CultivationSyncMessage {

	private CompoundTag instance;

	private boolean valid;

	public CultivationSyncMessage(ICultivation instance) {
		this.instance = instance.serialize();
		this.valid = true;
	}
	public CultivationSyncMessage(CompoundTag instance) {
		this.instance = instance;
		this.valid = true;
	}

	public CultivationSyncMessage() {
		this.instance = null;
		this.valid = false;
	}

	public static void encode(CultivationSyncMessage msg, FriendlyByteBuf buf) {
		if(msg.valid)
			buf.writeNbt(msg.instance);
	}

	public static CultivationSyncMessage decode(FriendlyByteBuf buf) {
		return new CultivationSyncMessage(buf.readAnySizeNbt());
	}

	public static void handleMessage(CultivationSyncMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
		var ctx = ctxSupplier.get();
		ctx.setPacketHandled(true);
		if(ctx.getDirection().getReceptionSide().isClient()) {
			handleClientMessage(msg, ctx);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void handleClientMessage(CultivationSyncMessage msg, NetworkEvent.Context ctx) {
		assert Minecraft.getInstance().player != null;
		ctx.enqueueWork(() -> {
			Cultivation.get(Minecraft.getInstance().player).deserialize(msg.instance);
		});
	}
}
