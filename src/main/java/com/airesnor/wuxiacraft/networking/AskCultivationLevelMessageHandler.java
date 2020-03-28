package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.SkillUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;

public class AskCultivationLevelMessageHandler implements IMessageHandler<AskCultivationLevelMessage, RespondCultivationLevelMessage> {
	@Override
	public RespondCultivationLevelMessage onMessage(AskCultivationLevelMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			List<Entity> entities = player.getServerWorld().getEntitiesWithinAABB(EntityPlayer.class, player.getEntityBoundingBox().grow(64, 32, 64));
			for (Entity entity : entities) {
				if (entity instanceof EntityPlayer) {
					if (entity.equals(player)) continue;
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity((EntityLivingBase) entity);
					CultivationLevel level = cultivation.getCurrentLevel();
					int subLevel = cultivation.getCurrentSubLevel();
					if (level.greaterThan(message.askerLevel)) {
						level = message.askerLevel.getNextLevel();
						subLevel = -1;
					}
					RespondCultivationLevelMessage rlcm = new RespondCultivationLevelMessage(level, subLevel, entity.getName());
					NetworkWrapper.INSTANCE.sendTo(rlcm, player);
				}
			}
		}
		return null;
	}
}