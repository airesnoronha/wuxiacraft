package wuxiacraft.capabilities.cultivation;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;

public class CultivationProvider implements ICapabilitySerializable<CompoundTag> {

	public static Capability<ICultivation> CULTIVATION_PROVIDER = CapabilityManager.get(new CapabilityToken<>() {});

	public ICultivation cultivation_instance = new Cultivation();

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return CULTIVATION_PROVIDER.orEmpty(cap, LazyOptional.of(() -> cultivation_instance));
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
		return CULTIVATION_PROVIDER.orEmpty(cap, LazyOptional.of(() -> cultivation_instance));
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag data = new CompoundTag();
		data.putDouble("health", cultivation_instance.getHealth());
		data.put("body-data", cultivation_instance.getSystemData(Cultivation.System.BODY).serialize());
		data.put("divine-data", cultivation_instance.getSystemData(Cultivation.System.DIVINE).serialize());
		data.put("essence-data", cultivation_instance.getSystemData(Cultivation.System.ESSENCE).serialize());
		return data;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		cultivation_instance.setHealth(tag.getDouble("health"));
		cultivation_instance.getSystemData(Cultivation.System.BODY).deserialize(tag.getCompound("body-data"));
		cultivation_instance.getSystemData(Cultivation.System.DIVINE).deserialize(tag.getCompound("divine-data"));
		cultivation_instance.getSystemData(Cultivation.System.ESSENCE).deserialize(tag.getCompound("essence-data"));
	}
}
