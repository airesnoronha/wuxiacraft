package wuxiacraft.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;

public class CultivationProvider implements ICapabilitySerializable<INBT> {

	@CapabilityInject(ICultivation.class)
	public static Capability<ICultivation> CULTIVATION_PROVIDER = null;

	private ICultivation cultivation_instance = new Cultivation();

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
	public INBT serializeNBT() {
		return CULTIVATION_PROVIDER.writeNBT(cultivation_instance, null);
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		CULTIVATION_PROVIDER.readNBT(cultivation_instance, null, nbt);
	}
}
