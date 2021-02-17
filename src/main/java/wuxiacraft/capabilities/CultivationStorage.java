package wuxiacraft.capabilities;

import net.minecraft.command.arguments.NBTCompoundTagArgument;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;

public class CultivationStorage implements Capability.IStorage<ICultivation> {

	@Nullable
	@Override
	public INBT writeNBT(Capability<ICultivation> capability, ICultivation instance, Direction side) {
		CompoundNBT tag = new CompoundNBT();
		Cultivation.SystemStats stats = instance.getStatsBySystem(CultivationLevel.System.BODY);
		tag.putString("bodyLevel", stats.getLevel().levelName);
		tag.putInt("bodySubLevel", stats.getSubLevel());
		tag.putDouble("bodyBase", stats.getBase());
		tag.putDouble("bodyFoundation", stats.getFoundation());
		stats = instance.getStatsBySystem(CultivationLevel.System.DIVINE);
		tag.putString("divineLevel", stats.getLevel().levelName);
		tag.putInt("divineSubLevel", stats.getSubLevel());
		tag.putDouble("divineBase", stats.getBase());
		tag.putDouble("divineFoundation", stats.getFoundation());
		stats = instance.getStatsBySystem(CultivationLevel.System.ESSENCE);
		tag.putString("essenceLevel", stats.getLevel().levelName);
		tag.putInt("essenceSubLevel", stats.getSubLevel());
		tag.putDouble("essenceBase", stats.getBase());
		tag.putDouble("essenceFoundation", stats.getFoundation());
		tag.putDouble("energy", instance.getEnergy());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICultivation> capability, ICultivation instance, Direction side, INBT nbt) {
		CompoundNBT tag = (CompoundNBT) nbt;
		//body stats
		instance.getStatsBySystem(CultivationLevel.System.BODY).setLevel(CultivationLevel.getLevelInListByName(CultivationLevel.BODY_LEVELS, tag.getString("bodyLevel")));
		instance.getStatsBySystem(CultivationLevel.System.BODY).setSubLevel(tag.getInt("bodySubLevel"));
		instance.getStatsBySystem(CultivationLevel.System.BODY).setBase(tag.getDouble("bodyBase"));
		instance.getStatsBySystem(CultivationLevel.System.BODY).setFoundation(tag.getDouble("bodyFoundation"));
		//divine stats
		instance.getStatsBySystem(CultivationLevel.System.DIVINE).setLevel(CultivationLevel.getLevelInListByName(CultivationLevel.BODY_LEVELS, tag.getString("divineLevel")));
		instance.getStatsBySystem(CultivationLevel.System.DIVINE).setSubLevel(tag.getInt("divineSubLevel"));
		instance.getStatsBySystem(CultivationLevel.System.DIVINE).setBase(tag.getDouble("divineBase"));
		instance.getStatsBySystem(CultivationLevel.System.DIVINE).setFoundation(tag.getDouble("divineFoundation"));
		//essence stats
		instance.getStatsBySystem(CultivationLevel.System.ESSENCE).setLevel(CultivationLevel.getLevelInListByName(CultivationLevel.BODY_LEVELS, tag.getString("essenceLevel")));
		instance.getStatsBySystem(CultivationLevel.System.ESSENCE).setSubLevel(tag.getInt("essenceSubLevel"));
		instance.getStatsBySystem(CultivationLevel.System.ESSENCE).setBase(tag.getDouble("essenceBase"));
		instance.getStatsBySystem(CultivationLevel.System.ESSENCE).setFoundation(tag.getDouble("essenceFoundation"));
		//energy
		instance.setEnergy(tag.getDouble("energy"));
	}
}
