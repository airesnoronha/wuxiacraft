package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.cultivation.techniques.*;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class CultTechMessage implements IMessage {

	public final ICultTech cultTech;

	public CultTechMessage(ICultTech cultTech) {
		this.cultTech = cultTech;
	}

	@SuppressWarnings("unused")
	public CultTechMessage() {
		this.cultTech = new CultTech();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		//noinspection ConstantConditions
		CultTechProvider.CULT_TECH_CAPABILITY.getStorage()
				.readNBT(CultTechProvider.CULT_TECH_CAPABILITY, this.cultTech, null, tag);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//noinspection ConstantConditions
		NBTTagCompound tag = (NBTTagCompound) CultTechProvider.CULT_TECH_CAPABILITY.getStorage()
				.writeNBT(CultTechProvider.CULT_TECH_CAPABILITY, this.cultTech, null);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class Handler implements IMessageHandler<CultTechMessage, IMessage> {
		@Override
		public IMessage onMessage(CultTechMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				return handleMessageClient(message, ctx);
			}
			return null;
		}

		@SuppressWarnings("unused")
		@SideOnly(Side.CLIENT)
		private IMessage handleMessageClient(CultTechMessage message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				EntityPlayer player = Minecraft.getMinecraft().player;
				ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
				cultTech.copyFrom(message.cultTech);
			});
			return null;
		}
	}

}
