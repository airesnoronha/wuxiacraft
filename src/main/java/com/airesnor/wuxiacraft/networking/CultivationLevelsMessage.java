package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class CultivationLevelsMessage implements IMessage {

	public final List<CultivationLevel> levels = new ArrayList<>();
	public String baseLevelName;

	public CultivationLevelsMessage() {
		levels.addAll(CultivationLevel.REGISTERED_LEVELS.values());
		baseLevelName = CultivationLevel.REGISTERED_BASE_LEVEL.levelName;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		levels.clear();
		int quantity = buf.readInt();
		for(int i = 0; i < quantity; i++) {
			String levelName = ByteBufUtils.readUTF8String(buf);
			String nextLevelName = ByteBufUtils.readUTF8String(buf);
			String displayName = ByteBufUtils.readUTF8String(buf);
			int subLevels = buf.readInt();
			long foundationMaxStat = buf.readLong();
			double baseProgress = buf.readDouble();
			double baseStrengthModifier = buf.readDouble();
			double baseSpeedModifier = buf.readDouble();
			boolean callsTribulation = buf.readBoolean();
			boolean canFly = buf.readBoolean();
			boolean energyAsFood = buf.readBoolean();
			boolean freeFlight = buf.readBoolean();
			boolean needNoFood = buf.readBoolean();
			boolean teleportation = buf.readBoolean();
			boolean tribulationEachSubLevel = buf.readBoolean();
			levels.add(new CultivationLevel(levelName, nextLevelName, displayName, subLevels, foundationMaxStat, baseProgress, baseSpeedModifier, baseStrengthModifier, energyAsFood, needNoFood, canFly, freeFlight, teleportation, callsTribulation, tribulationEachSubLevel));
		}
		this.baseLevelName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.levels.size());
		for(CultivationLevel level : levels) {
			ByteBufUtils.writeUTF8String(buf, level.levelName);
			ByteBufUtils.writeUTF8String(buf, level.nextLevelName);
			ByteBufUtils.writeUTF8String(buf, level.displayName);
			buf.writeInt(level.subLevels);
			buf.writeLong(level.foundationMaxStat);
			buf.writeDouble(level.baseProgress);
			buf.writeDouble(level.baseStrengthModifier);
			buf.writeDouble(level.baseSpeedModifier);
			buf.writeBoolean(level.callsTribulation);
			buf.writeBoolean(level.canFly);
			buf.writeBoolean(level.energyAsFood);
			buf.writeBoolean(level.freeFlight);
			buf.writeBoolean(level.needNoFood);
			buf.writeBoolean(level.teleportation);
			buf.writeBoolean(level.tribulationEachSubLevel);
		}
		ByteBufUtils.writeUTF8String(buf, this.baseLevelName);
	}

	public static class Handler implements IMessageHandler<CultivationLevelsMessage, IMessage> {

		@Override
		public IMessage onMessage(CultivationLevelsMessage message, MessageContext ctx) {
			if(ctx.side == Side.CLIENT) {
				return handleClientMessage(message);
			}
			return null;
		}

		@SideOnly(Side.CLIENT)
		private IMessage handleClientMessage(CultivationLevelsMessage message) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				CultivationLevel.LOADED_LEVELS.clear();
				for(CultivationLevel level : message.levels) {
					CultivationLevel.LOADED_LEVELS.put(level.levelName, level);
				}
				CultivationLevel.BASE_LEVEL = CultivationLevel.LOADED_LEVELS.get(message.baseLevelName);
				NetworkWrapper.INSTANCE.sendToServer(new CapabilityRequestMessage(Minecraft.getMinecraft().player.getUniqueID()));
			});
			return null;
		}
	}

}
