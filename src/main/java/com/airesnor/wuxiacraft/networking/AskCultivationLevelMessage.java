package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class AskCultivationLevelMessage implements IMessage {

    public CultivationLevel askerLevel;
    public int askerSubLevel;
    public String askerName;

    public AskCultivationLevelMessage(CultivationLevel askerLevel, int askerSubLevel, String askerName) {
        this.askerLevel = askerLevel;
        this.askerSubLevel = askerSubLevel;
        this.askerName = askerName;
    }

    public AskCultivationLevelMessage() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.askerSubLevel = buf.readInt();
        int length = buf.readInt();
        byte[] bytes = new byte[30];
        buf.readBytes(bytes, 0,length);
        bytes[length] = '\0';
        String cultlevelname = new String(bytes,0, length);
        this.askerLevel = CultivationLevel.valueOf(cultlevelname);
        length = buf.readInt();
        bytes = new byte[50];
        buf.readBytes(bytes, 0 ,length);
        bytes[length] = '\0';
        this.askerName = new String(bytes, 0, length);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.askerSubLevel);
        byte[] bytes = this.askerLevel.name().getBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        bytes = this.askerName.getBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

}
