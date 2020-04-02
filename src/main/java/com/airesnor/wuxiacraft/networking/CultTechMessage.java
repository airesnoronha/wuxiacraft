package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.techniques.*;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

public class CultTechMessage implements IMessage {

	public ICultTech cultTech;

	public CultTechMessage(ICultTech cultTech) {
		this.cultTech = cultTech;
	}

	public CultTechMessage() {
		this.cultTech = new CultTech();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readInt();
		List<String> names = new ArrayList<>();
		double[] progresses = new double[size];
		for (int i = 0; i < size; i++) {
			int length = buf.readInt();
			byte[] bytes = new byte[length];
			buf.readBytes(bytes, 0, length);
			names.add(new String(bytes));
			progresses[i] = buf.readDouble();
		}
		int i = 0;
		for (String name : names) {
			Technique t = Techniques.getTechniqueByUName(name);
			if (t != null) {
				cultTech.addTechnique(t, progresses[i]);
				i++;
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.cultTech.getKnownTechniques().size());
		for (KnownTechnique t : this.cultTech.getKnownTechniques()) {
			buf.writeInt(t.getTechnique().getUName().length());
			buf.writeBytes(t.getTechnique().getUName().getBytes());
			buf.writeDouble(t.getProgress());
		}
	}
}
