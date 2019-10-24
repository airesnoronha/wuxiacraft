package com.airesnor.wuxiacraft.networking;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SpawnParticleMessageHandler implements IMessageHandler {
    @Override
    public IMessage onMessage(IMessage message, MessageContext ctx) {
        if(ctx.side == Side.CLIENT) {
            if(message instanceof SpawnParticleMessage) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    SpawnParticleMessage spm = (SpawnParticleMessage)message;
                    Minecraft.getMinecraft().world.spawnParticle(spm.getParticleType(),
                            spm.isIgnoreRange(),
                            spm.getPosX(), spm.getPosY(), spm.getPosZ(),
                            spm.getMotionX(), spm.getMotionY(), spm.getMotionZ(),
                            spm.getArguments());
                });
            }
        }
        return null;
    }
}
