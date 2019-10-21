package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CastSkillMessageHandler implements IMessageHandler {

    @Override
    public IMessage onMessage(IMessage message, MessageContext ctx) {
        if(ctx.side == Side.SERVER) {
            if(message instanceof CastSkillMessage) {
                ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                    ISkillCap skillCap = ctx.getServerHandler().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
                    CastSkillMessage csm = (CastSkillMessage)message;
                    if(csm.casting)
                        skillCap.setCasting(true);
                    else {
                        skillCap.setDoneCasting(true);
                        skillCap.setCasting(false);
                    }
                });
            }
        }
        return null;
    }
}
