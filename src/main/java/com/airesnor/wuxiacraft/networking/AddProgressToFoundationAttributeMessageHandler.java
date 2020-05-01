package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class AddProgressToFoundationAttributeMessageHandler implements IMessageHandler<AddProgressToFoundationAttributeMessage, IMessage> {

	@Override
	public IMessage onMessage(AddProgressToFoundationAttributeMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			final WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() -> {
				double amount = message.amount;
				EntityPlayer player = world.getPlayerEntityByUUID(message.sender);
				if(player!= null ) {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
					IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
					if(cultivation.getCurrentProgress() < amount) {
						amount = cultivation.getCurrentProgress();
					}
					cultivation.addProgress(-amount, false);
					int attribute = MathUtils.clamp(message.attribute, 0, 5);
					switch (attribute) {
						case 0:
							foundation.addAgilityProgress(amount);
							break;
						case 1:
							foundation.addConstitutionProgress(amount);
							break;
						case 2:
							foundation.addDexterityProgress(amount);
							break;
						case 3:
							foundation.addResistanceProgress(amount);
							break;
						case 4:
							foundation.addSpiritProgress(amount);
							break;
						case 5:
							foundation.addStrengthProgress(amount);
							break;
					}
					foundation.keepMaxLevel(cultivation);
				}
			});
		}
 		return null;
	}
}
