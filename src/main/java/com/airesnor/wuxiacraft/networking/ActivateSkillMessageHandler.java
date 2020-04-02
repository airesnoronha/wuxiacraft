package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ActivateSkillMessageHandler implements IMessageHandler<ActivateSkillMessage, IMessage> {

	@Override
	public IMessage onMessage(ActivateSkillMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				EntityPlayer player = ctx.getServerHandler().player;
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
				Skill selectedSkill = skillCap.getSelectedSkills().get(skillCap.getActiveSkill());
				if (selectedSkill != null) {
					if (selectedSkill.activate(player)) {
						if (!player.isCreative())
							cultivation.remEnergy(selectedSkill.getCost());
						skillCap.stepCooldown(selectedSkill.getCooldown());
					}
				}
			});
		}
		return null;
	}
}
