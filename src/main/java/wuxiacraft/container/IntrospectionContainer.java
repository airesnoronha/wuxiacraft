package wuxiacraft.container;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IntrospectionContainer extends Container {

	public static ContainerType<IntrospectionContainer> registryType;

	public static IntrospectionContainer create(int id, PlayerInventory inventory, PacketBuffer buf) {
		return new IntrospectionContainer(id);
	}

	public IntrospectionContainer(int id) {
		super(registryType, id);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}
}
