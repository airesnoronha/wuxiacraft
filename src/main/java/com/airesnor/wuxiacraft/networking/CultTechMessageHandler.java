package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CultTechMessageHandler implements IMessageHandler {
	@Override
	public IMessage onMessage(IMessage message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			handleMessageClient(message, ctx);
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	private IMessage handleMessageClient(IMessage message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ICultTech cultTech = player.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
			if (cultTech != null) {
				CultTechMessage ctmessage = (CultTechMessage) message;
				cultTech.getKnownTechniques().clear();
				for (KnownTechnique t : ctmessage.cultTech.getKnownTechniques()) {
					cultTech.addTechnique(t.getTechnique(), t.getProgress());
				}
			}
		});
		return null;
	}
}
