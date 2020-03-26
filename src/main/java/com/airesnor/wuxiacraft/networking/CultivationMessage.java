package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CultivationMessage implements IMessage {

	public CultivationLevel messageLevel;
	public int messageSubLevel;
	public float messageProgress;
	public float messageEnergy;
	public int pelletCooldown;
	public boolean suppress;

	public CultivationMessage(CultivationLevel messageLevel, int messageSubLevel, float messageProgress, float messageEnergy, int pelletCooldown, boolean suppress) {
		this.messageLevel = messageLevel;
		this.messageSubLevel = messageSubLevel;
		this.messageProgress = messageProgress;
		this.messageEnergy = messageEnergy;
		this.pelletCooldown = pelletCooldown;
		this.suppress = suppress;
	}

	public CultivationMessage() {
		this.messageLevel = CultivationLevel.BODY_REFINEMENT;
		this.messageSubLevel = 0;
		this.messageProgress = 0;
		this.messageEnergy = 0;
		this.pelletCooldown = 0;
		this.suppress = false;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.suppress = buf.readBoolean();
		this.messageSubLevel = buf.readInt();
		this.messageProgress = buf.readFloat();
		this.messageEnergy = buf.readFloat();
		this.pelletCooldown = buf.readInt();
		int length = buf.readInt();
		byte[] bytes = new byte[30];
		buf.readBytes(bytes, 0, length);
		bytes[length] = '\0';
		String cultlevelname = new String(bytes, 0, length);
		this.messageLevel = CultivationLevel.valueOf(cultlevelname);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		byte[] bytes = messageLevel.name().getBytes();
		buf.writeBoolean(this.suppress);
		buf.writeInt(this.messageSubLevel);
		buf.writeFloat(this.messageProgress);
		buf.writeFloat(this.messageEnergy);
		buf.writeInt(this.pelletCooldown);
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}
}
