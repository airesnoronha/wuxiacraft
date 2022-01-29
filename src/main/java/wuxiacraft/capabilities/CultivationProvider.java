package wuxiacraft.capabilities;

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
		return cultivation_instance.serialize();
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		cultivation_instance.deserialize(tag);
	}
}
