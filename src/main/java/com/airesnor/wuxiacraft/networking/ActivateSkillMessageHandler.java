package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.handlers.KeyHandler;
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
                ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                    ActivateSkillMessage asm = (ActivateSkillMessage)message;
                    Skill skill = asm.SkillIndex > 0 ? Skills.SKILLS.get(asm.SkillIndex) : Skills.SKILLS.get(0);
                    ISkillCap skillCap = ctx.getServerHandler().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
                    skillCap.resetCastProgress();
                    skillCap.stepCastProgress(asm.castProgress);
                    KeyHandler.activateSkill(skillCap, skill, ctx.getServerHandler().player);
                });
            }
        }
        return null;
    }
}
