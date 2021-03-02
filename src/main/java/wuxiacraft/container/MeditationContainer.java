package wuxiacraft.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

public class MeditationContainer extends Container {

	public static ContainerType<MeditationContainer> registryType;

	public static MeditationContainer createContainer(int id, PlayerInventory inv, PacketBuffer buffer) {
		return new MeditationContainer(id);
	}

	public MeditationContainer(int id) {
		super(registryType, id);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}


}
