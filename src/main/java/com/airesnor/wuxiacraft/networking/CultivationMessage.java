package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CultivationMessage implements IMessage {

	public final ICultivation cultivation;

	public CultivationMessage(ICultivation cultivation) {
		this.cultivation = cultivation;
	}

	@SuppressWarnings("unused")
	public CultivationMessage() {
		this.cultivation = new Cultivation();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		//noinspection ConstantConditions
		CultivationProvider.CULTIVATION_CAP.getStorage().readNBT(CultivationProvider.CULTIVATION_CAP, this.cultivation, null, tag);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//noinspection ConstantConditions
		NBTTagCompound tag = (NBTTagCompound) CultivationProvider.CULTIVATION_CAP.getStorage().writeNBT(CultivationProvider.CULTIVATION_CAP, this.cultivation, null);
		ByteBufUtils.writeTag(buf, tag);
	}
}
