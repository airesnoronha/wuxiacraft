package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.BarrierProvider;
import com.airesnor.wuxiacraft.cultivation.Barrier;
import com.airesnor.wuxiacraft.cultivation.IBarrier;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class BarrierMessage implements IMessage {

    public IBarrier barrier;
    public UUID senderUUID;

    public BarrierMessage(IBarrier barrier, UUID senderUUID) {
        this.barrier = barrier;
        this.senderUUID = senderUUID;
    }

    public BarrierMessage() {
        this.barrier = new Barrier();
        this.senderUUID = null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        BarrierProvider.BARRIER_CAPABILITY.getStorage().readNBT(BarrierProvider.BARRIER_CAPABILITY, this.barrier, null, tag);
        this.senderUUID = packetBuffer.readUniqueId();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        NBTTagCompound tag = (NBTTagCompound) BarrierProvider.BARRIER_CAPABILITY.getStorage().writeNBT(BarrierProvider.BARRIER_CAPABILITY, this.barrier, null);
        ByteBufUtils.writeTag(buf, tag);
        packetBuffer.writeUniqueId(this.senderUUID);
    }

    public static class Handler implements IMessageHandler<BarrierMessage, IMessage> {

        @Override
        public IMessage onMessage(BarrierMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                handleClientMessage(message);
            } else if (ctx.side == Side.SERVER) {
                final WorldServer world = ctx.getServerHandler().player.getServerWorld();
                world.addScheduledTask(() -> {
                    EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
                    if (player != null) {
                        IBarrier barrier = CultivationUtils.getBarrierFromEntity(player);
                        barrier.copyFrom(message.barrier);
                    }
                });
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClientMessage(BarrierMessage message) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                EntityPlayer player = Minecraft.getMinecraft().player;
                IBarrier barrier = CultivationUtils.getBarrierFromEntity(player);
                barrier.copyFrom(message.barrier);
            });
        }
    }

}
