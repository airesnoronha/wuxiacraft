package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ActivateSkillMessageHandler implements IMessageHandler {

    @Override
    public IMessage onMessage(IMessage message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            if(message instanceof ActivateSkillMessage) {
            ActivateSkillMessage asm = (ActivateSkillMessage)message;
            final Skill skill = asm.SkillIndex > 0 ? Skills.SKILLS.get(asm.SkillIndex) : Skills.SKILLS.get(0);
                ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                    skill.activate(ctx.getServerHandler().player);
                });
            }
        }
        return null;
    }
}
