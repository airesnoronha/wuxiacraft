package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CastSkillMessage implements IMessage {

    public boolean casting;

    public CastSkillMessage() {
        this.casting = false;
    }

    public CastSkillMessage(boolean casting) {
        this.casting = casting;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.casting = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.casting);
    }
}
