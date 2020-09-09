package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.aura.AuraCap;
import com.airesnor.wuxiacraft.aura.IAuraCap;
import com.airesnor.wuxiacraft.capabilities.AuraCapProvider;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class RespondAuraForOtherPlayerMessage implements IMessage {

	public UUID target;
	public IAuraCap auraCap;

	public RespondAuraForOtherPlayerMessage() {
		this.auraCap = new AuraCap();
	}

	public RespondAuraForOtherPlayerMessage(UUID target, IAuraCap auraCap) {
		this.target = target;
		this.auraCap = auraCap;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		this.target = pb.readUniqueId();
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		//noinspection ConstantConditions
		AuraCapProvider.AURA_CAPABILITY.getStorage().readNBT(AuraCapProvider.AURA_CAPABILITY, this.auraCap, null, tag);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		pb.writeUniqueId(target);
		//noinspection ConstantConditions
		NBTTagCompound tag = (NBTTagCompound) AuraCapProvider.AURA_CAPABILITY.getStorage().writeNBT(AuraCapProvider.AURA_CAPABILITY, this.auraCap, null);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class Handler implements IMessageHandler<RespondAuraForOtherPlayerMessage, IMessage> {
		@Override
		public IMessage onMessage(RespondAuraForOtherPlayerMessage message, MessageContext ctx) {
			if (ctx.side.isClient()) {
				this.handleClientMessage(message);
			}
			return null;
		}

		@SideOnly(Side.CLIENT)
		public void handleClientMessage(RespondAuraForOtherPlayerMessage message) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				if(Minecraft.getMinecraft().world.getPlayerEntityByUUID(message.target) != null) {
					IAuraCap auraCap = CultivationUtils.getAuraFromEntity(Minecraft.getMinecraft().world.getPlayerEntityByUUID(message.target));
					auraCap.copyFrom(message.auraCap);
				}
			});
		}
	}
}
