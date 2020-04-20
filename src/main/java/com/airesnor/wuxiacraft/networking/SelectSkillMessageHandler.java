package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SelectSkillMessageHandler implements IMessageHandler<SelectSkillMessage, IMessage> {

	@Override
	public IMessage onMessage(SelectSkillMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			final WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() -> {
				EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
				if (player != null) {
					ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
					selectSkill(skillCap, message.selectSkill);
				}
			});
		}
		return null;
	}

	public static void selectSkill(ISkillCap skillCap, int i) {
		skillCap.setActiveSkill(Math.min(skillCap.getSelectedSkills().size() - 1, Math.max(0, i)));
		if (skillCap.getSelectedSkills().size() == 0) {
			skillCap.setActiveSkill(-1);
		}
	}
}
