package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CultivationMessage implements IMessage {

	public ICultivation cultivation;

	public CultivationMessage(ICultivation cultivation) {
		this.cultivation = cultivation;
	}

	public CultivationMessage() {
		this.cultivation = new Cultivation();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		boolean suppress = buf.readBoolean();
		int messageSubLevel = buf.readInt();
		double messageProgress = buf.readDouble();
		double messageEnergy = buf.readDouble();
		int pillCooldown = buf.readInt();
		int length = buf.readInt();
		byte[] bytes = new byte[length+1];
		buf.readBytes(bytes, 0, length);
		bytes[length] = '\0';
		String cultlevelname = new String(bytes, 0, length);
		this.cultivation.setCurrentLevel(CultivationLevel.REGISTERED_LEVELS.get(cultlevelname));
		this.cultivation.setCurrentSubLevel(messageSubLevel);
		this.cultivation.setProgress(messageProgress);
		this.cultivation.setEnergy(messageEnergy);
		this.cultivation.setSuppress(suppress);
		cultivation.setPillCooldown(pillCooldown);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		byte[] bytes = this.cultivation.getCurrentLevel().levelName.getBytes();
		buf.writeBoolean(this.cultivation.getSuppress());
		buf.writeInt(this.cultivation.getCurrentSubLevel());
		buf.writeDouble(this.cultivation.getCurrentProgress());
		buf.writeDouble(this.cultivation.getEnergy());
		buf.writeInt(this.cultivation.getPillCooldown());
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}
}
