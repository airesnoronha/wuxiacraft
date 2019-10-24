package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class AskCultivationLevelMessageHandler implements IMessageHandler {
    @Override
    public IMessage onMessage(IMessage message, MessageContext ctx) {
        if(ctx.side == Side.CLIENT) {
            if(message instanceof AskCultivationLevelMessage) {
                AskCultivationLevelMessage aclm = (AskCultivationLevelMessage)message;
                ICultivation cultivation = Minecraft.getMinecraft().player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
                CultivationLevel level = cultivation.getCurrentLevel();
                int subLevel = cultivation.getCurrentSubLevel();
                if(level.greaterThan(aclm.askerLevel)) {
                    level = aclm.askerLevel.getNextLevel();
                    subLevel = -1;
                }
                return new RespondCultivationLevelMessage(level, subLevel, aclm.askerName);
            }
        }
        if(ctx.side == Side.SERVER) {
            if(message instanceof AskCultivationLevelMessage) {
                AskCultivationLevelMessage aclm = (AskCultivationLevelMessage)message;
                EntityPlayerMP player = ctx.getServerHandler().player;
                Skills.sendMessageWithinRange(player.getServerWorld(), player.getPosition(), 1024, aclm);
            }
        }
        return null;
    }
}